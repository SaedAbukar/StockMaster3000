package org.stockmaster3000.stockmaster3000.model;

import com.vaadin.flow.component.template.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "nutritions")
@Data
public class Nutritions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer calories;
    private Float protein;
    private Float carbohydrate;
    private Float fat;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "product_id")  // Foreign key to the Product entity
    private Product product;

}
