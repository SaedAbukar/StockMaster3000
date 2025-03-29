package org.stockmaster3000.stockmaster3000.components;

import com.vaadin.flow.component.html.Div;
import java.util.Arrays;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import elemental.json.Json;
import elemental.json.JsonArray;

@Tag("canvas")
@JsModule("https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.0/chart.umd.js")
public class LineChart extends Div {

    public LineChart(String[] labels, int[] values) {
        getElement().setAttribute("id", "lineChart");

        // Increase size for better visibility
        getElement().getStyle().set("width", "550px");
        getElement().getStyle().set("height", "350px");

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

        String textY = getTranslation("insights.LineChartTextY");
        String textX = getTranslation("insights.LineChartTextX");

        getElement().executeJs("setTimeout(() => {" +
                "const ctx = document.getElementById('lineChart').getContext('2d');" +
                "ctx.canvas.width = 450;" + 
                "ctx.canvas.height = 350;" + 
                "window.lineChart = new Chart(ctx, {" + 
                "  type: 'line'," +
                "  data: {" +
                "    labels: $0," +
                "    datasets: [{" +
                "      label: 'Days Until Expiration'," +
                "      data: $1," +
                "      backgroundColor: 'rgba(255, 99, 132, 0.3)'," +
                "      borderColor: 'rgba(255, 99, 132, 1)'," +
                "      borderWidth: 2," +
                "      fill: true," +
                "      pointBackgroundColor: 'rgba(255, 99, 132, 1)'," +
                "      pointBorderColor: 'white'," +
                "      pointBorderWidth: 2," +
                "      pointRadius: 5" +
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
                "          text: $2," +
                "          font: { size: 14 }" +
                "        }," +
                "        ticks: { stepSize: 5 }," +
                "        grid: { color: 'rgba(200, 200, 200, 0.5)' }" +
                "      }," +
                "      x: {" +
                "        title: {" +
                "          display: true," +
                "          text: $3," +
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
                "        text: ''," +
                "        font: { size: 18 }," +
                "        padding: 15" +
                "      }" +
                "    }," +
                "    animation: {" +
                "      duration: 1000," +
                "      easing: 'easeOutQuart'" +
                "    }" +
                "  }" +
                "});" +
                "}, 100);", jsonLabels, jsonData, textY, textX);
    }

    public void updateChart(String[] labels, int[] values) {
        System.out.println("Updating chart with labels: " + Arrays.toString(labels) + ", values: " + Arrays.toString(values));
    
        JsonArray jsonLabels = Json.createArray();
        JsonArray jsonData = Json.createArray();
    
        for (int i = 0; i < labels.length; i++) {
            jsonLabels.set(i, labels[i]);
            jsonData.set(i, values[i]);
        }
    
        String YAxel = getTranslation("insights.LineChartTextY");  
        String XAxel = getTranslation("insights.LineChartTextX");  
    
        // Execute JavaScript to update the chart data and Y-axis title
        getElement().executeJs("setTimeout(() => {" +
                "if (window.lineChart) {" +
                "  window.lineChart.data.labels = $0;" +
                "  window.lineChart.data.datasets[0].data = $1;" +
                "  window.lineChart.options.scales.y.title.text = $2;" +
                "  window.lineChart.options.scales.x.title.text = $3;" +
                "  window.lineChart.update();" + 
                "}" +
                "}, 100);", jsonLabels, jsonData, YAxel, XAxel);
    }
    
}
