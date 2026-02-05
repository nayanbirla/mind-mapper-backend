package com.mindmapper.enrollment.dto;

import lombok.Data;

@Data
public class EnrollmentResponse {
    private String razorpayOrderId;
    private Double amount;
    private String currency; // INR
    private String status; // CREATED, ENROLLED
}
