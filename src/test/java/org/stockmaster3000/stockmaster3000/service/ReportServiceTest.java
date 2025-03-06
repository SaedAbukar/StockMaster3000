package org.stockmaster3000.stockmaster3000.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveReport_ShouldSaveAndReturnReport() {
        // Arrange
        Inventory inventory = new Inventory();
        String summary = "Test Report Summary";
        Report report = new Report(summary, inventory);

        when(reportRepository.save(any(Report.class))).thenReturn(report);

        // Act
        Report savedReport = reportService.saveReport(summary, inventory);

        // Making sure that the reports are not null and then testing
        assertNotNull(savedReport);
        assertEquals(summary, savedReport.getSummary());
        assertEquals(inventory, savedReport.getInventory());
        verify(reportRepository, times(1)).save(any(Report.class));
    }
}
