package org.stockmaster3000.stockmaster3000.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a category of products in the system.
 *
 * <p>This entity maps to the {@code categories} table in the database.
 * Each category can be associated with multiple products.</p>
 */
@Entity
@Table(name = "categories")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
  private Long id;

  private String name;

  // Relationships
  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Product> products = new ArrayList<>();

  /**
   * Default no-argument constructor.
   * Required by JPA for entity instantiation.
   */
  public Category() {
  }

  /**
   * Constructs a new {@code Category} with the specified name.
   *
   * @param name the name of the category
   */
  public Category(String name) {
    this.name = name;
  }

  // Getters and Setters

  /**
   * Returns the unique identifier of the category.
   *
   * @return the ID of the category
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the ID of the category.
   *
   * @param id the new ID to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Returns the name of the category.
   *
   * @return the category name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the category.
   *
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns a copy of the list of products associated with this category.
   * This prevents exposing the internal list to external modification.
   *
   * @return a defensive copy of the product list
   */
  public List<Product> getProducts() {
    return new ArrayList<>(products);
  }

  /**
   * Replaces the internal list of products with a copy of the provided list.
   *
   * @param products the new list of products to set
   */
  public void setProducts(List<Product> products) {
    this.products = new ArrayList<>(products);
  }

  // hashCode and equals

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Category category = (Category) o;
    return Objects.equals(id, category.id)
            && Objects.equals(name, category.name);
  }
}
