package com.djamware.springsecuritymongo.services;

import com.djamware.springsecuritymongo.domain.Booking;
import com.djamware.springsecuritymongo.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public class BookingService {

    private final BookingRepository bookingRepository;



    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    public void saveBooking(String userId, String timeSlot, LocalDate bookingDate) {
        try {
            // Check if there's an existing booking for the given user, time slot, and date
            Booking existingBooking = bookingRepository.findByUserIdAndBookingDate(userId, bookingDate);

            if (existingBooking != null && existingBooking.getTimeSlot().equals(timeSlot)) {
                // If the user already has a booking for the same time slot on the same day
                throw new RuntimeException("You have already booked this time slot for the day.");
            } else {
                // Save the booking
                Booking booking = new Booking();
                booking.setUserId(userId);
                booking.setTimeSlot(timeSlot);
                booking.setBookingDate(bookingDate);
                bookingRepository.save(booking);
            }
        } catch (Exception e) {
            // Handle exceptions appropriately, such as database errors
            throw new RuntimeException("Error while saving booking: " + e.getMessage());
        }
    }

    public boolean isBookingAvailable(LocalDate bookingDate, String timeSlot) {
        // Check if there's an existing booking for the given time slot and date
        Booking existingBooking = bookingRepository.findByTimeSlotAndBookingDate(timeSlot, bookingDate);
        // If there's no booking for the given time slot on the given date, the slot is available
        return existingBooking == null;
    }

    public void cancelBooking(String bookingId) {
        // Fetch the booking from the database
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            // Implement cancelation logic here, e.g., delete the booking
            bookingRepository.delete(booking);
        } else {
            // Handle case where booking is not found
            throw new IllegalArgumentException("Booking with ID " + bookingId + " not found.");
        }
    }

}
