package com.kalyani.car_rental_backend.car.service;

import com.kalyani.car_rental_backend.car.dto.CarRequest;
import com.kalyani.car_rental_backend.car.dto.CarResponse;
import com.kalyani.car_rental_backend.response.PageResponse;

import java.util.List;

public interface CarService {
    List<CarResponse> getAll();
    PageResponse<CarResponse> getPaged(int page, int size, String sortBy, String sortDir);
    CarResponse getById(Long id);
    CarResponse add(CarRequest r);
    CarResponse update(Long id, CarRequest r);
    void delete(Long id);
}
