package com.mindmapper.enrollment.dto;

import lombok.Data;

@Data
public class PaymentVerificationRequest {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String signature; // For verifying signature (optional but recommended)
}
