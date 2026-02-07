package com.mindmapper.courseManagement.service;

import com.mindmapper.courseManagement.dto.BlogDto;
import com.mindmapper.courseManagement.dto.CreateBlogRequest;
import com.mindmapper.entity.Blog;
import com.mindmapper.entity.Section;
import com.mindmapper.entity.SectionBlog;
import com.mindmapper.entity.UserInfo;
import com.mindmapper.repository.BlogRepository;
import com.mindmapper.repository.SectionBlogRepository;
import com.mindmapper.repository.SectionRepository;
import com.mindmapper.repository.UserRepository;
import com.mindmapper.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SectionBlogRepository sectionBlogRepository;

    @Override
    @Transactional
    public Response createBlog(CreateBlogRequest request, String userEmail) {
        UserInfo user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Blog blog = new Blog();
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setAuthor(user);

        Blog savedBlog = blogRepository.save(blog);

        if (request.getSectionIds() != null && !request.getSectionIds().isEmpty()) {
            for (Long sectionId : request.getSectionIds()) {
                addBlogToSection(savedBlog.getBlogId(), sectionId);
            }
        }

        return new Response("Blog created successfully with ID: " + savedBlog.getBlogId(), "201");
    }

    @Override
    public BlogDto getBlogById(Long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        return mapToDto(blog);
    }

    @Override
    public List<BlogDto> getAllBlogs() {
        return blogRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogDto> getMyBlogs(String userEmail) {
        UserInfo user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return blogRepository.findByAuthor(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Response updateBlog(Long blogId, CreateBlogRequest request, String userEmail) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        if (!blog.getAuthor().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized: You do not own this blog");
        }

        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blogRepository.save(blog);

        return new Response("Blog updated successfully", "200");
    }

    @Override
    @Transactional
    public Response addBlogToSection(Long blogId, Long sectionId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        // Check if already exists? (Optional, skipping for simple push)

        SectionBlog sectionBlog = new SectionBlog();
        sectionBlog.setBlog(blog);
        sectionBlog.setSection(section);
        // Position logic can be added later
        sectionBlogRepository.save(sectionBlog);

        return new Response("Blog added to section successfully", "200");
    }

    @Override
    @Transactional
    public Response removeBlogFromSection(Long blogId, Long sectionId) {
        // Implementation for removing can be added if needed
        return new Response("Not implemented yet", "501");
    }

    private BlogDto mapToDto(Blog blog) {
        BlogDto dto = new BlogDto();
        dto.setBlogId(blog.getBlogId());
        dto.setTitle(blog.getTitle());
        dto.setContent(blog.getContent());
        dto.setAuthorId(blog.getAuthor().getUserId());
        dto.setAuthorName(blog.getAuthor().getName().getFirstName() + " " + blog.getAuthor().getName().getLastName());
        dto.setCreatedAt(blog.getCreatedAt());
        dto.setUpdatedAt(blog.getUpdatedAt());
        return dto;
    }
}
