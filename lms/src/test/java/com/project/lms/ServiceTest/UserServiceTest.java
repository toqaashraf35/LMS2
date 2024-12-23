package com.project.lms.ServiceTest;

import com.project.lms.Entity.User;
import com.project.lms.Service.UserService;
import com.project.lms.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        testUser.setEmail("TestUserEmail@gmail.com");
        testUser.setRole("instructor");

        userRepository.save(testUser);
    }

    @Transactional
    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("newPassword");
        user.setEmail("newTestuser@gmail.com");
        user.setRole("student");

        User savedUser = userService.createUser(user);

        assertNotNull(savedUser.getId(), "User ID should not be null");
        assertEquals("newUser", savedUser.getUsername(), "User username should match");
        assertEquals("newPassword", savedUser.getPassword(), "User password should match");
        assertEquals("newTestuser@gmail.com", savedUser.getEmail(), "User email should match");
        assertEquals("student", savedUser.getRole(), "User role should match");
    }

    @Transactional
    @Test
    public void testUpdateUser() {
        
        testUser.setEmail("updatedemail@gamil.com");
        testUser.setUsername("updatedUser");

        User updatedUser = userService.updateUser(testUser.getId(), testUser);

        assertNotNull(updatedUser, "Updated user should not be null");
        assertEquals("updatedUser", updatedUser.getUsername(), "User username should be updated");
        assertEquals("updatedemail@gamil.com", updatedUser.getEmail(), "User email should be updated");
    }

    @Transactional
    @Test
    public void testDeleteUser() {
        
        userService.deleteUser(testUser.getId());

        Optional<User> deletedUser = userService.getUserById(testUser.getId());

        assertFalse(deletedUser.isPresent(), "User should be deleted from the database");
    }

    @Transactional
    @Test
    public void testAuthenticateValidUser() {
        
        Optional<User> authenticatedUser = userService.authenticate("testUser", "password");

        assertTrue(authenticatedUser.isPresent(), "User should be authenticated successfully");
        assertEquals("testUser", authenticatedUser.get().getUsername(), "Authenticated username should match");
    }

    @Transactional
    @Test
    public void testAuthenticateInvalidUser() {
        
        Optional<User> authenticatedUser = userService.authenticate("testUser", "wrongPassword");

        assertFalse(authenticatedUser.isPresent(), "User should not be authenticated with incorrect password");
    }

    @Transactional
    @Test
    public void testAuthenticateNonExistentUser() {
        Optional<User> authenticatedUser = userService.authenticate("testUser", "wrongpassword");

        assertFalse(authenticatedUser.isPresent(), "Non-existent user should not be authenticated");
    }
    
    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

}
