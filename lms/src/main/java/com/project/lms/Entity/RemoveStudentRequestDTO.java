package com.project.lms.Entity;

public class RemoveStudentRequestDTO{
    private String studentUsername;
    private String courseName;

    // Getters and Setters
    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}

