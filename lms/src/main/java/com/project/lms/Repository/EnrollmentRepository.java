package com.project.lms.Repository;

import com.project.lms.Entity.*;
import java.util.*;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Query("SELECT e.student FROM Enrollment e WHERE e.courseName = :courseName")
    List<User> findStudentNamesByCourseName(@Param("courseName") String courseName);

    Optional<Enrollment> findByStudentAndCourse(User student, Course course);

}
