package org.stockmaster3000.stockmaster3000.tab;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.Report;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import org.stockmaster3000.stockmaster3000.service.ReportService;


public class ReportCombox extends VerticalLayout {

  private final ComboBox<Report> reportComboBox;
  private final List<SelectionListener> listeners = new ArrayList<>();
  private final ReportService reportService;
  private final InventoryCombox inventorySelectorComponent;
  private Report selectedReport;

  // Constructor
  public ReportCombox(SecurityService securityService, ReportService reportService,
                      InventoryCombox inventorySelectorComponent) {
    this.reportService = reportService;
    this.inventorySelectorComponent = inventorySelectorComponent;

    // Initialize report ComboBox
    reportComboBox = new ComboBox<>(getTranslation("reportsSelector.selectReport"));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    reportComboBox.setItemLabelGenerator(report -> report.getCreatedAt().format(formatter));

    // Load reports based on selected inventory
    refreshReports();

    // Update reports when inventory selection changes
    inventorySelectorComponent.setSelectionListener(selectedInventory -> refreshReports());

    // Set up value change listener
    reportComboBox.addValueChangeListener(event -> {
      selectedReport = event.getValue();
      notifySelectionListeners(selectedReport);
      getUI().ifPresent(UI::push);
    });

    // Layout setup
    HorizontalLayout reportLayout = new HorizontalLayout(reportComboBox);
    reportLayout.setSpacing(true);
    add(reportLayout);
  }

  // Fetch reports based on selected inventory
  private void refreshReports() {
    Inventory selectedInventory = inventorySelectorComponent.getSelectedInventory();
    if (selectedInventory != null) {
      List<Report> reports = reportService.getReportsByInventoryId(selectedInventory.getId());
      reportComboBox.setItems(reports);
    } else {
      reportComboBox.clear();
      reportComboBox.setItems(new ArrayList<>()); // Clear the list if no inventory is selected
    }
  }

  // Notify listeners when a report is selected
  private void notifySelectionListeners(Report selectedReport) {
    for (SelectionListener listener : listeners) {
      listener.onReportSelected(selectedReport);
    }
  }

  // Allow external classes to listen for report selection
  public void setSelectionListener(SelectionListener listener) {
    listeners.add(listener);
  }

  public Report getSelectedReport() {
    return selectedReport;
  }

  public interface SelectionListener {
    /**
     * Called when a new report is selected.
     *
     * @param selectedReport the report that was selected
     */
    void onReportSelected(Report selectedReport);
  }

  public void updateTexts() {
    reportComboBox.setLabel(getTranslation("reportsSelector.selectReport"));
  }
}
