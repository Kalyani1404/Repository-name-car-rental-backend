package com.kalyani.car_rental_backend.user.dto;

public class UserResponse {
    private Long id; private String fullName; private String email; private String phone;
    private String role; private String provider; private String status;

    public UserResponse() {}
    public UserResponse(Long id,String fullName,String email,String phone,String role,String provider,String status){
        this.id=id; this.fullName=fullName; this.email=email; this.phone=phone;
        this.role=role; this.provider=provider; this.status=status;
    }
    public Long getId(){return id;} public String getFullName(){return fullName;}
    public String getEmail(){return email;} public String getPhone(){return phone;}
    public String getRole(){return role;} public String getProvider(){return provider;}
    public String getStatus(){return status;}
}
