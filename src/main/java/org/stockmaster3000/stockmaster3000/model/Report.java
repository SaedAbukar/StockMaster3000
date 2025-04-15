package org.stockmaster3000.stockmaster3000.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 * Represents an AI-generated report related to a specific inventory.
 */
@Entity
@Table(name = "reports")
public class Report {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT")
  private String summary;

  @ManyToOne
  @JoinColumn(name = "inventory_id", nullable = false)
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  private Inventory inventory;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;

  public Report() {}

  public Report(String summary, Inventory inventory) {
    this.summary = summary;
    this.inventory = inventory;
  }

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

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, summary, inventory, createdAt);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Report)) return false;
    Report report = (Report) o;
    return Objects.equals(id, report.id)
            && Objects.equals(summary, report.summary)
            && Objects.equals(inventory, report.inventory)
            && Objects.equals(createdAt, report.createdAt);
  }
}
