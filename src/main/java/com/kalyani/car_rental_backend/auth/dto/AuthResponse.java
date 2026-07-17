package com.kalyani.car_rental_backend.auth.dto;
import com.kalyani.car_rental_backend.user.dto.UserResponse;

public class AuthResponse {
    private String token; private UserResponse user;
    public AuthResponse() {}
    public AuthResponse(String token,UserResponse user){this.token=token;this.user=user;}
    public String getToken(){return token;} public UserResponse getUser(){return user;}
}
