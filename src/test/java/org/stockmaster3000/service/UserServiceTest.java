package org.stockmaster3000.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.stockmaster3000.stockmaster3000.model.User;
import org.stockmaster3000.stockmaster3000.repository.UserRepository;
import org.stockmaster3000.stockmaster3000.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    private String rawPassword = "password123";
    private String username = "testuser";
    private String existingUsername = "existinguser";
    private String shortPassword = "short";
    private String emptyPassword = "";

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create test user with raw password
        testUser = new User();
        testUser.setUsername(username);
        testUser.setPassword(rawPassword);
    }

    @Test
    public void testRegisterUser_success() {
        // Mock the repository to return an empty Optional when checking for an existing user
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Mock the password encoder to return a hashed version of the password
        when(passwordEncoder.encode(rawPassword)).thenReturn("hashedpassword");

        // Call the registerUser method
        String result = userService.registerUser(username, rawPassword);

        // Verify that the result is the expected success message
        assertEquals("Registration successful", result);

        // Verify that the repository's save method was called once
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_existingUsername() {
        // Mock the repository to return an existing user when checking for the username
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(testUser));

        // Call the registerUser method with an existing username
        String result = userService.registerUser(existingUsername, rawPassword);

        // Verify that the result is the expected error message
        assertEquals("Username already exists", result);

        // Verify that the repository's save method was not called
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_emptyUsername() {
        // Call the registerUser method with an empty username
        String result = userService.registerUser("", rawPassword);

        // Verify that the result is the expected error message
        assertEquals("Username cannot be empty", result);

        // Verify that the repository's save method was not called
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_shortPassword() {
        // Call the registerUser method with a short password
        String result = userService.registerUser(username, shortPassword);

        // Verify that the result is the expected error message
        assertEquals("Password must be at least 8 characters long", result);

        // Verify that the repository's save method was not called
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_nullPassword() {
        // Call the registerUser method with an empty password
        String result = userService.registerUser(username, emptyPassword);

        // Verify that the result is the expected error message
        assertEquals("Password must be at least 8 characters long", result);

        // Verify that the repository's save method was not called
        verify(userRepository, times(0)).save(any(User.class));
    }
}
