package com.mindmapper.courseManagement.service;

import com.mindmapper.courseManagement.dto.CourseRequest;
import com.mindmapper.courseManagement.dto.AddCourseResponse;
import com.mindmapper.courseManagement.dto.CourseResponse;
import com.mindmapper.entity.Course;
import com.mindmapper.utility.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {

    /**
     * Adds a new course to the system.
     *
     * @param addCourseRequest the request object containing course details
     */
    AddCourseResponse addCourse(CourseRequest addCourseRequest);

    /**
     * Updates an existing course in the system.
     *
     * @param updateCourseRequest the request object containing updated course details
     */
    AddCourseResponse updateCourse(CourseRequest updateCourseRequest);

    /**
     * Deletes a course from the system.
     *
     * @param courseId the ID of the course to be deleted
     */
    Response deleteCourse(Long courseId);

    List<CourseResponse> getAllCourses();

    CourseResponse getCourseById(Long courseId);

    Response uploadCourseImage(Long courseId, MultipartFile image);
}
