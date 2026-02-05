package com.mindmapper.courseManagement.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuestionDto {
    private Long questionId;
    private String text;
    private String type;
    private List<OptionDto> options;
}
