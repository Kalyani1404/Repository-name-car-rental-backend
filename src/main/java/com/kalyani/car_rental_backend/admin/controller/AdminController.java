package com.kalyani.car_rental_backend.admin.controller;

import com.kalyani.car_rental_backend.booking.repository.BookingRepository;
import com.kalyani.car_rental_backend.car.repository.CarRepository;
import com.kalyani.car_rental_backend.driver.entity.DriverProfile;
import com.kalyani.car_rental_backend.driver.repository.DriverProfileRepository;
import com.kalyani.car_rental_backend.exception.ResourceNotFoundException;
import com.kalyani.car_rental_backend.payment.repository.PaymentRepository;
import com.kalyani.car_rental_backend.response.ApiResponse;
import com.kalyani.car_rental_backend.response.PageResponse;
import com.kalyani.car_rental_backend.response.PaginationUtils;
import com.kalyani.car_rental_backend.user.entity.Role;
import com.kalyani.car_rental_backend.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository users;
    private final CarRepository cars;
    private final BookingRepository bookings;
    private final PaymentRepository payments;
    private final DriverProfileRepository drivers;

    public AdminController(UserRepository users,
                           CarRepository cars,
                           BookingRepository bookings,
                           PaymentRepository payments,
                           DriverProfileRepository drivers) {
        this.users = users;
        this.cars = cars;
        this.bookings = bookings;
        this.payments = payments;
        this.drivers = drivers;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> dashboard() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("totalUsers", users.count());
        m.put("users", users.findAll().stream().filter(x -> x.getRole() == Role.USER).count());
        m.put("drivers", users.findAll().stream().filter(x -> x.getRole() == Role.DRIVER).count());
        m.put("cars", cars.count());
        m.put("bookings", bookings.count());
        m.put("payments", payments.count());
        m.put("pendingDriverApprovals", drivers.findAll().stream()
                .filter(x -> "PENDING".equals(x.getVerificationStatus())).count());
        return ResponseEntity.ok(ApiResponse.success(m, "Admin dashboard loaded"));
    }

    // Existing endpoint kept unchanged.
    @GetMapping("/drivers")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> driverList() {
        return ResponseEntity.ok(
                ApiResponse.success(drivers.findAll().stream().map(this::out).toList(), "Drivers loaded")
        );
    }

    // New pagination + sorting endpoint for admin driver management.
    @GetMapping("/drivers/page")
    public ResponseEntity<ApiResponse<PageResponse<Map<String, Object>>>> pagedDrivers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Pageable pageable = PaginationUtils.createPageable(
                page,
                size,
                sortBy,
                sortDir,
                Set.of("id", "verificationStatus", "online", "completedTrips", "averageRating", "updatedAt")
        );

        Page<Map<String, Object>> result = drivers.findAll(pageable).map(this::out);
        return ResponseEntity.ok(
                ApiResponse.success(PageResponse.from(result, sortBy, sortDir), "Drivers loaded with pagination and sorting")
        );
    }

    @PatchMapping("/drivers/{id}/verification")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verify(
            @PathVariable Long id, @RequestParam String status) {
        if (!List.of("PENDING", "APPROVED", "REJECTED").contains(status.toUpperCase())) {
            throw new IllegalArgumentException("Invalid verification status");
        }
        DriverProfile d = drivers.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        d.setVerificationStatus(status.toUpperCase());
        if (!"APPROVED".equalsIgnoreCase(status)) {
            d.setOnline(false);
        }
        return ResponseEntity.ok(
                ApiResponse.success(out(drivers.save(d)), "Driver verification updated")
        );
    }

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<ApiResponse<Void>> status(
            @PathVariable Long id, @RequestParam String status) {
        var u = users.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        u.setStatus(status.toUpperCase());
        users.save(u);
        return ResponseEntity.ok(ApiResponse.success(null, "User status updated"));
    }

    private Map<String, Object> out(DriverProfile d) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", d.getId());
        m.put("userId", d.getUser().getId());
        m.put("name", d.getUser().getFullName());
        m.put("email", d.getUser().getEmail());
        m.put("phone", d.getUser().getPhone());
        m.put("verificationStatus", d.getVerificationStatus());
        m.put("online", d.isOnline());
        m.put("licenseNumber", d.getLicenseNumber());
        m.put("vehicleNumber", d.getVehicleNumber());
        m.put("vehicleModel", d.getVehicleModel());
        m.put("walletBalance", d.getWalletBalance());
        m.put("totalEarnings", d.getTotalEarnings());
        m.put("completedTrips", d.getCompletedTrips());
        return m;
    }
}
