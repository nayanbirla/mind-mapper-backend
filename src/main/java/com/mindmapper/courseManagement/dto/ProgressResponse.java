package com.mindmapper.courseManagement.dto;

import lombok.Data;

@Data
public class ProgressResponse {
    private Long courseId;
    private Double progressPercentage;
    private Integer completedLectures;
    private Integer totalLectures;
    private Boolean isCertificateEligible;
}
