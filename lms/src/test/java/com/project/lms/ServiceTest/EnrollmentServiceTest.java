package com.project.lms.ServiceTest;

import com.project.lms.Entity.Course;
import com.project.lms.Entity.Enrollment;
import com.project.lms.Entity.User;
import com.project.lms.Repository.CourseRepository;
import com.project.lms.Repository.EnrollmentRepository;
import com.project.lms.Repository.UserRepository;
import com.project.lms.Service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class EnrollmentServiceTest {

    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testEnrollInCourse() {
        User user = new User();
        user.setUsername("student1");
        user.setPassword("password");
        user.setEmail("student123@gmail.com");
        user.setRole("student");
        userRepository.save(user);

        Course course = new Course();
        course.setName("Java Basics");
        course.setDescription("Learn Java from scratch");
        course.setInstructorUsername("instructor1");
        courseRepository.save(course);

        Enrollment enrollment = enrollmentService.enrollInCourse("student1", "Java Basics");

        assertNotNull(enrollment.getId());
        assertEquals("student1", enrollment.getStudent().getUsername());
        assertEquals("Java Basics", enrollment.getCourse().getName());
    }

    @Test
    void testGetAllEnrollments() {
        testEnrollInCourse();

        List<Enrollment> enrollments = enrollmentService.getAllEnrollment();

        assertEquals(1, enrollments.size());
        assertEquals("Java Basics", enrollments.get(0).getCourse().getName());
        assertEquals("student1", enrollments.get(0).getStudent().getUsername());
    }

    @Test
    void testGetStudentNamesByCourseName() {
        testEnrollInCourse();

        List<User> students = enrollmentService.getStudentNamesByCourseName("Java Basics");

        assertEquals(1, students.size());
        assertEquals("student1", students.get(0).getUsername());
    }

    @Test
    void testRemoveStudentFromCourse() {
        testEnrollInCourse();

        enrollmentService.removeStudentFromCourse("instructor1", "student1", "Java Basics");

        List<Enrollment> enrollments = enrollmentService.getAllEnrollment();
        assertTrue(enrollments.isEmpty());
    }

    @Test
    void testRemoveStudentFromCourseUnauthorized() {
        testEnrollInCourse();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enrollmentService.removeStudentFromCourse("unauthorized_instructor", "student1", "Java Basics");
        });

        assertEquals("You are not the instructor of this course.", exception.getMessage());
    }
}
