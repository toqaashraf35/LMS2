package com.project.lms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.project.lms.Entity.*;
import com.project.lms.Repository.AssignmentRepository;
import com.project.lms.Repository.UserRepository;
import com.project.lms.Service.AssignmentService;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private UserRepository userRepository;

    // Create Assignment
    @PostMapping("create/course/{courseId}")
    public ResponseEntity<Assignment> createAssignment(
        @PathVariable Long courseId,
        @RequestParam String username, 
        @RequestParam String password, 
        @RequestBody Assignment assignment) {
        if (!authenticationController.isInstructor(username, password) && !authenticationController.isAdmin(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Assignment createdAssignment = assignmentService.createAssignment(courseId, assignment);
        assignmentRepository.save(createdAssignment);
        return new ResponseEntity<>(createdAssignment, HttpStatus.CREATED);
    }

    // Delete Assignment.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
    // Get all Assignments per Course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByCourse(@PathVariable Long courseId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByCourse(courseId);
        return ResponseEntity.ok(assignments);
    }

    // Get Assignment
    @GetMapping("/{assignmentId}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long assignmentId) {
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        return ResponseEntity.ok(assignment);
    }

    // Submit Assignment
    @PostMapping("/submit/{assignmentId}")
    public ResponseEntity<AssignmentSubmission> submitAssignment(
        @PathVariable Long assignmentId,
        @RequestParam String username, 
        @RequestParam String password) {
        if (!authenticationController.isStudent(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        AssignmentSubmission savedSubmission = assignmentService.submitAssignment(assignmentId, username);
        return ResponseEntity.ok(savedSubmission);
    }

    // Assign Grade
    @PutMapping("/assign-grade")
    public ResponseEntity<AssignmentSubmission> assignGrade(
        @RequestParam String username, 
        @RequestParam String password, 
        @RequestBody AssignmentSubmission submission) {
        if (!authenticationController.isInstructor(username, password) && !authenticationController.isAdmin(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        User instructor = userRepository.findByUsername(username)
           .orElseThrow(() -> new RuntimeException("Instructor not found"));
        String instructorEmail = instructor.getEmail();
        AssignmentSubmission updatedSubmission = assignmentService.gradeAssignment(submission.getAssignmentId(), submission.getStudentId(), submission.getGrade(), submission.getInstructorFeedback(), instructorEmail);
        return ResponseEntity.ok(updatedSubmission);
    }

    // Get Student Grade and Feedback
    @GetMapping("/get-grade/{assignmentId}")
    public ResponseEntity<Map<String, Object>> getGrade(
        @PathVariable Long assignmentId,
        @RequestParam String username, 
        @RequestParam String password) {
        if (!authenticationController.isStudent(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        AssignmentSubmission assignmentsubmission = assignmentService.getStudentGradeAndFeedback(assignmentId, username);
        Map<String, Object> response = new HashMap<>();
        response.put("grade", assignmentsubmission.getGrade());
        response.put("feedback", assignmentsubmission.getInstructorFeedback());
        return ResponseEntity.ok(response);
    }
}