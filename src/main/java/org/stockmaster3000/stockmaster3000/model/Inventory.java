package org.stockmaster3000.stockmaster3000.model;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.hibernate.envers.Audited;

/**
 * Represents an inventory belonging to a specific user.
 *
 * <p>Inventories hold multiple products and reports and are tracked with auditing.
 */
@Entity
@Audited
@Table(name = "inventories")
public class Inventory {

  /**
   * Unique identifier for the inventory.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Name of the inventory.
   */
  private String name;

  /**
   * Products associated with this inventory.
   */
  @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Product> products = new ArrayList<>();

  /**
   * Reports associated with this inventory.
   */
  @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
  @Audited(targetAuditMode = NOT_AUDITED)
  private List<Report> reports = new ArrayList<>();

  /**
   * The user who owns this inventory.
   */
  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @Audited(targetAuditMode = NOT_AUDITED)
  private User user;

  public Inventory() {}

  /**
   * Constructs a new inventory with a name and user.
   *
   * @param name the inventory name
   * @param user the user who owns the inventory
   */
  public Inventory(String name, User user) {
    this.name = name;
    this.user = user != null ? new User(user) : null;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Product> getProducts() {
    return Collections.unmodifiableList(products);
  }

  public void setProducts(List<Product> products) {
    this.products = new ArrayList<>(products);
  }

  public List<Report> getReports() {
    return Collections.unmodifiableList(reports);
  }

  public void setReports(List<Report> reports) {
    this.reports = new ArrayList<>(reports);
  }

  public User getUser() {
    return user != null ? new User(user) : null;
  }

  public void setUser(User user) {
    this.user = user != null ? new User(user) : null;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, user);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Inventory)) return false;
    Inventory that = (Inventory) o;
    return Objects.equals(id, that.id)
            && Objects.equals(name, that.name)
            && Objects.equals(user, that.user);
  }
}
