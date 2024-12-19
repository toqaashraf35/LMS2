package com.project.lms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.lms.Entity.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   User findByUsername(String username);

}
