package com.kalyani.car_rental_backend.car.controller;
import com.kalyani.car_rental_backend.car.dto.*;
import com.kalyani.car_rental_backend.car.service.CarService;
import com.kalyani.car_rental_backend.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {
    private final CarService service;
    public CarController(CarService service){this.service=service;}
    @GetMapping public ResponseEntity<ApiResponse<List<CarResponse>>> all(){
        return ResponseEntity.ok(ApiResponse.success(service.getAll(),"Cars loaded"));
    }
    @GetMapping("/{id}") public ResponseEntity<ApiResponse<CarResponse>> one(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(service.getById(id),"Car loaded"));
    }
    @PostMapping public ResponseEntity<ApiResponse<CarResponse>> add(@Valid @RequestBody CarRequest r){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(service.add(r),"Car added successfully"));
    }
    @PutMapping("/{id}") public ResponseEntity<ApiResponse<CarResponse>> update(@PathVariable Long id,@Valid @RequestBody CarRequest r){
        return ResponseEntity.ok(ApiResponse.success(service.update(id,r),"Car updated"));
    }
    @DeleteMapping("/{id}") public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        service.delete(id);return ResponseEntity.ok(ApiResponse.success(null,"Car deleted"));
    }
}
