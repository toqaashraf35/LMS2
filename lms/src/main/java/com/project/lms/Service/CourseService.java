package com.project.lms.Service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.lms.Repository.*;
import com.project.lms.Entity.*;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

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
}
