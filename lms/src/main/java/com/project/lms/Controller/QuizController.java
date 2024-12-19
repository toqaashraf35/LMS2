package com.project.lms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.lms.Entity.*;
import com.project.lms.Service.*;
import com.project.lms.Repository.*;


@RestController
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private AuthenticationController authenticationController;

    @PostMapping("/create")
     public ResponseEntity<Quiz> registerQuiz(@RequestParam String username, 
        @RequestParam String password, 
        @RequestBody Quiz quiz) {
        if (!authenticationController.isInstructor(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        Quiz newQuiz = quizService.createQuiz(username, quiz);
        quizRepository.save(quiz);
        return new ResponseEntity<>(newQuiz, HttpStatus.CREATED);
    }


    @PostMapping("/submit/{id}")
    public ResponseEntity<String> submitQuiz(@RequestParam String username,
        @RequestParam String password,
        @PathVariable Long id) {
        if (!authenticationController.isStudent(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only students can submit quizzes.");
        }
        if (quizService.submitQuiz(id, username)) {
            return ResponseEntity.ok("Quiz submitted successfully.");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz not found.");
        }
        
    }

}
