package com.project.lms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.project.lms.Service.QuizService;
import com.project.lms.Entity.Quiz;
import com.project.lms.Entity.Submission;
import com.project.lms.Repository.QuizRepository;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;
    @Autowired
    private  QuizRepository quizRepository;
    @Autowired
    private AuthenticationController authenticationController;

    // Create Quiz.
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
    
    //Get all Quizzes per Course.
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Quiz>> getQuizzesByCourse(@PathVariable Long courseId) {
        List<Quiz> quizzes = quizService.getQuizzesByCourse(courseId);
        return ResponseEntity.ok(quizzes);
    }

    // Get Quiz.
    @GetMapping("/{quizId}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long quizId) {
        Quiz quiz = quizService.getQuizById(quizId);
        return ResponseEntity.ok(quiz);
    }
    
    //Submit Quiz.
    @PostMapping("/submit/{quizId}")
    public ResponseEntity<Submission> submitQuiz(@PathVariable Long quizId,
        @RequestParam String username, 
        @RequestParam String password) {
        if (!authenticationController.isStudent(username, password) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } 
        Submission savedSubmission = quizService.submitQuiz(quizId, username);
        return ResponseEntity.ok(savedSubmission);
    }

    //Assign Score.
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

    //Get the grade.
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

}
