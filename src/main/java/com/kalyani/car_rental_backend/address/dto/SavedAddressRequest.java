package com.kalyani.car_rental_backend.address.dto;
import jakarta.validation.constraints.*;
public class SavedAddressRequest {
    @NotBlank @Size(max=40) public String label;
    @NotBlank @Size(max=500) public String addressLine;
    @Size(max=80) public String city; @Size(max=80) public String state;
    @Pattern(regexp="^$|^[0-9]{6}$",message="Postal code must be 6 digits") public String postalCode;
    public Double latitude; public Double longitude; public boolean primaryAddress;
}
