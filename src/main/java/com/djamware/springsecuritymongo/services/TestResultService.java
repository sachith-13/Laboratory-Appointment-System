package com.djamware.springsecuritymongo.services;

import com.djamware.springsecuritymongo.domain.TestResult;
import com.djamware.springsecuritymongo.repositories.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestResultService {

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    public TestResultService(TestResultRepository testResultRepository) {
        this.testResultRepository = testResultRepository;
    }
    public TestResult getTestResultByBookingId(String bookingId) {
        return testResultRepository.findByBookingId(bookingId);
    }


    public TestResult uploadTestResult(String bookingId, byte[] file) {
        TestResult testResult = new TestResult();
        testResult.setBookingId(bookingId);
        testResult.setFile(file);
        return testResultRepository.save(testResult);
    }


}

