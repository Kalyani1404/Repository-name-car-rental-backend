package com.kalyani.car_rental_backend.booking.entity;

public enum BookingStatus {
    PENDING,
    SEARCHING_DRIVER,
    WAITING_FOR_DRIVER,
    DRIVER_ASSIGNED,
    CONFIRMED,
    ARRIVED,
    ACTIVE,
    COMPLETED,
    CANCELLED,
    REJECTED
}
