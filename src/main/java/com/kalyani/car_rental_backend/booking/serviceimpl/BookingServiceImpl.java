package com.kalyani.car_rental_backend.booking.serviceimpl;

import com.kalyani.car_rental_backend.booking.dto.*;
import com.kalyani.car_rental_backend.booking.entity.*;
import com.kalyani.car_rental_backend.booking.repository.BookingRepository;
import com.kalyani.car_rental_backend.booking.service.BookingService;
import com.kalyani.car_rental_backend.car.repository.CarRepository;
import com.kalyani.car_rental_backend.driver.request.service.DriverDispatchService;
import com.kalyani.car_rental_backend.exception.ResourceNotFoundException;
import com.kalyani.car_rental_backend.notification.entity.Notification;
import com.kalyani.car_rental_backend.notification.repository.NotificationRepository;
import com.kalyani.car_rental_backend.response.PageResponse;
import com.kalyani.car_rental_backend.response.PaginationUtils;
import com.kalyani.car_rental_backend.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.*;
import java.time.Duration;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookings;
    private final CarRepository cars;
    private final UserRepository users;
    private final NotificationRepository notifications;
    private final DriverDispatchService dispatch;

    public BookingServiceImpl(BookingRepository b, CarRepository c, UserRepository u,
                              NotificationRepository n, DriverDispatchService dispatch) {
        bookings=b; cars=c; users=u; notifications=n; this.dispatch=dispatch;
    }

    @Transactional
    public BookingResponse create(BookingRequest r,String email){
        if(!r.getEndDate().isAfter(r.getStartDate())) throw new IllegalArgumentException("End date must be after start date");
        var user=users.findByEmailIgnoreCase(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
        var car=cars.findById(r.getCarId()).orElseThrow(()->new ResourceNotFoundException("Car not found"));

        BookingType type;
        try { type = BookingType.valueOf(String.valueOf(r.getBookingType()).toUpperCase()); }
        catch (Exception e) { throw new IllegalArgumentException("Booking type must be SELF_DRIVE or WITH_DRIVER"); }

        if(type == BookingType.SELF_DRIVE){
            if(r.getDrivingLicenseNumber()==null || r.getDrivingLicenseNumber().isBlank()) throw new IllegalArgumentException("Driving licence number is required for self drive");
            if(!Boolean.TRUE.equals(r.getAgeConfirmed())) throw new IllegalArgumentException("Age confirmation is required for self drive");
        } else {
            if(r.getPickupLatitude()==null || r.getPickupLongitude()==null)
                throw new IllegalArgumentException("Pickup live location is required to find the nearest driver");
        }

        long hours=Math.max(1,Duration.between(r.getStartDate(),r.getEndDate()).toHours());
        long days=Math.max(1,(long)Math.ceil(hours/24.0));
        BigDecimal rental=car.getPricePerDay().multiply(BigDecimal.valueOf(days));
        BigDecimal insurance=Boolean.TRUE.equals(r.getIncludeInsurance())?BigDecimal.valueOf(150).multiply(BigDecimal.valueOf(days)):BigDecimal.ZERO;

        Double routeDistance = null;
        if(r.getPickupLatitude()!=null && r.getPickupLongitude()!=null && r.getDropLatitude()!=null && r.getDropLongitude()!=null){
            routeDistance = DriverDispatchService.distanceKm(r.getPickupLatitude(),r.getPickupLongitude(),r.getDropLatitude(),r.getDropLongitude());
        }
        String tripType = routeDistance != null && routeDistance > 50 ? "OUTSTATION" : "LOCAL";
        BigDecimal driverAmount = type == BookingType.WITH_DRIVER
                ? BigDecimal.valueOf(routeDistance != null && routeDistance > 50 ? 1800L * days : 800L * days)
                : BigDecimal.ZERO;

        Booking b=new Booking();
        b.setBookingReference("RR-"+UUID.randomUUID().toString().substring(0,8).toUpperCase());
        b.setUser(user); b.setCar(car); b.setBookingType(type); b.setTripType(tripType);
        b.setPickupLatitude(r.getPickupLatitude()); b.setPickupLongitude(r.getPickupLongitude());
        b.setDropLatitude(r.getDropLatitude()); b.setDropLongitude(r.getDropLongitude()); b.setEstimatedDistanceKm(routeDistance);
        b.setStartDate(r.getStartDate()); b.setEndDate(r.getEndDate()); b.setStartPoint(r.getStartPoint()); b.setEndPoint(r.getEndPoint());
        b.setIncludeInsurance(r.getIncludeInsurance()); b.setInsuranceType(r.getInsuranceType()); b.setCouponCode(r.getCouponCode());
        b.setSpecialInstructions(r.getSpecialInstructions()); b.setDrivingLicenseNumber(r.getDrivingLicenseNumber());
        b.setIdentityProofReference(r.getIdentityProofReference()); b.setAgeConfirmed(r.getAgeConfirmed());
        b.setDurationDays(days); b.setRentalAmount(rental); b.setInsuranceAmount(insurance); b.setDriverAmount(driverAmount);
        b.setGrandTotal(rental.add(insurance).add(driverAmount).setScale(2,RoundingMode.HALF_UP));
        b.setStatus(type == BookingType.WITH_DRIVER ? BookingStatus.SEARCHING_DRIVER : BookingStatus.PENDING);

        Booking saved=bookings.save(b);
        Notification n=new Notification(); n.setUser(user); n.setTitle("Booking submitted");
        n.setMessage(type == BookingType.WITH_DRIVER
                ? "Your booking " + saved.getBookingReference() + " was created. We are searching for the nearest online driver."
                : "Your self-drive booking " + saved.getBookingReference() + " was submitted for document verification.");
        n.setType("BOOKING"); notifications.save(n);

        if(type == BookingType.WITH_DRIVER) dispatch.dispatchNearest(saved);
        return out(bookings.findById(saved.getId()).orElse(saved));
    }

    public List<BookingResponse> getForUser(String email,boolean admin){
        List<Booking> list=admin?bookings.findAll():bookings.findByUserEmailIgnoreCaseOrderByCreatedAtDesc(email);
        return list.stream().map(this::out).toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<BookingResponse> getPagedForUser(String email, boolean admin, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtils.createPageable(
                page,
                size,
                sortBy,
                sortDir,
                Set.of("id", "bookingReference", "startDate", "endDate", "createdAt", "grandTotal", "status")
        );

        Page<Booking> result = admin
                ? bookings.findAll(pageable)
                : bookings.findByUserEmailIgnoreCase(email, pageable);

        Page<BookingResponse> mapped = result.map(this::out);
        return PageResponse.from(mapped, sortBy, sortDir);
    }

    private BookingResponse out(Booking b){
        BookingResponse r=new BookingResponse(); r.id=b.getId(); r.bookingReference=b.getBookingReference();
        r.carId=b.getCar().getId(); r.carName=b.getCar().getBrand()+" "+b.getCar().getName(); r.bookingType=b.getBookingType().name();
        r.tripType=b.getTripType(); r.startPoint=b.getStartPoint(); r.endPoint=b.getEndPoint(); r.startDate=b.getStartDate(); r.endDate=b.getEndDate();
        r.estimatedDistanceKm=b.getEstimatedDistanceKm(); r.durationDays=b.getDurationDays(); r.rentalAmount=b.getRentalAmount();
        r.insuranceAmount=b.getInsuranceAmount(); r.driverAmount=b.getDriverAmount(); r.grandTotal=b.getGrandTotal(); r.status=b.getStatus().name();
        if(b.getAssignedDriver()!=null){
            r.driverId=b.getAssignedDriver().getId(); r.driverName=b.getAssignedDriver().getUser().getFullName();
            r.driverPhone=b.getAssignedDriver().getUser().getPhone(); r.driverVehicle=b.getAssignedDriver().getVehicleModel()+" · "+b.getAssignedDriver().getVehicleNumber();
        }
        return r;
    }
}
