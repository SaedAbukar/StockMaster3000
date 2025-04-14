package org.stockmaster3000.stockmaster3000.tab;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.stockmaster3000.stockmaster3000.components.BarChart;
import org.stockmaster3000.stockmaster3000.components.DoughnutChart;
import org.stockmaster3000.stockmaster3000.components.LineChart;
import org.stockmaster3000.stockmaster3000.model.Category;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.Product;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import org.stockmaster3000.stockmaster3000.service.ProductService;

/**
 * UI Tab that visualizes inventory insights using charts.
 *
 * <p>Displays category distribution, spending breakdown, and expiration timeline charts
 * for the selected inventory.
 */
public class InsightsTab extends VerticalLayout {

  private final ProductService productService;
  private DoughnutChart doughnutChart;
  private BarChart spendingChart;
  private LineChart expirationChart;
  private String noData = "insights.noData";

  H1 componentTopic;
  VerticalLayout doughnutContainer;
  VerticalLayout spendingContainer;
  VerticalLayout expirationContainer;

  /**
   * Constructs the InsightsTab layout and initializes chart containers.
   *
   * @param securityService service used to retrieve current user data
   * @param productService service to fetch product-related data
   */
  public InsightsTab(SecurityService securityService, ProductService productService) {
    this.productService = productService;

    componentTopic = new H1(getTranslation("insights.title"));
    componentTopic.getStyle().set("text-align", "center");
    add(componentTopic);

    HorizontalLayout topChartsLayout = new HorizontalLayout();
    topChartsLayout.setWidthFull();
    topChartsLayout.setSpacing(true);
    topChartsLayout.getStyle().set("gap", "16px");

    doughnutChart = new DoughnutChart(new String[]{getTranslation(noData)}, new int[]{1});
    spendingChart = new BarChart(new String[]{getTranslation(noData)}, new double[]{0.0});
    expirationChart = new LineChart(new String[]{getTranslation(noData)}, new int[]{0});

    doughnutContainer = createChartContainer(
        getTranslation("insights.subTitleDoughnut"), doughnutChart);
    spendingContainer = createChartContainer(
        getTranslation("insights.spending"), spendingChart);
    expirationContainer = createChartContainer(
        getTranslation("insights.expTimeline"), expirationChart);

    topChartsLayout.add(doughnutContainer, spendingContainer);
    add(topChartsLayout, expirationContainer);

    setSpacing(true);
    setPadding(true);
  }

  /**
   * Creates a styled chart container with a title and provided chart component.
   *
   * @param title the chart's title
   * @param chart the chart component
   * @return a styled layout containing the chart
   */
  private VerticalLayout createChartContainer(String title, Div chart) {
    VerticalLayout container = new VerticalLayout();
    H3 chartTitle = new H3(title);
    chartTitle.getStyle().set("text-align", "center");

    container.add(chartTitle, chart);
    container.getStyle().set("border", "1px solid #e0e0e0");
    container.getStyle().set("border-radius", "8px");
    container.getStyle().set("padding", "16px");
    container.getStyle().set("background-color", "#fafafa");
    container.getStyle().set("box-shadow", "0px 4px 8px rgba(0, 0, 0, 0.1)");
    container.setWidth("48%");
    return container;
  }

  /**
   * Updates all charts with data from the selected inventory.
   *
   * @param selectedInventory the inventory to fetch data from
   */
  public void updateCharts(Inventory selectedInventory) {
    String languageCode = UI.getCurrent().getLocale().getLanguage();
    if (selectedInventory != null) {
      Map<Category, Integer> productData = productService.getProductDataByInventory(
          selectedInventory.getId(), languageCode);
      updateCategoryChart(productData);

      List<Product> products = productService.getProductsByInventory(
          selectedInventory.getId(), languageCode);
      updateSpendingChart(products);
      updateExpirationChart(products);
    } else {
      clearCharts();
    }
  }

  /**
   * Resets charts to "No Data" placeholders.
   */
  private void clearCharts() {
    doughnutChart.updateChart(new String[]{getTranslation(noData)}, new int[]{1});
    spendingChart.updateChart(new String[]{getTranslation(noData)}, new double[]{0.0});
    expirationChart.updateChart(new String[]{getTranslation(noData)}, new int[]{0});
  }

  /**
   * Updates the doughnut chart for category distribution.
   *
   * @param productData map of categories to product counts
   */
  private void updateCategoryChart(Map<Category, Integer> productData) {
    if (productData.isEmpty()) {
      doughnutChart.updateChart(new String[]{getTranslation(noData)}, new int[]{1});
    } else {
      String[] labels = productData.keySet().stream()
          .map(Category::getName).toArray(String[]::new);
      int[] values = productData.values().stream().mapToInt(Integer::intValue).toArray();
      doughnutChart.updateChart(labels, values);
    }
  }

  /**
   * Updates the bar chart showing top product spending.
   *
   * @param products list of products in the inventory
   */
  private void updateSpendingChart(List<Product> products) {
    if (products == null || products.isEmpty()) {
      spendingChart.updateChart(new String[]{"No Data"}, new double[]{0.0});
      return;
    }

    Map<String, Double> productSpending = new HashMap<>();
    for (Product product : products) {
      if (product.getName() != null
          && product.getPrice() != null
          && product.getQuantity() != null) {
        productSpending.put(
            product.getName(), product.getQuantity() * product.getPrice());
      }
    }

    if (productSpending.isEmpty()) {
      spendingChart.updateChart(new String[]{getTranslation(noData)}, new double[]{0.0});
      return;
    }

    List<Map.Entry<String, Double>> topSpending = productSpending.entrySet().stream()
        .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
        .limit(10)
        .collect(Collectors.toList());

    String[] labels = topSpending.stream().map(Map.Entry::getKey).toArray(String[]::new);
    double[] values = topSpending.stream().mapToDouble(Map.Entry::getValue).toArray();
    spendingChart.updateChart(labels, values);
  }

  /**
   * Updates the line chart showing product expiration timelines.
   *
   * @param products list of products with expiration info
   */
  private void updateExpirationChart(List<Product> products) {
    if (products == null || products.isEmpty()) {
      expirationChart.updateChart(new String[]{getTranslation(noData)}, new int[]{0});
      return;
    }

    Map<String, Integer> expirationDays = new HashMap<>();
    for (Product product : products) {
      if (product.getName() != null && product.getAmountOfDaysUntilExpiration() != null) {
        expirationDays.put(product.getName(), product.getAmountOfDaysUntilExpiration());
      }
    }

    if (expirationDays.isEmpty()) {
      expirationChart.updateChart(new String[]{getTranslation(noData)}, new int[]{0});
      return;
    }

    List<Map.Entry<String, Integer>> sortedByExpiration = expirationDays.entrySet().stream()
        .sorted(Map.Entry.comparingByValue())
        .limit(10)
        .collect(Collectors.toList());

    String[] labels = sortedByExpiration.stream()
        .map(Map.Entry::getKey).toArray(String[]::new);
    int[] values = sortedByExpiration.stream()
        .mapToInt(Map.Entry::getValue).toArray();
    expirationChart.updateChart(labels, values);
  }

  /**
   * Updates all chart titles based on the current UI locale.
   */
  public void updateTexts() {
    componentTopic.setText(getTranslation("insights.title"));
    doughnutContainer.getElement().getChildren().findFirst()
        .ifPresent(title -> title.setText(getTranslation("insights.subTitleDoughnut")));
    spendingContainer.getElement().getChildren().findFirst()
        .ifPresent(title -> title.setText(getTranslation("insights.spending")));
    expirationContainer.getElement().getChildren().findFirst()
        .ifPresent(title -> title.setText(getTranslation("insights.expTimeline")));
  }
}
