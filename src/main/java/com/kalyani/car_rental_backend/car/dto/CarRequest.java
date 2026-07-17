package com.kalyani.car_rental_backend.car.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CarRequest {
    @NotBlank private String name; @NotBlank private String brand; @NotBlank private String model;
    private Integer year; private String color; @NotBlank private String registrationNumber;
    private String fuelType; private String transmission; private Integer seatingCapacity;
    @NotNull private BigDecimal pricePerDay; private BigDecimal pricePerWeek; private BigDecimal pricePerMonth;
    private String currentLocationCity; private Double currentLocationLatitude; private Double currentLocationLongitude;
    private String insuranceProvider; private String insurancePolicyNumber; private String features; private String fuelPolicy;
    private Integer mileageLimitPerDay; private BigDecimal lateFeePerHour; private BigDecimal securityDeposit; private String imageUrl;

    public String getName(){return name;} public void setName(String v){name=v;}
    public String getBrand(){return brand;} public void setBrand(String v){brand=v;}
    public String getModel(){return model;} public void setModel(String v){model=v;}
    public Integer getYear(){return year;} public void setYear(Integer v){year=v;}
    public String getColor(){return color;} public void setColor(String v){color=v;}
    public String getRegistrationNumber(){return registrationNumber;} public void setRegistrationNumber(String v){registrationNumber=v;}
    public String getFuelType(){return fuelType;} public void setFuelType(String v){fuelType=v;}
    public String getTransmission(){return transmission;} public void setTransmission(String v){transmission=v;}
    public Integer getSeatingCapacity(){return seatingCapacity;} public void setSeatingCapacity(Integer v){seatingCapacity=v;}
    public BigDecimal getPricePerDay(){return pricePerDay;} public void setPricePerDay(BigDecimal v){pricePerDay=v;}
    public BigDecimal getPricePerWeek(){return pricePerWeek;} public void setPricePerWeek(BigDecimal v){pricePerWeek=v;}
    public BigDecimal getPricePerMonth(){return pricePerMonth;} public void setPricePerMonth(BigDecimal v){pricePerMonth=v;}
    public String getCurrentLocationCity(){return currentLocationCity;} public void setCurrentLocationCity(String v){currentLocationCity=v;}
    public Double getCurrentLocationLatitude(){return currentLocationLatitude;} public void setCurrentLocationLatitude(Double v){currentLocationLatitude=v;}
    public Double getCurrentLocationLongitude(){return currentLocationLongitude;} public void setCurrentLocationLongitude(Double v){currentLocationLongitude=v;}
    public String getInsuranceProvider(){return insuranceProvider;} public void setInsuranceProvider(String v){insuranceProvider=v;}
    public String getInsurancePolicyNumber(){return insurancePolicyNumber;} public void setInsurancePolicyNumber(String v){insurancePolicyNumber=v;}
    public String getFeatures(){return features;} public void setFeatures(String v){features=v;}
    public String getFuelPolicy(){return fuelPolicy;} public void setFuelPolicy(String v){fuelPolicy=v;}
    public Integer getMileageLimitPerDay(){return mileageLimitPerDay;} public void setMileageLimitPerDay(Integer v){mileageLimitPerDay=v;}
    public BigDecimal getLateFeePerHour(){return lateFeePerHour;} public void setLateFeePerHour(BigDecimal v){lateFeePerHour=v;}
    public BigDecimal getSecurityDeposit(){return securityDeposit;} public void setSecurityDeposit(BigDecimal v){securityDeposit=v;}
    public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
}
