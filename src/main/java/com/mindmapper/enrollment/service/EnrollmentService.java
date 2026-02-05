package com.mindmapper.enrollment.service;

import com.mindmapper.enrollment.dto.EnrollmentResponse;
import com.mindmapper.enrollment.dto.PaymentVerificationRequest;
import com.mindmapper.utility.Response;

public interface EnrollmentService {
    EnrollmentResponse initiateEnrollment(Long courseId, String userEmail);

    Response verifyPayment(PaymentVerificationRequest request, String userEmail);
}
