package com.mindmapper.repository;

import com.mindmapper.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture,Long> {

    @Query("SELECT l FROM Lecture l WHERE l.section.sectionId = :sectionId")
    List<Lecture> findAllLectureBySectionId(Long sectionId);
}
