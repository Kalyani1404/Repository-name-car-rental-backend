package com.kalyani.car_rental_backend.payment.serviceimpl;

import com.kalyani.car_rental_backend.booking.entity.BookingStatus;
import com.kalyani.car_rental_backend.booking.repository.BookingRepository;
import com.kalyani.car_rental_backend.exception.ResourceNotFoundException;
import com.kalyani.car_rental_backend.payment.dto.*;
import com.kalyani.car_rental_backend.payment.entity.*;
import com.kalyani.car_rental_backend.payment.repository.PaymentRepository;
import com.kalyani.car_rental_backend.payment.service.PaymentService;
import com.kalyani.car_rental_backend.notification.entity.Notification;
import com.kalyani.car_rental_backend.notification.repository.NotificationRepository;
import com.kalyani.car_rental_backend.user.entity.Role;
import com.kalyani.car_rental_backend.user.repository.UserRepository;
import com.kalyani.car_rental_backend.response.PageResponse;
import com.kalyani.car_rental_backend.response.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.Set;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository payments; private final BookingRepository bookings; private final UserRepository users; private final NotificationRepository notifications;
    public PaymentServiceImpl(PaymentRepository p,BookingRepository b,UserRepository u,NotificationRepository n){payments=p;bookings=b;users=u;notifications=n;}

    @Transactional public PaymentResponse create(PaymentRequest r,String email){
        if(payments.existsByBookingId(r.getBookingId())) throw new IllegalArgumentException("Payment already exists for this booking");
        var booking=bookings.findById(r.getBookingId()).orElseThrow(()->new ResourceNotFoundException("Booking not found"));
        var user=users.findByEmailIgnoreCase(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
        if(user.getRole()==Role.ADMIN) throw new IllegalArgumentException("Admin can view payments but cannot pay on behalf of a user");
        if(!booking.getUser().getId().equals(user.getId())) throw new IllegalArgumentException("You cannot pay for this booking");
        Payment p=new Payment();p.setTransactionReference("PAY-"+UUID.randomUUID().toString().substring(0,10).toUpperCase());
        p.setBooking(booking);p.setUser(user);p.setAmount(booking.getGrandTotal());p.setPaymentMethod(r.getPaymentMethod());p.setStatus(PaymentStatus.SUCCESS);
        booking.setStatus(BookingStatus.CONFIRMED);bookings.save(booking);
        Payment saved=payments.save(p);
        Notification n=new Notification();
        n.setUser(booking.getUser());
        n.setTitle("Payment successful");
        n.setMessage("Payment of ₹" + saved.getAmount() + " was successful for booking " + booking.getBookingReference() + ". Your booking is confirmed.");
        n.setType("PAYMENT");
        notifications.save(n);
        return out(saved);
    }
    public List<PaymentResponse> list(String email,boolean admin){
        var list=admin?payments.findAll():payments.findByUserEmailIgnoreCaseOrderByCreatedAtDesc(email);
        return list.stream().map(this::out).toList();
    }
    @Transactional(readOnly = true)
    public PageResponse<PaymentResponse> listPaged(String email, boolean admin, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtils.createPageable(
                page,
                size,
                sortBy,
                sortDir,
                Set.of("id", "transactionReference", "amount", "status", "paymentMethod", "createdAt")
        );

        Page<Payment> result = admin
                ? payments.findAll(pageable)
                : payments.findByUserEmailIgnoreCase(email, pageable);

        Page<PaymentResponse> mapped = result.map(this::out);
        return PageResponse.from(mapped, sortBy, sortDir);
    }

    private PaymentResponse out(Payment p){
        PaymentResponse r=new PaymentResponse();r.id=p.getId();r.transactionReference=p.getTransactionReference();
        r.bookingId=p.getBooking().getId();r.amount=p.getAmount();r.paymentMethod=p.getPaymentMethod();
        r.status=p.getStatus().name();r.createdAt=p.getCreatedAt();return r;
    }
}
