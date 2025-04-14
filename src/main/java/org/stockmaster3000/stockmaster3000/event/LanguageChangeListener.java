package org.stockmaster3000.stockmaster3000.event;

import java.util.Locale;

public interface LanguageChangeListener {
  void onLanguageChange(Locale newLocale);
}
