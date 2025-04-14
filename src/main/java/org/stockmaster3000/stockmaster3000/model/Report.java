package org.stockmaster3000.stockmaster3000.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 * Represents an AI-generated report related to a specific inventory.
 *
 * <p>Each report includes a summary generated from the current state of an inventory, and
 * is timestamped upon creation.</p>
 */
@Entity
@Table(name = "reports")
public class Report {

  /**
   * Unique identifier for the report.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Summary content generated for the report.
   */
  @Column(columnDefinition = "TEXT")
  private String summary;

  /**
   * The inventory associated with this report.
   */
  @ManyToOne
  @JoinColumn(name = "inventory_id", nullable = false)
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  private Inventory inventory;

  /**
   * Timestamp indicating when the report was created.
   */
  @CreationTimestamp
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;

  /**
   * Default constructor.
   */
  public Report() {
  }

  /**
   * Constructs a report with a summary and linked inventory.
   *
   * @param summary   the content of the report
   * @param inventory the associated inventory
   */
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
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Report report = (Report) o;
    return Objects.equals(id, report.id)
        && Objects.equals(summary, report.summary)
        && Objects.equals(inventory, report.inventory)
        && Objects.equals(createdAt, report.createdAt);
  }
}
