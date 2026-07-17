package com.kalyani.car_rental_backend.booking.service;
import com.kalyani.car_rental_backend.booking.dto.*;
import java.util.List;
public interface BookingService {
    BookingResponse create(BookingRequest request,String email);
    List<BookingResponse> getForUser(String email,boolean admin);
}
