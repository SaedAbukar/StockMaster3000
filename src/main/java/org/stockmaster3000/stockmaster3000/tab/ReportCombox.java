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

/**
 * A component for selecting reports based on inventory selection.
 */
public class ReportCombox extends VerticalLayout {

  private final ComboBox<Report> reportComboBox;
  private final List<SelectionListener> listeners = new ArrayList<>();
  private final ReportService reportService;
  private final InventoryCombox inventorySelectorComponent;
  private Report selectedReport;

  /**
   * Creates a report selection component that updates dynamically based on inventory.
   *
   * @param securityService the user session handler
   * @param reportService service to fetch reports
   * @param inventorySelectorComponent inventory selection component dependency
   */
  public ReportCombox(SecurityService securityService, ReportService reportService,
                      InventoryCombox inventorySelectorComponent) {
    this.reportService = reportService;
    this.inventorySelectorComponent = inventorySelectorComponent;

    reportComboBox = new ComboBox<>(getTranslation("reportsSelector.selectReport"));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    reportComboBox.setItemLabelGenerator(report ->
        report.getCreatedAt().format(formatter));

    refreshReports();

    inventorySelectorComponent.setSelectionListener(
        selectedInventory -> refreshReports());

    reportComboBox.addValueChangeListener(event -> {
      selectedReport = event.getValue();
      notifySelectionListeners(selectedReport);
      getUI().ifPresent(UI::push);
    });

    HorizontalLayout reportLayout = new HorizontalLayout(reportComboBox);
    reportLayout.setSpacing(true);
    add(reportLayout);
  }

  /**
   * Refreshes the list of reports based on the currently selected inventory.
   */
  private void refreshReports() {
    Inventory selectedInventory = inventorySelectorComponent.getSelectedInventory();
    if (selectedInventory != null) {
      List<Report> reports = reportService
          .getReportsByInventoryId(selectedInventory.getId());
      reportComboBox.setItems(reports);
    } else {
      reportComboBox.clear();
      reportComboBox.setItems(new ArrayList<>());
    }
  }

  /**
   * Notifies registered listeners when a new report is selected.
   *
   * @param selectedReport the report that was selected
   */
  private void notifySelectionListeners(Report selectedReport) {
    for (SelectionListener listener : listeners) {
      listener.onReportSelected(selectedReport);
    }
  }

  /**
   * Registers a listener for report selection changes.
   *
   * @param listener the listener to be added
   */
  public void setSelectionListener(SelectionListener listener) {
    listeners.add(listener);
  }

  /**
   * Gets the currently selected report.
   *
   * @return the selected report
   */
  public Report getSelectedReport() {
    return selectedReport;
  }

  /**
   * Interface for receiving selection change notifications.
   */
  public interface SelectionListener {
    /**
     * Called when a new report is selected.
     *
     * @param selectedReport the report that was selected
     */
    void onReportSelected(Report selectedReport);
  }

  /**
   * Updates all translated labels.
   */
  public void updateTexts() {
    reportComboBox.setLabel(getTranslation("reportsSelector.selectReport"));
  }
}
