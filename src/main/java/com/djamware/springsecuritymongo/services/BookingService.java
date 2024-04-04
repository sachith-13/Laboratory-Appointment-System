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
        this.emailService = emailService; 
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(String bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    public List<Booking> getUserBookings(String userId) {
        return bookingRepository.findByUserId(userId);
    }
    public long saveBooking(String userId, String timeSlot, LocalDate bookingDate, String lab) {
        try {

            Booking existingBooking = bookingRepository.findByUserIdAndTimeSlotAndBookingDate(userId, timeSlot, bookingDate);

            if (existingBooking != null && existingBooking.getLab().equals(lab)) {

                throw new RuntimeException("You have already booked this time slot for the selected lab on the day.");
            } else {

                Booking booking = new Booking();
                booking.setUserId(userId);
                booking.setTimeSlot(timeSlot);
                booking.setBookingDate(bookingDate);
                booking.setLab(lab);
                bookingRepository.save(booking);


            }
        } catch (Exception e) {

            throw new RuntimeException("Error while saving booking: " + e.getMessage());
        }
        return 0;
    }



    public boolean isBookingAvailable(LocalDate bookingDate, String timeSlot, String lab) {

        Booking existingBooking = bookingRepository.findByTimeSlotAndBookingDate(timeSlot, bookingDate);

        return existingBooking == null;
    }

    public void cancelBooking(String bookingId) {

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();

            bookingRepository.delete(booking);
        } else {

            throw new IllegalArgumentException("Booking with ID " + bookingId + " not found.");
        }
    }

    public void usercancelBooking(String bookingId) {

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();

            bookingRepository.delete(booking);
        } else {

            throw new IllegalArgumentException("Booking with ID " + bookingId + " not found.");
        }
    }

    public void approveBooking(String bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            booking.setApproved(true);
            bookingRepository.save(booking);
        } else {
            // Handle case when booking is not found
            throw new RuntimeException("Booking not found with id: " + bookingId);
        }
    }
    }



