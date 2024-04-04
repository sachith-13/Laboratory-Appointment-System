package com.djamware.springsecuritymongo.repositories;

import com.djamware.springsecuritymongo.domain.TestResult;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestResultRepository extends MongoRepository<TestResult, String> {
    TestResult findByBookingId(String bookingId);



}
