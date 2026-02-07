package com.mindmapper.courseManagement.controller;

import com.mindmapper.courseManagement.dto.LectureRequest;
import com.mindmapper.courseManagement.dto.LectureResponse;
import com.mindmapper.courseManagement.dto.SectionLectureResponse;
import com.mindmapper.courseManagement.service.LectureService;
import com.mindmapper.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/courseManagement/lecture")
@CrossOrigin
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @PostMapping(value = "/addLecture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LectureResponse> addLecture(
            @RequestPart("lectureRequest") LectureRequest lectureRequest,
            @RequestPart("lectureVideo") MultipartFile lectureVideo) {
        // This method should handle the request to add a lecture
        // The actual implementation will depend on the LectureService methods

        return new ResponseEntity<>(lectureService.addLecture(lectureRequest,lectureVideo), HttpStatus.CREATED); // Placeholder response
    }

    @PutMapping("/updateLecture/{lectureId}")
    public ResponseEntity<LectureResponse> updateLecture(@RequestBody LectureRequest lectureRequest, @PathVariable Long lectureId) {
        // This method should handle the request to update a lecture
        // The actual implementation will depend on the LectureService methods

        return new ResponseEntity<>(lectureService.updateLecture(lectureRequest, lectureId), HttpStatus.OK); // Placeholder response
    }

    @DeleteMapping("/deleteLecture/{lectureId}")
    public ResponseEntity<Response> deleteLecture(@PathVariable Long lectureId) {
        // This method should handle the request to delete a lecture
        // The actual implementation will depend on the LectureService methods
        return new ResponseEntity<>(lectureService.deleteLecture(lectureId), HttpStatus.OK); // Placeholder response
    }

    @GetMapping("/getLectureBySectionId/{sectionId}")
    public ResponseEntity<SectionLectureResponse> getLectureBySectionId(@PathVariable Long sectionId) {
        // This method should handle the request to get lectures by section ID
        // The actual implementation will depend on the LectureService methods
        return new ResponseEntity<>(lectureService.getLectureBySectionId(sectionId), HttpStatus.OK); // Placeholder response
    }

}
