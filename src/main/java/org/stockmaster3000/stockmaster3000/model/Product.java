package org.stockmaster3000.stockmaster3000.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;

import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    private Long id;

    private String name;

    private Double price;

    private Integer quantity;

    @Column(name = "json_summary", columnDefinition = "json")
    @Type(JsonType.class)
    private Map<String, Double> nutritions;

    private Integer amountOfDaysUntilExpiration;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    // Constructor
    public Product() {}

    public Product(String name, Double price, Integer quantity, Map<String, Double> nutritions, Integer amountOfDaysUntilExpiration, Supplier supplier, Category category, Inventory inventory) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.nutritions = nutritions;
        this.amountOfDaysUntilExpiration = amountOfDaysUntilExpiration;
        this.supplier = supplier;
        this.category = category;
        this.inventory = inventory;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Map<String, Double> getNutritions() {
        return nutritions;
    }

    public void setNutritions(Map<String, Double> nutritions) {
        this.nutritions = nutritions;
    }

    public Integer getAmountOfDaysUntilExpiration() {
        return amountOfDaysUntilExpiration;
    }

    public void setAmountOfDaysUntilExpiration(Integer amountOfDaysUntilExpiration) {
        this.amountOfDaysUntilExpiration = amountOfDaysUntilExpiration;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    // hashCode and equals
    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, quantity, nutritions, amountOfDaysUntilExpiration, supplier, category, inventory);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(price, product.price) &&
                Objects.equals(quantity, product.quantity) &&
                Objects.equals(nutritions, product.nutritions) &&
                Objects.equals(amountOfDaysUntilExpiration, product.amountOfDaysUntilExpiration) &&
                Objects.equals(supplier, product.supplier) &&
                Objects.equals(category, product.category) &&
                Objects.equals(inventory, product.inventory);
    }
}
