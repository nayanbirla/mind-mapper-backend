package com.mindmapper.courseManagement.service;

import com.mindmapper.courseManagement.dto.SectionRequest;
import com.mindmapper.courseManagement.dto.SectionResponse;
import com.mindmapper.entity.Section;
import com.mindmapper.utility.Response;

import java.util.List;

public interface SectionService {

    // Define methods for section management, e.g., addSection, updateSection, deleteSection, getSectionsByCourseId
    // Example:
       SectionResponse addSection(SectionRequest sectionRequest);

       SectionResponse updateSection(SectionRequest sectionRequest, Long sectionId);

       Response deleteSection(Long sectionId);

       List<Section> getSectionsByCourseId(Long courseId);

       Section getSectionById(Long sectionId);
}
