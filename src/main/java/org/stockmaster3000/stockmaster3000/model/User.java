package org.stockmaster3000.stockmaster3000.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;

/**
 * Represents a user of the StockMaster3000 system.
 *
 * <p>Each user has a unique username and password and can own multiple inventories.</p>
 */
@Entity
@Table(name = "_users")
@Data
public class User {

  /** The unique ID of the user. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The username of the user. Must be unique and not null. */
  @Column(nullable = false, unique = true)
  private String username;

  /** The password of the user. Must not be null. */
  @Column(nullable = false)
  private String password;

  /** The list of inventories owned by the user. */
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
   * Returns the user's ID.
   *
   * @return the user's ID
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the user's ID.
   *
   * @param id the ID to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Returns the username of the user.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username of the user.
   *
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Returns the password of the user.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password of the user.
   *
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Returns the list of inventories owned by the user.
   *
   * @return the inventories associated with the user
   */
  public List<Inventory> getInventories() {
    return inventories;
  }

  /**
   * Sets the list of inventories for the user.
   *
   * @param inventories the list of inventories to associate with the user
   */
  public void setInventories(List<Inventory> inventories) {
    this.inventories = inventories;
  }

  /**
   * Generates a hash code for this user based on ID and username.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, username);
  }

  /**
   * Compares this user to another object for equality.
   *
   * @param obj the object to compare to
   * @return true if equal, false otherwise
   */
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

  /**
   * Returns a string representation of the user.
   *
   * @return string describing the user
   */
  @Override
  public String toString() {
    return "User{"
        + "id=" + id
        + ", username='" + username + '\''
        + ", password='" + password + '\''
        + '}';
  }
}
