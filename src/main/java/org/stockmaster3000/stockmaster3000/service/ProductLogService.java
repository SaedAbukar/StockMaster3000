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

@Service
public class ProductLogService {

  private final EntityManager entityManager;

  // ProductLogService constructor
  public ProductLogService(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  // Fetching Product Details by Inventory Id
  @Transactional
  public List<Map<String, Object>> getProductDetailsByInventory(Long inventoryId) {
    AuditReader auditReader = AuditReaderFactory.get(entityManager);

    // Calculate the timestamp for 30 days ago
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

    // Convert the results into Map objects with key-value pairs
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

  // Fetching the Product Details by Inventory and TimeSpan
  @Transactional
  public List<Map<String, Object>> getProductDetailsByInventoryAndTimeSpan(Long inventoryId,
                                                                           LocalDateTime startTime, LocalDateTime endTime) {
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

    // Convert the results into Map objects with key-value pairs
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
