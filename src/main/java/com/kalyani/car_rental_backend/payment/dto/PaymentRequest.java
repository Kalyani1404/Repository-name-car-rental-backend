package com.kalyani.car_rental_backend.payment.dto;
import jakarta.validation.constraints.NotNull;
public class PaymentRequest {
    @NotNull private Long bookingId; private String paymentMethod="UPI";
    public Long getBookingId(){return bookingId;} public void setBookingId(Long v){bookingId=v;}
    public String getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(String v){paymentMethod=v;}
}
