package com.kalyani.car_rental_backend.car.serviceimpl;

import com.kalyani.car_rental_backend.car.dto.CarRequest;
import com.kalyani.car_rental_backend.car.dto.CarResponse;
import com.kalyani.car_rental_backend.car.entity.Car;
import com.kalyani.car_rental_backend.car.repository.CarRepository;
import com.kalyani.car_rental_backend.car.service.CarService;
import com.kalyani.car_rental_backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository repo;

    public CarServiceImpl(CarRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CarResponse getById(Long id) {
        Car car = findCarById(id);
        return convertToResponse(car);
    }

    @Override
    @Transactional
    public CarResponse add(CarRequest request) {

        if (repo.existsByRegistrationNumberIgnoreCase(
                request.getRegistrationNumber()
        )) {
            throw new IllegalArgumentException(
                    "Registration number already exists"
            );
        }

        Car car = new Car();

        updateCarFromRequest(car, request);

        car.setIsAvailable(true);
        car.setAvailabilityStatus("AVAILABLE");
        car.setRating(4.8);

        Car savedCar = repo.save(car);

        return convertToResponse(savedCar);
    }

    @Override
    @Transactional
    public CarResponse update(Long id, CarRequest request) {

        Car car = findCarById(id);

        updateCarFromRequest(car, request);

        Car updatedCar = repo.save(car);

        return convertToResponse(updatedCar);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Car car = findCarById(id);
        repo.delete(car);
    }

    private Car findCarById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Car not found with ID: " + id
                        )
                );
    }

    private void updateCarFromRequest(
            Car car,
            CarRequest request
    ) {
        car.setName(request.getName());
        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setColor(request.getColor());

        car.setRegistrationNumber(
                request.getRegistrationNumber()
        );

        car.setFuelType(request.getFuelType());
        car.setTransmission(request.getTransmission());

        car.setSeatingCapacity(
                request.getSeatingCapacity()
        );

        car.setPricePerDay(
                request.getPricePerDay()
        );

        car.setPricePerWeek(
                request.getPricePerWeek()
        );

        car.setPricePerMonth(
                request.getPricePerMonth()
        );

        car.setCurrentLocationCity(
                request.getCurrentLocationCity()
        );

        car.setCurrentLocationLatitude(
                request.getCurrentLocationLatitude()
        );

        car.setCurrentLocationLongitude(
                request.getCurrentLocationLongitude()
        );

        car.setInsuranceProvider(
                request.getInsuranceProvider()
        );

        car.setInsurancePolicyNumber(
                request.getInsurancePolicyNumber()
        );

        car.setFeatures(
                request.getFeatures()
        );

        car.setFuelPolicy(
                request.getFuelPolicy()
        );

        car.setMileageLimitPerDay(
                request.getMileageLimitPerDay()
        );

        car.setLateFeePerHour(
                request.getLateFeePerHour()
        );

        car.setSecurityDeposit(
                request.getSecurityDeposit()
        );

        car.setImageUrl(
                request.getImageUrl()
        );
    }

    private CarResponse convertToResponse(Car car) {

        CarResponse response = new CarResponse();

        response.setId(car.getId());
        response.setName(car.getName());
        response.setBrand(car.getBrand());
        response.setModel(car.getModel());
        response.setYear(car.getYear());
        response.setColor(car.getColor());

        response.setRegistrationNumber(
                car.getRegistrationNumber()
        );

        response.setFuelType(
                car.getFuelType()
        );

        response.setTransmission(
                car.getTransmission()
        );

        response.setSeatingCapacity(
                car.getSeatingCapacity()
        );

        response.setPricePerDay(
                car.getPricePerDay()
        );

        response.setPricePerWeek(
                car.getPricePerWeek()
        );

        response.setPricePerMonth(
                car.getPricePerMonth()
        );

        response.setCurrentLocationCity(
                car.getCurrentLocationCity()
        );

        response.setCurrentLocationLatitude(
                car.getCurrentLocationLatitude()
        );

        response.setCurrentLocationLongitude(
                car.getCurrentLocationLongitude()
        );

        response.setInsuranceProvider(
                car.getInsuranceProvider()
        );

        response.setInsurancePolicyNumber(
                car.getInsurancePolicyNumber()
        );

        response.setFeatures(
                car.getFeatures()
        );

        response.setFuelPolicy(
                car.getFuelPolicy()
        );

        response.setMileageLimitPerDay(
                car.getMileageLimitPerDay()
        );

        response.setLateFeePerHour(
                car.getLateFeePerHour()
        );

        response.setSecurityDeposit(
                car.getSecurityDeposit()
        );

        response.setImageUrl(
                car.getImageUrl()
        );

        response.setIsAvailable(
                car.getIsAvailable()
        );

        response.setAvailabilityStatus(
                car.getAvailabilityStatus()
        );

        response.setRating(
                car.getRating()
        );

        return response;
    }
}