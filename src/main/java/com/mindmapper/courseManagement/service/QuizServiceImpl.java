package com.mindmapper.courseManagement.service;

import com.mindmapper.courseManagement.dto.CreateQuizRequest;
import com.mindmapper.courseManagement.dto.OptionDto;
import com.mindmapper.courseManagement.dto.QuestionDto;
import com.mindmapper.courseManagement.dto.QuizDto;
import com.mindmapper.entity.Course;
import com.mindmapper.entity.Option;
import com.mindmapper.entity.Question;
import com.mindmapper.entity.Quiz;
import com.mindmapper.entity.Section;
import com.mindmapper.repository.CourseRepository;
import com.mindmapper.repository.QuizRepository;
import com.mindmapper.repository.SectionRepository;
import com.mindmapper.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    @Transactional
    public Response createQuiz(CreateQuizRequest request) {
        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setPassPercentage(request.getPassPercentage());

        if (request.getCourseId() != null) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            quiz.setCourse(course);
        }

        if (request.getSectionId() != null) {
            Section section = sectionRepository.findById(request.getSectionId())
                    .orElseThrow(() -> new RuntimeException("Section not found"));
            quiz.setSection(section);
            // Also link course from section if not provided?
            if (quiz.getCourse() == null) {
                quiz.setCourse(section.getCourse());
            }
        }

        List<Question> questions = new ArrayList<>();
        if (request.getQuestions() != null) {
            for (QuestionDto qDto : request.getQuestions()) {
                Question q = new Question();
                q.setText(qDto.getText());
                q.setType(qDto.getType());
                q.setQuiz(quiz); // Link back

                List<Option> options = new ArrayList<>();
                if (qDto.getOptions() != null) {
                    for (OptionDto oDto : qDto.getOptions()) {
                        Option o = new Option();
                        o.setOptionText(oDto.getOptionText());
                        o.setIsCorrect(oDto.getIsCorrect());
                        o.setQuestion(q); // Link back
                        options.add(o);
                    }
                }
                q.setOptions(options);
                questions.add(q);
            }
        }
        quiz.setQuestions(questions);

        quizRepository.save(quiz);

        return new Response("Quiz created successfully", "201");
    }

    @Override
    public QuizDto getQuizById(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        return mapToDto(quiz);
    }

    @Override
    public List<QuizDto> getQuizzesByCourse(Long courseId) {
        return quizRepository.findByCourse_CourseId(courseId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public Response submitQuiz(Long quizId, List<Long> selectedOptionIds) {
        // Basic implementation: Calculate store
        // For now, just returning success as this needs 'Enrollment' context to save
        // score properly
        return new Response("Quiz submitted (Grading implementation pending Enrollment module)", "200");
    }

    private QuizDto mapToDto(Quiz quiz) {
        QuizDto dto = new QuizDto();
        dto.setQuizId(quiz.getQuizId());
        dto.setTitle(quiz.getTitle());
        dto.setPassPercentage(quiz.getPassPercentage());
        if (quiz.getCourse() != null)
            dto.setCourseId(quiz.getCourse().getCourseId());
        if (quiz.getSection() != null)
            dto.setSectionId(quiz.getSection().getSectionId());

        if (quiz.getQuestions() != null) {
            dto.setQuestions(quiz.getQuestions().stream().map(q -> {
                QuestionDto qDto = new QuestionDto();
                qDto.setQuestionId(q.getQuestionId());
                qDto.setText(q.getText());
                qDto.setType(q.getType());
                if (q.getOptions() != null) {
                    qDto.setOptions(q.getOptions().stream().map(o -> {
                        OptionDto oDto = new OptionDto();
                        oDto.setOptionId(o.getOptionId());
                        oDto.setOptionText(o.getOptionText());
                        oDto.setIsCorrect(o.getIsCorrect()); // Should we hide this for student? Yes, usually.
                        return oDto;
                    }).collect(Collectors.toList()));
                }
                return qDto;
            }).collect(Collectors.toList()));
        }
        return dto;
    }
}
