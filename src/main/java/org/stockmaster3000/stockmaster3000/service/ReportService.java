package org.stockmaster3000.stockmaster3000.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.Report;
import org.stockmaster3000.stockmaster3000.repository.ReportRepository;

/**
 * Service for managing {@link Report} entities.
 *
 * <p>Handles saving and retrieving reports linked to inventories.</p>
 */
@Service
public class ReportService {

  private final ReportRepository reportRepository;

  /**
   * Creates a new {@code ReportService} instance.
   *
   * @param reportRepository the report repository to use
   */
  public ReportService(ReportRepository reportRepository) {
    this.reportRepository = reportRepository;
  }

  /**
   * Creates and saves a new report with the given summary and associated inventory.
   *
   * @param summary the text content of the report
   * @param inventory the inventory the report is related to
   * @return the saved {@code Report} object
   */
  public Report saveReport(String summary, Inventory inventory) {
    Report report = new Report(summary, inventory);
    return reportRepository.save(report);
  }

  /**
   * Retrieves all reports linked to a specific inventory.
   *
   * @param inventoryId the ID of the inventory
   * @return list of reports for the given inventory
   */
  public List<Report> getReportsByInventoryId(Long inventoryId) {
    return reportRepository.getReportsByInventoryId(inventoryId);
  }
}
