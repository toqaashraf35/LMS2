package com.project.lms.ServiceTest;

import com.project.lms.Entity.*;
import com.project.lms.Repository.*;
import com.project.lms.Service.AssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AssignmentServiceTest {

    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AssignmentSubmissionRepository assignmentSubmissionRepository;

    private Long courseId;

    @BeforeEach
    void setUp() {
        assignmentRepository.deleteAll();
        assignmentSubmissionRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();

        Course course = new Course();
        course.setName("Math Basics");
        course.setDescription("Basic math concepts.");
        course.setInstructorUsername("instructor1");
        course = courseRepository.save(course);
        courseId = course.getId();

        User student = new User();
        student.setUsername("student1");
        student.setPassword("password");
        student.setEmail("student@gmail.com");
        student.setRole("student");
        userRepository.save(student);
    }

    @Test
    void testCreateAssignment() {
        Assignment assignment = new Assignment();
        assignment.setTitle("Math Assignment");
        assignment.setDueDate(LocalDate.now().plusDays(7));

        Assignment savedAssignment = assignmentService.createAssignment(courseId, assignment);

        assertNotNull(savedAssignment.getId());
        assertEquals("Math Assignment", savedAssignment.getTitle());
        assertEquals(courseId, savedAssignment.getCourse().getId());
    }

    @Test
    void testSubmitAssignment() {
        Assignment assignment = new Assignment();
        assignment.setTitle("Math Assignment");
        assignment.setDueDate(LocalDate.now().plusDays(7));
        assignment = assignmentService.createAssignment(courseId, assignment);

        AssignmentSubmission submission = assignmentService.submitAssignment(assignment.getId(), "student1");

        assertNotNull(submission.getId());
        assertEquals("student1", submission.getStudentUsername());
        assertEquals(assignment.getId(), submission.getAssignmentId());
        assertNotNull(submission.getSubmissionDate());
    }

    @Test
    void testGradeAssignment() {
        Assignment assignment = new Assignment();
        assignment.setTitle("Math Assignment");
        assignment.setDueDate(LocalDate.now().plusDays(7));
        assignment = assignmentService.createAssignment(courseId, assignment);

        AssignmentSubmission submission = assignmentService.submitAssignment(assignment.getId(), "student1");

        AssignmentSubmission gradedSubmission = assignmentService.gradeAssignment(assignment.getId(), submission.getStudentId(), 95.0, "Well done!", "shahdtarek@gmail.com");

        assertEquals(95.0, gradedSubmission.getGrade());
        assertEquals("Well done!", gradedSubmission.getInstructorFeedback());
    }
    
    @Test
    void testGetStudentGradeAndFeedback() {
        Assignment assignment = new Assignment();
        assignment.setTitle("Science Assignment");
        assignment.setDueDate(LocalDate.now().plusDays(5));
        assignment = assignmentService.createAssignment(courseId, assignment);

        AssignmentSubmission submission = assignmentService.submitAssignment(assignment.getId(), "student1");

        assignmentService.gradeAssignment(assignment.getId(), submission.getStudentId(), 85.0, "Good job!", "shahdtarek@gmail.com");

        AssignmentSubmission result = assignmentService.getStudentGradeAndFeedback(assignment.getId(), "student1");

        assertEquals(85.0, result.getGrade());
        assertEquals("Good job!", result.getInstructorFeedback());
    }
    
    @Test
    void testGetAssignmentsByCourse() {
        Assignment assignment1 = new Assignment();
        assignment1.setTitle("Assignment 1");
        assignment1.setDueDate(LocalDate.now().plusDays(3));
        assignmentService.createAssignment(courseId, assignment1);

        Assignment assignment2 = new Assignment();
        assignment2.setTitle("Assignment 2");
        assignment2.setDueDate(LocalDate.now().plusDays(7));
        assignmentService.createAssignment(courseId, assignment2);

        List<Assignment> assignments = assignmentService.getAssignmentsByCourse(courseId);

        assertEquals(2, assignments.size());
        assertTrue(assignments.stream().anyMatch(a -> a.getTitle().equals("Assignment 1")));
        assertTrue(assignments.stream().anyMatch(a -> a.getTitle().equals("Assignment 2")));
    }

}