package com.project.lms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.project.lms.Entity.Lesson;
import com.project.lms.Repository.LessonRepository;
import com.project.lms.Service.LessonService;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private  LessonRepository lessonRepository;
    @Autowired
    private AuthenticationController authenticationController;

    //Create Lesson.
    @PostMapping("/create")
    public ResponseEntity<Lesson> registerCourse(
            @RequestParam String username, 
            @RequestParam String password, 
            @RequestBody Lesson lesson) {
        if (!authenticationController.isInstructor(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Lesson newLesson = lessonService.createLesson(lesson, lesson.getCourseName());
        lessonRepository.save(lesson);
        return new ResponseEntity<>(newLesson, HttpStatus.CREATED);
    }

}

