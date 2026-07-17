package com.kalyani.car_rental_backend.payment.service;
import com.kalyani.car_rental_backend.payment.dto.*;
import java.util.List;
public interface PaymentService {
    PaymentResponse create(PaymentRequest r,String email);
    List<PaymentResponse> list(String email,boolean admin);
}
