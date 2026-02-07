package com.mindmapper.courseManagement.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BlogDto {
    private Long blogId;
    private String title;
    private String content;
    private String authorName;
    private Long authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
