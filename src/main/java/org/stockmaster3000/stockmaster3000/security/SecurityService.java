package org.stockmaster3000.stockmaster3000.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

/**
 * Security-related utility service for handling authentication and logout.
 *
 * <p>Provides access to the current authenticated user and manages logout behavior
 * integrated with Vaadin and Spring Security.</p>
 */
@Component
public class SecurityService {

  private static final String LOGOUT_SUCCESS_URL = "/";

  /**
   * Returns the currently authenticated user, if available.
   *
   * @return the {@code UserDetails} of the authenticated user, or {@code null} if unauthenticated
   */
  public UserDetails getAuthenticatedUser() {
    SecurityContext context = SecurityContextHolder.getContext();
    Object principal = context.getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
      return (UserDetails) context.getAuthentication().getPrincipal();
    }
    // Anonymous or no authentication.
    return null;
  }

  /**
   * Logs the user out and redirects to the logout success URL.
   */
  public void logout() {
    UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    logoutHandler.logout(
        VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
        null);
  }
}
