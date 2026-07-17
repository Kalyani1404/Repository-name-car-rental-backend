package com.kalyani.car_rental_backend.address.dto;
import java.time.LocalDateTime;
public record SavedAddressResponse(Long id,String label,String addressLine,String city,String state,String postalCode,Double latitude,Double longitude,boolean primaryAddress,LocalDateTime createdAt) {}
