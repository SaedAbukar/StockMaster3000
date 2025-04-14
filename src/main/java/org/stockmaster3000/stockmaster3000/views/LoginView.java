package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

/**
 * The LoginView provides a custom login page with localization and register redirect.
 */
@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout
    implements BeforeEnterListener, LocaleChangeObserver {

  private final LoginForm loginForm = new LoginForm();
  private Button registerButton;
  private Paragraph notUserText;
  private H1 title;

  /**
   * Constructs the login view UI, including form, language switch, and register button.
   */
  public LoginView() {
    addClassName("login-view");
    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);

    Div loginCard = new Div();
    loginCard.addClassName("login-card");

    title = new H1(getTranslation("header.title"));
    title.addClassName("app-title");

    loginForm.setAction("login");
    loginForm.setForgotPasswordButtonVisible(false);

    registerButton = new Button(getTranslation("button.register"));
    registerButton.addClassName("signup-button");
    registerButton.addClickListener(click ->
        registerButton.getUI().ifPresent(ui -> ui.navigate("register")));

    notUserText = new Paragraph(getTranslation("noaccount"));
    notUserText.addClassName("plain-text");

    String currentLang = UI.getCurrent().getLocale().getLanguage();

    Button englishButton = new Button("🇬🇧",
        click -> UI.getCurrent().setLocale(Locale.ENGLISH));
    Button russianButton = new Button("🇷🇺",
        click -> UI.getCurrent().setLocale(new Locale("ru", "RU")));
    Button greekButton = new Button("🇬🇷",
        click -> UI.getCurrent().setLocale(new Locale("el", "GR")));
    Button finnishButton = new Button("🇫🇮",
        click -> UI.getCurrent().setLocale(new Locale("fi", "FI")));

    Map<String, Button> buttonMap = Map.of(
        "en", englishButton,
        "ru", russianButton,
        "el", greekButton,
        "fi", finnishButton
    );
    buttonMap.getOrDefault(currentLang, englishButton).addClassName("active");

    Stream.of(englishButton, russianButton, greekButton, finnishButton)
        .forEach(button -> button.getElement().getStyle().set("cursor", "pointer"));

    HorizontalLayout languageSelectorLayout = new HorizontalLayout(
        englishButton, russianButton, greekButton, finnishButton);
    languageSelectorLayout.addClassName("login-register-language-selector");

    loginCard.add(
        title,
        loginForm,
        notUserText,
        registerButton,
        languageSelectorLayout
    );

    add(loginCard);
  }

  /**
   * Displays login error message when login fails.
   *
   * @param event the navigation event
   */
  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
      loginForm.setError(true);
    }
  }

  /**
   * Updates UI text when the locale is changed.
   *
   * @param event the locale change event
   */
  @Override
  public void localeChange(LocaleChangeEvent event) {
    notUserText.setText(getTranslation("noaccount"));
    title.setText(getTranslation("header.title"));
    registerButton.setText(getTranslation("button.register"));

    LoginI18n i18n = LoginI18n.createDefault();
    i18n.getForm().setUsername(getTranslation("login.username"));
    i18n.getForm().setPassword(getTranslation("login.password"));
    i18n.getForm().setTitle(getTranslation("login.title"));
    i18n.getForm().setSubmit(getTranslation("login.submit"));

    loginForm.setI18n(i18n);
  }
}
