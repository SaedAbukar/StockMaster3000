package org.stockmaster3000.stockmaster3000.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a user of the StockMaster3000 system.
 *
 * <p>Each user has a unique username and password and can own multiple inventories.</p>
 */
@Entity
@Table(name = "_users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Inventory> inventories = new ArrayList<>();

  /**
   * Default constructor required by JPA.
   */
  public User() {
  }

  /**
   * Constructs a new User with the given ID, username, and password.
   *
   * @param id the user ID
   * @param username the username
   * @param password the password
   */
  public User(Long id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
  }

  /**
   * Copy constructor for defensive copying.
   *
   * @param other the User to copy from
   */
  public User(User other) {
    this.id = other.id;
    this.username = other.username;
    this.password = other.password;
    // Note: inventories are intentionally not deep-copied
    // since that's usually a shared relation or handled by JPA
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<Inventory> getInventories() {
    return inventories;
  }

  public void setInventories(List<Inventory> inventories) {
    this.inventories = inventories;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    User user = (User) obj;
    return Objects.equals(id, user.id)
            && Objects.equals(username, user.username);
  }

  @Override
  public String toString() {
    return "User{"
            + "id=" + id
            + ", username='" + username + '\''
            + ", password='" + password + '\''
            + '}';
  }
}
