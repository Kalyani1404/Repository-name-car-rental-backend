package com.kalyani.car_rental_backend.payment.controller;

import com.kalyani.car_rental_backend.payment.dto.PaymentRequest;
import com.kalyani.car_rental_backend.payment.dto.PaymentResponse;
import com.kalyani.car_rental_backend.payment.service.PaymentService;
import com.kalyani.car_rental_backend.response.ApiResponse;
import com.kalyani.car_rental_backend.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> create(
            @Valid @RequestBody PaymentRequest r, Authentication a) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(service.create(r, a.getName()), "Payment successful"));
    }

    // Existing endpoint kept unchanged.
    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> list(Authentication a) {
        boolean admin = isAdmin(a);
        return ResponseEntity.ok(ApiResponse.success(service.list(a.getName(), admin), "Payments loaded"));
    }

    // New pagination + sorting endpoint.
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<PageResponse<PaymentResponse>>> paged(
            Authentication a,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        boolean admin = isAdmin(a);
        return ResponseEntity.ok(
                ApiResponse.success(
                        service.listPaged(a.getName(), admin, page, size, sortBy, sortDir),
                        "Payments loaded with pagination and sorting"
                )
        );
    }

    private boolean isAdmin(Authentication a) {
        return a.getAuthorities().stream()
                .anyMatch(x -> x.getAuthority().equals("ROLE_ADMIN"));
    }
}
