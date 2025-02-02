package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("/help")
public class MainViewWithSignLogin extends VerticalLayout {
    private Div mainContent;
    private InventoryView inventoryView;
    private InsightsView insightsView;
    private Tabs tabs;

    public MainViewWithSignLogin() {
        setPadding(false);
        setSpacing(false);
        setSizeFull();

        // Create header
        createHeader();

        // Create tabs
        createTabs();

        // Create content area
        createContent();

        // Show inventory by default
        showInventory();
    }

    private void createHeader() {
        // Header container
        Div header = new Div();
        header.addClassName("header");

        // Logo/title on the left
        H1 title = new H1();
        title.addClassName("logo");
        title.getElement().setProperty("innerHTML", "StockMaster<span style='color: #00E676;'>3000</span>");

        // Settings button
        Button settingsButton = new Button(VaadinIcon.COG.create());
        settingsButton.addClassName("settings-button");
        settingsButton.getStyle().set("margin-left", "auto");

        // Signup/Login button
        Button authButton = new Button("Signup/Login", event -> openAuthModal());
        authButton.addClassName("auth-button");

        header.add(title, settingsButton, authButton);
        add(header);
    }

    private void createTabs() {
        // Create tabs
        Tab inventoryTab = new Tab("Inventory");
        Tab insightsTab = new Tab("Insights");
        Tab aboutTab = new Tab("About");

        tabs = new Tabs(inventoryTab, insightsTab, aboutTab);
        tabs.setWidthFull(); // Stretch tabs across the screen
        tabs.addClassName("nav-tabs");

        // Align tabs to the center
        tabs.getStyle().set("justify-content", "center");
        tabs.getStyle().set("border-bottom", "1px solid var(--primary-color)");
        tabs.getStyle().set("margin-bottom", "0");

        // Add tab selection listener
        tabs.addSelectedChangeListener(event -> {
            Tab selectedTab = event.getSelectedTab();
            if (selectedTab.equals(inventoryTab)) {
                showInventory();
            } else if (selectedTab.equals(insightsTab)) {
                showInsights();
            } else if (selectedTab.equals(aboutTab)) {
                showAbout();
            }
        });

        // Add tabs to layout
        add(tabs);
    }

    private void createContent() {
        mainContent = new Div();
        mainContent.addClassName("main-content");
        mainContent.setSizeFull();

        // Initialize views
        inventoryView = new InventoryView();
        insightsView = new InsightsView();

        add(mainContent);
    }

    private void showInventory() {
        mainContent.removeAll();
        mainContent.add(inventoryView);
    }

    private void showInsights() {
        mainContent.removeAll();
        mainContent.add(insightsView);
    }

    private void showAbout() {
        mainContent.removeAll();
        Div aboutContent = new Div();
        aboutContent.setText("About Coming Soon");
        mainContent.add(aboutContent);
    }

    private void openAuthModal() {
        // Create dialog
        Dialog authDialog = new Dialog();
        authDialog.setWidth("400px");
        authDialog.setHeight("500px");

        // Create tabs for Sign Up and Login
        Tab signUpTab = new Tab("Sign Up");
        Tab loginTab = new Tab("Login");
        Tabs authTabs = new Tabs(signUpTab, loginTab);

        // Create content layouts for Sign Up and Login
        VerticalLayout signUpLayout = createSignUpLayout();
        VerticalLayout loginLayout = createLoginLayout();

        Div authContent = new Div(signUpLayout);
        authContent.setSizeFull();

        // Add tab change listener
        authTabs.addSelectedChangeListener(event -> {
            if (event.getSelectedTab().equals(signUpTab)) {
                authContent.removeAll();
                authContent.add(signUpLayout);
            } else {
                authContent.removeAll();
                authContent.add(loginLayout);
            }
        });

        // Add components to dialog
        VerticalLayout dialogLayout = new VerticalLayout(authTabs, authContent);
        dialogLayout.setSizeFull();
        authDialog.add(dialogLayout);

        authDialog.open();
    }

    private VerticalLayout createSignUpLayout() {
        // Components for Sign Up
        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");
        Button registerButton = new Button("Register", event -> {
            // Registration logic
            // Replace with actual registration service
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            String confirmPassword = confirmPasswordField.getValue();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Notification.show("All fields are required.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                Notification.show("Passwords do not match.");
                return;
            }

            Notification.show("Registration successful!");
        });

        // Layout
        VerticalLayout signUpLayout = new VerticalLayout(usernameField, passwordField, confirmPasswordField, registerButton);
        signUpLayout.setPadding(false);
        return signUpLayout;
    }

    private VerticalLayout createLoginLayout() {
        // Components for Login
        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        Button loginButton = new Button("Login", event -> {
            // Login logic
            // Replace with actual login service
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            if (username.isEmpty() || password.isEmpty()) {
                Notification.show("All fields are required.");
                return;
            }

            Notification.show("Login successful!");
        });

        // Layout
        VerticalLayout loginLayout = new VerticalLayout(usernameField, passwordField, loginButton);
        loginLayout.setPadding(false);
        return loginLayout;
    }
}