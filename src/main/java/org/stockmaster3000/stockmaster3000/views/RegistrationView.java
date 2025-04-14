package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.stockmaster3000.stockmaster3000.service.UserService;

@Route("register")
@PageTitle("Register")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout implements LocaleChangeObserver {

  private final UserService userService;

  // Global UI components for translation updates
  private TextField usernameField;
  private PasswordField passwordField;
  private PasswordField confirmPasswordField;
  private Button registerButton;
  private H2 title;
  private Paragraph alreadyUserText;
  private Button loginButton;

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
    title = new H2(getTranslation("registration.title"));

    // Input Fields
    usernameField = new TextField(getTranslation("registration.username"));
    passwordField = new PasswordField(getTranslation("registration.password"));
    confirmPasswordField = new PasswordField(getTranslation("registration.confirmPassword"));

    usernameField.addClassName("input-field");
    passwordField.addClassName("input-field");
    confirmPasswordField.addClassName("input-field");

    // Sign Up Button
    registerButton = new Button(getTranslation("button.register"));
    registerButton.addClassName("register-button");
    registerButton.addClickListener(event -> registerUser());

    // "Already have an account?" text
    alreadyUserText = new Paragraph(getTranslation("registration.alreadyUser"));
    alreadyUserText.addClassName("plain-text");

    // Log In Button (styled as text-only)
    loginButton = new Button(getTranslation("login.submit"));
    loginButton.addClassName("signup-button");
    loginButton.addClickListener(click -> UI.getCurrent().navigate("login"));

    // Layout
    FormLayout formLayout = new FormLayout();
    formLayout.add(usernameField, passwordField, confirmPasswordField);

    String currentLang = UI.getCurrent().getLocale().getLanguage();

    Button englishButton = new Button("🇬🇧",
        click -> UI.getCurrent().setLocale(Locale.ENGLISH));
    Button russianButton = new Button("🇷🇺", click -> UI.getCurrent().setLocale(new Locale(
        "ru", "RU")));
    Button greekButton = new Button("🇬🇷", click -> UI.getCurrent().setLocale(
        new Locale("el", "GR")));
    Button finnishButton = new Button("🇫🇮", click -> UI.getCurrent().setLocale(new Locale(
        "fi", "FI")));

    // Add active class based on current language
    Map<String, Button> buttonMap = Map.of(
        "en", englishButton,
        "ru", russianButton,
        "el", greekButton,
        "fi", finnishButton
    );


    // Apply active class to the correct button
    buttonMap.getOrDefault(currentLang, englishButton).addClassName("active");

    // Style buttons
    Stream.of(englishButton, russianButton, greekButton, finnishButton).forEach(button ->
        button.getElement().getStyle().set("cursor", "pointer")
    );

    HorizontalLayout languageSelectorLayout = new HorizontalLayout(englishButton,
        russianButton, greekButton, finnishButton);
    languageSelectorLayout.addClassName("login-register-language-selector");
    // Add elements to the form card
    formCard.add(title, formLayout, registerButton, alreadyUserText, loginButton,
        languageSelectorLayout);

    // Add the card to the main layout
    add(formCard);
  }

  private void registerUser() {
    String username = usernameField.getValue();
    String password = passwordField.getValue();
    String confirmPassword = confirmPasswordField.getValue();

    if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
      showError(getTranslation("registration.errorFieldsRequired"));
      return;
    }

    if (!password.equals(confirmPassword)) {
      showError(getTranslation("registration.errorPasswordsMatch"));
      return;
    }

    String registrationResult = userService.registerUser(username, password);

    if (registrationResult.equals(getTranslation("registration.success"))) {
      Notification.show(getTranslation("registration.successMessage"), 3000,
          Notification.Position.MIDDLE);
      UI.getCurrent().navigate("login");
    } else {
      showError(registrationResult);
    }
  }

  private void showError(String message) {
    Notification.show(message, 3000, Notification.Position.MIDDLE);
  }

  @Override
  public void localeChange(LocaleChangeEvent localeChangeEvent) {
    // Update translations globally on locale change
    title.setText(getTranslation("registration.title"));
    usernameField.setLabel(getTranslation("registration.username"));
    passwordField.setLabel(getTranslation("registration.password"));
    confirmPasswordField.setLabel(getTranslation("registration.confirmPassword"));
    registerButton.setText(getTranslation("button.register"));
    alreadyUserText.setText(getTranslation("registration.alreadyUser"));
    loginButton.setText(getTranslation("login.submit"));
  }
}
