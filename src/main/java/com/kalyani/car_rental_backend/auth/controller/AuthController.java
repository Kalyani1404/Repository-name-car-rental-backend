package com.kalyani.car_rental_backend.auth.controller;

import com.kalyani.car_rental_backend.auth.dto.AuthResponse;
import com.kalyani.car_rental_backend.auth.dto.DriverRegisterRequest;
import com.kalyani.car_rental_backend.auth.dto.LoginRequest;
import com.kalyani.car_rental_backend.auth.dto.RegisterRequest;

import com.kalyani.car_rental_backend.auth.service.AuthService;
import com.kalyani.car_rental_backend.response.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {
        this.authService =
                authService;
    }

    /*
     * USER REGISTRATION
     *
     * Full URL:
     * POST http://localhost:8081/api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<
            ApiResponse<AuthResponse>
            > register(
            @Valid
            @RequestBody
            RegisterRequest request
    ) {

        AuthResponse response =
                authService.register(
                        request
                );

        return ResponseEntity
                .status(
                        HttpStatus.CREATED
                )
                .body(
                        ApiResponse.success(
                                response,
                                "Registration successful"
                        )
                );
    }

    /*
     * DRIVER REGISTRATION
     *
     * Full URL:
     * POST http://localhost:8081/api/auth/register-driver
     */
    @PostMapping(
            "/register-driver"
    )
    public ResponseEntity<
            ApiResponse<AuthResponse>
            > registerDriver(
            @Valid
            @RequestBody
            DriverRegisterRequest request
    ) {

        AuthResponse response =
                authService
                        .registerDriver(
                                request
                        );

        return ResponseEntity
                .status(
                        HttpStatus.CREATED
                )
                .body(
                        ApiResponse.success(
                                response,
                                "Driver registration submitted"
                        )
                );
    }

    /*
     * USER / ADMIN / DRIVER LOGIN
     *
     * Full URL:
     * POST http://localhost:8081/api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<
            ApiResponse<AuthResponse>
            > login(
            @Valid
            @RequestBody
            LoginRequest request
    ) {

        AuthResponse response =
                authService.login(
                        request
                );

        return ResponseEntity
                .ok(
                        ApiResponse.success(
                                response,
                                "Login successful"
                        )
                );
    }
}