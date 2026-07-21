package com.kalyani.car_rental_backend.driver.controller;

import com.kalyani.car_rental_backend.booking.entity.BookingStatus;
import com.kalyani.car_rental_backend.booking.repository.BookingRepository;
import com.kalyani.car_rental_backend.driver.dto.DriverProfileRequest;
import com.kalyani.car_rental_backend.driver.entity.DriverProfile;
import com.kalyani.car_rental_backend.driver.repository.DriverProfileRepository;
import com.kalyani.car_rental_backend.driver.request.service.DriverDispatchService;
import com.kalyani.car_rental_backend.exception.ResourceNotFoundException;
import com.kalyani.car_rental_backend.notification.entity.Notification;
import com.kalyani.car_rental_backend.notification.repository.NotificationRepository;
import com.kalyani.car_rental_backend.response.ApiResponse;
import com.kalyani.car_rental_backend.user.entity.Role;
import com.kalyani.car_rental_backend.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/driver")
public class DriverController {
    private final DriverProfileRepository driverProfiles;
    private final UserRepository users;
    private final DriverDispatchService dispatch;
    private final BookingRepository bookings;
    private final NotificationRepository notifications;

    public DriverController(DriverProfileRepository driverProfiles, UserRepository users,
                            DriverDispatchService dispatch, BookingRepository bookings,
                            NotificationRepository notifications) {
        this.driverProfiles=driverProfiles; this.users=users; this.dispatch=dispatch; this.bookings=bookings; this.notifications=notifications;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String,Object>>> profile(Authentication a){return ResponseEntity.ok(ApiResponse.success(toResponse(findProfile(a.getName())),"Driver profile loaded"));}

    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String,Object>>> saveProfile(@Valid @RequestBody DriverProfileRequest r,Authentication a){
        var user=users.findByEmailIgnoreCase(a.getName()).orElseThrow(()->new ResourceNotFoundException("User not found"));
        if(user.getRole()!= Role.DRIVER) throw new IllegalArgumentException("Only DRIVER role can create a driver profile");
        DriverProfile p=driverProfiles.findByUserEmailIgnoreCase(a.getName()).orElseGet(()->{DriverProfile x=new DriverProfile();x.setUser(user);return x;});
        p.setLicenseNumber(r.licenseNumber);p.setAadhaarLast4(r.aadhaarLast4);p.setVehicleNumber(r.vehicleNumber);p.setVehicleModel(r.vehicleModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(toResponse(driverProfiles.save(p)),"Driver profile submitted for verification"));
    }

    @PatchMapping("/availability")
    public ResponseEntity<ApiResponse<Map<String,Object>>> updateAvailability(@RequestParam boolean online,Authentication a){
        DriverProfile p=findProfile(a.getName());
        if(online&&!"APPROVED".equalsIgnoreCase(p.getVerificationStatus())) throw new IllegalArgumentException("Driver verification must be approved before going online");
        p.setOnline(online);return ResponseEntity.ok(ApiResponse.success(toResponse(driverProfiles.save(p)),online?"You are online":"You are offline"));
    }

    @PatchMapping("/location")
    public ResponseEntity<ApiResponse<Map<String,Object>>> updateLocation(@RequestParam Double latitude,@RequestParam Double longitude,Authentication a){
        if(latitude==null||latitude<-90||latitude>90)throw new IllegalArgumentException("Latitude must be between -90 and 90");
        if(longitude==null||longitude<-180||longitude>180)throw new IllegalArgumentException("Longitude must be between -180 and 180");
        DriverProfile p=findProfile(a.getName());p.setCurrentLatitude(latitude);p.setCurrentLongitude(longitude);
        return ResponseEntity.ok(ApiResponse.success(toResponse(driverProfiles.save(p)),"Driver location updated"));
    }

    @GetMapping("/requests")
    public ResponseEntity<ApiResponse<List<Map<String,Object>>>> pendingRequests(Authentication a){
        return ResponseEntity.ok(ApiResponse.success(dispatch.pendingForDriver(a.getName()),"Pending trip requests loaded"));
    }

    @PostMapping("/requests/{id}/accept")
    public ResponseEntity<ApiResponse<Void>> accept(@PathVariable Long id,Authentication a){dispatch.respond(id,a.getName(),true);return ResponseEntity.ok(ApiResponse.success(null,"Trip accepted"));}

    @PostMapping("/requests/{id}/reject")
    public ResponseEntity<ApiResponse<Void>> reject(@PathVariable Long id,Authentication a){dispatch.respond(id,a.getName(),false);return ResponseEntity.ok(ApiResponse.success(null,"Trip rejected and sent to next nearest driver"));}

    @PatchMapping("/bookings/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateTripStatus(@PathVariable Long id,@RequestParam String status,Authentication a){
        DriverProfile driver=findProfile(a.getName());
        var booking=bookings.findById(id).orElseThrow(()->new ResourceNotFoundException("Booking not found"));
        if(booking.getAssignedDriver()==null||!booking.getAssignedDriver().getId().equals(driver.getId())) throw new IllegalArgumentException("This trip is not assigned to you");
        BookingStatus next;
        try{next=BookingStatus.valueOf(status.toUpperCase());}catch(Exception e){throw new IllegalArgumentException("Invalid trip status");}
        if(!List.of(BookingStatus.ARRIVED,BookingStatus.ACTIVE,BookingStatus.COMPLETED).contains(next)) throw new IllegalArgumentException("Driver can only mark ARRIVED, ACTIVE or COMPLETED");
        booking.setStatus(next);bookings.save(booking);
        if(next==BookingStatus.COMPLETED){driver.setOnline(true);driver.setCompletedTrips(driver.getCompletedTrips()+1);driverProfiles.save(driver);}
        Notification n=new Notification();n.setUser(booking.getUser());n.setTitle("Trip status updated");n.setMessage("Your trip " + booking.getBookingReference() + " is now " + next.name().replace('_',' ') + ".");n.setType("TRIP");notifications.save(n);
        return ResponseEntity.ok(ApiResponse.success(null,"Trip status updated"));
    }

    private DriverProfile findProfile(String email){return driverProfiles.findByUserEmailIgnoreCase(email).orElseThrow(()->new ResourceNotFoundException("Driver profile not found"));}
    private Map<String,Object> toResponse(DriverProfile p){Map<String,Object>m=new LinkedHashMap<>();m.put("id",p.getId());m.put("userId",p.getUser().getId());m.put("name",p.getUser().getFullName());m.put("email",p.getUser().getEmail());m.put("phone",p.getUser().getPhone());m.put("verificationStatus",p.getVerificationStatus());m.put("online",p.isOnline());m.put("licenseNumber",p.getLicenseNumber());m.put("vehicleNumber",p.getVehicleNumber());m.put("vehicleModel",p.getVehicleModel());m.put("latitude",p.getCurrentLatitude());m.put("longitude",p.getCurrentLongitude());m.put("walletBalance",p.getWalletBalance());m.put("totalEarnings",p.getTotalEarnings());m.put("averageRating",p.getAverageRating());m.put("completedTrips",p.getCompletedTrips());m.put("updatedAt",p.getUpdatedAt());return m;}
}
