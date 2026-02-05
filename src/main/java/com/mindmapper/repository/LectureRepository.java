package com.mindmapper.repository;

import com.mindmapper.entity.Lecture;
import com.mindmapper.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findBySection_SectionId(Long sectionId);

    Integer countBySection_SectionId(Long sectionId);
}
