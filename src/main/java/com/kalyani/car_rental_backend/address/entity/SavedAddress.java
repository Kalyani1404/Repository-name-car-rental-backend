package com.kalyani.car_rental_backend.address.entity;

import com.kalyani.car_rental_backend.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "saved_addresses")
public class SavedAddress {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) private User user;
    @Column(nullable = false) private String label;
    @Column(nullable = false, length = 500) private String addressLine;
    private String city; private String state; private String postalCode;
    private Double latitude; private Double longitude;
    @Column(nullable = false) private boolean primaryAddress;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    @PrePersist void pre(){createdAt=LocalDateTime.now();}
    public Long getId(){return id;} public User getUser(){return user;} public void setUser(User v){user=v;}
    public String getLabel(){return label;} public void setLabel(String v){label=v;} public String getAddressLine(){return addressLine;} public void setAddressLine(String v){addressLine=v;}
    public String getCity(){return city;} public void setCity(String v){city=v;} public String getState(){return state;} public void setState(String v){state=v;}
    public String getPostalCode(){return postalCode;} public void setPostalCode(String v){postalCode=v;} public Double getLatitude(){return latitude;} public void setLatitude(Double v){latitude=v;}
    public Double getLongitude(){return longitude;} public void setLongitude(Double v){longitude=v;} public boolean isPrimaryAddress(){return primaryAddress;} public void setPrimaryAddress(boolean v){primaryAddress=v;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
