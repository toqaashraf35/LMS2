package com.project.lms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.lms.Service.QuizService;
import com.project.lms.Entity.Quiz;
import com.project.lms.Entity.Submission;
import com.project.lms.Repository.QuizRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;
    @Autowired
    private  QuizRepository quizRepository;
    @Autowired
    private AuthenticationController authenticationController;

    // Create a new quiz
    @PostMapping("create/course/{courseId}")
    public ResponseEntity<Quiz> createQuiz(@PathVariable Long courseId,
        @RequestParam String username, 
        @RequestParam String password, 
        @RequestBody Quiz quiz) {
        if (!authenticationController.isInstructor(username, password) && !authenticationController.isAdmin(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Quiz createdQuiz = quizService.createQuiz(courseId, quiz);
        quizRepository.save(createdQuiz);
        return new ResponseEntity<>(createdQuiz, HttpStatus.CREATED);
    }
    @PostMapping("/{quizId}/submit")
    public ResponseEntity<Submission> submitQuiz(@PathVariable Long quizId,
        @RequestParam String username, 
        @RequestParam String password) {
        if (!authenticationController.isStudent(username, password) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } 


        Submission savedSubmission = quizService.submitQuiz(quizId, username);
        return ResponseEntity.ok(savedSubmission);
    }

    @PutMapping("/assign-score")
    public ResponseEntity<Submission> assignScore(
        @RequestParam String username, 
        @RequestParam String password, 
        @RequestBody Submission submission) {
        if (!authenticationController.isInstructor(username, password) && !authenticationController.isAdmin(username, password)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Submission updatedSubmission = quizService.assignScore(submission.getQuizId(),submission.getStudentId(),submission.getScore(),submission.getFeedback());
        return ResponseEntity.ok(updatedSubmission);
    }
    @GetMapping("get-score/{quizId}")
    public ResponseEntity<Map<String, Object>> getFeedback(
        @PathVariable Long quizId,
        @RequestParam String username, 
        @RequestParam String password) {
        if (!authenticationController.isStudent(username, password) ) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        
        Submission submission = quizService.getStudentScoreAndFeedback(quizId, username);

        Map<String, Object> response = new HashMap<>();
        response.put("score", submission.getScore());
        response.put("feedback", submission.getFeedback());
        return ResponseEntity.ok(response);
    }


    // Get all quizzes for a course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Quiz>> getQuizzesByCourse(@PathVariable Long courseId) {
        List<Quiz> quizzes = quizService.getQuizzesByCourse(courseId);
        return ResponseEntity.ok(quizzes);
    }

    // Get a specific quiz by ID
    @GetMapping("/{quizId}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long quizId) {
        Quiz quiz = quizService.getQuizById(quizId);
        return ResponseEntity.ok(quiz);
    }



}
