package com.project.lms.ServiceTest;

import com.project.lms.Entity.*;
import com.project.lms.Repository.*;
import com.project.lms.Service.QuizService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class QuizServiceTest {

    @Autowired
    private QuizService quizService;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    private Course course;
    private User student;

    @BeforeEach
    public void setUp() {
        course = new Course();
        course.setName("Java Basics");
        course.setDescription("Test Course Description"); 
        course.setInstructorUsername("Test instructorUsername");

        course = courseRepository.save(course);

        student = new User();
        student.setUsername("student1");
        student.setPassword("studentpassword");
        student.setRole("student");
        student.setEmail("TeststudentEmail@gmail.com");
        student = userRepository.save(student);
    }

    @Test
    @Transactional
    public void testCreateQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitle("Java Quiz");
        quiz.setCourse(course);
        Quiz createdQuiz = quizService.createQuiz(course.getId(), quiz);

        assertNotNull(createdQuiz.getId(), "Quiz should have an ID after saving");
        assertEquals(course.getId(), createdQuiz.getCourse().getId(), "Quiz should be linked to the correct course");
    }

    @Test
    @Transactional
    public void testSubmitQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitle("Java Quiz");
        quiz.setCourse(course);
        quiz = quizRepository.save(quiz);

        Submission submission = quizService.submitQuiz(quiz.getId(), student.getUsername());

        assertNotNull(submission.getId(), "Submission should be saved");
        assertEquals(student.getUsername(), submission.getStudentUsername(), "Submission should be for the correct student");
    }

    @Test
    @Transactional
    public void testAssignScore() {
        Quiz quiz = new Quiz();
        quiz.setTitle("Java Quiz");
        quiz.setCourse(course);
        quiz = quizRepository.save(quiz);

        Submission submission = quizService.submitQuiz(quiz.getId(), student.getUsername());
        submission = quizService.assignScore(quiz.getId(), student.getId(), 90.0, "Good job!");

        assertEquals(90.0, submission.getScore(), "Score should be assigned correctly");
        assertEquals("Good job!", submission.getFeedback(), "Feedback should be assigned correctly");
    }

    @Test
    @Transactional
    public void testGetStudentScoreAndFeedback() {
        Quiz quiz = new Quiz();
        quiz.setTitle("Java Quiz");
        quiz.setCourse(course);
        quiz = quizRepository.save(quiz);

        @SuppressWarnings("unused")
        Submission submission = quizService.submitQuiz(quiz.getId(), student.getUsername());
        quizService.assignScore(quiz.getId(), student.getId(), 80.0, "Well done!");

        Submission retrievedSubmission = quizService.getStudentScoreAndFeedback(quiz.getId(), student.getUsername());

        assertNotNull(retrievedSubmission, "Submission should be retrieved");
        assertEquals(80.0, retrievedSubmission.getScore(), "Score should be correct");
        assertEquals("Well done!", retrievedSubmission.getFeedback(), "Feedback should be correct");
    }

    @Test
    @Transactional
    public void testGetQuizzesByCourse() {
        Quiz quiz1 = new Quiz();
        quiz1.setTitle("Java Basics Quiz");
        quiz1.setCourse(course);
        quizRepository.save(quiz1);

        Quiz quiz2 = new Quiz();
        quiz2.setTitle("Advanced Java Quiz");
        quiz2.setCourse(course);
        quizRepository.save(quiz2);

        List<Quiz> quizzes = quizService.getQuizzesByCourse(course.getId());

        assertEquals(2, quizzes.size(), "There should be two quizzes for this course");
    }
}