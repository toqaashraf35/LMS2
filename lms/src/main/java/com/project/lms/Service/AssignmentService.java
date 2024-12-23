package com.project.lms.Service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.lms.Repository.*;
import com.project.lms.Entity.*;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private AssignmentSubmissionRepository assignmentSubmissionRepository;

    // Create Assignment
    public Assignment createAssignment(Long courseId, Assignment assignment) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        assignment.setCourse(course);
        return assignmentRepository.save(assignment);
    }

    // Delete Assignment.
    public void deleteAssignment(Long assignmentId) {
        assignmentRepository.deleteById(assignmentId);
    }

    // Submit Assignment
    public AssignmentSubmission submitAssignment(Long assignmentId, String studentUsername) {
        @SuppressWarnings("unused")
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new RuntimeException("Assignment not found"));

        User student = userRepository.findByUsername(studentUsername)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        boolean alreadySubmitted = assignmentSubmissionRepository.existsByAssignmentIdAndStudentId(assignmentId, student.getId());
        if (alreadySubmitted) {
            throw new RuntimeException("Student has already submitted this assignment");
        }

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignmentId(assignmentId);
        submission.setStudentId(student.getId());
        submission.setStudentUsername(studentUsername);
        submission.setSubmissionDate(LocalDateTime.now());

        return assignmentSubmissionRepository.save(submission);
    }

    // Assign Grade and Feedback
    public AssignmentSubmission gradeAssignment(Long assignmentId, Long studentId, Double grade, String feedback, String instructorEmail) {
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        AssignmentSubmission submission = assignmentSubmissionRepository.findByAssignmentIdAndStudentId(assignmentId, studentId)
            .orElseThrow(() -> new RuntimeException("Submission not found"));

        submission.setGrade(grade);
        submission.setInstructorFeedback(feedback);

        String studentEmail = student.getEmail();

        Notification notification = new Notification();
        notification.setFromEmail(instructorEmail);
        notification.setToEmail(studentEmail); 
        notification.setMessage(String.format("Assignment %d has been graded.", assignmentId));
        notification.setSentAt(LocalDateTime.now()); 
        notification.setRead(false); 

        notificationRepository.save(notification);

        return assignmentSubmissionRepository.save(submission);
    }

    // Get Student Grade and Feedback
    public AssignmentSubmission getStudentGradeAndFeedback(Long assignmentId, String studentUsername) {
        User student = userRepository.findByUsername(studentUsername)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        return assignmentSubmissionRepository.findByAssignmentIdAndStudentId(assignmentId, student.getId())
            .orElseThrow(() -> new RuntimeException("Submission not found"));
    }

    // Get all Assignments per Course
    public List<Assignment> getAssignmentsByCourse(Long courseId) {
        return assignmentRepository.findByCourseId(courseId);
    }

    // Get Assignment Details
    public Assignment getAssignmentById(Long assignmentId) {
        return assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }
}