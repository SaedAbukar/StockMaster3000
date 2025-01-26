package org.stockmaster3000.stockmaster3000.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;

import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;

import lombok.Data;

import java.util.Map;


@Entity
@Table(name = "reports")
@Data // Lombok
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    private Long id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private String summary;

    @Column(name = "json_summary", columnDefinition = "json")
    @Type(JsonType.class)
    private Map<String, Double> json_summary;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false) 
    private Inventory inventory;
}
