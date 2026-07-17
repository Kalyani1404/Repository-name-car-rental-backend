package com.kalyani.car_rental_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final ObjectMapper objectMapper;

    public SecurityConfig(
            JwtFilter jwtFilter,
            @Qualifier("corsConfigurationSource")
            CorsConfigurationSource corsConfigurationSource,
            ObjectMapper objectMapper
    ) {
        this.jwtFilter = jwtFilter;
        this.corsConfigurationSource = corsConfigurationSource;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource
                        )
                )

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint(
                                (
                                        request,
                                        response,
                                        authenticationException
                                ) -> writeErrorResponse(
                                        response,
                                        401,
                                        "Authentication required"
                                )
                        )

                        .accessDeniedHandler(
                                (
                                        request,
                                        response,
                                        accessDeniedException
                                ) -> {
                                    String message =
                                            "Access denied";

                                    String method =
                                            request.getMethod();

                                    if (
                                            request
                                                    .getRequestURI()
                                                    .contains("/cars")
                                                    &&
                                                    List.of(
                                                                    "POST",
                                                                    "PUT",
                                                                    "PATCH",
                                                                    "DELETE"
                                                            )
                                                            .contains(
                                                                    method.toUpperCase()
                                                            )
                                    ) {
                                        message =
                                                "Only ADMIN can add, update or delete cars";
                                    }

                                    writeErrorResponse(
                                            response,
                                            403,
                                            message
                                    );
                                }
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        )
                        .permitAll()

                        .requestMatchers(
                                "/auth/register",
                                "/auth/register-driver",
                                "/auth/login",
                                "/error"
                        )
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/cars",
                                "/cars/**"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/cars/**"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/cars/**"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/cars/**"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/cars",
                                "/cars/**",
                                "/bookings",
                                "/bookings/**",
                                "/payments",
                                "/payments/**",
                                "/users/me"
                        )
                        .authenticated()

                        .requestMatchers("/admin", "/admin/**", "/coupons", "/coupons/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/driver", "/driver/**")
                        .hasRole("DRIVER")

                        .requestMatchers(
                                "/addresses", "/addresses/**",
                                "/notifications", "/notifications/**",
                                "/reviews", "/reviews/**",
                                "/emergency-contacts", "/emergency-contacts/**"
                        )
                        .authenticated()

                        .requestMatchers(
                                "/users",
                                "/users/**"
                        )
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    private void writeErrorResponse(
            HttpServletResponse response,
            int status,
            String message
    ) throws IOException {

        response.setStatus(status);

        response.setContentType(
                MediaType.APPLICATION_JSON_VALUE
        );

        Map<String, Object> body =
                new LinkedHashMap<>();

        body.put("success", false);
        body.put("message", message);
        body.put("data", null);

        objectMapper.writeValue(
                response.getOutputStream(),
                body
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration
                .getAuthenticationManager();
    }
}