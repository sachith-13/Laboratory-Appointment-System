package com.djamware.springsecuritymongo.controllers;

import com.djamware.springsecuritymongo.domain.TestResult;
import com.djamware.springsecuritymongo.services.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/test-results")
public class TestResultController {

    @Autowired
    private TestResultService testResultService;

    @PostMapping("/upload")
    public ResponseEntity<TestResult> uploadTestResult(@RequestParam("bookingId") String bookingId,
                                                       @RequestParam("file") MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();
        TestResult uploadedTestResult = testResultService.uploadTestResult(bookingId, fileBytes);

        // Returning ResponseEntity with uploaded test result
        return ResponseEntity.ok()
                .body(uploadedTestResult);
    }

    @GetMapping("/download/{bookingId}")
    public ResponseEntity<ByteArrayResource> downloadTestResult(@PathVariable String bookingId) {
        TestResult testResult = testResultService.getTestResultByBookingId(bookingId);
        if (testResult != null && testResult.getFile() != null) {
            ByteArrayResource resource = new ByteArrayResource(testResult.getFile());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + bookingId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(testResult.getFile().length)
                    .body(resource);
        } else {
            // If test result not found or file is null, return appropriate error response
            return ResponseEntity.notFound().build();
        }
    }
}
