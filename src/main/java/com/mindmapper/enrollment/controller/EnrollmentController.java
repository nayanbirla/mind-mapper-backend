package com.mindmapper.enrollment.controller;

import com.mindmapper.enrollment.dto.EnrollmentResponse;
import com.mindmapper.enrollment.dto.InitiateEnrollmentRequest;
import com.mindmapper.enrollment.dto.PaymentVerificationRequest;
import com.mindmapper.enrollment.service.EnrollmentService;
import com.mindmapper.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/initiate")
    public ResponseEntity<EnrollmentResponse> initiateEnrollment(@RequestBody InitiateEnrollmentRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(enrollmentService.initiateEnrollment(request.getCourseId(), email), HttpStatus.OK);
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<Response> verifyPayment(@RequestBody PaymentVerificationRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(enrollmentService.verifyPayment(request, email), HttpStatus.OK);
    }
}
