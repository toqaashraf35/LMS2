package com.project.lms.Entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String courseName;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(nullable = false)
    private LocalDateTime enrolledDate;

    public String getCourseName() {
    return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Long getId() {
    return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Course getCourse() {
    return course;
    }

    public void setCourse(Course course ) {
        this.course = course;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledDate;
    }

    public void setEnrolledAt(LocalDateTime enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

}


