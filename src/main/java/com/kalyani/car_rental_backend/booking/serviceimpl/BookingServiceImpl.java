package com.kalyani.car_rental_backend.booking.serviceimpl;

import com.kalyani.car_rental_backend.booking.dto.*;
import com.kalyani.car_rental_backend.booking.entity.Booking;
import com.kalyani.car_rental_backend.booking.repository.BookingRepository;
import com.kalyani.car_rental_backend.booking.service.BookingService;
import com.kalyani.car_rental_backend.car.repository.CarRepository;
import com.kalyani.car_rental_backend.exception.ResourceNotFoundException;
import com.kalyani.car_rental_backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.Duration;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookings; private final CarRepository cars; private final UserRepository users;
    public BookingServiceImpl(BookingRepository b,CarRepository c,UserRepository u){bookings=b;cars=c;users=u;}

    @Transactional public BookingResponse create(BookingRequest r,String email){
        if(!r.getEndDate().isAfter(r.getStartDate())) throw new IllegalArgumentException("End date must be after start date");
        var user=users.findByEmailIgnoreCase(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
        var car=cars.findById(r.getCarId()).orElseThrow(()->new ResourceNotFoundException("Car not found"));
        long hours=Math.max(1,Duration.between(r.getStartDate(),r.getEndDate()).toHours());
        long days=Math.max(1,(long)Math.ceil(hours/24.0));
        BigDecimal rental=car.getPricePerDay().multiply(BigDecimal.valueOf(days));
        BigDecimal insurance=Boolean.TRUE.equals(r.getIncludeInsurance())?BigDecimal.valueOf(150).multiply(BigDecimal.valueOf(days)):BigDecimal.ZERO;
        Booking b=new Booking();b.setBookingReference("RR-"+UUID.randomUUID().toString().substring(0,8).toUpperCase());
        b.setUser(user);b.setCar(car);b.setStartDate(r.getStartDate());b.setEndDate(r.getEndDate());
        b.setStartPoint(r.getStartPoint());b.setEndPoint(r.getEndPoint());b.setIncludeInsurance(r.getIncludeInsurance());
        b.setInsuranceType(r.getInsuranceType());b.setCouponCode(r.getCouponCode());b.setSpecialInstructions(r.getSpecialInstructions());
        b.setDurationDays(days);b.setRentalAmount(rental);b.setInsuranceAmount(insurance);
        b.setGrandTotal(rental.add(insurance).setScale(2,RoundingMode.HALF_UP));
        return out(bookings.save(b));
    }
    public List<BookingResponse> getForUser(String email,boolean admin){
        List<Booking> list=admin?bookings.findAll():bookings.findByUserEmailIgnoreCaseOrderByCreatedAtDesc(email);
        return list.stream().map(this::out).toList();
    }
    private BookingResponse out(Booking b){
        BookingResponse r=new BookingResponse();r.id=b.getId();r.bookingReference=b.getBookingReference();
        r.carId=b.getCar().getId();r.carName=b.getCar().getName();r.startPoint=b.getStartPoint();r.endPoint=b.getEndPoint();
        r.startDate=b.getStartDate();r.endDate=b.getEndDate();r.durationDays=b.getDurationDays();
        r.rentalAmount=b.getRentalAmount();r.insuranceAmount=b.getInsuranceAmount();r.grandTotal=b.getGrandTotal();
        r.status=b.getStatus().name();return r;
    }
}
