package org.stockmaster3000.stockmaster3000.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

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
    private Map<String, Double> nutritions;
    private Integer amountOfDaysUntilExpiration;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false) 
    private Category category;

    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false) 
    private Inventory inventory;
}
