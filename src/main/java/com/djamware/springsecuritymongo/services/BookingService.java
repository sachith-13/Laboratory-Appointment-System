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
    private final EmailService emailService;


    @Autowired
    public BookingService(BookingRepository bookingRepository, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.emailService = emailService; // Initialize EmailService
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getUserBookings(String userId) {
        return bookingRepository.findByUserId(userId);
    }
    public void saveBooking(String userId, String timeSlot, LocalDate bookingDate, String lab) {
        try {
            // Check if there's an existing booking for the given user, time slot, lab, and date
            Booking existingBooking = bookingRepository.findByUserIdAndTimeSlotAndBookingDate(userId, timeSlot, bookingDate);

            if (existingBooking != null && existingBooking.getLab().equals(lab)) {
                // If the user already has a booking for the same time slot, lab, and day
                throw new RuntimeException("You have already booked this time slot for the selected lab on the day.");
            } else {
                // Save the booking
                Booking booking = new Booking();
                booking.setUserId(userId);
                booking.setTimeSlot(timeSlot);
                booking.setBookingDate(bookingDate);
                booking.setLab(lab);
                bookingRepository.save(booking);


            }
        } catch (Exception e) {
            // Handle exceptions
            throw new RuntimeException("Error while saving booking: " + e.getMessage());
        }
    }



    public boolean isBookingAvailable(LocalDate bookingDate, String timeSlot, String lab) {
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

    public void usercancelBooking(String bookingId) {
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
