package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.stockmaster3000.stockmaster3000.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.UI;

import org.springframework.beans.factory.annotation.Autowired;

@Route("register") // Accessible at /register
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

        // Initialize components
        usernameField = new TextField("Username");
        passwordField = new PasswordField("Password");
        confirmPasswordField = new PasswordField("Confirm Password");
        registerButton = new Button("Register");

        // Set up button click listener
        registerButton.addClickListener(event -> registerUser());

        // Set up the form layout
        FormLayout formLayout = new FormLayout();
        formLayout.add(usernameField, passwordField, confirmPasswordField, registerButton);

        // Add the form to the layout
        add(formLayout);
    }

    private void registerUser() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();
        String confirmPassword = confirmPasswordField.getValue();

        // Basic validation for empty fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        // Password confirmation check
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        // Call the registerUser method and get the result
        String registrationResult = userService.registerUser(username, password);

        // Show either success or error message based on the result
        if (registrationResult.equals("Registration successful")) {
            Notification.show("Registration successful! You can now log in.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("login"); // Redirect to login view
        } else {
            showError(registrationResult); // Show the error message
        }
    }

    private void showError(String message) {
        // Show the error message in a notification
        Notification.show(message, 3000, Notification.Position.MIDDLE);
    }
}
