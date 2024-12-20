package com.project.lms.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.project.lms.Entity.Course;
import com.project.lms.Repository.CourseRepository;
import com.project.lms.Service.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private  CourseRepository courseRepository;
    @Autowired
    private AuthenticationController authenticationController;

    //Create Course.
    @PostMapping("/create")
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
    
    //Delete Course.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    //Update Course.
    @PutMapping("/update/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourse(id, course);
        return ResponseEntity.ok(updatedCourse);
    }

    //Get all Courses.
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    
}
