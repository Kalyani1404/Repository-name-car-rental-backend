package com.kalyani.car_rental_backend.payment.entity;
import com.kalyani.car_rental_backend.booking.entity.Booking;
import com.kalyani.car_rental_backend.user.entity.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="payments")
public class Payment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,unique=true) private String transactionReference;
    @ManyToOne(optional=false) private Booking booking;
    @ManyToOne(optional=false) private User user;
    @Column(nullable=false) private BigDecimal amount;
    private String paymentMethod;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private PaymentStatus status=PaymentStatus.SUCCESS;
    @Column(nullable=false,updatable=false) private LocalDateTime createdAt;
    @PrePersist void pre(){createdAt=LocalDateTime.now();if(status==null)status=PaymentStatus.SUCCESS;}
    public Long getId(){return id;} public String getTransactionReference(){return transactionReference;} public void setTransactionReference(String v){transactionReference=v;}
    public Booking getBooking(){return booking;} public void setBooking(Booking v){booking=v;} public User getUser(){return user;} public void setUser(User v){user=v;}
    public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;} public String getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(String v){paymentMethod=v;}
    public PaymentStatus getStatus(){return status;} public void setStatus(PaymentStatus v){status=v;} public LocalDateTime getCreatedAt(){return createdAt;}
}
