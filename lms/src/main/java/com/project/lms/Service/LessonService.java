package com.project.lms.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.lms.Entity.*;
import com.project.lms.Repository.*;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseRepository courseRepository;

    //Create Lesson.
    public Lesson createLesson(Lesson lesson, String courseName) {
        Course course = courseRepository.findByName(courseName)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        lesson.setCourseName(courseName);
        return lessonRepository.save(lesson);
    }

    
}
