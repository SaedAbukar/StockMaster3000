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
import org.stockmaster3000.stockmaster3000.security.SecurityService;

@Route(value = "/")
@PageTitle("Stock Master 3000")
@AnonymousAllowed
public class MainLayout extends VerticalLayout {

    private final SecurityService securityService;

    public MainLayout(@Autowired SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createHeroSection();
    }

    private void createHeader() {
        // Header container
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setWidthFull();
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setSpacing(true);

        header.getStyle().set("background-color", "#03fc7f")
                .set("padding", "5px")
                .set("color", "white");

        H1 title = new H1();
        title.addClassName("logo");
        title.getElement().setProperty("innerHTML", "StockMaster <span>3000</span>");
        title.getStyle().set("color", "white");

        HorizontalLayout authSection = new HorizontalLayout();
        authSection.setWidthFull();
        authSection.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        authSection.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        Button login = new Button("Login");
        if (securityService.getAuthenticatedUser() != null) {
            String username = securityService.getAuthenticatedUser().getUsername();
            Span greeting = new Span("Hello, " + username + "!");
            greeting.getStyle().set("color", "white");
            Button logout = new Button("Logout", click -> securityService.logout());
            logout.getStyle().set("color", "white");
            authSection.add(greeting, logout);
        } else {
            login.addClickListener(click -> login.getUI().ifPresent(ui -> ui.navigate("login")));
            login.getStyle().set("color", "white");
            authSection.add(login);
        }

        header.add(title, authSection);
        add(header);
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
