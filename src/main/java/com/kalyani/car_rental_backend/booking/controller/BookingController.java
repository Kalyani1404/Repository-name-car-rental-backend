package com.kalyani.car_rental_backend.booking.controller;

import com.kalyani.car_rental_backend.booking.dto.BookingRequest;
import com.kalyani.car_rental_backend.booking.dto.BookingResponse;
import com.kalyani.car_rental_backend.booking.service.BookingService;
import com.kalyani.car_rental_backend.response.ApiResponse;
import com.kalyani.car_rental_backend.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> create(
            @Valid @RequestBody BookingRequest r, Authentication a) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(service.create(r, a.getName()), "Booking created"));
    }

    // Existing endpoint kept unchanged.
    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponse>>> list(Authentication a) {
        boolean admin = isAdmin(a);
        return ResponseEntity.ok(ApiResponse.success(service.getForUser(a.getName(), admin), "Bookings loaded"));
    }

    // New pagination + sorting endpoint. Admin sees all bookings; normal users see only their own.
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<PageResponse<BookingResponse>>> paged(
            Authentication a,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        boolean admin = isAdmin(a);
        return ResponseEntity.ok(
                ApiResponse.success(
                        service.getPagedForUser(a.getName(), admin, page, size, sortBy, sortDir),
                        "Bookings loaded with pagination and sorting"
                )
        );
    }

    private boolean isAdmin(Authentication a) {
        return a.getAuthorities().stream()
                .anyMatch(x -> x.getAuthority().equals("ROLE_ADMIN"));
    }
}
