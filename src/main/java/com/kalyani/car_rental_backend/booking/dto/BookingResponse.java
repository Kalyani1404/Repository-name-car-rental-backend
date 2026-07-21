package com.kalyani.car_rental_backend.booking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingResponse {
    public Long id;
    public String bookingReference;
    public Long carId;
    public String carName;
    public String bookingType;
    public String tripType;
    public String startPoint;
    public String endPoint;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public Double estimatedDistanceKm;
    public Long durationDays;
    public BigDecimal rentalAmount;
    public BigDecimal insuranceAmount;
    public BigDecimal driverAmount;
    public BigDecimal grandTotal;
    public String status;
    public Long driverId;
    public String driverName;
    public String driverPhone;
    public String driverVehicle;
}
