package org.stockmaster3000.stockmaster3000.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.stockmaster3000.stockmaster3000.security.SecurityService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HeaderComponentTest {

    private SecurityService securityService;
    private HeaderComponent headerComponent;

    @BeforeEach
    public void setUp() {
        // Mock the SecurityService to control authentication behavior
        securityService = Mockito.mock(SecurityService.class);
        headerComponent = new HeaderComponent(securityService);
    }

    @Test
    public void hasTitle() {
        // Verify that the header contains the expected title
        H1 title = (H1) headerComponent.getComponentAt(0);
        assertNotNull(title, "Title should not be null");
        assertEquals("StockMaster3000", title.getText(), "Title should match 'StockMaster3000'");
    }
    

    @Test
    public void showsLoginIfNotAuthenticated() {
        // Simulate no authenticated user
        when(securityService.getAuthenticatedUser()).thenReturn(null);
        
        // Reinitialize header to reflect authentication state
        headerComponent = new HeaderComponent(securityService);
        
        HorizontalLayout authSection = (HorizontalLayout) headerComponent.getComponentAt(1);
        Button loginButton = (Button) authSection.getComponentAt(0);
        
        assertNotNull(loginButton, "Login button should be present");
        assertEquals("Login", loginButton.getText(), "Login button should display 'Login'");
        
        // Simulate login button click
        loginButton.click();
    }

    @Test
    public void showsUserAndLogoutIfAuthenticated() {
        // Simulate an authenticated user
        UserDetails mockUser = new User("testUser", "password", Collections.emptyList());
        when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
        
        // Reinitialize header to reflect authentication state
        headerComponent = new HeaderComponent(securityService);
        
        HorizontalLayout authSection = (HorizontalLayout) headerComponent.getComponentAt(1);
        Span greeting = (Span) authSection.getComponentAt(0);
        Button logoutButton = (Button) authSection.getComponentAt(1);
        
        assertNotNull(greeting, "Greeting message should be present");
        assertEquals("Hello, testUser!", greeting.getText(), "Greeting should match username");
        
        assertNotNull(logoutButton, "Logout button should be present");
        assertEquals("Logout", logoutButton.getText(), "Logout button should display 'Logout'");
        
        // Simulate logout button click
        logoutButton.click();
        verify(securityService, times(1)).logout(); // Verify that logout is triggered
    }
}
