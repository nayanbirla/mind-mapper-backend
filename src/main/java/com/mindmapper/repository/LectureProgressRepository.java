package com.mindmapper.repository;

import com.mindmapper.entity.LectureProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureProgressRepository extends JpaRepository<LectureProgress, Long> {
    List<LectureProgress> findByEnrollment_EnrollmentId(Long enrollmentId);

    Optional<LectureProgress> findByEnrollment_EnrollmentIdAndLecture_LectureId(Long enrollmentId, Long lectureId);
}
