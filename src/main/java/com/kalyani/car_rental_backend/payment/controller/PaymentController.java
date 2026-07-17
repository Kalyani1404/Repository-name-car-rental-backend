package com.kalyani.car_rental_backend.payment.controller;
import com.kalyani.car_rental_backend.payment.dto.*;
import com.kalyani.car_rental_backend.payment.service.PaymentService;
import com.kalyani.car_rental_backend.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService service;
    public PaymentController(PaymentService service){this.service=service;}
    @PostMapping public ResponseEntity<ApiResponse<PaymentResponse>> create(@Valid @RequestBody PaymentRequest r,Authentication a){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(service.create(r,a.getName()),"Payment successful"));
    }
    @GetMapping public ResponseEntity<ApiResponse<List<PaymentResponse>>> list(Authentication a){
        boolean admin=a.getAuthorities().stream().anyMatch(x->x.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(ApiResponse.success(service.list(a.getName(),admin),"Payments loaded"));
    }
}
