package com.project.lms.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.project.lms.Entity.*;
import com.project.lms.Repository.*;
import com.project.lms.Service.*;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private  EnrollmentRepository enrollmentRepository;
    @Autowired
    private AuthenticationController authenticationController;

    //Enroll in Course.
    @PostMapping("/enroll")
    public ResponseEntity<Enrollment> enrollInCourse(@RequestParam String username,
        @RequestParam String password, 
        @RequestBody Enrollment enrollment) {
        if (!authenticationController.isStudent(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Enrollment newEnrollment = enrollmentService.enrollInCourse(username, enrollment.getCourseName());
        enrollmentRepository.save(newEnrollment);
        return new ResponseEntity<>(newEnrollment, HttpStatus.CREATED);
    }

    //Get all Enrollments.
    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollment(@RequestParam String username,
        @RequestParam String password) {
        if (!authenticationController.isInstructor(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        List<Enrollment> enrollments = enrollmentService.getAllEnrollment();
        return ResponseEntity.ok(enrollments);
    }

    //Get Students per Course.
    @GetMapping("/students/{courseName}")
    public ResponseEntity<List<String>> getStudentNames(@RequestParam String username,
        @RequestParam String password,
        @PathVariable String courseName) {
        if (!authenticationController.isInstructor(username, password) && !authenticationController.isAdmin(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        List<String> studentNames = enrollmentService.getStudentNamesByCourseName(courseName);
        return ResponseEntity.ok(studentNames);
    }

    //Delete Student from Course.
    @DeleteMapping("/students/delete/{courseName}")
    public ResponseEntity<String> removeStudentFromCourse(@RequestParam String username,
        @RequestParam String password,
        @RequestParam String studentUsername,
        @PathVariable String courseName) {
        if (!authenticationController.isInstructor(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }
        try {
            enrollmentService.removeStudentFromCourse(username, studentUsername, courseName);
            return ResponseEntity.ok("Student removed from the course.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
