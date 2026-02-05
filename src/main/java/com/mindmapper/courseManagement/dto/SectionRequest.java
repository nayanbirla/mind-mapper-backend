package com.mindmapper.courseManagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectionRequest {

    private String sectionName;
    private Long courseId;

    public SectionRequest() {
    }

    public SectionRequest(String sectionName, Long courseId) {
        this.sectionName = sectionName;
        this.courseId = courseId;
    }
}
