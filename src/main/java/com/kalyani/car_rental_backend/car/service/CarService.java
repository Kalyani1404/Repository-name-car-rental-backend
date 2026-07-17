package com.kalyani.car_rental_backend.car.service;
import com.kalyani.car_rental_backend.car.dto.*;
import java.util.List;
public interface CarService {
    List<CarResponse> getAll(); CarResponse getById(Long id); CarResponse add(CarRequest r);
    CarResponse update(Long id,CarRequest r); void delete(Long id);
}
