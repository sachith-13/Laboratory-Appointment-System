package com.djamware.springsecuritymongo.repositories;
import com.djamware.springsecuritymongo.domain.Booking;
import com.djamware.springsecuritymongo.domain.Lab;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LabRepository extends MongoRepository<Lab, String> {
    List<Lab> findAll();

    Optional<Lab> findById(String labId);

}