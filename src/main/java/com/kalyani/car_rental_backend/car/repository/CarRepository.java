package com.kalyani.car_rental_backend.car.repository;
import com.kalyani.car_rental_backend.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CarRepository extends JpaRepository<Car,Long>{
    boolean existsByRegistrationNumberIgnoreCase(String registrationNumber);
}
