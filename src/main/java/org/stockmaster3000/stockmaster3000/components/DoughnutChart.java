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

        // Add inline CSS for width & height
        getElement().getStyle().set("width", "400px");
        getElement().getStyle().set("height", "400px");

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
                "ctx.canvas.width = 300;" + 
                "ctx.canvas.height = 300;" + 
                "window.doughnutChart = new Chart(ctx, {" + // Store the chart instance in a global variable
                "  type: 'doughnut'," +
                "  data: {" +
                "    labels: $0," +
                "    datasets: [{" +
                "      label: 'Product Types'," +
                "      data: $1," +
                "      backgroundColor: [\n" + //
                                        "    '#FF6384', '#36A2EB', '#FFCE56', '#4CAF50', '#BA68C8', '#FF5722', '#795548',\n" + //
                                        "    '#E91E63', '#9C27B0', '#2196F3', '#4CAF50', '#FF9800', '#9E9E9E', '#607D8B',\n" + //
                                        "    '#F44336', '#03A9F4', '#8BC34A', '#FFEB3B', '#FF4081', '#673AB7', '#CDDC39',\n" + //
                                        "    '#FF5722', '#9C27B0', '#00BCD4', '#FFC107', '#FF9800', '#9E9E9E', '#607D8B'\n" + //
                                        "]\n" + //
                                        "" +
                "    }]" +
                "  }," +
                "  options: {" +
                "    responsive: false," +  
                "    maintainAspectRatio: false" + 
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
                "  window.doughnutChart.update();" + // Update the chart
                "}" +
                "}, 100);", jsonLabels, jsonData);
    }
}