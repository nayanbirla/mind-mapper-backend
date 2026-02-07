package com.mindmapper.courseManagement.controller;

import com.mindmapper.courseManagement.dto.BlogDto;
import com.mindmapper.courseManagement.dto.CreateBlogRequest;
import com.mindmapper.courseManagement.service.BlogService;
import com.mindmapper.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping("/")
    public ResponseEntity<Response> createBlog(@RequestBody CreateBlogRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(blogService.createBlog(request, email), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<BlogDto>> getAllBlogs() {
        return new ResponseEntity<>(blogService.getAllBlogs(), HttpStatus.OK);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<BlogDto> getBlog(@PathVariable Long blogId) {
        return new ResponseEntity<>(blogService.getBlogById(blogId), HttpStatus.OK);
    }

    @GetMapping("/my-blogs")
    public ResponseEntity<List<BlogDto>> getMyBlogs() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(blogService.getMyBlogs(email), HttpStatus.OK);
    }

    @PutMapping("/{blogId}")
    public ResponseEntity<Response> updateBlog(@PathVariable Long blogId, @RequestBody CreateBlogRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(blogService.updateBlog(blogId, request, email), HttpStatus.OK);
    }

    @PostMapping("/{blogId}/addToSection/{sectionId}")
    public ResponseEntity<Response> addToSection(@PathVariable Long blogId, @PathVariable Long sectionId) {
        // In a real app, verify user owns the section/course
        return new ResponseEntity<>(blogService.addBlogToSection(blogId, sectionId), HttpStatus.OK);
    }
}
