package com.mindmapper.courseManagement.dto;

import com.mindmapper.utility.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectionResponse extends Response {

    private Long sectionId;
    private String sectionName;
    private Long courseId;

    public SectionResponse() {
    }

    public SectionResponse(String message, String statusCode, Long sectionId, String sectionName, Long courseId) {
        super(message, statusCode);
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.courseId = courseId;
    }
}
