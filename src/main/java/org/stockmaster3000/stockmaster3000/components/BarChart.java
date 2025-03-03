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

        // Set width and height
        getElement().getStyle().set("width", "500px");
        getElement().getStyle().set("height", "300px");

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
                "ctx.canvas.width = 400;" + 
                "ctx.canvas.height = 300;" + 
                "window.barChart = new Chart(ctx, {" + 
                "  type: 'bar'," +
                "  data: {" +
                "    labels: $0," +
                "    datasets: [{" +
                "      label: 'Spending per Food Item'," +
                "      data: $1," +
                "      backgroundColor: 'rgba(75, 192, 192, 0.6)'," +
                "      borderColor: 'rgba(75, 192, 192, 1)'," +
                "      borderWidth: 1" +
                "    }]" +
                "  }," +
                "  options: {" +
                "    responsive: false," +  
                "    maintainAspectRatio: false," +
                "    scales: {" +
                "      y: { beginAtZero: true }" +
                "    }" +
                "  }" +
                "});" +
                "}, 100);", jsonLabels, jsonData);
    }

    public void updateChart(String[] labels, double[] values) {
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
