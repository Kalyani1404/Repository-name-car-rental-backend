package com.kalyani.car_rental_backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(
            message = "Full name is required"
    )
    @Size(
            min = 3,
            max = 60,
            message = "Full name must contain 3 to 60 characters"
    )
    @Pattern(
            regexp = "^[A-Za-z]+(?:\\s+[A-Za-z]+)+$",
            message = "Please enter your complete name using letters only"
    )
    private String fullName;

    @NotBlank(
            message = "Email address is required"
    )
    @Email(
            message = "Please enter a valid email address"
    )
    private String email;

    @NotBlank(
            message = "Mobile number is required"
    )
    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Mobile number must contain exactly 10 digits"
    )
    private String phone;

    @NotBlank(
            message = "Password is required"
    )
    @Size(
            min = 7,
            max = 50,
            message = "Password must contain at least 7 characters"
    )
    @Pattern(
            regexp = "^(?=(?:.*[A-Za-z]){2,})(?=(?:.*[0-9]){3,})(?=(?:.*[^A-Za-z0-9\\s]){2,})\\S+$",
            message = "Password must contain at least 2 letters, 3 numbers and 2 symbols"
    )
    private String password;

    public RegisterRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(
            String fullName
    ) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(
            String email
    ) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(
            String phone
    ) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(
            String password
    ) {
        this.password = password;
    }
}