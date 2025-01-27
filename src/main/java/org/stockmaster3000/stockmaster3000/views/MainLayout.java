package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import com.vaadin.flow.component.orderedlayout.FlexComponent; // Correct import for Alignment

@Route(value = "/")
@PageTitle("Public View")
@AnonymousAllowed
public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    public MainLayout(@Autowired SecurityService securityService) {
        this.securityService = securityService;

        // Logo for the application
        H1 logo = new H1("Stock Master 3000");
        logo.addClassName("logo");

        // Header to hold dynamic content
        HorizontalLayout header;
        Button login = new Button("Login");

        if (securityService.getAuthenticatedUser() != null) {
            // Authenticated: Display greeting and logout button
            String username = securityService.getAuthenticatedUser().getUsername(); // Fetch authenticated user's name
            Span greeting = new Span("Hello, " + username + "!");
            Button logout = new Button("Logout", click -> securityService.logout());
            header = new HorizontalLayout(logo, greeting, logout);
        } else {
            // Not authenticated: Display login message and login button
            // Not authenticated: Display login message and login button
            Span message = new Span("Please log in to access your account.");
         
            login.addClickListener(click ->
                    login.getUI().ifPresent(ui -> ui.navigate("login")));
            header = new HorizontalLayout(logo, message, login);
        }

        // Styling the header
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassName("header");

        addToNavbar(header);
    }
}
