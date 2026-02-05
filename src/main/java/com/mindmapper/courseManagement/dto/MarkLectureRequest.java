package com.mindmapper.courseManagement.dto;

import lombok.Data;

@Data
public class MarkLectureRequest {
    private Long courseId;
    private Long lectureId;
}
