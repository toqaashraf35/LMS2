package com.project.lms.Service;

import com.project.lms.Repository.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.lms.Entity.*;

@Service
public class EnrollmentService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private  EnrollmentRepository enrollmentRepository;

    // Enroll in Course.
    public Enrollment enrollInCourse(String studentUsername, String courseName) {
        User student = userRepository.findByUsername(studentUsername)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findByName(courseName)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        String studentEmail = student.getEmail();
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setCourseName(courseName);
        enrollment.setEnrolledAt(LocalDateTime.now());

        Notification notification = new Notification();
        notification.setFromEmail("SYSTEM");
        notification.setToEmail(studentEmail); 
        notification.setMessage("Enrollment has Done!"); 
        notification.setSentAt(LocalDateTime.now()); 
        notification.setRead(false); 

        notificationRepository.save(notification);
        return enrollmentRepository.save(enrollment);
        
    }

    //Get all Enrollments.
    public List<Enrollment> getAllEnrollment(){
            return enrollmentRepository.findAll();
    }

    //Get Students per Course.
    public List<User> getStudentNamesByCourseName(String courseName) {
        return enrollmentRepository.findStudentNamesByCourseName(courseName);
    }

    //Delete Student from Course.
    public void removeStudentFromCourse(String instructorUsername, String studentUsername, String courseName) {
        Course course = courseRepository.findByName(courseName)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        User student = userRepository.findByUsername(studentUsername)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!course.getInstructorUsername().equals(instructorUsername)) {
            throw new RuntimeException("You are not the instructor of this course.");
        }
        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student, course)
            .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollmentRepository.delete(enrollment);
    }
    
}
