package com.project.lms.Entity;

import java.time.LocalDateTime;
import java.util.*;
import jakarta.persistence.*;



@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    private String instructorUsername;

    @Column(nullable = false)
    private String courseName; 

    @ElementCollection 
    @CollectionTable(name = "quiz_submissions", joinColumns = @JoinColumn(name = "quiz_id"))
    @Column(name = "student_username")
    private Set<String> submittedBy = new HashSet<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getInstructorUsername() {
        return instructorUsername;
    }

    public void setInstructorUsername(String instructorUsername) {
        this.instructorUsername = instructorUsername;
    }
    public String getCourseName() {
        return courseName;
    }

    public void setCourseId(String courseName) {
        this.courseName = courseName;
    }

    public Set<String> getSubmittedBy() { 
        return submittedBy; 
    } 
    public void setSubmittedBy(Set<String> submittedBy) {
         this.submittedBy = submittedBy; 
    }
 

}
