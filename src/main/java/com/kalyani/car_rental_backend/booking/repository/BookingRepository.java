package com.kalyani.car_rental_backend.booking.repository;

import com.kalyani.car_rental_backend.booking.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserEmailIgnoreCaseOrderByCreatedAtDesc(String email);
    Page<Booking> findByUserEmailIgnoreCase(String email, Pageable pageable);
}
