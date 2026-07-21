package com.kalyani.car_rental_backend.payment.repository;

import com.kalyani.car_rental_backend.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserEmailIgnoreCaseOrderByCreatedAtDesc(String email);
    Page<Payment> findByUserEmailIgnoreCase(String email, Pageable pageable);
    boolean existsByBookingId(Long bookingId);
}
