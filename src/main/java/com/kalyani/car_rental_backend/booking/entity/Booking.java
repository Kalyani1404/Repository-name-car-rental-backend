package com.kalyani.car_rental_backend.booking.entity;
import com.kalyani.car_rental_backend.car.entity.Car;
import com.kalyani.car_rental_backend.user.entity.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="bookings")
public class Booking {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,unique=true) private String bookingReference;
    @ManyToOne(optional=false) private User user;
    @ManyToOne(optional=false) private Car car;
    @Column(nullable=false) private LocalDateTime startDate;
    @Column(nullable=false) private LocalDateTime endDate;
    @Column(nullable=false) private String startPoint;
    @Column(nullable=false) private String endPoint;
    private Boolean includeInsurance=true; private String insuranceType; private String couponCode;
    @Column(length=2000) private String specialInstructions;
    private Long durationDays; private BigDecimal rentalAmount; private BigDecimal insuranceAmount; private BigDecimal grandTotal;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private BookingStatus status=BookingStatus.PENDING;
    @Column(nullable=false,updatable=false) private LocalDateTime createdAt;
    @PrePersist void pre(){createdAt=LocalDateTime.now();if(status==null)status=BookingStatus.PENDING;}

    public Long getId(){return id;} public String getBookingReference(){return bookingReference;} public void setBookingReference(String v){bookingReference=v;}
    public User getUser(){return user;} public void setUser(User v){user=v;} public Car getCar(){return car;} public void setCar(Car v){car=v;}
    public LocalDateTime getStartDate(){return startDate;} public void setStartDate(LocalDateTime v){startDate=v;}
    public LocalDateTime getEndDate(){return endDate;} public void setEndDate(LocalDateTime v){endDate=v;}
    public String getStartPoint(){return startPoint;} public void setStartPoint(String v){startPoint=v;}
    public String getEndPoint(){return endPoint;} public void setEndPoint(String v){endPoint=v;}
    public Boolean getIncludeInsurance(){return includeInsurance;} public void setIncludeInsurance(Boolean v){includeInsurance=v;}
    public String getInsuranceType(){return insuranceType;} public void setInsuranceType(String v){insuranceType=v;}
    public String getCouponCode(){return couponCode;} public void setCouponCode(String v){couponCode=v;}
    public String getSpecialInstructions(){return specialInstructions;} public void setSpecialInstructions(String v){specialInstructions=v;}
    public Long getDurationDays(){return durationDays;} public void setDurationDays(Long v){durationDays=v;}
    public BigDecimal getRentalAmount(){return rentalAmount;} public void setRentalAmount(BigDecimal v){rentalAmount=v;}
    public BigDecimal getInsuranceAmount(){return insuranceAmount;} public void setInsuranceAmount(BigDecimal v){insuranceAmount=v;}
    public BigDecimal getGrandTotal(){return grandTotal;} public void setGrandTotal(BigDecimal v){grandTotal=v;}
    public BookingStatus getStatus(){return status;} public void setStatus(BookingStatus v){status=v;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
