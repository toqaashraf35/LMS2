package com.project.lms.Entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
@Entity
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String courseName;

    @Column(nullable = false)
    private String studentUsername;

    @Column(nullable = false)
    private LocalDateTime enrolledDate;

    public Long getId() {
    return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
    return courseName;
    }

    public void setCourseName(String courseName ) {
        this.courseName = courseName;
    }

    public String getStudentName() {
        return studentUsername;
    }

    public void setStudentName(String studentUsername) {
        this.studentUsername = studentUsername;
    }
    
    public LocalDateTime getEnrolledAt() {
        return enrolledDate;
    }

    public void setEnrolledAt(LocalDateTime enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

}
