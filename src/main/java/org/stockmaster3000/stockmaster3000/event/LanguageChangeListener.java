package org.stockmaster3000.stockmaster3000.event;

import java.util.Locale;

/**
 * Listener interface for handling language change events across components.
 */
public interface LanguageChangeListener {

  /**
   * Called when the application's language is changed.
   *
   * @param newLocale the newly selected locale
   */
  void onLanguageChange(Locale newLocale);
}
