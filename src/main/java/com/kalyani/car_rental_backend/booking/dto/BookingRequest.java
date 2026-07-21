package com.kalyani.car_rental_backend.booking.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class BookingRequest {
    @NotNull private Long carId;
    @NotNull private LocalDateTime startDate;
    @NotNull private LocalDateTime endDate;
    @NotBlank private String startPoint;
    @NotBlank private String endPoint;
    private String bookingType = "SELF_DRIVE";
    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropLatitude;
    private Double dropLongitude;
    private Boolean includeInsurance = true;
    private String insuranceType = "BASIC";
    private String couponCode;
    private String specialInstructions;
    private String drivingLicenseNumber;
    private String identityProofReference;
    private Boolean ageConfirmed = false;

    public Long getCarId(){return carId;} public void setCarId(Long v){carId=v;}
    public LocalDateTime getStartDate(){return startDate;} public void setStartDate(LocalDateTime v){startDate=v;}
    public LocalDateTime getEndDate(){return endDate;} public void setEndDate(LocalDateTime v){endDate=v;}
    public String getStartPoint(){return startPoint;} public void setStartPoint(String v){startPoint=v;}
    public String getEndPoint(){return endPoint;} public void setEndPoint(String v){endPoint=v;}
    public String getBookingType(){return bookingType;} public void setBookingType(String v){bookingType=v;}
    public Double getPickupLatitude(){return pickupLatitude;} public void setPickupLatitude(Double v){pickupLatitude=v;}
    public Double getPickupLongitude(){return pickupLongitude;} public void setPickupLongitude(Double v){pickupLongitude=v;}
    public Double getDropLatitude(){return dropLatitude;} public void setDropLatitude(Double v){dropLatitude=v;}
    public Double getDropLongitude(){return dropLongitude;} public void setDropLongitude(Double v){dropLongitude=v;}
    public Boolean getIncludeInsurance(){return includeInsurance;} public void setIncludeInsurance(Boolean v){includeInsurance=v;}
    public String getInsuranceType(){return insuranceType;} public void setInsuranceType(String v){insuranceType=v;}
    public String getCouponCode(){return couponCode;} public void setCouponCode(String v){couponCode=v;}
    public String getSpecialInstructions(){return specialInstructions;} public void setSpecialInstructions(String v){specialInstructions=v;}
    public String getDrivingLicenseNumber(){return drivingLicenseNumber;} public void setDrivingLicenseNumber(String v){drivingLicenseNumber=v;}
    public String getIdentityProofReference(){return identityProofReference;} public void setIdentityProofReference(String v){identityProofReference=v;}
    public Boolean getAgeConfirmed(){return ageConfirmed;} public void setAgeConfirmed(Boolean v){ageConfirmed=v;}
}
