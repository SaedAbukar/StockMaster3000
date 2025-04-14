package org.stockmaster3000.stockmaster3000.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Report;


@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
  @Query("SELECT r FROM Report r WHERE r.inventory.id = :inventoryId")
  List<Report> getReportsByInventoryId(@Param("inventoryId") Long inventoryId);
}

