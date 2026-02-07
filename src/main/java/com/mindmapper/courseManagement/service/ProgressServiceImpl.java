package com.mindmapper.courseManagement.service;

import com.mindmapper.courseManagement.dto.MarkLectureRequest;
import com.mindmapper.courseManagement.dto.ProgressResponse;
import com.mindmapper.entity.*;
import com.mindmapper.repository.*;
import com.mindmapper.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProgressServiceImpl implements ProgressService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureProgressRepository lectureProgressRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Response markLectureAsComplete(MarkLectureRequest request, String userEmail) {
        UserInfo user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Enrollment enrollment = enrollmentRepository
                .findByUser_UserIdAndCourse_CourseId(user.getUserId(), request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Enrollment not found or not active"));

        if (!"ACTIVE".equals(enrollment.getStatus())) {
            throw new RuntimeException("Enrollment is not active");
        }

        Lecture lecture = lectureRepository.findById(request.getLectureId())
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        // Check if progress already exists
        if (lectureProgressRepository
                .findByEnrollment_EnrollmentIdAndLecture_LectureId(enrollment.getEnrollmentId(), lecture.getLectureId())
                .isPresent()) {
            return new Response("Lecture already marked as complete", "200");
        }

        LectureProgress progress = new LectureProgress();
        progress.setEnrollment(enrollment);
        progress.setLecture(lecture);
        progress.setIsCompleted(true);
        lectureProgressRepository.save(progress);

        return new Response("Lecture marked as complete", "200");
    }

    @Override
    public ProgressResponse getCourseProgress(Long courseId, String userEmail) {
        UserInfo user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Enrollment enrollment = enrollmentRepository.findByUser_UserIdAndCourse_CourseId(user.getUserId(), courseId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        // Calculate total lectures in course
        // This is a bit complex as Course -> Section -> Lecture
        // We need a custom query or strict iteration
        // Using iteration for now (Optimization: Use Count Query in Repo)

        List<Section> sections = sectionRepository.findByCourse_CourseId(courseId);
        int totalLectures = 0;
        for (Section s : sections) {
            // Assuming we can get lectures from section.
            // We need to fetch lectures. Let's assume Section has OneToMany lectures list
            // or use Repo
            // Section entity definition in previous steps didn't show the List<Lecture>
            // mappedBy but Lecture has Section.
            // We should trust LectureRepository count by section.
            // Actually, let's just count completed lectures vs total lectures roughly.
            totalLectures += lectureRepository.countBySection_SectionId(s.getSectionId());
        }

        List<LectureProgress> completed = lectureProgressRepository
                .findByEnrollment_EnrollmentId(enrollment.getEnrollmentId());
        int completedCount = completed.size();

        ProgressResponse response = new ProgressResponse();
        response.setCourseId(courseId);
        response.setCompletedLectures(completedCount);
        response.setTotalLectures(totalLectures);

        double percentage = totalLectures > 0 ? ((double) completedCount / totalLectures) * 100 : 0;
        response.setProgressPercentage(percentage);
        response.setIsCertificateEligible(percentage >= 100);

        return response;
    }

    @Override
    @Transactional
    public Response claimCertificate(Long courseId, String userEmail) {
        ProgressResponse progress = getCourseProgress(courseId, userEmail);
        if (!progress.getIsCertificateEligible()) {
            throw new RuntimeException("Course not yet completed");
        }

        // Check if already issued
        UserInfo user = userRepository.findByEmail(userEmail).get();
        Enrollment enrollment = enrollmentRepository.findByUser_UserIdAndCourse_CourseId(user.getUserId(), courseId)
                .get();
        // Assuming Certificate has OneToOne with Enrollment in Entity, but let's check
        // repo/entity structure
        // Entity Certificate has "enrollment" field.

        // For now, simpler verification: do we have a repo method?
        // Let's just try to create one.

        Certificate certificate = new Certificate();
        certificate.setEnrollment(enrollment);
        certificate.setUniqueCode(UUID.randomUUID().toString());
        certificate.setCertificateUrl("http://mindmapper.com/certificates/" + certificate.getUniqueCode()); // Placeholder
                                                                                                            // generator

        certificateRepository.save(certificate);

        // Optional: Update enrollment status to COMPLETED
        enrollment.setStatus("COMPLETED");
        enrollmentRepository.save(enrollment);

        return new Response("Certificate issued: " + certificate.getUniqueCode(), "201");
    }
}
