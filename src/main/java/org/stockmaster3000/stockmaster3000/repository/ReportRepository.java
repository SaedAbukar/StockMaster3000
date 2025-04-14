package org.stockmaster3000.stockmaster3000.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Report;

/**
 * Repository interface for accessing Report data.
 *
 * <p>Provides methods to perform CRUD operations and custom queries related to reports.</p>
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

  /**
   * Retrieves all reports associated with a specific inventory by its ID.
   *
   * @param inventoryId the ID of the inventory
   * @return a list of reports related to the specified inventory
   */
  @Query("SELECT r FROM Report r WHERE r.inventory.id = :inventoryId")
  List<Report> getReportsByInventoryId(@Param("inventoryId") Long inventoryId);
}
