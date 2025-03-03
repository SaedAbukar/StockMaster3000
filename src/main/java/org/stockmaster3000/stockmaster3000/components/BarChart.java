package org.stockmaster3000.stockmaster3000.components;

import com.vaadin.flow.component.html.Div;
import java.util.Arrays;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import elemental.json.Json;
import elemental.json.JsonArray;

@Tag("canvas")
@JsModule("https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.0/chart.umd.js")
public class BarChart extends Div {

    public BarChart(String[] labels, double[] values) {
        getElement().setAttribute("id", "barChart");

        // Increase size for better readability
        getElement().getStyle().set("width", "550px");
        getElement().getStyle().set("height", "350px");

        // Initialize the chart
        initializeChart(labels, values);
    }

    private void initializeChart(String[] labels, double[] values) {
        JsonArray jsonLabels = Json.createArray();
        JsonArray jsonData = Json.createArray();

        for (int i = 0; i < labels.length; i++) {
            jsonLabels.set(i, labels[i]);
            jsonData.set(i, values[i]);
        }

        getElement().executeJs("setTimeout(() => {" +
                "const ctx = document.getElementById('barChart').getContext('2d');" +
                "ctx.canvas.width = 450;" + 
                "ctx.canvas.height = 350;" + 
                "window.barChart = new Chart(ctx, {" + 
                "  type: 'bar'," +
                "  data: {" +
                "    labels: $0," +
                "    datasets: [{" +
                "      label: 'Total Spending (€)'," +
                "      data: $1," +
                "      backgroundColor: [" +
                "        'rgba(255, 99, 132, 0.7)'," +
                "        'rgba(54, 162, 235, 0.7)'," +
                "        'rgba(255, 206, 86, 0.7)'," +
                "        'rgba(75, 192, 192, 0.7)'," +
                "        'rgba(153, 102, 255, 0.7)'," +
                "        'rgba(255, 159, 64, 0.7)'" +
                "      ]," +
                "      borderColor: 'rgba(0, 0, 0, 0.2)'," +
                "      borderWidth: 2" +
                "    }]" +
                "  }," +
                "  options: {" +
                "    responsive: false," +  
                "    maintainAspectRatio: false," +
                "    scales: {" +
                "      y: {" +
                "        beginAtZero: true," +
                "        title: {" +
                "          display: true," +
                "          text: 'Total Cost (€)'," +
                "          font: { size: 14 }" +
                "        }," +
                "        ticks: { stepSize: 5 }," +
                "        grid: { color: 'rgba(200, 200, 200, 0.5)' }" +
                "      }," +
                "      x: {" +
                "        title: {" +
                "          display: true," +
                "          text: 'Food Items'," +
                "          font: { size: 14 }" +
                "        }," +
                "        grid: { display: false }" +
                "      }" +
                "    }," +
                "    plugins: {" +
                "      legend: {" +
                "        display: false" +
                "      }," +
                "      tooltip: {" +
                "        enabled: true," +
                "        backgroundColor: 'rgba(0, 0, 0, 0.8)'," +
                "        titleFont: { size: 14 }," +
                "        bodyFont: { size: 12 }" +
                "      }," +
                "      title: {" +
                "        display: true," +
                "        text: 'Spending Per Food Item'," +
                "        font: { size: 18 }," +
                "        padding: 15" +
                "      }" +
                "    }," +
                "    animation: {" +
                "      duration: 1000," +
                "      easing: 'easeInOutBounce'" +
                "    }" +
                "  }" +
                "});" +
                "}, 100);", jsonLabels, jsonData);
    }

    public void updateChart(String[] labels, double[] values) {
        System.out.println("Updating chart with labels: " + Arrays.toString(labels) + ", values: " + Arrays.toString(values));

        JsonArray jsonLabels = Json.createArray();
        JsonArray jsonData = Json.createArray();

        for (int i = 0; i < labels.length; i++) {
            jsonLabels.set(i, labels[i]);
            jsonData.set(i, values[i]);
        }

        getElement().executeJs("setTimeout(() => {" +
                "if (window.barChart) {" +
                "  window.barChart.data.labels = $0;" +
                "  window.barChart.data.datasets[0].data = $1;" +
                "  window.barChart.update();" + 
                "}" +
                "}, 100);", jsonLabels, jsonData);
    }
}
