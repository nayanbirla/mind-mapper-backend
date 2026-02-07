package com.mindmapper.courseManagement.service;

import com.mindmapper.courseManagement.dto.BlogDto;
import com.mindmapper.courseManagement.dto.CreateBlogRequest;
import com.mindmapper.utility.Response;

import java.util.List;

public interface BlogService {
    Response createBlog(CreateBlogRequest request, String userEmail);

    BlogDto getBlogById(Long blogId);

    List<BlogDto> getAllBlogs();

    List<BlogDto> getMyBlogs(String userEmail);

    Response updateBlog(Long blogId, CreateBlogRequest request, String userEmail);

    Response addBlogToSection(Long blogId, Long sectionId);

    Response removeBlogFromSection(Long blogId, Long sectionId);
}
