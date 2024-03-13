package com.djamware.springsecuritymongo.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.djamware.springsecuritymongo.domain.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingRepository extends MongoRepository<Booking, String> {

    Booking findByTimeSlotAndBookingDate( String timeSlot ,LocalDate bookingDate);

    Booking findByUserIdAndBookingDate(String userId, LocalDate bookingDate);

    List<Booking> findAll();

    Optional<Booking> findById(String bookingId);

}


