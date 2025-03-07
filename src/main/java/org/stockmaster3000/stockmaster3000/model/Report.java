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
import java.time.LocalDateTime;

import com.vladmihalcea.hibernate.type.json.JsonType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import java.util.Map;
import java.util.Objects;

@Entity
@Audited
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    // Constructor
    public Report() {}

    public Report(String summary, Inventory inventory) {
        this.summary = summary;
        this.inventory = inventory;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    // hashCode and equals
    @Override
    public int hashCode() {
        return Objects.hash(id, summary, inventory, createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id) &&
                Objects.equals(summary, report.summary) &&
                Objects.equals(inventory, report.inventory) &&
                Objects.equals(createdAt, report.createdAt);
    }
}
