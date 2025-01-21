package org.stockmaster3000.stockmaster3000.views;

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

        // Basic validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        // Check if the username already exists
        if (userService.findByUsername(username).isPresent()) {
            showError("Username is already taken.");
            return;
        }

        // Register the user
        userService.registerUser(username, password);

        // Show success message and redirect to login view
        Notification.show("Registration successful! You can now log in.", 3000, Notification.Position.MIDDLE);

        // Redirect to the login view after a successful registration
        UI.getCurrent().navigate(LoginView.class);
    }

    private void showError(String message) {
        // Show error message
        Notification.show(message, 3000, Notification.Position.MIDDLE);
    }
}
