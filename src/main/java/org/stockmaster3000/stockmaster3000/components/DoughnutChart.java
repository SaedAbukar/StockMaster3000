package org.stockmaster3000.stockmaster3000.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import elemental.json.Json;
import elemental.json.JsonArray;

@Tag("canvas")
@JsModule("https://cdn.jsdelivr.net/npm/chart.js")
public class DoughnutChart extends Div {

    public DoughnutChart(String[] labels, int[] values) {
        getElement().setAttribute("id", "doughnutChart");

        // Add inline CSS for width & height
        getElement().getStyle().set("width", "400px");
        getElement().getStyle().set("height", "400px");

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
                "new Chart(ctx, {" +
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

    public void updateChart(String[] productTypes, int[] productCount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateChart'");
    }
}
