package com.project.lms.Entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long assignmentId; 

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String studentUsername;

    @Column(nullable = false)
    private LocalDateTime submissionDate; 

    private Double grade; 

    private String feedback; 

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getInstructorFeedback() {
        return feedback;
    }

    public void setInstructorFeedback(String feedback) {
        this.feedback = feedback;
    }
    
}