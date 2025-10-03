package com.mindmapper.repository;

import com.mindmapper.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByCourse_CourseId(Long courseId);

    // Custom query methods can be defined here if needed
    // For example, to find sections by courseId:
    // List<Section> findByCourseId(Long courseId);
}
