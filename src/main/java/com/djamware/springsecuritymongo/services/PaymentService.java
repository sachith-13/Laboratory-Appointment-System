package com.djamware.springsecuritymongo.services;
import com.djamware.springsecuritymongo.domain.Payment;
import com.djamware.springsecuritymongo.domain.TestResult;
import com.djamware.springsecuritymongo.repositories.PaymentRepository;
import com.djamware.springsecuritymongo.repositories.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

@Service
public class PaymentService {

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public String getPaymentAmountByBookingId(String bookingId) {
        try {
            TestResult testResult = testResultRepository.findByBookingId(bookingId);
            if (testResult == null) {
                throw new NoSuchElementException("Test result not found for booking ID: " + bookingId);
            }
            return testResult.getAmount();
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new RuntimeException("Non-unique result for booking ID: " + bookingId);
        }
    }




    public void processPayment(String bookingId, String cardDetails, double paymentAmount) {
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setCardDetails(cardDetails);
        payment.setPaymentAmount(paymentAmount);


        paymentRepository.save(payment);
    }


}

