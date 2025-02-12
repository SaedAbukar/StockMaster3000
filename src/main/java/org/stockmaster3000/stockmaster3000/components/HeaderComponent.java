package org.stockmaster3000.stockmaster3000.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.stockmaster3000.stockmaster3000.security.SecurityService;

public class HeaderComponent extends HorizontalLayout {

    private final SecurityService securityService;

    public HeaderComponent(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
    }

    private void createHeader() {
        addClassName("header");
        setWidthFull();
        setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        setSpacing(true);

        H1 title = new H1();
        title.addClassName("logo");
        title.setText("StockMaster3000");

        HorizontalLayout authSection = new HorizontalLayout();
        authSection.setWidthFull();
        authSection.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        authSection.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        Button login = new Button("Login");
        if (securityService.getAuthenticatedUser() != null) {
            String username = securityService.getAuthenticatedUser().getUsername();
            Span greeting = new Span("Hello, " + username + "!");
            Button logout = new Button("Logout", click -> securityService.logout());
            logout.addClassName("log-button");
            authSection.add(greeting, logout);
        } else {
            login.addClickListener(click -> login.getUI().ifPresent(ui -> ui.navigate("login")));
            login.addClassName("log-button");
            authSection.add(login);
        }

        add(title, authSection);
    }
}
