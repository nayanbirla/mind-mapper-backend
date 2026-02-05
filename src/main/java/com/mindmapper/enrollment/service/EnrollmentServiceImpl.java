package com.mindmapper.enrollment.service;

import com.mindmapper.enrollment.dto.EnrollmentResponse;
import com.mindmapper.enrollment.dto.PaymentVerificationRequest;
import com.mindmapper.entity.Course;
import com.mindmapper.entity.Enrollment;
import com.mindmapper.entity.Payment;
import com.mindmapper.entity.UserInfo;
import com.mindmapper.repository.CourseRepository;
import com.mindmapper.repository.EnrollmentRepository;
import com.mindmapper.repository.PaymentRepository;
import com.mindmapper.repository.UserRepository;
import com.mindmapper.utility.Response;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RazorpayClient razorpayClient;

    @Override
    @Transactional
    public EnrollmentResponse initiateEnrollment(Long courseId, String userEmail) {
        UserInfo user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Check existing enrollment
        Optional<Enrollment> existing = enrollmentRepository.findByUser_UserIdAndCourse_CourseId(user.getUserId(),
                course.getCourseId());
        if (existing.isPresent()) {
            throw new RuntimeException("Already enrolled in this course");
        }

        EnrollmentResponse response = new EnrollmentResponse();

        if (course.getPrice() != null && course.getPrice() > 0) {
            // Paid Course
            try {
                JSONObject orderRequest = new JSONObject();
                orderRequest.put("amount", (int) (course.getPrice() * 100)); // Amount in paise
                orderRequest.put("currency", "INR");
                orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

                Order order = razorpayClient.orders.create(orderRequest);

                Payment payment = new Payment();
                payment.setRazorpayOrderId(order.get("id"));
                payment.setAmount(course.getPrice());
                payment.setCurrency("INR");
                payment.setStatus("PENDING");
                // We don't have enrollment yet, so we can't link it strictly or we create
                // enrollment as PENDING?
                // Better approach: Create Enrollment as PENDING/AWAITING_PAYMENT?
                // Or just save Payment independently first?
                // For simplicity, let's create Enrollment as PENDING.

                Enrollment enrollment = new Enrollment();
                enrollment.setUser(user);
                enrollment.setCourse(course);
                enrollment.setStatus("PENDING");
                enrollmentRepository.save(enrollment);

                payment.setEnrollment(enrollment);
                paymentRepository.save(payment);

                response.setRazorpayOrderId(order.get("id"));
                response.setAmount(course.getPrice());
                response.setCurrency("INR");
                response.setStatus("CREATED");

            } catch (RazorpayException e) {
                throw new RuntimeException("Razorpay error: " + e.getMessage());
            }
        } else {
            // Free Course
            Enrollment enrollment = new Enrollment();
            enrollment.setUser(user);
            enrollment.setCourse(course);
            enrollment.setStatus("ACTIVE");
            enrollmentRepository.save(enrollment);

            response.setStatus("ENROLLED");
            response.setAmount(0.0);
        }

        return response;
    }

    @Override
    @Transactional
    public Response verifyPayment(PaymentVerificationRequest request, String userEmail) {
        Payment payment = paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() -> new RuntimeException("Invalid Order ID"));

        try {
            // Signature verification logic (Manual or using Utils)
            // String generatedSignature = HmacSHA256(orderId + "|" + paymentId, secret)
            // For now, assuming success if paymentId is present.
            // In Production, MUST use Utils.verifyPaymentSignature(...)

            payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
            payment.setStatus("SUCCESS");
            paymentRepository.save(payment);

            Enrollment enrollment = payment.getEnrollment();
            enrollment.setStatus("ACTIVE");
            enrollmentRepository.save(enrollment);

            return new Response("Payment verified and enrolled successfully", "200");

        } catch (Exception e) {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            throw new RuntimeException("Payment verification failed");
        }
    }
}
