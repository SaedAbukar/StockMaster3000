package org.stockmaster3000.stockmaster3000.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.model.User;
import org.stockmaster3000.stockmaster3000.repository.UserRepository;

/**
 * Custom implementation of {@link UserDetailsService} for loading user authentication data.
 *
 * <p>This service is used by Spring Security to fetch user details based on the provided username.
 * </p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   * Constructs a new {@code CustomUserDetailsService} with the given repository.
   *
   * @param userRepository the repository used to retrieve user data
   */
  @Autowired
  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Loads user details by username for Spring Security authentication.
   *
   * @param username the username to search for
   * @return the user details for authentication
   * @throws UsernameNotFoundException if the user is not found
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .build();
  }
}
