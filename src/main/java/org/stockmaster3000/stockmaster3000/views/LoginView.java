package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.html.Div;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterListener {

    private final LoginForm loginForm = new LoginForm();
    private final Button registerButton = new Button("Sign Up");

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Login form container (for styling)
        Div loginCard = new Div();
        loginCard.addClassName("login-card");

        // Branding header instead of "Login"
        H1 title = new H1("StockMaster3000");
        title.addClassName("app-title");

        // Configure login form (remove Forgot Password)
        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(false);

        // Register button
        registerButton.addClassName("signup-button");
        registerButton.addClickListener(click ->
                registerButton.getUI().ifPresent(ui -> ui.navigate("register")));

        // "Not a user yet?" text
        Paragraph notUserText = new Paragraph("Don't have an account yet?");
        notUserText.addClassName("plain-text");

        // Add elements to login card
        loginCard.add(
                title,
                loginForm,
                notUserText,
                registerButton
        );

        // Add the card to the layout
        add(loginCard);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
