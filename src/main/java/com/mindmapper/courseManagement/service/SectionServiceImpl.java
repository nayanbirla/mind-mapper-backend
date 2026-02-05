package com.mindmapper.courseManagement.service;

import com.mindmapper.courseManagement.dto.SectionRequest;
import com.mindmapper.courseManagement.dto.SectionResponse;
import com.mindmapper.entity.Course;
import com.mindmapper.entity.Section;
import com.mindmapper.repository.CourseRepository;
import com.mindmapper.repository.SectionRepository;
import com.mindmapper.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    public SectionResponse addSection(SectionRequest sectionRequest) {

        // Validate the section request
        validateSectionRequest(sectionRequest);

        Optional<Course> course = courseRepository.findById(sectionRequest.getCourseId());
        if(!course.isPresent()) {
            throw new IllegalArgumentException("Course not found with ID: " + sectionRequest.getCourseId());
        }

        Section section = new Section();
        section.setTitle(sectionRequest.getSectionName());
        section.setCourse(course.get());

        section = sectionRepository.save(section);

        return new SectionResponse(
                "Section added successfully",
                "201",
                section.getSectionId(),
                section.getTitle(),
                section.getCourse().getCourseId()
        );
    }

    @Override
    public SectionResponse updateSection(SectionRequest sectionRequest, Long sectionId) {

        // Validate the section request
        validateSectionRequest(sectionRequest);

        Optional<Section> existingSection = sectionRepository.findById(sectionId);
        if (!existingSection.isPresent()) {
            throw new IllegalArgumentException("Section not found with ID: " + sectionId);
        }

        Section section = existingSection.get();
        section.setTitle(sectionRequest.getSectionName());

        Optional<Course> course = courseRepository.findById(sectionRequest.getCourseId());
        if (!course.isPresent()) {
            throw new IllegalArgumentException("Course not found with ID: " + sectionRequest.getCourseId());
        }
        section.setCourse(course.get());

        section = sectionRepository.save(section);

        return new SectionResponse(
                "Section updated successfully",
                "200",
                section.getSectionId(),
                section.getTitle(),
                section.getCourse().getCourseId()
        );
    }

    @Override
    public Response deleteSection(Long sectionId) {

        Optional<Section> section = sectionRepository.findById(sectionId);
        if (!section.isPresent()) {
            throw new IllegalArgumentException("Section not found with ID: " + sectionId);
        }

        sectionRepository.delete(section.get());

        return new Response("Section deleted successfully", "200");
    }

    @Override
    public List<Section> getSectionsByCourseId(Long courseId) {

        if (courseId == null) {
            throw new IllegalArgumentException("Course ID cannot be null.");
        }

        return sectionRepository.findByCourse_CourseId(courseId);
    }

    @Override
    public Section getSectionById(Long sectionId) {
        if(sectionId == null) {
            throw new IllegalArgumentException("Section ID cannot be null.");
        }

        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("Section not found with ID: " + sectionId));
    }

    private void validateSectionRequest(SectionRequest sectionRequest) {
        if (sectionRequest.getCourseId() == null || sectionRequest.getSectionName() == null || sectionRequest.getSectionName().isEmpty()) {
            throw new IllegalArgumentException("Invalid section request: Course ID and Section Name are required.");
        }
    }

}
