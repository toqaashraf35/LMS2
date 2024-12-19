package com.project.lms.Controller;

import com.project.lms.Entity.*;
import com.project.lms.Repository.*;
import com.project.lms.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private  CourseRepository courseRepository;
    @Autowired
    private AuthenticationController authenticationController;

    @PostMapping("/register")
    public ResponseEntity<Course> registerCourse(@RequestParam String username, 
            @RequestParam String password, 
            @RequestBody Course course) {
        if (!authenticationController.isInstructor(username, password) && !authenticationController.isAdmin(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        
        Course newCourse = courseService.createCourse(username, course);
        courseRepository.save(course);
        return new ResponseEntity<>(newCourse, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourse(id, course);
        return ResponseEntity.ok(updatedCourse);
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
}
