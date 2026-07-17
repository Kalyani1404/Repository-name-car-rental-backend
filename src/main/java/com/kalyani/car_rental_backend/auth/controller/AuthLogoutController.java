package com.kalyani.car_rental_backend.auth.controller;
import com.kalyani.car_rental_backend.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthLogoutController {
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(){
        return ResponseEntity.ok(ApiResponse.success(null,"Logged out successfully"));
    }
}
