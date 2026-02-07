package com.mindmapper.courseManagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureRequest {

    private String lectureName;
    private String lectureVideoUrl;
    private Long sectionId;

    public LectureRequest() {
    }

    public LectureRequest(String lectureName, Long sectionId) {
        this.lectureName = lectureName;
        this.sectionId = sectionId;
    }
}
