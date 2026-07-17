package com.kalyani.car_rental_backend.address.repository;
import com.kalyani.car_rental_backend.address.entity.SavedAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SavedAddressRepository extends JpaRepository<SavedAddress,Long>{
    List<SavedAddress> findByUserEmailIgnoreCaseOrderByPrimaryAddressDescCreatedAtDesc(String email);
}
