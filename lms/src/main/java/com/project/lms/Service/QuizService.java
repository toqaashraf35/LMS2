package com.project.lms.Service;

import org.springframework.beans.factory.annotation.Autowired;
import com.project.lms.Repository.*;
import com.project.lms.Entity.*;

public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public Quiz createQuiz(String instructorUsername, Quiz quiz) {
        quiz.setInstructorUsername(instructorUsername); 
        return quizRepository.save(quiz);

    }

    public boolean submitQuiz(Long quizId, String studentUsername) {
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));
        quiz.getSubmittedBy().add(studentUsername);
        quizRepository.save(quiz);
        return true;
    }
}
