package org.stockmaster3000.stockmaster3000;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the StockMaster 3000 Spring Boot application.
 *
 * <p>This class sets up the application shell configuration, initializes
 * the Vaadin PWA (Progressive Web App) configuration, and applies a custom theme.
 */
@SpringBootApplication
@PWA(name = "StockMaster 3000", shortName = "StockMaster")
@Theme("my-theme")  // Ensure the theme is applied
public class Stockmaster3000Application implements AppShellConfigurator {

  /**
   * Main method that launches the Spring Boot application.
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(Stockmaster3000Application.class, args);
  }
}
