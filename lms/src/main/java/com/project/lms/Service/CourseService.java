package com.project.lms.Service;

import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.lms.Repository.*;
import com.project.lms.Entity.*;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    //Create course.
    public Course createCourse(String instructorName, Course course) {
        course.setInstructorUsername(instructorName); 
        return courseRepository.save(course);
    }

    //Delete course.
    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    //Update course.
    public Course updateCourse(Long courseId, Course courseDetails) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setName(courseDetails.getName());
        course.setDescription(courseDetails.getDescription());
        return courseRepository.save(course);
    }

    //Get all courses.
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    //Enroll in course.
    public Enrollment enrollInCourse(String studentUsername, String courseName) {
        User student = userRepository.findByUsername(studentUsername)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findByName(courseName)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentName(studentUsername);
        enrollment.setCourseName(courseName);
        enrollment.setEnrolledAt(LocalDateTime.now());

        return enrollmentRepository.save(enrollment);
    }
}
