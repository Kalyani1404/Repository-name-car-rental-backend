package com.kalyani.car_rental_backend.car.controller;

import com.kalyani.car_rental_backend.car.dto.CarRequest;
import com.kalyani.car_rental_backend.car.dto.CarResponse;
import com.kalyani.car_rental_backend.car.service.CarService;
import com.kalyani.car_rental_backend.response.ApiResponse;
import com.kalyani.car_rental_backend.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {
    private final CarService service;

    public CarController(CarService service) {
        this.service = service;
    }

    // Existing endpoint kept unchanged so the current frontend continues to work.
    @GetMapping
    public ResponseEntity<ApiResponse<List<CarResponse>>> all() {
        return ResponseEntity.ok(ApiResponse.success(service.getAll(), "Cars loaded"));
    }

    // New pagination + sorting endpoint.
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<PageResponse<CarResponse>>> paged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        service.getPaged(page, size, sortBy, sortDir),
                        "Cars loaded with pagination and sorting"
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CarResponse>> one(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(service.getById(id), "Car loaded"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CarResponse>> add(@Valid @RequestBody CarRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(service.add(r), "Car added successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CarResponse>> update(@PathVariable Long id, @Valid @RequestBody CarRequest r) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, r), "Car updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Car deleted"));
    }
}
