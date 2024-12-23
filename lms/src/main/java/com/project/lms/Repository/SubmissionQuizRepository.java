package com.project.lms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.lms.Entity.Submission;
import java.util.List;
import java.util.Optional;

public interface SubmissionQuizRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByQuizId(Long quizId); 
    Optional<Submission> findByQuizIdAndStudentId(Long quizId, Long studentId);
}
