package com.project.lms.Controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.project.lms.Entity.*;
import com.project.lms.Service.*;
import com.project.lms.Repository.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private  UserService userService;
    @Autowired
    private  AuthenticationController authenticationController;
    @Autowired
    private  UserRepository userRepository;

    //Login for every member in system.
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        Optional<User> user = userService.authenticate(username, password);
        if (user.isPresent()) {
            return new ResponseEntity<>("Login successful!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials!", HttpStatus.UNAUTHORIZED);
        }
    }

    //Create user.
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestParam String username, 
                                             @RequestParam String password, 
                                             @RequestBody User user) {
        if (!authenticationController.isAdmin(username, password)) {
            return ResponseEntity.status(403).body(null);  // Forbidden if not an admin
        }
        
        User newUser = userService.createUser(user);
        userRepository.save(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    //Delete user.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Update user.
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    //Get an user.
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}

