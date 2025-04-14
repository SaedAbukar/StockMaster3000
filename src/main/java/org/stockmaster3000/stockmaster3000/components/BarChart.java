package org.stockmaster3000.stockmaster3000.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import elemental.json.Json;
import elemental.json.JsonArray;
import java.util.Arrays;

/**
 * A custom Vaadin component that renders a bar chart using Chart.js.
 *
 * <p>This component creates a canvas element and uses Chart.js (loaded via CDN)
 * to display a bar chart representing labeled data and their associated values.
 * The chart is initialized during component construction and can be updated dynamically.
 * </p>
 *
 * <h3>Usage example:</h3>
 * <pre>{@code
 * String[] labels = {"Food", "Transport", "Utilities", "Entertainment"};
 * double[] values = {120.0, 75.5, 60.0, 90.0};
 * BarChart chart = new BarChart(labels, values);
 * add(chart);
 * }</pre>
 *
 * <p>The chart uses custom styles and configuration options such as tooltips,
 * animation, color scheme, and axis titles which are localized using translation keys:
 * {@code insights.BarChartTextX} and {@code insights.BarChartTextY}.</p>
 *
 * @see <a href="https://www.chartjs.org/">Chart.js Documentation</a>
 */
@Tag("canvas")
@JsModule("https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.0/chart.umd.js")
public class BarChart extends Div {

  /**
   * Constructs a new {@code BarChart} component with the specified labels and values.
   *
   * <p>This constructor initializes the chart by creating a canvas element
   * and rendering the chart using Chart.js with the provided data.</p>
   *
   * @param labels the labels for the bar chart (e.g., categories)
   * @param values the corresponding values for each label
   */
  public BarChart(String[] labels, double[] values) {
    getElement().setAttribute("id", "barChart");

    // Increase size for better readability
    getElement().getStyle().set("width", "550px");
    getElement().getStyle().set("height", "350px");

    // Initialize the chart
    initializeChart(labels, values);
  }

  /**
   * Initializes the Chart.js bar chart with the given labels and values.
   *
   * @param labels the labels for the x-axis
   * @param values the values for the y-axis
   */
  private void initializeChart(String[] labels, double[] values) {
    JsonArray jsonLabels = Json.createArray();
    JsonArray jsonData = Json.createArray();

    for (int i = 0; i < labels.length; i++) {
      jsonLabels.set(i, labels[i]);
      jsonData.set(i, values[i]);
    }

    String textY = getTranslation("insights.BarChartTextY");
    String textX = getTranslation("insights.BarChartTextX");

    getElement().executeJs("setTimeout(() => {"
        + "const ctx = document.getElementById('barChart').getContext('2d');"
        + "ctx.canvas.width = 450;"
        + "ctx.canvas.height = 350;"
        + "window.barChart = new Chart(ctx, {"
        + "  type: 'bar',"
        + "  data: {"
        + "    labels: $0,"
        + "    datasets: [{"
        + "      label: 'Total Spending (€)',"
        + "      data: $1,"
        + "      backgroundColor: ["
        + "        'rgba(255, 99, 132, 0.7)',"
        + "        'rgba(54, 162, 235, 0.7)',"
        + "        'rgba(255, 206, 86, 0.7)',"
        + "        'rgba(75, 192, 192, 0.7)',"
        + "        'rgba(153, 102, 255, 0.7)',"
        + "        'rgba(255, 159, 64, 0.7)'"
        + "      ],"
        + "      borderColor: 'rgba(0, 0, 0, 0.2)',"
        + "      borderWidth: 2"
        + "    }]"
        + "  },"
        + "  options: {"
        + "    responsive: false,"
        + "    maintainAspectRatio: false,"
        + "    scales: {"
        + "      y: {"
        + "        beginAtZero: true,"
        + "        title: {"
        + "          display: true,"
        + "          text: $2,"
        + "          font: { size: 14 }"
        + "        },"
        + "        ticks: { stepSize: 5 },"
        + "        grid: { color: 'rgba(200, 200, 200, 0.5)' }"
        + "      },"
        + "      x: {"
        + "        title: {"
        + "          display: true,"
        + "          text: $3,"
        + "          font: { size: 14 }"
        + "        },"
        + "        grid: { display: false }"
        + "      }"
        + "    },"
        + "    plugins: {"
        + "      legend: { display: false },"
        + "      tooltip: {"
        + "        enabled: true,"
        + "        backgroundColor: 'rgba(0, 0, 0, 0.8)',"
        + "        titleFont: { size: 14 },"
        + "        bodyFont: { size: 12 }"
        + "      },"
        + "      title: {"
        + "        display: true,"
        + "        text: '',"
        + "        font: { size: 18 },"
        + "        padding: 15"
        + "      }"
        + "    },"
        + "    animation: {"
        + "      duration: 1000,"
        + "      easing: 'easeInOutBounce'"
        + "    }"
        + "  }"
        + "});"
        + "}, 100);", jsonLabels, jsonData, textY, textX);
  }

  /**
   * Updates the bar chart with new labels and values.
   *
   * <p>This method updates the data and axis titles of the existing chart
   * without recreating the chart instance.</p>
   *
   * @param labels the new labels for the bar chart
   * @param values the new values corresponding to each label
   */
  public void updateChart(String[] labels, double[] values) {
    System.out.println("Updating chart with labels: " + Arrays.toString(labels) + ", values: "
        + Arrays.toString(values));

    JsonArray jsonLabels = Json.createArray();
    JsonArray jsonData = Json.createArray();

    for (int i = 0; i < labels.length; i++) {
      jsonLabels.set(i, labels[i]);
      jsonData.set(i, values[i]);
    }

    String textY = getTranslation("insights.BarChartTextY");
    String textX = getTranslation("insights.BarChartTextX");

    getElement().executeJs("setTimeout(() => {"
        + "if (window.barChart) {"
        + "  window.barChart.data.labels = $0;"
        + "  window.barChart.data.datasets[0].data = $1;"
        + "  window.barChart.options.scales.y.title.text = $2;"
        + "  window.barChart.options.scales.x.title.text = $3;"
        + "  window.barChart.update();"
        + "}"
        + "}, 100);", jsonLabels, jsonData, textY, textX);
  }
}
