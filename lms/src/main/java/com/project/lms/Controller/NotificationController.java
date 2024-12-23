package com.project.lms.Controller;

import com.project.lms.Entity.*;
import com.project.lms.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Get unread Notifications.
    @GetMapping("/unread")
    public List<Notification> getUnreadNotifications(
            @RequestParam String username, 
            @RequestParam String password) {
        return notificationService.getUnreadNotifications(username, password);
    }

    // Get all Notifications.
    @GetMapping("/all")
    public List<Notification> getAllNotifications(
            @RequestParam String username, 
            @RequestParam String password) {
        return notificationService.getAllNotifications(username, password);
    }

    // Mark a Notification as read
    @PostMapping("/markAsRead/{notificationId}")
    public void markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
    }
}
