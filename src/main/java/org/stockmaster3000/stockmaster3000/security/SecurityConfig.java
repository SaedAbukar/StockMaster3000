package org.stockmaster3000.stockmaster3000.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.stockmaster3000.stockmaster3000.views.LoginView;

/**
 * Configuration class for application security settings.
 *
 * <p>Extends {@link VaadinWebSecurity} to integrate Spring Security with Vaadin,
 * sets up authentication details and login view.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurity {

  private final CustomUserDetailsService customUserDetailsService;

  /**
   * Creates a new {@code SecurityConfig} with the custom user details service.
   *
   * @param customUserDetailsService the user details service used for authentication
   */
  @Autowired
  public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
    this.customUserDetailsService = customUserDetailsService;
  }

  /**
   * Configures HTTP security for the application, allowing public access to certain endpoints
   * and setting the login view for authenticated routes.
   *
   * @param http the {@code HttpSecurity} object to configure
   * @throws Exception if an error occurs during configuration
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/public/**"))
        .permitAll());
    super.configure(http);
    setLoginView(http, LoginView.class);
  }

  /**
   * Defines the password encoder bean used to hash user passwords.
   *
   * @return the password encoder instance
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
