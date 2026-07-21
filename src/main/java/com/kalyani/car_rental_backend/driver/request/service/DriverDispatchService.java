package com.kalyani.car_rental_backend.driver.request.service;

import com.kalyani.car_rental_backend.booking.entity.Booking;
import com.kalyani.car_rental_backend.booking.entity.BookingStatus;
import com.kalyani.car_rental_backend.booking.repository.BookingRepository;
import com.kalyani.car_rental_backend.driver.entity.DriverProfile;
import com.kalyani.car_rental_backend.driver.repository.DriverProfileRepository;
import com.kalyani.car_rental_backend.driver.request.entity.*;
import com.kalyani.car_rental_backend.driver.request.repository.DriverRequestRepository;
import com.kalyani.car_rental_backend.exception.ResourceNotFoundException;
import com.kalyani.car_rental_backend.notification.entity.Notification;
import com.kalyani.car_rental_backend.notification.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DriverDispatchService {
    private final DriverProfileRepository drivers;
    private final DriverRequestRepository requests;
    private final BookingRepository bookings;
    private final NotificationRepository notifications;

    public DriverDispatchService(DriverProfileRepository drivers, DriverRequestRepository requests,
                                 BookingRepository bookings, NotificationRepository notifications) {
        this.drivers = drivers;
        this.requests = requests;
        this.bookings = bookings;
        this.notifications = notifications;
    }

    @Transactional
    public DriverRequest dispatchNearest(Booking booking) {
        if (booking.getPickupLatitude() == null || booking.getPickupLongitude() == null) {
            booking.setStatus(BookingStatus.WAITING_FOR_DRIVER);
            bookings.save(booking);
            notifyUser(booking, "Driver search waiting", "Share a pickup location so we can find the nearest online driver.");
            return null;
        }

        Set<Long> alreadyTried = new HashSet<>();
        for (DriverRequest r : requests.findByBookingId(booking.getId())) alreadyTried.add(r.getDriver().getId());

        List<DriverProfile> candidates = drivers.findByOnlineTrueAndVerificationStatus("APPROVED").stream()
                .filter(d -> d.getCurrentLatitude() != null && d.getCurrentLongitude() != null)
                .filter(d -> !alreadyTried.contains(d.getId()))
                .sorted(Comparator.comparingDouble(d -> distanceKm(
                        booking.getPickupLatitude(), booking.getPickupLongitude(),
                        d.getCurrentLatitude(), d.getCurrentLongitude())))
                .toList();

        if (candidates.isEmpty()) {
            booking.setStatus(BookingStatus.WAITING_FOR_DRIVER);
            bookings.save(booking);
            notifyUser(booking, "No driver available yet", "All nearby drivers are busy or offline. We will keep your booking waiting for a driver.");
            return null;
        }

        DriverProfile driver = candidates.get(0);
        DriverRequest request = new DriverRequest();
        request.setBooking(booking);
        request.setDriver(driver);
        request.setDistanceKm(distanceKm(booking.getPickupLatitude(), booking.getPickupLongitude(), driver.getCurrentLatitude(), driver.getCurrentLongitude()));
        request.setDispatchRank(alreadyTried.size() + 1);
        request.setExpiresAt(LocalDateTime.now().plusSeconds(30));
        request = requests.save(request);

        booking.setStatus(BookingStatus.SEARCHING_DRIVER);
        bookings.save(booking);
        notifyDriver(driver, "New trip request", booking.getStartPoint() + " → " + booking.getEndPoint() + ". Accept within 30 seconds. Approx. " + String.format(Locale.US, "%.1f", request.getDistanceKm()) + " km from pickup.");
        notifyUser(booking, "Searching for a driver", "Your request was sent to the nearest online driver. If they do not accept, it will automatically move to the next nearest driver.");
        return request;
    }

    @Transactional
    public void respond(Long requestId, String driverEmail, boolean accept) {
        DriverRequest request = requests.findByIdAndDriverUserEmailIgnoreCase(requestId, driverEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Driver request not found"));
        if (request.getStatus() != DriverRequestStatus.PENDING) throw new IllegalArgumentException("This request is no longer active");

        Booking booking = request.getBooking();
        DriverProfile driver = request.getDriver();

        if (accept) {
            request.setStatus(DriverRequestStatus.ACCEPTED);
            booking.setAssignedDriver(driver);
            booking.setStatus(BookingStatus.DRIVER_ASSIGNED);
            driver.setOnline(false);
            drivers.save(driver);
            requests.save(request);
            bookings.save(booking);
            notifyUser(booking, "Driver accepted your trip", driver.getUser().getFullName() + " accepted your trip. Phone: " + safe(driver.getUser().getPhone()) + ". You can now coordinate pickup details.");
            notifyDriver(driver, "Trip confirmed", "You accepted " + booking.getBookingReference() + ". Passenger: " + booking.getUser().getFullName() + ", phone: " + safe(booking.getUser().getPhone()) + ". Pickup: " + booking.getStartPoint() + ".");
        } else {
            request.setStatus(DriverRequestStatus.REJECTED);
            requests.save(request);
            notifyUser(booking, "Finding another driver", "A driver could not take the trip. Your request is moving to the next nearest online driver.");
            dispatchNearest(booking);
        }
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void expireTimedOutRequests() {
        for (DriverRequest request : requests.findByStatusAndExpiresAtBefore(DriverRequestStatus.PENDING, LocalDateTime.now())) {
            request.setStatus(DriverRequestStatus.EXPIRED);
            requests.save(request);
            dispatchNearest(request.getBooking());
        }
    }

    public List<Map<String,Object>> pendingForDriver(String email) {
        return requests.findByDriverUserEmailIgnoreCaseAndStatusOrderByCreatedAtDesc(email, DriverRequestStatus.PENDING)
                .stream().map(this::toMap).toList();
    }

    private Map<String,Object> toMap(DriverRequest r) {
        Booking b = r.getBooking();
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("requestId", r.getId());
        m.put("bookingId", b.getId());
        m.put("bookingReference", b.getBookingReference());
        m.put("passengerName", b.getUser().getFullName());
        m.put("passengerPhone", b.getUser().getPhone());
        m.put("startPoint", b.getStartPoint());
        m.put("endPoint", b.getEndPoint());
        m.put("startDate", b.getStartDate());
        m.put("endDate", b.getEndDate());
        m.put("tripType", b.getTripType());
        m.put("distanceFromPickupKm", r.getDistanceKm());
        m.put("specialInstructions", b.getSpecialInstructions());
        m.put("expiresAt", r.getExpiresAt());
        return m;
    }

    private void notifyUser(Booking booking, String title, String message) {
        Notification n = new Notification(); n.setUser(booking.getUser()); n.setTitle(title); n.setMessage(message); n.setType("BOOKING"); notifications.save(n);
    }
    private void notifyDriver(DriverProfile driver, String title, String message) {
        Notification n = new Notification(); n.setUser(driver.getUser()); n.setTitle(title); n.setMessage(message); n.setType("DRIVER_REQUEST"); notifications.save(n);
    }
    private String safe(String v){return v == null ? "Not provided" : v;}

    public static double distanceKm(double lat1,double lon1,double lat2,double lon2){
        double r=6371.0; double dLat=Math.toRadians(lat2-lat1); double dLon=Math.toRadians(lon2-lon1);
        double a=Math.sin(dLat/2)*Math.sin(dLat/2)+Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*Math.sin(dLon/2)*Math.sin(dLon/2);
        return r*2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
    }
}
