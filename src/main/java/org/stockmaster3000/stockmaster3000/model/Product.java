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
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Audited
@AuditTable(value = "product_AUD")
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    private Long id;

    private String name;

    private Double price;

    private Integer quantity;

    @Column(name = "nutritions", columnDefinition = "LONGTEXT", nullable = true)
    @Type(JsonType.class)
    private String nutritions;

    private Integer amountOfDaysUntilExpiration;

    @Column(name = "language_code")
    private String languageCode;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    @Audited(targetAuditMode = NOT_AUDITED)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @Audited(targetAuditMode = NOT_AUDITED)
    private Category category;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @Audited
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Column(name = "created_at")
    private LocalDateTime createdAt; 
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; 
    

    // Constructor
    public Product() {}

    public Product(String name, Double price, Integer quantity, String nutritions, Integer amountOfDaysUntilExpiration, String languageCode, Supplier supplier, Category category, Inventory inventory) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.nutritions = nutritions;
        this.amountOfDaysUntilExpiration = amountOfDaysUntilExpiration;
        this.languageCode = languageCode;
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

    public String getNutritions() {
        return nutritions;
    }

    public void setNutritions(String nutritions) {
        this.nutritions = nutritions;
    }

    public String getLanguageCode () {
        return languageCode;
    }

    public void setLanguageCode (String languageCode) {
        this.languageCode = languageCode;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // hashCode and equals
    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, quantity, nutritions, amountOfDaysUntilExpiration, languageCode, supplier, category, inventory);
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
                Objects.equals(languageCode, product.languageCode) &&
                Objects.equals(supplier, product.supplier) &&
                Objects.equals(category, product.category) &&
                Objects.equals(inventory, product.inventory);
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", nutritions=" + nutritions +
                ", amountOfDaysUntilExpiration=" + amountOfDaysUntilExpiration +
                ", language_code=" + languageCode +
                ", supplier=" + (supplier != null ? supplier.getName() : "null") +
                ", category=" + (category != null ? category.getName() : "null") +
                ", inventory=" + (inventory != null ? inventory.getName() : "null");
    }

}
