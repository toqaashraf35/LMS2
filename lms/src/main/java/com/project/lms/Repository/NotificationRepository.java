package com.project.lms.Repository;

import com.project.lms.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByToAndIsRead(String to, boolean isRead);
    List<Notification> findByTo(String to);
}
