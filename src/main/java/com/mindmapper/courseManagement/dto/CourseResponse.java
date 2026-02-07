package com.mindmapper.courseManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseResponse {

    private Long courseId;
    private String title;
    private String description;
    private String category;
    private Double price;
    private String level;
    private String imageUrl;
    private Long instructorId;
    private String instructorName;
}
