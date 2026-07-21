package com.kalyani.car_rental_backend.user.controller;

import com.kalyani.car_rental_backend.response.ApiResponse;
import com.kalyani.car_rental_backend.response.PageResponse;
import com.kalyani.car_rental_backend.response.PaginationUtils;
import com.kalyani.car_rental_backend.user.dto.UserResponse;
import com.kalyani.car_rental_backend.user.entity.User;
import com.kalyani.car_rental_backend.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository users;

    public UserController(UserRepository users) {
        this.users = users;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(Authentication a) {
        User u = users.findByEmailIgnoreCase(a.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(ApiResponse.success(out(u), "Profile loaded"));
    }

    // Existing endpoint kept unchanged.
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> all() {
        return ResponseEntity.ok(
                ApiResponse.success(users.findAll().stream().map(this::out).toList(), "Users loaded")
        );
    }

    // New pagination + sorting endpoint.
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> paged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Pageable pageable = PaginationUtils.createPageable(
                page,
                size,
                sortBy,
                sortDir,
                Set.of("id", "fullName", "email", "role", "status", "createdAt")
        );

        Page<UserResponse> result = users.findAll(pageable).map(this::out);
        return ResponseEntity.ok(
                ApiResponse.success(PageResponse.from(result, sortBy, sortDir), "Users loaded with pagination and sorting")
        );
    }

    private UserResponse out(User u) {
        return new UserResponse(
                u.getId(),
                u.getFullName(),
                u.getEmail(),
                u.getPhone(),
                u.getRole().name(),
                u.getProvider(),
                u.getStatus()
        );
    }
}
