package org.stockmaster3000.stockmaster3000.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.User;

/**
 * Repository interface for User entity.
 *
 * <p>Provides methods for accessing user data including finding users by username.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Finds a user by their username.
   *
   * @param username the username to search for
   * @return an Optional containing the user if found, or empty otherwise
   */
  Optional<User> findByUsername(String username);
}
