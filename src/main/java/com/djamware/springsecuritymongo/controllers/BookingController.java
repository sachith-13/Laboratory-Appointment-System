package com.djamware.springsecuritymongo.controllers;

import com.djamware.springsecuritymongo.domain.Booking;
import com.djamware.springsecuritymongo.domain.Lab;
import com.djamware.springsecuritymongo.repositories.LabRepository;
import com.djamware.springsecuritymongo.services.BookingService;
import com.djamware.springsecuritymongo.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final EmailService emailService;

    @Autowired
    public BookingController(BookingService bookingService, EmailService emailService) {
        this.bookingService = bookingService;
        this.emailService = emailService;
    }


    @Autowired
    private LabRepository labRepository; // Inject LabRepository

    @GetMapping("user/dashboard")
    public String showBookingForm(Model model) {
        List<Lab> labs = labRepository.findAll();
        model.addAttribute("labs", labs);
        return "user/dashboard"; // Return the HTML template for booking
    }

    @GetMapping("admin/bookings")
    public String showBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "admin/bookings"; // Assuming your Thymeleaf template is named "bookings.html"
    }
    @GetMapping("/user/bookings")
    public String showUserBookings(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); // Get the username (userId) of the currently logged-in user

        List<Booking> userBookings = bookingService.getUserBookings(userId);
        model.addAttribute("bookings", userBookings);

        return "user/bookings"; // Assuming your Thymeleaf template is named "bookings.html" under "user" directory
    }



    @PostMapping("/book")
    public String bookTimeSlot(@RequestParam("bookingDate") String bookingDate,
                               @RequestParam("timeSlot") String timeSlot,
                               @RequestParam("lab") String lab,
                               RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName(); // Retrieves the logged-in user's user ID
            LocalDate parsedBookingDate = LocalDate.parse(bookingDate);

            if (bookingService.isBookingAvailable(parsedBookingDate, timeSlot, lab)) {
                bookingService.saveBooking(userId, timeSlot, parsedBookingDate, lab);
                redirectAttributes.addFlashAttribute("successMessage", "Booking successful!");

                String bookingDetails = "Time Slot: " + timeSlot + "\nBooking Date: " + parsedBookingDate.toString() + "\nLab: " + lab;
                emailService.sendBookingConfirmationEmail(userId, bookingDetails);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Booking slot not available for the selected lab.");
            }
        } catch (RuntimeException ex) {
            // Handle specific exception for existing booking
            redirectAttributes.addFlashAttribute("errorMessage", "A booking already exists for the time slot, lab, and day.");
        } catch (Exception e) {
            // Handle other exceptions such as parsing errors or booking conflicts
            redirectAttributes.addFlashAttribute("errorMessage", "Error while processing booking.");
        }

        return "redirect:/user/dashboard"; // Redirect to the dashboard after booking attempt
    }



    @PostMapping("admin/bookings/{bookingId}/cancel")
    public String cancelBooking(@PathVariable("bookingId") String bookingId) {
        bookingService.cancelBooking(bookingId);
        // Redirect to the dashboard or any other page after canceling the booking
        return "redirect:/admin/bookings";
    }

    @PostMapping("user/bookings/{bookingId}/cancel")
    public String usercancelBooking(@PathVariable("bookingId") String bookingId) {
        bookingService.cancelBooking(bookingId);
        // Redirect to the dashboard or any other page after canceling the booking
        return "redirect:/user/bookings";
    }



}


