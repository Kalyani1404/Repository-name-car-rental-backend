package com.kalyani.car_rental_backend.config;

import com.kalyani.car_rental_backend.user.entity.Role;
import com.kalyani.car_rental_backend.user.entity.User;
import com.kalyani.car_rental_backend.user.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class GoogleOAuth2SuccessHandler
        implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${frontend.url}")
    private String frontendUrl;

    public GoogleOAuth2SuccessHandler(
            UserRepository userRepository,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oauthUser =
                (OAuth2User) authentication.getPrincipal();

        String email =
                oauthUser.getAttribute("email");

        String name =
                oauthUser.getAttribute("name");

        if (email == null || email.isBlank()) {
            response.sendRedirect(
                    frontendUrl
                            + "/?error="
                            + URLEncoder.encode(
                            "Google account email not found",
                            StandardCharsets.UTF_8
                    )
            );

            return;
        }

        String normalizedEmail =
                email.trim().toLowerCase();

        User user =
                userRepository
                        .findByEmailIgnoreCase(
                                normalizedEmail
                        )
                        .orElseGet(() -> {

                            User newUser =
                                    new User();

                            newUser.setEmail(
                                    normalizedEmail
                            );

                            newUser.setFullName(
                                    name != null
                                            && !name.isBlank()
                                            ? name
                                            : normalizedEmail
                                            .split("@")[0]
                            );

                            /*
                             * Google users do not use this password
                             * for login.
                             *
                             * We still store a random value because
                             * your User entity currently requires
                             * password to be NOT NULL.
                             */
                            newUser.setPassword(
                                    UUID.randomUUID()
                                            .toString()
                            );

                            newUser.setRole(
                                    Role.USER
                            );

                            newUser.setProvider(
                                    "GOOGLE"
                            );

                            newUser.setStatus(
                                    "ACTIVE"
                            );

                            newUser.setEmailVerified(
                                    true
                            );

                            return userRepository.save(
                                    newUser
                            );
                        });

        /*
         * If an existing LOCAL account logs in
         * through Google using the same email,
         * we keep the same account.
         *
         * We only mark the email as verified.
         */
        if (!Boolean.TRUE.equals(
                user.getEmailVerified()
        )) {

            user.setEmailVerified(
                    true
            );

            userRepository.save(
                    user
            );
        }

        String token =
                jwtUtil.generateToken(
                        user
                );

        String redirectUrl =
                frontendUrl
                        + "/oauth-success?token="
                        + URLEncoder.encode(
                        token,
                        StandardCharsets.UTF_8
                );

        response.sendRedirect(
                redirectUrl
        );
    }
}