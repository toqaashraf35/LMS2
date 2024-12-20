package com.project.lms.Repository;

import com.project.lms.Entity.Enrollment;
import java.util.*;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Query("SELECT e.studentUsername FROM Enrollment e WHERE e.courseName = :courseName")
    List<String> findStudentNamesByCourseName(@Param("courseName") String courseName);

    @Query("SELECT e FROM Enrollment e WHERE e.studentUsername = :studentUsername AND e.courseName = :courseName") 
    Optional<Enrollment> findByStudentUsernameAndCourseName(@Param("studentUsername") String studentUsername, @Param("courseName") String courseName);

}
