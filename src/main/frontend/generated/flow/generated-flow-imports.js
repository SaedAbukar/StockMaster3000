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
  if (key === 'a1937aa60de8769fb8cf511465edecca24a6ac3cd35d0ae1045169296020a920') {
    pending.push(import('./chunks/chunk-ad9fb9f1d4d0c583af830269d082365b88f82750c53c23ee16ca586a72b72a83.js'));
  }
  if (key === 'ca9bd51a380521522accefd702825df3f99187565565f7dfc924892bd962e067') {
    pending.push(import('./chunks/chunk-f993328803ee7ddcfbe2b28fd13c514af764642aa2628dc06c4e42d483d0b7f3.js'));
  }
  if (key === '046d813b669ff911d58ce4f124427dc919a7d904adb8abc8a2cb573b8a7e0315') {
    pending.push(import('./chunks/chunk-eb52879258ba5d4e4eb49390beddb09f0bc7fb2335334ebd0e4ea9bd558b3ea9.js'));
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