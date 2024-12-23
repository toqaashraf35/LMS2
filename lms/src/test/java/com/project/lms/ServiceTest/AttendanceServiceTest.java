package com.project.lms.ServiceTest;

import com.project.lms.Entity.*;
import com.project.lms.Repository.*;
import com.project.lms.Service.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AttendanceServiceTest {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseRepository courseRepository;

    private Course course;


   @BeforeEach
    public void setUp() {
        course = new Course();
        course.setName("Java Basics");
        course.setDescription("Test Course Description"); 
        course.setInstructorUsername("Test instructorUsername");

        course = courseRepository.save(course);

        attendanceRepository.deleteAll();
        lessonRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testMarkAttendance() {
        User user = new User();
        user.setUsername("student1");
        user.setPassword("password");
        user.setEmail("student1@egmail.com");
        user.setRole("student");
        userRepository.save(user);

        
        Lesson lesson = new Lesson();
        lesson.setName("Math Lesson");
        lesson.setCourse(course);
        lessonRepository.save(lesson);

        Attendance attendance = new Attendance();
        attendance.setStatus(true);
        Attendance savedAttendance = attendanceService.markAttendance(attendance, "student1", "Math Lesson");

        assertNotNull(savedAttendance.getId());
        assertEquals("student1", savedAttendance.getStudentUsername());
        assertEquals("Math Lesson", savedAttendance.getLessonName());
        assertTrue(savedAttendance.getStatus());
        assertNotNull(savedAttendance.getCreatedAt());
    }

    @Test
    void testGetAttendance() {
        testMarkAttendance(); 

        Attendance attendance = attendanceService.getAttendance("student1");

        assertNotNull(attendance);
        assertEquals("student1", attendance.getStudentUsername());
        assertEquals("Math Lesson", attendance.getLessonName());
    }

    @Test
    void testGetAttendanceForNonExistentStudent() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            attendanceService.getAttendance("nonexistentUser");
        });

        assertEquals("Student not found", exception.getMessage());
    } 
}
