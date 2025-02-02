package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DashboardView extends VerticalLayout {
    
    public DashboardView() {
        addClassName("dashboard-view");
        setSpacing(false);
        
        Div dashboardCard = new Div();
        dashboardCard.addClassName("dashboard-card");
        dashboardCard.getStyle()
            .set("background-color", "#00E676")
            .set("padding", "2rem")
            .set("border-radius", "8px")
            .set("width", "100%")
            .set("min-height", "200px");
        
        H2 title = new H2("Dashboard");
        title.getStyle()
            .set("margin", "0")
            .set("color", "#1a1a1a");
        
        dashboardCard.add(title);
        add(dashboardCard);
    }
}