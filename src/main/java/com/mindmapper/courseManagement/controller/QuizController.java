package com.mindmapper.courseManagement.controller;

import com.mindmapper.courseManagement.dto.CreateQuizRequest;
import com.mindmapper.courseManagement.dto.QuizDto;
import com.mindmapper.courseManagement.service.QuizService;
import com.mindmapper.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/")
    public ResponseEntity<Response> createQuiz(@RequestBody CreateQuizRequest request) {
        return new ResponseEntity<>(quizService.createQuiz(request), HttpStatus.CREATED);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDto> getQuiz(@PathVariable Long quizId) {
        return new ResponseEntity<>(quizService.getQuizById(quizId), HttpStatus.OK);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<QuizDto>> getQuizzesByCourse(@PathVariable Long courseId) {
        return new ResponseEntity<>(quizService.getQuizzesByCourse(courseId), HttpStatus.OK);
    }
}
