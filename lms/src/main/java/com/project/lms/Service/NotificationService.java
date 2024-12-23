package com.project.lms.Service;

import com.project.lms.Entity.Notification;
import com.project.lms.Repository.NotificationRepository;
import com.project.lms.Entity.*; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserService userService; 

    // Get unread notifications for a user by username and password
    public List<Notification> getUnreadNotifications(String username, String password) {
        User user = userService.authenticate(username, password)
            .orElseThrow(() -> new RuntimeException("User not found"));
        String userEmail = user.getEmail();
        List<Notification> notifications = notificationRepository.findByToAndIsRead(userEmail, false);
        notifications.forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
        return notifications;
        
    }

    // Get all notifications for a user
    public List<Notification> getAllNotifications(String username, String password) {
        User user = userService.authenticate(username, password)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
            String userEmail = user.getEmail();
            return notificationRepository.findByTo(userEmail);
    }

    // Mark a notification as read
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
