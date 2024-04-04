package com.djamware.springsecuritymongo.repositories;

import com.djamware.springsecuritymongo.domain.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    Payment findByBookingId(String bookingId);
}

