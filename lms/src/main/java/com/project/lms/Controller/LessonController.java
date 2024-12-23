package com.project.lms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.project.lms.Entity.*;
import com.project.lms.Repository.*;
import com.project.lms.Service.*;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private  LessonRepository lessonRepository;
    @Autowired
    private  AttendanceRepository attendanceRepository;
    @Autowired
    private AuthenticationController authenticationController;

    //Create Lesson.
    @PostMapping("/create/{courseName}")
    public ResponseEntity<Lesson> registerCourse(@PathVariable String courseName,
            @RequestParam String username, 
            @RequestParam String password, 
            @RequestBody Lesson lesson) {
        if (!authenticationController.isInstructor(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Lesson newLesson = lessonService.createLesson(lesson, courseName);
        lessonRepository.save(lesson);
        return new ResponseEntity<>(newLesson, HttpStatus.CREATED);
    }
    
    //Delete Lesson.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Set Attendance to Student.
    @PostMapping("/set-attendance")
    public ResponseEntity<Attendance> setAttendance(@RequestParam String username,
        @RequestParam String password,
        @RequestBody Attendance attendance) {
         if (!authenticationController.isInstructor(username, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } 
        Attendance newAttendance= attendanceService.markAttendance(attendance, attendance.getStudentUsername(), attendance.getLessonName());
        attendanceRepository.save(attendance);
        return new ResponseEntity<>(newAttendance, HttpStatus.CREATED);
    }

    //Get Attendance
    @GetMapping("/get-attendance")
    public ResponseEntity<Attendance> getAttendance(
        @RequestParam String username, 
        @RequestParam String password) {
        if (!authenticationController.isStudent(username, password) ) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Attendance attendance = attendanceService.getAttendance(username);
        return ResponseEntity.ok(attendance);
    }

}

