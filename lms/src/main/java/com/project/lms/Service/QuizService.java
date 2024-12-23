package com.project.lms.Service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.lms.Repository.*;
import com.project.lms.Entity.*;

@Service
public class QuizService {

    @Autowired
    public QuizRepository quizRepository;
    @Autowired
    public CourseRepository courseRepository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public SubmissionRepository submissionRepository;

    // Create Quiz
    public Quiz createQuiz(Long courseId, Quiz quiz) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        quiz.setCourse(course);
        return quizRepository.save(quiz);
    }

    //Submit Quiz.
    public Submission submitQuiz(Long quizId, String studentUsername) {
        boolean quizExists = quizRepository.existsById(quizId);
        if (!quizExists) {
            throw new RuntimeException("Quiz not found");
        }
        User student = userRepository.findByUsername(studentUsername)
        .orElseThrow(() -> new RuntimeException("Student not found"));

        Submission submission = new Submission();;
        submission.setQuizId(quizId);
        submission.setStudentUsername(studentUsername);
        submission.setStudentId(student.getId());
       
        List<Submission> existingSubmissions = submissionRepository.findByQuizId(quizId);
        boolean alreadySubmitted = existingSubmissions.stream()
            .anyMatch(sub -> sub.getStudentId().equals(submission.getStudentId()));
        if (alreadySubmitted) {
            throw new RuntimeException("Student has already submitted this quiz");
        }
        return submissionRepository.save(submission);
    }

    //Assign a Score.
    public Submission assignScore(Long QuizId , Long StudentId , Double Score , String Feedback) { // 
        Submission submission = submissionRepository.findByQuizIdAndStudentId(QuizId,StudentId)
            .orElseThrow(() -> new RuntimeException("Submission not found"));
        submission.setScore(Score);
        submission.setFeedback(Feedback);
        return submissionRepository.save(submission);
    }
    
    //Get the grade and feedback.
    public Submission getStudentScoreAndFeedback(Long quizId, String studentUsername) {
        User student = userRepository.findByUsername(studentUsername)
        .orElseThrow(() -> new RuntimeException("Student not found"));
        return submissionRepository.findByQuizIdAndStudentId(quizId, student.getId())
                .orElseThrow(() -> new RuntimeException("Submission not found"));
    }

    //Get all Quizzes per Course
    public List<Quiz> getQuizzesByCourse(Long courseId) {
        return quizRepository.findByCourseId(courseId);
    }

    // Get Quiz Details
    public Quiz getQuizById(Long quizId) {
        return quizRepository.findById(quizId)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

}
