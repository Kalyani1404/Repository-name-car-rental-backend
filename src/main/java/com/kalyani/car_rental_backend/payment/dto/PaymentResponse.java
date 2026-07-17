package com.kalyani.car_rental_backend.payment.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class PaymentResponse {
    public Long id; public String transactionReference; public Long bookingId; public BigDecimal amount;
    public String paymentMethod; public String status; public LocalDateTime createdAt;
}
