package com.mindmapper.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private Double amount;
    private String currency; // INR

    private String status; // PENDING, SUCCESS, FAILED

    @CreationTimestamp
    private LocalDateTime paymentDate;
}
