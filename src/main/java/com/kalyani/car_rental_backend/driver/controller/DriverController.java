package com.kalyani.car_rental_backend.driver.controller;

import com.kalyani.car_rental_backend.driver.dto.DriverProfileRequest;
import com.kalyani.car_rental_backend.driver.entity.DriverProfile;
import com.kalyani.car_rental_backend.driver.repository.DriverProfileRepository;
import com.kalyani.car_rental_backend.exception.ResourceNotFoundException;
import com.kalyani.car_rental_backend.response.ApiResponse;
import com.kalyani.car_rental_backend.user.entity.Role;
import com.kalyani.car_rental_backend.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/driver")
public class DriverController {

    private final DriverProfileRepository driverProfiles;
    private final UserRepository users;

    public DriverController(
            DriverProfileRepository driverProfiles,
            UserRepository users
    ) {
        this.driverProfiles = driverProfiles;
        this.users = users;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> profile(
            Authentication authentication
    ) {
        DriverProfile profile = findProfile(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.success(toResponse(profile), "Driver profile loaded")
        );
    }

    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> saveProfile(
            @Valid @RequestBody DriverProfileRequest request,
            Authentication authentication
    ) {
        var user = users.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.DRIVER) {
            throw new IllegalArgumentException("Only DRIVER role can create a driver profile");
        }

        DriverProfile profile = driverProfiles
                .findByUserEmailIgnoreCase(authentication.getName())
                .orElseGet(() -> {
                    DriverProfile newProfile = new DriverProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        profile.setLicenseNumber(request.licenseNumber);
        profile.setAadhaarLast4(request.aadhaarLast4);
        profile.setVehicleNumber(request.vehicleNumber);
        profile.setVehicleModel(request.vehicleModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        toResponse(driverProfiles.save(profile)),
                        "Driver profile submitted for verification"
                )
        );
    }

    @PatchMapping("/availability")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateAvailability(
            @RequestParam boolean online,
            Authentication authentication
    ) {
        DriverProfile profile = findProfile(authentication.getName());

        if (online && !"APPROVED".equalsIgnoreCase(profile.getVerificationStatus())) {
            throw new IllegalArgumentException(
                    "Driver verification must be approved before going online"
            );
        }

        profile.setOnline(online);

        return ResponseEntity.ok(
                ApiResponse.success(
                        toResponse(driverProfiles.save(profile)),
                        online ? "You are online" : "You are offline"
                )
        );
    }

    @PatchMapping("/location")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            Authentication authentication
    ) {
        if (latitude == null || latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude == null || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }

        DriverProfile profile = findProfile(authentication.getName());
        profile.setCurrentLatitude(latitude);
        profile.setCurrentLongitude(longitude);

        return ResponseEntity.ok(
                ApiResponse.success(
                        toResponse(driverProfiles.save(profile)),
                        "Driver location updated"
                )
        );
    }

    private DriverProfile findProfile(String email) {
        return driverProfiles.findByUserEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found"));
    }

    private Map<String, Object> toResponse(DriverProfile profile) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", profile.getId());
        response.put("userId", profile.getUser().getId());
        response.put("name", profile.getUser().getFullName());
        response.put("email", profile.getUser().getEmail());
        response.put("phone", profile.getUser().getPhone());
        response.put("verificationStatus", profile.getVerificationStatus());
        response.put("online", profile.isOnline());
        response.put("licenseNumber", profile.getLicenseNumber());
        response.put("vehicleNumber", profile.getVehicleNumber());
        response.put("vehicleModel", profile.getVehicleModel());
        response.put("latitude", profile.getCurrentLatitude());
        response.put("longitude", profile.getCurrentLongitude());
        response.put("walletBalance", profile.getWalletBalance());
        response.put("totalEarnings", profile.getTotalEarnings());
        response.put("averageRating", profile.getAverageRating());
        response.put("completedTrips", profile.getCompletedTrips());
        response.put("updatedAt", profile.getUpdatedAt());
        return response;
    }
}
