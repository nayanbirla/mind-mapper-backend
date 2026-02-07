package com.mindmapper.courseManagement.service;

import com.mindmapper.courseManagement.dto.CreateQuizRequest;
import com.mindmapper.courseManagement.dto.QuizDto;
import com.mindmapper.utility.Response;

import java.util.List;

public interface QuizService {
    Response createQuiz(CreateQuizRequest request);

    QuizDto getQuizById(Long quizId);

    List<QuizDto> getQuizzesByCourse(Long courseId);

    Response submitQuiz(Long quizId, List<Long> selectedOptionIds); // Basic grading
}
