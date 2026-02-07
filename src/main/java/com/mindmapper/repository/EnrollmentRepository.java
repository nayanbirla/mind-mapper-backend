package com.mindmapper.repository;

import com.mindmapper.entity.Enrollment;
import com.mindmapper.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByUser(UserInfo user);

    Optional<Enrollment> findByUser_UserIdAndCourse_CourseId(Long userId, Long courseId);
}
