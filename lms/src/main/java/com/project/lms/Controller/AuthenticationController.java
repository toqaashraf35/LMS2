package com.project.lms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.lms.Repository.*;
import com.project.lms.Entity.*;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
     @Autowired
    private UserRepository userRepository;
    
    private static final String USERNAME = "farida";
    private static final String PASSWORD = "farida123@gmail.com";

    public boolean isAdmin(String username, String password) {
        return USERNAME.equals(username) && PASSWORD.equals(password);
    }

    public boolean isInstructor(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return "instructor".equals(user.getRole());
        }

        return false;
    }

    public boolean isStudent(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return "student".equals(user.getRole());
        }

        return false;
    }
    @GetMapping("/secure-endpoint")
    public ResponseEntity<String> secureEndpoint(@RequestParam String username, @RequestParam String password) {
        if (isAdmin(username, password)) {
            return ResponseEntity.ok("Welcome, Admin " + username + "!");
        } else if (isInstructor(username, password)) {
            return ResponseEntity.ok("Welcome, Instructor " + username + "!");
        } else if(isStudent(username, password)){
            return ResponseEntity.ok("Welcome, Student " + username + "!");
        }
        else {
            return ResponseEntity.status(401).body("Unauthorized: Invalid username or password.");
        }
    }
}