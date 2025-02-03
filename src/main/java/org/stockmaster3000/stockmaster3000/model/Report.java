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

import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "reports")
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

    // Constructor
    public Report() {}

    public Report(LocalDate startDate, LocalDate endDate, String summary, Map<String, Double> json_summary, Inventory inventory) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.summary = summary;
        this.json_summary = json_summary;
        this.inventory = inventory;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Map<String, Double> getJson_summary() {
        return json_summary;
    }

    public void setJson_summary(Map<String, Double> json_summary) {
        this.json_summary = json_summary;
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
        return Objects.hash(id, startDate, endDate, summary, json_summary, inventory);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id) &&
                Objects.equals(startDate, report.startDate) &&
                Objects.equals(endDate, report.endDate) &&
                Objects.equals(summary, report.summary) &&
                Objects.equals(json_summary, report.json_summary) &&
                Objects.equals(inventory, report.inventory);
    }
}
