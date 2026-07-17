package com.kalyani.car_rental_backend.auth.controller;

import com.kalyani.car_rental_backend.auth.dto.*;
import com.kalyani.car_rental_backend.auth.service.AuthService;
import com.kalyani.car_rental_backend.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){this.authService=authService;}
    @PostMapping("/register") public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(authService.register(request),"Registration successful"));}
    @PostMapping("/register-driver") public ResponseEntity<ApiResponse<AuthResponse>> registerDriver(@Valid @RequestBody DriverRegisterRequest request){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(authService.registerDriver(request),"Driver registration submitted"));}
    @PostMapping("/login") public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request){return ResponseEntity.ok(ApiResponse.success(authService.login(request),"Login successful"));}
}
