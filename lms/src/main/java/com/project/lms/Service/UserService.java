package com.project.lms.Service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.lms.Repository.UserRepository;
import com.project.lms.Entity.User;

@Service
public class UserService {

    @Autowired
    public  UserRepository userRepository;

    //Create User.
    public User createUser(User user) {
        return userRepository.save(user);
    }

    //Delete User.
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    //Update User.
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setRole(user.getRole());
        return userRepository.save(existingUser);
    }

    //Get User.
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    //Authenticate user.
    public Optional<User>  authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        if (user != null && user.getPassword().equals(password)) {
            return Optional.of(user); 
        }
        return Optional.empty();
    }

}


