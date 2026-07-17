package com.kalyani.car_rental_backend.auth.dto;

import jakarta.validation.constraints.*;

public class DriverRegisterRequest extends RegisterRequest {
    @NotBlank @Size(max=40) private String licenseNumber;
    @NotBlank @Size(max=30) private String vehicleNumber;
    @NotBlank @Size(max=80) private String vehicleModel;
    @Pattern(regexp="^[0-9]{4}$", message="Enter last 4 Aadhaar digits") private String aadhaarLast4;
    public String getLicenseNumber(){return licenseNumber;} public void setLicenseNumber(String v){licenseNumber=v;}
    public String getVehicleNumber(){return vehicleNumber;} public void setVehicleNumber(String v){vehicleNumber=v;}
    public String getVehicleModel(){return vehicleModel;} public void setVehicleModel(String v){vehicleModel=v;}
    public String getAadhaarLast4(){return aadhaarLast4;} public void setAadhaarLast4(String v){aadhaarLast4=v;}
}
