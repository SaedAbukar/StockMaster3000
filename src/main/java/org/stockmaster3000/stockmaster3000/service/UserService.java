package org.stockmaster3000.stockmaster3000.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.model.User;
import org.stockmaster3000.stockmaster3000.repository.UserRepository;

/**
 * Service for handling user-related operations like registration and lookups.
 *
 * <p>Includes validation, password encoding, and checks for existing users.</p>
 */
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Constructs a new {@code UserService} with the required dependencies.
   *
   * @param userRepository the repository for user data
   * @param passwordEncoder the encoder used to hash user passwords
   */
  @Autowired
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Registers a new user if the username is available and password is valid.
   *
   * @param username the username of the new user
   * @param password the raw password to be encoded
   * @return a message indicating success or the reason for failure
   */
  public String registerUser(String username, String password) {
    if (userRepository.findByUsername(username).isPresent()) {
      return "Username already exists";
    }

    if (username.isEmpty()) {
      return "Username cannot be empty";
    }

    if (password == null || password.length() < 8) {
      return "Password must be at least 8 characters long";
    }

    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);

    return "Registration successful";
  }

  /**
   * Finds a user by their username.
   *
   * @param username the username to look up
   * @return an {@code Optional} containing the user if found
   */
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }
}
