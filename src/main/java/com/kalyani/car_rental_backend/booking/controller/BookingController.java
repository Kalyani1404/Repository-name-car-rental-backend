package com.kalyani.car_rental_backend.booking.controller;
import com.kalyani.car_rental_backend.booking.dto.*;
import com.kalyani.car_rental_backend.booking.service.BookingService;
import com.kalyani.car_rental_backend.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService service;
    public BookingController(BookingService service){this.service=service;}
    @PostMapping public ResponseEntity<ApiResponse<BookingResponse>> create(@Valid @RequestBody BookingRequest r,Authentication a){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(service.create(r,a.getName()),"Booking created"));
    }
    @GetMapping public ResponseEntity<ApiResponse<List<BookingResponse>>> list(Authentication a){
        boolean admin=a.getAuthorities().stream().anyMatch(x->x.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(ApiResponse.success(service.getForUser(a.getName(),admin),"Bookings loaded"));
    }
}
