package com.kalyani.car_rental_backend.auth.service;
import com.kalyani.car_rental_backend.auth.dto.*;
public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse registerDriver(DriverRegisterRequest request);
    AuthResponse login(LoginRequest request);
}
