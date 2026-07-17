package com.kalyani.car_rental_backend.user.controller;
import com.kalyani.car_rental_backend.response.ApiResponse;
import com.kalyani.car_rental_backend.user.dto.UserResponse;
import com.kalyani.car_rental_backend.user.entity.User;
import com.kalyani.car_rental_backend.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository users;
    public UserController(UserRepository users){this.users=users;}
    @GetMapping("/me") public ResponseEntity<ApiResponse<UserResponse>> me(Authentication a){
        User u=users.findByEmailIgnoreCase(a.getName()).orElseThrow(()->new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(ApiResponse.success(out(u),"Profile loaded"));
    }
    @GetMapping public ResponseEntity<ApiResponse<List<UserResponse>>> all(){
        return ResponseEntity.ok(ApiResponse.success(users.findAll().stream().map(this::out).toList(),"Users loaded"));
    }
    private UserResponse out(User u){return new UserResponse(u.getId(),u.getFullName(),u.getEmail(),u.getPhone(),u.getRole().name(),u.getProvider(),u.getStatus());}
}
