package com.djamware.springsecuritymongo.controllers;
import com.djamware.springsecuritymongo.services.BookingService;
import com.djamware.springsecuritymongo.services.EmailService;
import com.djamware.springsecuritymongo.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    private final EmailService emailService;


    @Autowired
    public PaymentController(BookingService bookingService, EmailService emailService) {
        this.paymentService = paymentService;
        this.emailService = emailService;
    }


    @GetMapping("/payment/{bookingId}")
    public ResponseEntity<?> getPaymentAmountByBookingId(@PathVariable String bookingId) {

        try {
            String amount = paymentService.getPaymentAmountByBookingId(bookingId);
            return ResponseEntity.ok(amount);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found for booking ID: " + bookingId);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid amount format for booking ID: " + bookingId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }







    @PostMapping("/payment/submit")
    public ResponseEntity<String> processPayment(@RequestParam("bookingIdPayment") String bookingId,
                                                 @RequestParam("cardDetails") String cardDetails,
                                                 @RequestParam("paymentAmount") double paymentAmount) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();

            paymentService.processPayment(bookingId, cardDetails, paymentAmount);

            String bookingDetails = "Amount: " + paymentAmount;
            emailService.sendPaymentNotification(userId, bookingDetails);

            return ResponseEntity.ok("Payment processed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing payment: " + e.getMessage());
        }
    }




}
