package org.stockmaster3000.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.stockmaster3000.stockmaster3000.model.User;
import org.stockmaster3000.stockmaster3000.repository.UserRepository;
import org.stockmaster3000.stockmaster3000.security.CustomUserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;  // Mock PasswordEncoder

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;

    private String rawPassword = "password123";
    private String wrongPassword = "wrongpassword123";

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Initialize the test user with the encoded password
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword(rawPassword); // Set the dynamically encoded password
    }

    @Test
    public void testLoadUserByUsername_success() {
        // Mock the repository to return the test user when a specific username is searched
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Mock the PasswordEncoder to return true when comparing the passwords
        when(passwordEncoder.matches(rawPassword, testUser.getPassword())).thenReturn(true);

        // Call the loadUserByUsername method
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        // Verify the results
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertTrue(passwordEncoder.matches(rawPassword, testUser.getPassword()));

        // Verify that the repository's findByUsername method was called
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    public void testLoadUserByUsername_userNotFound() {
        // Mock the repository to return empty for a non-existent username
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        // Call the loadUserByUsername method and expect an exception
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("nonexistentuser");
        });

        // Verify that the repository's findByUsername method was called
        verify(userRepository, times(1)).findByUsername("nonexistentuser");
    }
}
