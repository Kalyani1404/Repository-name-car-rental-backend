package com.kalyani.car_rental_backend.car.dto;

import java.math.BigDecimal;

public class CarResponse {

    private Long id;

    private String name;
    private String brand;
    private String model;

    private Integer year;
    private String color;

    private String registrationNumber;

    private String fuelType;
    private String transmission;
    private Integer seatingCapacity;

    private BigDecimal pricePerDay;
    private BigDecimal pricePerWeek;
    private BigDecimal pricePerMonth;

    private String currentLocationCity;
    private Double currentLocationLatitude;
    private Double currentLocationLongitude;

    private String insuranceProvider;
    private String insurancePolicyNumber;

    private String features;
    private String fuelPolicy;

    private Integer mileageLimitPerDay;

    private BigDecimal lateFeePerHour;
    private BigDecimal securityDeposit;

    private String imageUrl;

    private Boolean isAvailable;
    private String availabilityStatus;

    private Double rating;

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

    public void setTransmission(
            String transmission
    ) {
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
        this.currentLocationCity =
                currentLocationCity;
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

    public void setFuelPolicy(
            String fuelPolicy
    ) {
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

    public void setImageUrl(
            String imageUrl
    ) {
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
}