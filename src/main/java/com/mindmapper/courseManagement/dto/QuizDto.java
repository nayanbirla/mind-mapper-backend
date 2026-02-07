package com.mindmapper.courseManagement.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuizDto {
    private Long quizId;
    private String title;
    private Integer passPercentage;
    private Long courseId;
    private Long sectionId;
    private List<QuestionDto> questions;
}
