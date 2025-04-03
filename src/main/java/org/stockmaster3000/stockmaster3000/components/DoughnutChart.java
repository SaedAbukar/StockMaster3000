package org.stockmaster3000.stockmaster3000.components;

import com.vaadin.flow.component.html.Div;
import java.util.Arrays;


import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import elemental.json.Json;
import elemental.json.JsonArray;

@Tag("canvas")
@JsModule("https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.0/chart.umd.js")
public class DoughnutChart extends Div {

    public DoughnutChart(String[] labels, int[] values) {
        getElement().setAttribute("id", "doughnutChart");

        // Increase the size for better visibility
        getElement().getStyle().set("width", "450px");
        getElement().getStyle().set("height", "450px");

        // Initialize the chart
        initializeChart(labels, values);
    }

    private void initializeChart(String[] labels, int[] values) {
        JsonArray jsonLabels = Json.createArray();
        JsonArray jsonData = Json.createArray();

        for (int i = 0; i < labels.length; i++) {
            jsonLabels.set(i, labels[i]);
            jsonData.set(i, values[i]);
        }

        getElement().executeJs("setTimeout(() => {" +
                "const ctx = document.getElementById('doughnutChart').getContext('2d');" +
                "ctx.canvas.width = 350;" + 
                "ctx.canvas.height = 350;" + 
                "window.doughnutChart = new Chart(ctx, {" + 
                "  type: 'doughnut'," +
                "  data: {" +
                "    labels: $0," +
                "    datasets: [{" +
                "      label: 'Category Distribution'," +
                "      data: $1," +
                "      backgroundColor: [" +
                "        '#E74C3C', '#3498DB', '#F1C40F', '#2ECC71', '#9B59B6', '#1ABC9C', '#F39C12'" +
                "      ]," +
                "      hoverOffset: 8" +
                "    }]" +
                "  }," +
                "  options: {" +
                "    responsive: false," +  
                "    maintainAspectRatio: false," +
                "    plugins: {" +
                "      legend: {" +
                "        position: 'top'," +
                "        labels: {" +
                "          font: { size: 14 }," +
                "          color: '#333'" +
                "        }" +
                "      }," +
                "      title: {" +
                "        display: true," +
                "        text: ''," +
                "        font: { size: 18 }," +
                "        padding: 15" +
                "      }," +
                "      tooltip: {" +
                "        enabled: true," +
                "        backgroundColor: 'rgba(0, 0, 0, 0.8)'," +
                "        titleFont: { size: 14 }," +
                "        bodyFont: { size: 12 }" +
                "      }" +
                "    }" +
                "  }" +
                "});" +
                "}, 100);", jsonLabels, jsonData);
    }

    public void updateChart(String[] labels, int[] values) {
        System.out.println("Updating chart with labels: " + Arrays.toString(labels) + ", values: " + Arrays.toString(values));
    
        JsonArray jsonLabels = Json.createArray();
        JsonArray jsonData = Json.createArray();
    
        for (int i = 0; i < labels.length; i++) {
            jsonLabels.set(i, labels[i]);
            jsonData.set(i, values[i]);
        }
    
        getElement().executeJs("setTimeout(() => {" +
                "if (window.doughnutChart) {" +
                "  window.doughnutChart.data.labels = $0;" +
                "  window.doughnutChart.data.datasets[0].data = $1;" +
                "  window.doughnutChart.update();" + 
                "}" +
                "}, 100);", jsonLabels, jsonData);
    }
}
