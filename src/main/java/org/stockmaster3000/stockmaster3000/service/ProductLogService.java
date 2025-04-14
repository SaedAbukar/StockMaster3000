package org.stockmaster3000.stockmaster3000.service;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stockmaster3000.stockmaster3000.model.Product;

/**
 * Service for retrieving historical product data using Hibernate Envers.
 *
 * <p>Allows querying for product changes over time based on inventory and optional time ranges.</p>
 */
@Service
public class ProductLogService {

  private final EntityManager entityManager;

  /**
   * Creates a new instance of {@code ProductLogService}.
   *
   * @param entityManager the JPA entity manager used for accessing audit data
   */
  public ProductLogService(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  /**
   * Retrieves product change logs for a specific inventory from the past 30 days.
   *
   * @param inventoryId the ID of the inventory
   * @return a list of maps containing product name, quantity, and price for each audit entry
   */
  @Transactional
  public List<Map<String, Object>> getProductDetailsByInventory(Long inventoryId) {
    AuditReader auditReader = AuditReaderFactory.get(entityManager);

    long thirtyDaysAgoTimestamp = LocalDateTime.now().minusDays(30)
        .toInstant(ZoneOffset.UTC).toEpochMilli();

    List<Object[]> results = auditReader.createQuery()
        .forRevisionsOfEntity(Product.class, false, true)
        .add(AuditEntity.property("inventory_id").eq(inventoryId))
        .add(AuditEntity.revisionProperty("timestamp").ge(thirtyDaysAgoTimestamp))
        .addProjection(AuditEntity.property("name"))
        .addProjection(AuditEntity.property("quantity"))
        .addProjection(AuditEntity.property("price"))
        .getResultList();

    return results.stream()
        .map(result -> {
          Map<String, Object> productDetails = new HashMap<>();
          productDetails.put("name", result[0]);
          productDetails.put("quantity", result[1]);
          productDetails.put("price", result[2]);
          return productDetails;
        })
        .collect(Collectors.toList());
  }

  /**
   * Retrieves product change logs for a specific inventory within a custom time range.
   *
   * @param inventoryId the ID of the inventory
   * @param startTime the start time of the range
   * @param endTime the end time of the range
   * @return a list of maps containing product name, quantity, and price for each audit entry
   */
  @Transactional
  public List<Map<String, Object>> getProductDetailsByInventoryAndTimeSpan(Long inventoryId,
                                                                           LocalDateTime startTime,
                                                                           LocalDateTime endTime) {
    AuditReader auditReader = AuditReaderFactory.get(entityManager);

    long startTimestamp = startTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    long endTimestamp = endTime.toInstant(ZoneOffset.UTC).toEpochMilli();

    List<Object[]> results = auditReader.createQuery()
        .forRevisionsOfEntity(Product.class, false, true)
        .add(AuditEntity.property("inventory_id").eq(inventoryId))
        .add(AuditEntity.revisionProperty("timestamp").ge(startTimestamp))
        .add(AuditEntity.revisionProperty("timestamp").le(endTimestamp))
        .addProjection(AuditEntity.property("name"))
        .addProjection(AuditEntity.property("quantity"))
        .addProjection(AuditEntity.property("price"))
        .getResultList();

    return results.stream()
        .map(result -> {
          Map<String, Object> productDetails = new HashMap<>();
          productDetails.put("name", result[0]);
          productDetails.put("quantity", result[1]);
          productDetails.put("price", result[2]);
          return productDetails;
        })
        .collect(Collectors.toList());
  }
}
