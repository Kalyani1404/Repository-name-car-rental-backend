package com.kalyani.car_rental_backend.car.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "cars",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_cars_registration",
                        columnNames = "registration_number"
                )
        }
)
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    private Integer year;

    private String color;

    @Column(
            name = "registration_number",
            nullable = false,
            unique = true
    )
    private String registrationNumber;

    @Column(name = "fuel_type")
    private String fuelType;

    private String transmission;

    @Column(name = "seating_capacity")
    private Integer seatingCapacity;

    @Column(name = "price_per_day")
    private BigDecimal pricePerDay;

    @Column(name = "price_per_week")
    private BigDecimal pricePerWeek;

    @Column(name = "price_per_month")
    private BigDecimal pricePerMonth;

    @Column(name = "current_location_city")
    private String currentLocationCity;

    @Column(name = "current_location_latitude")
    private Double currentLocationLatitude;

    @Column(name = "current_location_longitude")
    private Double currentLocationLongitude;

    @Column(name = "insurance_provider")
    private String insuranceProvider;

    @Column(name = "insurance_policy_number")
    private String insurancePolicyNumber;

    @Column(length = 2000)
    private String features;

    @Column(name = "fuel_policy")
    private String fuelPolicy;

    @Column(name = "mileage_limit_per_day")
    private Integer mileageLimitPerDay;

    @Column(name = "late_fee_per_hour")
    private BigDecimal lateFeePerHour;

    @Column(name = "security_deposit")
    private BigDecimal securityDeposit;

    @Column(name = "image_url", length = 2000)
    private String imageUrl;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "availability_status", nullable = false)
    private String availabilityStatus = "AVAILABLE";

    private Double rating = 4.8;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        updatedAt = now;

        if (isAvailable == null) {
            isAvailable = true;
        }

        if (availabilityStatus == null
                || availabilityStatus.isBlank()) {
            availabilityStatus = "AVAILABLE";
        }

        if (rating == null) {
            rating = 4.8;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(
            String registrationNumber
    ) {
        this.registrationNumber = registrationNumber;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(
            Integer seatingCapacity
    ) {
        this.seatingCapacity = seatingCapacity;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(
            BigDecimal pricePerDay
    ) {
        this.pricePerDay = pricePerDay;
    }

    public BigDecimal getPricePerWeek() {
        return pricePerWeek;
    }

    public void setPricePerWeek(
            BigDecimal pricePerWeek
    ) {
        this.pricePerWeek = pricePerWeek;
    }

    public BigDecimal getPricePerMonth() {
        return pricePerMonth;
    }

    public void setPricePerMonth(
            BigDecimal pricePerMonth
    ) {
        this.pricePerMonth = pricePerMonth;
    }

    public String getCurrentLocationCity() {
        return currentLocationCity;
    }

    public void setCurrentLocationCity(
            String currentLocationCity
    ) {
        this.currentLocationCity = currentLocationCity;
    }

    public Double getCurrentLocationLatitude() {
        return currentLocationLatitude;
    }

    public void setCurrentLocationLatitude(
            Double currentLocationLatitude
    ) {
        this.currentLocationLatitude =
                currentLocationLatitude;
    }

    public Double getCurrentLocationLongitude() {
        return currentLocationLongitude;
    }

    public void setCurrentLocationLongitude(
            Double currentLocationLongitude
    ) {
        this.currentLocationLongitude =
                currentLocationLongitude;
    }

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(
            String insuranceProvider
    ) {
        this.insuranceProvider = insuranceProvider;
    }

    public String getInsurancePolicyNumber() {
        return insurancePolicyNumber;
    }

    public void setInsurancePolicyNumber(
            String insurancePolicyNumber
    ) {
        this.insurancePolicyNumber =
                insurancePolicyNumber;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getFuelPolicy() {
        return fuelPolicy;
    }

    public void setFuelPolicy(String fuelPolicy) {
        this.fuelPolicy = fuelPolicy;
    }

    public Integer getMileageLimitPerDay() {
        return mileageLimitPerDay;
    }

    public void setMileageLimitPerDay(
            Integer mileageLimitPerDay
    ) {
        this.mileageLimitPerDay =
                mileageLimitPerDay;
    }

    public BigDecimal getLateFeePerHour() {
        return lateFeePerHour;
    }

    public void setLateFeePerHour(
            BigDecimal lateFeePerHour
    ) {
        this.lateFeePerHour = lateFeePerHour;
    }

    public BigDecimal getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(
            BigDecimal securityDeposit
    ) {
        this.securityDeposit = securityDeposit;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(
            Boolean available
    ) {
        isAvailable = available;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(
            String availabilityStatus
    ) {
        this.availabilityStatus =
                availabilityStatus;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt
    ) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(
            LocalDateTime updatedAt
    ) {
        this.updatedAt = updatedAt;
    }
}