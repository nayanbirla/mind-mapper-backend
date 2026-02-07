package com.mindmapper.courseManagement.controller;

import com.mindmapper.courseManagement.dto.MarkLectureRequest;
import com.mindmapper.courseManagement.dto.ProgressResponse;
import com.mindmapper.courseManagement.service.ProgressService;
import com.mindmapper.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @PostMapping("/mark-lecture")
    public ResponseEntity<Response> markLecture(@RequestBody MarkLectureRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(progressService.markLectureAsComplete(request, email), HttpStatus.OK);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<ProgressResponse> getProgress(@PathVariable Long courseId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(progressService.getCourseProgress(courseId, email), HttpStatus.OK);
    }

    @PostMapping("/{courseId}/claim-certificate")
    public ResponseEntity<Response> claimCertificate(@PathVariable Long courseId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(progressService.claimCertificate(courseId, email), HttpStatus.OK);
    }
}
