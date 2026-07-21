package com.kalyani.car_rental_backend.booking.service;

import com.kalyani.car_rental_backend.booking.dto.BookingRequest;
import com.kalyani.car_rental_backend.booking.dto.BookingResponse;
import com.kalyani.car_rental_backend.response.PageResponse;

import java.util.List;

public interface BookingService {
    BookingResponse create(BookingRequest request, String email);
    List<BookingResponse> getForUser(String email, boolean admin);
    PageResponse<BookingResponse> getPagedForUser(String email, boolean admin, int page, int size, String sortBy, String sortDir);
}
