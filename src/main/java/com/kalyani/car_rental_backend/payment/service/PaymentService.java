package com.kalyani.car_rental_backend.payment.service;

import com.kalyani.car_rental_backend.payment.dto.PaymentRequest;
import com.kalyani.car_rental_backend.payment.dto.PaymentResponse;
import com.kalyani.car_rental_backend.response.PageResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse create(PaymentRequest r, String email);
    List<PaymentResponse> list(String email, boolean admin);
    PageResponse<PaymentResponse> listPaged(String email, boolean admin, int page, int size, String sortBy, String sortDir);
}
