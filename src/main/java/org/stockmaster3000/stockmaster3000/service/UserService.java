package org.stockmaster3000.stockmaster3000.service;

import org.stockmaster3000.stockmaster3000.model.User;
import org.stockmaster3000.stockmaster3000.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(String username, String password) {
        // Check if the username already exists
        if (userRepository.findByUsername(username).isPresent()) {
            return "Username already exists";  // Return error message
        }

        if (username.isEmpty()) {
            return "Username cannot be empty";  // Return error message
        }

        if (password == null || password.length() < 8) {
            return "Password must be at least 8 characters long";  // Return error message
        }

        // Proceed with user registration
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Password hashing
        userRepository.save(user);

        return "Registration successful";  // Success message
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
