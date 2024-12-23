package com.project.lms.ServiceTest;

import com.project.lms.Entity.*;
import com.project.lms.Repository.NotificationRepository;
import com.project.lms.Repository.UserRepository;
import com.project.lms.Service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();
        userRepository.deleteAll();

        // Create a user
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setEmail("testUser@gmail.com");
        user.setRole("student");
        userRepository.save(user);

        Notification notification1 = new Notification();
        notification1.setFromEmail("admin@gmail.com");
        notification1.setToEmail("testUser@gmail.com");
        notification1.setMessage("Welcome to the platform!");
        notification1.setSentAt(LocalDateTime.now());
        notification1.setRead(false);
        notificationRepository.save(notification1);

        Notification notification2 = new Notification();
        notification2.setFromEmail("admin@gmail.com");
        notification2.setToEmail("testUser@gmail.com");
        notification2.setMessage("Your course starts tomorrow.");
        notification2.setSentAt(LocalDateTime.now());
        notification2.setRead(false);
        notificationRepository.save(notification2);
    }

    @Test
    void testGetUnreadNotifications() {
        List<Notification> unreadNotifications = notificationService.getUnreadNotifications("testUser", "password123");

        assertEquals(2, unreadNotifications.size());
        assertTrue(unreadNotifications.stream().allMatch(Notification::isRead));
    }

    @Test
    void testGetUnreadNotificationsInvalidUser() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.getUnreadNotifications("invalidUser", "wrongPassword");
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testGetAllNotifications() {
        List<Notification> allNotifications = notificationService.getAllNotifications("testUser", "password123");

        assertEquals(2, allNotifications.size());
        assertTrue(allNotifications.stream().anyMatch(n -> n.getMessage().equals("Welcome to the platform!")));
        assertTrue(allNotifications.stream().anyMatch(n -> n.getMessage().equals("Your course starts tomorrow.")));
    }

    @Test
    void testMarkAsRead() {
        Notification notification = notificationRepository.findAll().get(0);
        notificationService.markAsRead(notification.getId());
        Notification updatedNotification = notificationRepository.findById(notification.getId())
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        assertTrue(updatedNotification.isRead());
    }

    @Test
    void testMarkAsReadInvalidId() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.markAsRead(999L);
        });

        assertEquals("Notification not found", exception.getMessage());
    }
}