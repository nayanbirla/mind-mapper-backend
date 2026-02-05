package com.mindmapper.courseManagement.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateBlogRequest {
    private String title;
    private String content;
    private List<Long> sectionIds; // Optional: Link to sections immediately on create
}
