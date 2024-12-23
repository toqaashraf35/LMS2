package com.project.lms.ServiceTest;

import com.project.lms.Entity.Course;
import com.project.lms.Entity.Lesson;
import com.project.lms.Repository.CourseRepository;
import com.project.lms.Repository.LessonRepository;
import com.project.lms.Service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class LessonServiceTest {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        lessonRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    void testCreateLesson() {
    Course course = new Course();
    course.setName("Java");
    course.setDescription("Introduction to Java");
    courseRepository.save(course);

    Lesson lesson = new Lesson();
    lesson.setName("Lesson 1");

    Lesson createdLesson = lessonService.createLesson(lesson, "Java");

    assertNotNull(createdLesson.getId(), "Lesson ID should not be null");
    assertEquals("Lesson 1", createdLesson.getName(), "Lesson name should match");
    assertEquals("Java", createdLesson.getCourse().getName(), "Course name should match");
    }


    @Test
    void testCreateLessonCourseNotFound() {
        Lesson lesson = new Lesson();
        lesson.setName("Lesson 1");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            lessonService.createLesson(lesson, "Nonexistent Course");
        });

        assertEquals("Course not found", exception.getMessage(), "Exception message should match when course is not found");
    }

    @Test
    void testDeleteLesson() {
        // Create and save a course
        Course course = new Course();
        course.setName("Java");
        course.setDescription("Introduction to Java");
        courseRepository.save(course);

        // Create a lesson and associate with the course
        Lesson lesson = new Lesson();
        lesson.setName("Lesson 1");
        lesson = lessonService.createLesson(lesson, "Java");

        Long lessonId = lesson.getId();
        assertNotNull(lessonId, "Lesson ID should not be null before deletion");

        // Delete the lesson
        lessonService.deleteLesson(lessonId);

        assertFalse(lessonRepository.existsById(lessonId), "Lesson should no longer exist after deletion");
    }
}