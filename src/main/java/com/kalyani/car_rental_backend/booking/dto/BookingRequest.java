package com.kalyani.car_rental_backend.booking.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
public class BookingRequest {
    @NotNull private Long carId; @NotNull private LocalDateTime startDate; @NotNull private LocalDateTime endDate;
    @NotBlank private String startPoint; @NotBlank private String endPoint;
    private Boolean includeInsurance=true; private String insuranceType="BASIC"; private String couponCode; private String specialInstructions;
    public Long getCarId(){return carId;} public void setCarId(Long v){carId=v;}
    public LocalDateTime getStartDate(){return startDate;} public void setStartDate(LocalDateTime v){startDate=v;}
    public LocalDateTime getEndDate(){return endDate;} public void setEndDate(LocalDateTime v){endDate=v;}
    public String getStartPoint(){return startPoint;} public void setStartPoint(String v){startPoint=v;}
    public String getEndPoint(){return endPoint;} public void setEndPoint(String v){endPoint=v;}
    public Boolean getIncludeInsurance(){return includeInsurance;} public void setIncludeInsurance(Boolean v){includeInsurance=v;}
    public String getInsuranceType(){return insuranceType;} public void setInsuranceType(String v){insuranceType=v;}
    public String getCouponCode(){return couponCode;} public void setCouponCode(String v){couponCode=v;}
    public String getSpecialInstructions(){return specialInstructions;} public void setSpecialInstructions(String v){specialInstructions=v;}
}
