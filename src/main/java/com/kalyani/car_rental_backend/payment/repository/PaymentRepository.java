package com.kalyani.car_rental_backend.payment.repository;
import com.kalyani.car_rental_backend.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PaymentRepository extends JpaRepository<Payment,Long>{
    List<Payment> findByUserEmailIgnoreCaseOrderByCreatedAtDesc(String email);
    boolean existsByBookingId(Long bookingId);
}
