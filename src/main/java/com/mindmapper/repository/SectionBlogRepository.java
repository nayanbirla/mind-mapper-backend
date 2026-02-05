package com.mindmapper.repository;

import com.mindmapper.entity.SectionBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionBlogRepository extends JpaRepository<SectionBlog, Long> {
    List<SectionBlog> findBySection_SectionId(Long sectionId);
}
