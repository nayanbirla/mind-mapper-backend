package com.mindmapper.courseManagement.controller;

import com.mindmapper.courseManagement.dto.CourseRequest;
import com.mindmapper.courseManagement.dto.AddCourseResponse;
import com.mindmapper.courseManagement.dto.CourseResponse;
import com.mindmapper.courseManagement.service.CourseService;
import com.mindmapper.entity.Course;
import com.mindmapper.utility.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/courseManagement/course")
@Slf4j
@CrossOrigin
public class CourseController {

    @Autowired
    private CourseService courseService;

      // Add course methods
      @PostMapping(value = "/add",consumes = MediaType.APPLICATION_JSON_VALUE)
      ResponseEntity<AddCourseResponse> addCourse(@RequestBody CourseRequest courseRequest) {
          log.info("Received request to add course: {}", courseRequest);
          return ResponseEntity.ok(courseService.addCourse(courseRequest));
      }

      // Update course methods
      @PutMapping(value = "/update",consumes = MediaType.APPLICATION_JSON_VALUE)
      ResponseEntity<AddCourseResponse> updateCourse(@RequestBody CourseRequest updateCourseRequest) {
          log.info("Received request to update course: {}", updateCourseRequest);
          return ResponseEntity.ok(courseService.updateCourse(updateCourseRequest));
      }

      //upload course image
      @PostMapping(value = "/uploadImage/{courseId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
      ResponseEntity<Response> uploadCourseImage(@PathVariable Long courseId, @RequestParam("image") MultipartFile image) {
          log.info("Received request to upload image for course with ID: {}", courseId);
          return ResponseEntity.ok(courseService.uploadCourseImage(courseId, image));
      }

      // Delete course methods
      @DeleteMapping(value = "/delete/{courseId}", produces = MediaType.APPLICATION_JSON_VALUE)
      ResponseEntity<Response> deleteCourse(@PathVariable Long courseId) {
          log.info("Received request to delete course with ID: {}", courseId);
          return ResponseEntity.ok(courseService.deleteCourse(courseId));
      }

      // Get all courses
      @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
      ResponseEntity<List<CourseResponse>> getAllCourses() {
            log.info("Received request to get all courses");
            return ResponseEntity.ok(courseService.getAllCourses());
      }

      // Get course by ID
      @GetMapping(value = "/{courseId}", produces = MediaType.APPLICATION_JSON_VALUE)
      ResponseEntity<CourseResponse> getCourseById(@PathVariable Long courseId) {
          log.info("Received request to get course with ID: {}", courseId);
          return ResponseEntity.ok(courseService.getCourseById(courseId));
      }

}
