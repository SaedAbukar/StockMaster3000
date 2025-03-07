package org.stockmaster3000.stockmaster3000.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.Report;
import org.stockmaster3000.stockmaster3000.repository.ReportRepository;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report saveReport(String summary, Inventory inventory) {
        Report report = new Report(summary, inventory);
        return reportRepository.save(report);
    }

    public List<Report> getReportsByInventoryId(Long inventoryId) {
        return reportRepository.getReportsByInventoryId(inventoryId);
    }
}
