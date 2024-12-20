package com.project.lms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.lms.Entity.Quiz;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    // Custom query to fetch quizzes by course ID
    List<Quiz> findByCourseId(Long courseId);
}
