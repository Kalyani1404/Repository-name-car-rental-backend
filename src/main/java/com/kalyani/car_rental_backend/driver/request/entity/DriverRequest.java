package com.kalyani.car_rental_backend.driver.request.entity;

import com.kalyani.car_rental_backend.booking.entity.Booking;
import com.kalyani.car_rental_backend.driver.entity.DriverProfile;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="driver_requests")
public class DriverRequest {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) private Booking booking;
    @ManyToOne(optional=false) private DriverProfile driver;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private DriverRequestStatus status=DriverRequestStatus.PENDING;
    private Double distanceKm;
    private Integer dispatchRank;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    @PrePersist void pre(){createdAt=LocalDateTime.now();if(expiresAt==null)expiresAt=createdAt.plusSeconds(30);}
    public Long getId(){return id;}
    public Booking getBooking(){return booking;} public void setBooking(Booking v){booking=v;}
    public DriverProfile getDriver(){return driver;} public void setDriver(DriverProfile v){driver=v;}
    public DriverRequestStatus getStatus(){return status;} public void setStatus(DriverRequestStatus v){status=v;}
    public Double getDistanceKm(){return distanceKm;} public void setDistanceKm(Double v){distanceKm=v;}
    public Integer getDispatchRank(){return dispatchRank;} public void setDispatchRank(Integer v){dispatchRank=v;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public LocalDateTime getExpiresAt(){return expiresAt;} public void setExpiresAt(LocalDateTime v){expiresAt=v;}
}
