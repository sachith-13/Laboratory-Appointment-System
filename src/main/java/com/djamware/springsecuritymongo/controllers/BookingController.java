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
    private LabRepository labRepository;

    @GetMapping("user/dashboard")
    public String showBookingForm(Model model) {
        List<Lab> labs = labRepository.findAll();
        model.addAttribute("labs", labs);
        return "user/dashboard";
    }

    @GetMapping("admin/bookings")
    public String showBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "admin/bookings";
    }
    @GetMapping("/user/bookings")
    public String showUserBookings(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        List<Booking> userBookings = bookingService.getUserBookings(userId);
        model.addAttribute("bookings", userBookings);

        return "user/bookings";
    }



    @PostMapping("/book")
    public String bookTimeSlot(@RequestParam("bookingDate") String bookingDate,
                               @RequestParam("timeSlot") String timeSlot,
                               @RequestParam("lab") String lab,
                               RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            LocalDate parsedBookingDate = LocalDate.parse(bookingDate);

            if (bookingService.isBookingAvailable(parsedBookingDate, timeSlot, lab)) {

                long bookingId = bookingService.saveBooking(userId, timeSlot, parsedBookingDate, lab);

                redirectAttributes.addFlashAttribute("successMessage", "Booking successful!");


                String bookingDetails =  "Time Slot: " + timeSlot + "\nBooking Date: " + parsedBookingDate.toString() + "\nLab: " + lab;
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

        return "redirect:/user/dashboard";
    }




    @PostMapping("admin/bookings/{bookingId}/cancel")
    public String cancelBooking(@PathVariable("bookingId") String bookingId) {

        Booking booking = bookingService.getBookingById(bookingId);
        String userId = booking.getUserId();

        if (booking == null) {

            return "redirect:/admin/bookings";
        }

        String timeSlot = booking.getTimeSlot();
        String lab = booking.getLab();

        bookingService.cancelBooking(bookingId);

        String bookingDetails = "Booking ID: " + bookingId + "\nTime Slot: " + timeSlot + "\nBooking Date: " + booking.getBookingDate().toString() + "\nLab: " + lab;
        emailService.sendBookingCancelNotification(userId, bookingDetails);


        return "redirect:/admin/bookings";
    }


    @PostMapping("user/bookings/{bookingId}/cancel")
    public String usercancelBooking(@PathVariable("bookingId") String bookingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Booking booking = bookingService.getBookingById(bookingId);
        String timeSlot = booking.getTimeSlot();
        String lab = booking.getLab();

        bookingService.cancelBooking(bookingId);

        String bookingDetails = "Booking ID: " + bookingId + "\nTime Slot: " + timeSlot + "\nBooking Date: " + booking.getBookingDate().toString() + "\nLab: " + lab;
        emailService.sendBookingCancelNotification(userId, bookingDetails);

        return "redirect:/user/bookings";
    }


    @PostMapping("admin/bookings/{bookingId}/approve")
    public String approveBooking(@PathVariable("bookingId") String bookingId) {

        Booking booking = bookingService.getBookingById(bookingId);
        String userId = booking.getUserId();

        bookingService.approveBooking(bookingId);

        String bookingDetails = "Booking ID: " + bookingId;
        emailService.sendBookingApprovalNotification(userId, bookingDetails);

        return "redirect:/admin/bookings/";
    }


}


