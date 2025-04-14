package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.stockmaster3000.stockmaster3000.components.HeaderComponent;
import org.stockmaster3000.stockmaster3000.security.SecurityService;


@Route(value = "/")
@PageTitle("Stock Master 3000")
@AnonymousAllowed
public class PreMainView extends VerticalLayout implements LocaleChangeObserver {

  private final SecurityService securityService;
  private H1 heroTitle;
  private Paragraph heroDescription;
  private Button inventoryButton;
  private HeaderComponent headerComponent;

  public PreMainView(@Autowired SecurityService securityService) {
    this.securityService = securityService;
    this.headerComponent = new HeaderComponent(securityService);
    add(headerComponent);
    createHeroSection();
  }

  private void createHeroSection() {
    VerticalLayout heroSection = new VerticalLayout();
    heroSection.addClassName("hero-section");
    heroSection.setWidthFull();
    heroSection.setAlignItems(FlexComponent.Alignment.CENTER);
    heroSection.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

    heroTitle = new H1(getTranslation("hero-title"));
    heroDescription = new Paragraph(getTranslation("hero-description"));
    inventoryButton = new Button(getTranslation("hero.inventoryButton"));
    inventoryButton.addClickListener(click -> inventoryButton.getUI().ifPresent(
        ui -> ui.navigate("/main")));

    heroSection.add(heroTitle, heroDescription, inventoryButton);
    add(heroSection);
  }

  @Override
  public void localeChange(LocaleChangeEvent localeChangeEvent) {
    // Update text when the locale changes
    heroTitle.setText(getTranslation("hero.title"));
    heroDescription.setText(getTranslation("hero.description"));
    inventoryButton.setText(getTranslation("hero.inventoryButton"));
    headerComponent.updateTexts();
  }
}
