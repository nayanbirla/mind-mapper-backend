package com.mindmapper.courseManagement.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateQuizRequest {
    private String title;
    private Integer passPercentage;
    private Long courseId; // Optional if sectionId is present, but good to have
    private Long sectionId; // Optional -> if null, course level quiz
    private List<QuestionDto> questions;
}
