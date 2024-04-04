package com.djamware.springsecuritymongo.repositories;

import com.djamware.springsecuritymongo.domain.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends MongoRepository<Booking, String> {

    Booking findByTimeSlotAndBookingDate( String timeSlot ,LocalDate bookingDate);

    Booking findByUserIdAndTimeSlotAndBookingDate(String userId, String timeSlot,LocalDate bookingDate);

    Booking findByIdAndUserId(String id, String userId);

    List<Booking> findAll();

    Optional<Booking> findById(String bookingId);

    List<Booking> findByUserId(String userId);

}


