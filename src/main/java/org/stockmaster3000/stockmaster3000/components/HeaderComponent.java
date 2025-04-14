package org.stockmaster3000.stockmaster3000.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.stockmaster3000.stockmaster3000.event.LanguageChangeListener;
import org.stockmaster3000.stockmaster3000.security.SecurityService;

public class HeaderComponent extends HorizontalLayout {

  private final SecurityService securityService;
  private H1 title;
  private Button login;
  private Button logout;
  private Span greeting;

  private final List<LanguageChangeListener> languageChangeListeners = new ArrayList<>();

  public HeaderComponent(SecurityService securityService) {
    this.securityService = securityService;
    createHeader();
    addLanguageSelector();
  }

  private void createHeader() {
    addClassName("header");
    setWidthFull();
    setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    setSpacing(true);

    title = new H1(getTranslation("header.title"));
    title.addClassName("logo");

    HorizontalLayout authSection = new HorizontalLayout();
    authSection.setWidthFull();
    authSection.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
    authSection.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

    login = new Button(getTranslation("header.login"));
    if (securityService.getAuthenticatedUser() != null) {
      String username = securityService.getAuthenticatedUser().getUsername();
      greeting = new Span(getTranslation("header.hello") + " " + username + "!");
      logout = new Button(getTranslation("header.logout"), click -> securityService.logout());
      logout.addClassName("log-button");
      authSection.add(greeting, logout);
    } else {
      login.addClickListener(click -> login.getUI().ifPresent(ui -> ui.navigate("login")));
      login.addClassName("log-button");
      authSection.add(login);
    }

    add(title, authSection);
  }

  private void addLanguageSelector() {
    ComboBox<Locale> languageSelector = new ComboBox<>();
    languageSelector.setItems(Locale.ENGLISH, new Locale("ru", "RU"),
        new Locale("el", "GR"),
        new Locale("fi", "FI"));

    languageSelector.setItemLabelGenerator(locale -> switch (locale.getLanguage()) {
      case "ru" -> "🇷🇺";
      case "el" -> "🇬🇷";
      case "fi" -> "🇫🇮";
      default -> "🇬🇧";
    });

    Locale currentLocale = (UI.getCurrent() != null) ? UI.getCurrent().getLocale() :
        Locale.ENGLISH;
    languageSelector.setValue(currentLocale);

    languageSelector.addValueChangeListener(event -> {
      if (event.getValue() != null) {
        Locale newLocale = event.getValue();
        UI.getCurrent().setLocale(newLocale);
        notifyLanguageChangeListeners(newLocale); // Notify other components
      }
    });

    languageSelector.getElement().getStyle().set("color", "white").set("cursor", "pointer");
    add(new HorizontalLayout(languageSelector));
    languageSelector.addClassName("custom-language-selector");
  }

  public void addLanguageChangeListener(LanguageChangeListener listener) {
    languageChangeListeners.add(listener);
  }

  private void notifyLanguageChangeListeners(Locale newLocale) {
    for (LanguageChangeListener listener : languageChangeListeners) {
      listener.onLanguageChange(newLocale);
    }
  }

  public void updateTexts() {
    title.setText(getTranslation("header.title"));
    login.setText(getTranslation("header.login"));

    if (securityService.getAuthenticatedUser() != null) {
      String username = securityService.getAuthenticatedUser().getUsername();
      greeting.setText(getTranslation("header.hello") + " " + username + "!");
      logout.setText(getTranslation("header.logout"));
    }
  }
}
