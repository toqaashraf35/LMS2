package com.project.lms.ServiceTest;

import com.project.lms.Entity.Course;
import com.project.lms.Repository.CourseRepository;
import com.project.lms.Service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CourseServiceIntegrationTest {

    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll(); 
    }

    @Test
    void testCreateCourse() {
        Course course = new Course();
        course.setName("Java");
        course.setDescription("Introduction to Java");

        Course createdCourse = courseService.createCourse("instructor1", course);

        assertNotNull(createdCourse.getId());
        assertEquals("Java", createdCourse.getName());
        assertEquals("Introduction to Java", createdCourse.getDescription());
        assertEquals("instructor1", createdCourse.getInstructorUsername());
    }

    @Test
    void testDeleteCourse() {
        Course course = new Course();
        course.setName("Java");
        course.setDescription("Introduction to Java");

        Course savedCourse = courseRepository.save(course);
        Long courseId = savedCourse.getId();

        courseService.deleteCourse(courseId);

        assertFalse(courseRepository.findById(courseId).isPresent());
    }

    @Test
    void testUpdateCourse() {
        Course course = new Course();
        course.setName("Java");
        course.setDescription("Old Description");

        Course savedCourse = courseRepository.save(course);

        Course updatedCourse = new Course();
        updatedCourse.setName("Java 2");
        updatedCourse.setDescription("Updated Description");

        Course result = courseService.updateCourse(savedCourse.getId(), updatedCourse);

        assertEquals("Java 2", result.getName());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    void testUpdateCourseNotFound() {
        Long invalidCourseId = 999L;
        Course updatedCourse = new Course();
        updatedCourse.setName("Java 2");
        updatedCourse.setDescription("Updated Description");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.updateCourse(invalidCourseId, updatedCourse);
        });

        assertEquals("Course not found", exception.getMessage());
    }

    @Test
    void testGetAllCourses() {
        Course course1 = new Course();
        course1.setName("Java");
        course1.setDescription("Introduction to Java");

        Course course2 = new Course();
        course2.setName("Python");
        course2.setDescription("Introduction to Python");

        courseRepository.save(course1);
        courseRepository.save(course2);

        List<Course> courses = courseService.getAllCourses();

        assertEquals(2, courses.size());
        assertTrue(courses.stream().anyMatch(course -> course.getName().equals("Java")));
        assertTrue(courses.stream().anyMatch(course -> course.getName().equals("Python")));
    }
}
