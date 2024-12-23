package com.project.lms.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.lms.Repository.*;
import com.project.lms.Entity.*;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LessonRepository lessonRepository;

    //Set Attendance.
    public Attendance markAttendance(Attendance attendance, String studentUsername, String lessonName){
        @SuppressWarnings("unused")
        User student = userRepository.findByUsername(studentUsername)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        @SuppressWarnings("unused")
        Lesson lesson = lessonRepository.findByName(lessonName)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));
        attendance.setLessonName(lessonName);
        attendance.setStudentUsername(studentUsername);
        return attendanceRepository.save(attendance);
    }

    //Get Attendance.
    public Attendance getAttendance(String username){
        @SuppressWarnings("unused")
        User student = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        return attendanceRepository.findByStudentUsername(username)
            .orElseThrow(() -> new RuntimeException("Attendance not found"));
    }

}
