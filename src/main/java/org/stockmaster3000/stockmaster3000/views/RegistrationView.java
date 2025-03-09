package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.stockmaster3000.stockmaster3000.service.UserService;

@Route("register")
@PageTitle("Register")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {

    private final UserService userService;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button registerButton;

    @Autowired
    public RegistrationView(UserService userService) {
        this.userService = userService;

        // Center the layout
        addClassName("registration-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Create the form container
        Div formCard = new Div();
        formCard.addClassName("registration-card");

        // Title
        H2 title = new H2("Create an Account");

        // Input Fields
        usernameField = new TextField("Username");
        passwordField = new PasswordField("Password");
        confirmPasswordField = new PasswordField("Confirm Password");

        usernameField.addClassName("input-field");
        passwordField.addClassName("input-field");
        confirmPasswordField.addClassName("input-field");

// Sign Up Button
registerButton = new Button("Sign Up");
registerButton.addClassName("register-button");
registerButton.addClickListener(event -> registerUser());

// "Already have an account?" text
Paragraph alreadyUserText = new Paragraph("Already have an account?");
alreadyUserText.addClassName("plain-text");

// Log In Button (styled as text-only)
Button loginButton = new Button("Log In");
loginButton.addClassName("signup-button");
loginButton.addClickListener(click -> UI.getCurrent().navigate("login"));

        // Layout
        FormLayout formLayout = new FormLayout();
        formLayout.add(usernameField, passwordField, confirmPasswordField);

        // Add elements to the form card
        formCard.add(title, formLayout, registerButton, alreadyUserText, loginButton);

        // Add the card to the main layout
        add(formCard);
    }

    private void registerUser() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();
        String confirmPassword = confirmPasswordField.getValue();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        String registrationResult = userService.registerUser(username, password);

        if (registrationResult.equals("Registration successful")) {
            Notification.show("Registration successful! You can now log in.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("login");
        } else {
            showError(registrationResult);
        }
    }

    private void showError(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
    }
}
