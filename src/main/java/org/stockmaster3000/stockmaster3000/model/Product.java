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

import lombok.Data;

import java.util.Map;


@Entity
@Table(name = "products")
@Data // Lombok
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

    @ManyToOne (cascade = CascadeType.PERSIST)
    @JoinColumn(name = "inventory_id", nullable = false) 
    private Inventory inventory;
}
