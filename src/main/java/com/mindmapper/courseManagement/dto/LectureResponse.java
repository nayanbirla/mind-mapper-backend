package com.mindmapper.courseManagement.dto;

import com.mindmapper.utility.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureResponse extends Response {

    private Long lectureId;
    private String lectureName;
    private Long sectionId;

    public LectureResponse() {
    }

    public LectureResponse(String message, Long lectureId, String lectureName, Long sectionId) {
        super(message, "200");
        this.lectureId = lectureId;
        this.lectureName = lectureName;
        this.sectionId = sectionId;
    }
}
