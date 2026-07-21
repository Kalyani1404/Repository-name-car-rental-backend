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

    private final CorsConfigurationSource
            corsConfigurationSource;

    private final ObjectMapper objectMapper;

    private final GoogleOAuth2SuccessHandler
            googleOAuth2SuccessHandler;

    public SecurityConfig(
            JwtFilter jwtFilter,

            @Qualifier(
                    "corsConfigurationSource"
            )
            CorsConfigurationSource
                    corsConfigurationSource,

            ObjectMapper objectMapper,

            GoogleOAuth2SuccessHandler
                    googleOAuth2SuccessHandler
    ) {

        this.jwtFilter =
                jwtFilter;

        this.corsConfigurationSource =
                corsConfigurationSource;

        this.objectMapper =
                objectMapper;

        this.googleOAuth2SuccessHandler =
                googleOAuth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain
    securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                /*
                 * CORS CONFIGURATION
                 */
                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource
                        )
                )

                /*
                 * CSRF DISABLED
                 *
                 * Your REST API uses JWT.
                 */
                .csrf(csrf ->
                        csrf.disable()
                )

                /*
                 * JWT APIs remain stateless.
                 *
                 * IF_REQUIRED is needed because
                 * OAuth2 authorization temporarily
                 * needs a session while redirecting
                 * between your backend and Google.
                 */
                .sessionManagement(
                        session ->
                                session
                                        .sessionCreationPolicy(
                                                SessionCreationPolicy
                                                        .IF_REQUIRED
                                        )
                )

                /*
                 * ERROR HANDLING
                 */
                .exceptionHandling(
                        exception ->
                                exception

                                        .authenticationEntryPoint(
                                                (
                                                        request,
                                                        response,
                                                        authenticationException
                                                ) ->
                                                        writeErrorResponse(
                                                                response,
                                                                HttpServletResponse
                                                                        .SC_UNAUTHORIZED,
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
                                                            request
                                                                    .getMethod();

                                                    if (
                                                            request
                                                                    .getRequestURI()
                                                                    .contains(
                                                                            "/cars"
                                                                    )
                                                                    &&
                                                                    List.of(
                                                                                    "POST",
                                                                                    "PUT",
                                                                                    "PATCH",
                                                                                    "DELETE"
                                                                            )
                                                                            .contains(
                                                                                    method
                                                                                            .toUpperCase()
                                                                            )
                                                    ) {

                                                        message =
                                                                "Only ADMIN can add, update or delete cars";
                                                    }

                                                    writeErrorResponse(
                                                            response,
                                                            HttpServletResponse
                                                                    .SC_FORBIDDEN,
                                                            message
                                                    );
                                                }
                                        )
                )

                /*
                 * URL AUTHORIZATION
                 */
                .authorizeHttpRequests(
                        auth ->
                                auth

                                        /*
                                         * Allow browser
                                         * preflight requests.
                                         */
                                        .requestMatchers(
                                                HttpMethod.OPTIONS,
                                                "/**"
                                        )
                                        .permitAll()

                                        /*
                                         * Public authentication APIs.
                                         */
                                        .requestMatchers(
                                                "/auth/register",
                                                "/auth/register-driver",
                                                "/auth/login",
                                                "/error"
                                        )
                                        .permitAll()

                                        /*
                                         * Google OAuth2 URLs.
                                         *
                                         * Example:
                                         *
                                         * /oauth2/authorization/google
                                         *
                                         * /login/oauth2/code/google
                                         */
                                        .requestMatchers(
                                                "/oauth2/**",
                                                "/login/oauth2/**"
                                        )
                                        .permitAll()

                                        /*
                                         * Only ADMIN can add cars.
                                         */
                                        .requestMatchers(
                                                HttpMethod.POST,
                                                "/cars",
                                                "/cars/**"
                                        )
                                        .hasRole(
                                                "ADMIN"
                                        )

                                        /*
                                         * Only ADMIN can update cars.
                                         */
                                        .requestMatchers(
                                                HttpMethod.PUT,
                                                "/cars/**"
                                        )
                                        .hasRole(
                                                "ADMIN"
                                        )

                                        .requestMatchers(
                                                HttpMethod.PATCH,
                                                "/cars/**"
                                        )
                                        .hasRole(
                                                "ADMIN"
                                        )

                                        /*
                                         * Only ADMIN can delete cars.
                                         */
                                        .requestMatchers(
                                                HttpMethod.DELETE,
                                                "/cars/**"
                                        )
                                        .hasRole(
                                                "ADMIN"
                                        )

                                        /*
                                         * Logged-in users.
                                         */
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

                                        /*
                                         * Admin APIs.
                                         */
                                        .requestMatchers(
                                                "/admin",
                                                "/admin/**",
                                                "/coupons",
                                                "/coupons/**"
                                        )
                                        .hasRole(
                                                "ADMIN"
                                        )

                                        /*
                                         * Driver APIs.
                                         */
                                        .requestMatchers(
                                                "/driver",
                                                "/driver/**"
                                        )
                                        .hasRole(
                                                "DRIVER"
                                        )

                                        /*
                                         * General protected APIs.
                                         */
                                        .requestMatchers(
                                                "/addresses",
                                                "/addresses/**",
                                                "/notifications",
                                                "/notifications/**",
                                                "/reviews",
                                                "/reviews/**",
                                                "/emergency-contacts",
                                                "/emergency-contacts/**"
                                        )
                                        .authenticated()

                                        /*
                                         * User management
                                         * only ADMIN.
                                         */
                                        .requestMatchers(
                                                "/users",
                                                "/users/**"
                                        )
                                        .hasRole(
                                                "ADMIN"
                                        )

                                        .anyRequest()
                                        .authenticated()
                )

                /*
                 * GOOGLE OAUTH2 LOGIN
                 */
                .oauth2Login(
                        oauth2 ->
                                oauth2
                                        .successHandler(
                                                googleOAuth2SuccessHandler
                                        )
                )

                /*
                 * Existing JWT filter.
                 *
                 * This keeps your current JWT
                 * authentication working.
                 */
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter
                                .class
                );

        return http.build();
    }

    private void writeErrorResponse(
            HttpServletResponse response,
            int status,
            String message
    ) throws IOException {

        response.setStatus(
                status
        );

        response.setContentType(
                MediaType
                        .APPLICATION_JSON_VALUE
        );

        Map<String, Object> body =
                new LinkedHashMap<>();

        body.put(
                "success",
                false
        );

        body.put(
                "message",
                message
        );

        body.put(
                "data",
                null
        );

        objectMapper.writeValue(
                response.getOutputStream(),
                body
        );
    }

    @Bean
    public PasswordEncoder
    passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager
    authenticationManager(
            AuthenticationConfiguration
                    configuration
    ) throws Exception {

        return configuration
                .getAuthenticationManager();
    }
}