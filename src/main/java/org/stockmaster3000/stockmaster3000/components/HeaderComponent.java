package org.stockmaster3000.stockmaster3000.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.stockmaster3000.stockmaster3000.security.SecurityService;

import java.util.Locale;

public class HeaderComponent extends HorizontalLayout {

    private final SecurityService securityService;
    private H1 title;
    private Button login;
    private Button logout;
    private Span greeting;

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

        // Use Locale with both language and country codes
        Locale russian = new Locale("ru", "RU"); // Russian (Russia)
        Locale greek = new Locale("el", "GR"); // Greek (Greece)
        Locale finnish = new Locale("fi", "FI"); // Finnish (Finland)

        languageSelector.setItems(Locale.ENGLISH, russian, greek, finnish);

        // Set custom ItemLabelGenerator to show flag emojis
        languageSelector.setItemLabelGenerator(locale -> {
            if (locale == null) {
                return "🌍"; // Default icon if null
            }
            switch (locale.getLanguage()) {
                case "ru":
                    return "🇷🇺"; // Flag for Russia
                case "el":
                    return "🇬🇷"; // Flag for Greece
                case "fi":
                    return "🇫🇮"; // Flag for Finland
                default:
                    return "🇬🇧"; // Flag for English
            }
        });

        // Add a null check for UI.getCurrent() before setting locale
        Locale currentLocale = (UI.getCurrent() != null) ? UI.getCurrent().getLocale() : Locale.ENGLISH;
        languageSelector.setValue(currentLocale); // Set current UI locale if not null, otherwise fallback to English

        languageSelector.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().setLocale(event.getValue()); // Change locale, triggers localeChange()
            }
        });

        // Set background color to white, text color to black, and cursor to pointer
        languageSelector.getElement().getStyle()
                .set("color", "white") // Set text color to black (or any color you want)
                .set("cursor", "pointer"); // Change mouse cursor to pointer

        add(new HorizontalLayout(languageSelector));
        languageSelector.addClassName("custom-language-selector");
    }




    public void updateTexts() {
        // Update all translatable text
        title.setText(getTranslation("header.title"));
        login.setText(getTranslation("header.login"));

        if (securityService.getAuthenticatedUser() != null) {
            String username = securityService.getAuthenticatedUser().getUsername();
            greeting.setText(getTranslation("header.hello") + " " + username + "!");
            logout.setText(getTranslation("header.logout"));
        }
    }
}
