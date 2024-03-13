package com.djamware.springsecuritymongo.controllers;

import com.djamware.springsecuritymongo.domain.Booking;
import com.djamware.springsecuritymongo.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("user/bookings")
    public String showBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "user/bookings"; // Assuming your Thymeleaf template is named "bookings.html"
    }




    @PostMapping("/book")
    public String bookTimeSlot(@RequestParam("bookingDate") String bookingDate,
                               @RequestParam("timeSlot") String timeSlot,
                               RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName(); // Retrieves the logged-in user's user ID
            LocalDate parsedBookingDate = LocalDate.parse(bookingDate);

            if (bookingService.isBookingAvailable(parsedBookingDate, timeSlot)) {
                bookingService.saveBooking(userId, timeSlot, parsedBookingDate);
                redirectAttributes.addFlashAttribute("successMessage", "Booking successful!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Booking slot not available.");
            }
        } catch (RuntimeException ex) {
            // Handle specific exception for existing booking
            redirectAttributes.addFlashAttribute("errorMessage", "A booking already exists for the time slot and day.");
        } catch (Exception e) {
            // Handle other exceptions such as parsing errors or booking conflicts
            redirectAttributes.addFlashAttribute("errorMessage", "Error while processing booking.");
        }

        return "redirect:/user/bookings"; // Redirect to the dashboard after booking attempt
    }



    @PostMapping("user/bookings/{bookingId}/cancel")
    public String cancelBooking(@PathVariable("bookingId") String bookingId) {
        bookingService.cancelBooking(bookingId);
        // Redirect to the dashboard or any other page after canceling the booking
        return "redirect:/user/bookings";
    }



}


