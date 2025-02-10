package org.stockmaster3000.stockmaster3000;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 *
 * This ensures that the custom theme and styles are correctly applied.
 */
@SpringBootApplication
@PWA(name = "StockMaster 3000", shortName = "StockMaster")
@Theme("my-theme")  // Ensure the theme is applied
public class Stockmaster3000Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Stockmaster3000Application.class, args);
    }
}
