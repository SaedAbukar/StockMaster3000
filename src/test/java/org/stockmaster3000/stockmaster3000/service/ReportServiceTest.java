package org.stockmaster3000.stockmaster3000.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.Report;
import org.stockmaster3000.stockmaster3000.repository.ReportRepository;

class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    // Injecting the mock Report Repository into Report Service
    @InjectMocks
    private ReportService reportService;

    private Inventory inventory;
    private Report report;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventory = new Inventory();
        inventory.setId(1L); // Assuming Inventory has an ID field with a setter
        report = new Report("Test Report Summary", inventory);
    }

    @Test
    void saveReport_ShouldSaveAndReturnReport() {
        // Arrange
        when(reportRepository.save(any(Report.class))).thenReturn(report);

        // Act
        Report savedReport = reportService.saveReport(report.getSummary(), inventory);

        // Assert
        assertNotNull(savedReport);
        assertEquals(report.getSummary(), savedReport.getSummary());
        assertEquals(inventory, savedReport.getInventory());

        // Verify that save was called once
        verify(reportRepository, times(1)).save(any(Report.class));
    }

    @Test
    void getReportsByInventoryId_ShouldReturnReportsList() {
        // Arrange
        List<Report> reports = Arrays.asList(report);
        when(reportRepository.getReportsByInventoryId(inventory.getId())).thenReturn(reports);

        // Act
        List<Report> result = reportService.getReportsByInventoryId(inventory.getId());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(report, result.get(0));

        // Verify that getReportsByInventoryId was called once
        verify(reportRepository, times(1)).getReportsByInventoryId(inventory.getId());
    }
}