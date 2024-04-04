package com.djamware.springsecuritymongo.controllers;
import com.djamware.springsecuritymongo.domain.Payment;
import com.djamware.springsecuritymongo.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/{bookingId}")
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

    @PostMapping("/submit")
    public ResponseEntity<String> processPayment(@RequestParam("bookingIdPayment") String bookingId,
                                                 @RequestParam("cardDetails") String cardDetails,
                                                 @RequestParam("paymentAmount") double paymentAmount) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName(); // Retrieves the logged-in user's user ID

            paymentService.processPayment(bookingId, cardDetails, paymentAmount);

            return ResponseEntity.ok("Payment processed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing payment: " + e.getMessage());
        }
    }




}
