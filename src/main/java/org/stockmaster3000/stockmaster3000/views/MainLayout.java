package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.stockmaster3000.stockmaster3000.components.HeaderComponent;
import org.stockmaster3000.stockmaster3000.security.SecurityService;

@Route(value = "/")
@PageTitle("Stock Master 3000")
@AnonymousAllowed
public class MainLayout extends VerticalLayout {

    private final SecurityService securityService;

    public MainLayout(@Autowired SecurityService securityService) {
        this.securityService = securityService;
        add(new HeaderComponent(securityService));
        createHeroSection();
    }

    private void createHeroSection() {
        VerticalLayout heroSection = new VerticalLayout();
        heroSection.addClassName("hero-section");
        heroSection.setWidthFull();
        heroSection.setAlignItems(FlexComponent.Alignment.CENTER);
        heroSection.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        H1 heroTitle = new H1("Manage Your Stocks with Ease");
        Paragraph heroDescription = new Paragraph("Stock Master 3000 provides powerful insights and inventory tracking for your business.");
        Button inventoryButton = new Button("Go to Inventory");
        inventoryButton.addClickListener(click -> inventoryButton.getUI().ifPresent(ui -> ui.navigate("/inventory")));

        heroSection.add(heroTitle, heroDescription, inventoryButton);
        add(heroSection);
    }
}
