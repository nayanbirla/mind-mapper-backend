package com.mindmapper.courseManagement.dto;

import lombok.Data;

@Data
public class OptionDto {
    private Long optionId;
    private String optionText;
    private Boolean isCorrect;
}
