package com.mindmapper.courseManagement.service;

import com.mindmapper.courseManagement.dto.CourseRequest;
import com.mindmapper.courseManagement.dto.AddCourseResponse;
import com.mindmapper.courseManagement.dto.CourseResponse;
import com.mindmapper.entity.Course;
import com.mindmapper.entity.UserInfo;
import com.mindmapper.entity.embeddable.Name;
import com.mindmapper.repository.CourseRepository;
import com.mindmapper.repository.UserRepository;
import com.mindmapper.utility.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userInfoRepository;

    public AddCourseResponse addCourse(CourseRequest addCourseRequest) {
        // Logic to add a course using courseRepository
        validateCourseRequest(addCourseRequest);
        // Convert AddCourseRequest to Course entity and save it
        Course course = new Course();
        course.setTitle(addCourseRequest.getTitle());
        course.setDescription(addCourseRequest.getDescription());
        course.setCategory(addCourseRequest.getCategory());
        course.setPrice(addCourseRequest.getPrice());
        course.setLevel(addCourseRequest.getLevel());
        //for demo purposes, we are not extracting the instructor ID from the token
        UserInfo instructor = userInfoRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        course.setInstructorId(instructor); // Assuming a default instructor ID for simplicity

        course = courseRepository.save(course);

        return new AddCourseResponse("Course added successfully","201",course.getCourseId());
    }

    @Override
    public AddCourseResponse updateCourse(CourseRequest updateCourseRequest) {
        // Logic to update a course using courseRepository
        validateCourseRequest(updateCourseRequest);

        Course course = courseRepository.findById(updateCourseRequest.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + updateCourseRequest.getCourseId()));

        course.setTitle(updateCourseRequest.getTitle());
        course.setDescription(updateCourseRequest.getDescription());
        course.setCategory(updateCourseRequest.getCategory());
        course.setPrice(updateCourseRequest.getPrice());
        course.setLevel(updateCourseRequest.getLevel());

        courseRepository.save(course);

        return new AddCourseResponse("Course updated successfully", "200",course.getCourseId());
    }

    private void validateCourseRequest(CourseRequest addCourseRequest) {

        if(addCourseRequest.getTitle() == null || addCourseRequest.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Course title is required");
        }
        if(addCourseRequest.getDescription() == null || addCourseRequest.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Course description is required");
        }
        if(addCourseRequest.getCategory() == null || addCourseRequest.getCategory().isEmpty()) {
            throw new IllegalArgumentException("Course category is required");
        }
        if(addCourseRequest.getPrice() == null || addCourseRequest.getPrice() < 0) {
            throw new IllegalArgumentException("Course price must be greater than zero");
        }
        if(addCourseRequest.getLevel() == null || addCourseRequest.getLevel().isEmpty()) {
            throw new IllegalArgumentException("Course level is required");
        }
    }

    @Override
    public Response deleteCourse(Long courseId) {
        // Logic to delete a course using courseRepository
        if (courseId == null) {
            throw new IllegalArgumentException("Course ID is required for deletion");
        }
        if (!courseRepository.existsById(courseId)) {
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }
        courseRepository.deleteById(courseId);
        return new Response("Course deleted successfully", "200");
    }

    @Override
    public List<CourseResponse> getAllCourses() {

        List<Course> courseList = courseRepository.findAll();

        return courseList.stream().map(course -> new CourseResponse(
                course.getCourseId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory(),
                course.getPrice(),
                course.getLevel(),
                course.getImageUrl(),
                course.getInstructorId().getUserId(),
                nameBuilder(course.getInstructorId().getName())
        )).toList();
    }

    @Override
    public CourseResponse getCourseById(Long courseId) {
        if(courseId== null) {
            throw new IllegalArgumentException("Course ID is required");
        }
        Course course=courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        return new CourseResponse(
                course.getCourseId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory(),
                course.getPrice(),
                course.getLevel(),
                course.getImageUrl(),
                course.getInstructorId().getUserId(),
                nameBuilder(course.getInstructorId().getName())
        );
    }

    @Override
    public Response uploadCourseImage(Long courseId, MultipartFile image) {
        if (courseId == null || image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Course ID and image file are required for upload");
        }

        // Check if course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        // Upload the image and get the path
        String imagePath = uploadCourseImage(image);

        // Set the image path in the course entity
        course.setImageUrl(imagePath);
        courseRepository.save(course);

        return new Response("Course image uploaded successfully", "200");
    }

    private String uploadCourseImage(MultipartFile courseImage) {

        try {
            // Path to static/images (during development)
            String projectDir = System.getProperty("user.dir"); // gets current project directory
            String uploadDir = projectDir + File.separator + "uploads" + File.separator + "course-images";

            // Ensure the directory exists
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Create full path to save
            String originalFilename = courseImage.getOriginalFilename();
            String filePath = uploadDir + File.separator + originalFilename;

            // Save the file
            File dest = new File(filePath);
            courseImage.transferTo(dest);

            // Return path for use (you can construct public URL)
            return "/uploads/course-images/" + originalFilename;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store course image", e);
        }
    }

    private String nameBuilder(Name name){
        return name.getFirstName() + " " + name.getLastName();
    }
}
