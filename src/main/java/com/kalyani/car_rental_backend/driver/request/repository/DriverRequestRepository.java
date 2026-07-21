package com.kalyani.car_rental_backend.driver.request.repository;
import com.kalyani.car_rental_backend.driver.request.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.*;
public interface DriverRequestRepository extends JpaRepository<DriverRequest,Long>{
    List<DriverRequest> findByDriverUserEmailIgnoreCaseAndStatusOrderByCreatedAtDesc(String email,DriverRequestStatus status);
    List<DriverRequest> findByBookingId(Long bookingId);
    List<DriverRequest> findByStatusAndExpiresAtBefore(DriverRequestStatus status, LocalDateTime time);
    Optional<DriverRequest> findByIdAndDriverUserEmailIgnoreCase(Long id,String email);
}
