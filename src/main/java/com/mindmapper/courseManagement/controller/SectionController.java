package com.mindmapper.courseManagement.controller;

import com.mindmapper.courseManagement.dto.SectionRequest;
import com.mindmapper.courseManagement.dto.SectionResponse;
import com.mindmapper.courseManagement.service.SectionService;
import com.mindmapper.entity.Section;
import com.mindmapper.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courseManagement/section")
@CrossOrigin
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @PostMapping("/addSection")
    public ResponseEntity<SectionResponse> addSection(@RequestBody SectionRequest sectionRequest) {
        return new ResponseEntity<>(sectionService.addSection(sectionRequest), HttpStatus.CREATED);
    }

    @PutMapping("/updateSection/{sectionId}")
    public ResponseEntity<SectionResponse> updateSection(@RequestBody SectionRequest sectionRequest, @PathVariable Long sectionId) {
        return new ResponseEntity<>(sectionService.updateSection(sectionRequest, sectionId), HttpStatus.OK);
    }

    @DeleteMapping("/deleteSection/{sectionId}")
    public ResponseEntity<Response> deleteSection(@PathVariable Long sectionId) {
        return new ResponseEntity<>(sectionService.deleteSection(sectionId), HttpStatus.OK);
    }

    @GetMapping("/getSectionsByCourseId/{courseId}")
    public ResponseEntity<List<Section>> getSectionsByCourseId(@PathVariable Long courseId) {
        return new ResponseEntity<>(sectionService.getSectionsByCourseId(courseId), HttpStatus.OK);
    }

    @GetMapping("/getSectionById/{sectionId}")
    public ResponseEntity<Section> getSectionById(@PathVariable Long sectionId) {
        return new ResponseEntity<>(sectionService.getSectionById(sectionId), HttpStatus.OK);
    }
}
