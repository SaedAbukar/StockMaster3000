import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/button/theme/lumo/vaadin-button.js';
import '@vaadin/login/theme/lumo/vaadin-login-form.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === 'ca9bd51a380521522accefd702825df3f99187565565f7dfc924892bd962e067') {
    pending.push(import('./chunks/chunk-f993328803ee7ddcfbe2b28fd13c514af764642aa2628dc06c4e42d483d0b7f3.js'));
  }
  if (key === 'ae1ffea947c6d509814069a689a0998f8a7fb38e5035e2d1df1027b6a5b5a208') {
    pending.push(import('./chunks/chunk-1b5142a6413e37d41eb4bbd07c5589cfb47fd5e7959771681ff8cbdbf815745a.js'));
  }
  if (key === 'a1937aa60de8769fb8cf511465edecca24a6ac3cd35d0ae1045169296020a920') {
    pending.push(import('./chunks/chunk-ad9fb9f1d4d0c583af830269d082365b88f82750c53c23ee16ca586a72b72a83.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}