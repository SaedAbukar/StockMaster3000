import { n as g, b as d, x as p, E as f, R as m, K as e, y as b } from "./copilot-CrQlGUim.js";
import { B as $ } from "./base-panel-BPFs0JUr.js";
import { i as o } from "./icons-CenXfqbT.js";
const v = "copilot-shortcuts-panel{font:var(--font-xsmall);padding:var(--space-200);display:flex;flex-direction:column;gap:var(--space-50)}copilot-shortcuts-panel h3{font:var(--font-xsmall-semibold);margin:0;padding:0}copilot-shortcuts-panel h3:not(:first-of-type){margin-top:var(--space-200)}copilot-shortcuts-panel ul{list-style:none;margin:0;padding:0 var(--space-50);display:flex;flex-direction:column}copilot-shortcuts-panel ul li{display:flex;align-items:center;gap:var(--space-150);padding:var(--space-75) 0}copilot-shortcuts-panel ul li:not(:last-of-type){border-bottom:1px dashed var(--border-color)}copilot-shortcuts-panel ul li svg{height:16px;width:16px}copilot-shortcuts-panel ul li .kbds{flex:1;text-align:right}copilot-shortcuts-panel kbd{display:inline-block;border-radius:var(--radius-1);border:1px solid var(--border-color);min-width:1em;min-height:1em;text-align:center;margin:0 .1em;padding:.25em;box-sizing:border-box;font-size:var(--font-size-1);font-family:var(--font-family);line-height:1}", h = window.Vaadin.copilot.tree;
if (!h)
  throw new Error("Tried to access copilot tree before it was initialized.");
var x = Object.defineProperty, w = Object.getOwnPropertyDescriptor, y = (l, a, n, s) => {
  for (var i = s > 1 ? void 0 : s ? w(a, n) : a, r = l.length - 1, c; r >= 0; r--)
    (c = l[r]) && (i = (s ? c(a, n, i) : c(i)) || i);
  return s && i && x(a, n, i), i;
};
let u = class extends $ {
  constructor() {
    super(), this.onTreeUpdated = () => {
      this.requestUpdate();
    };
  }
  connectedCallback() {
    super.connectedCallback(), d.on("copilot-tree-created", this.onTreeUpdated);
  }
  disconnectedCallback() {
    super.disconnectedCallback(), d.off("copilot-tree-created", this.onTreeUpdated);
  }
  render() {
    const l = h.hasFlowComponents();
    return p`<style>
        ${v}
      </style>
      <h3>Global</h3>
      <ul>
        <li>${o.vaadinLogo} Copilot ${t(e.toggleCopilot)}</li>
        <li>${o.terminal} Command window ${t(e.toggleCommandWindow)}</li>
        <li>${o.undo} Undo ${t(e.undo)}</li>
        <li>${o.redo} Redo ${t(e.redo)}</li>
      </ul>
      <h3>Selected component</h3>
      <ul>
        <li>${o.code} Go to source ${t(e.goToSource)}</li>
        ${l ? p`<li>${o.code} Go to attach source ${t(e.goToAttachSource)}</li>` : f}
        <li>${o.copy} Copy ${t(e.copy)}</li>
        <li>${o.paste} Paste ${t(e.paste)}</li>
        <li>${o.duplicate} Duplicate ${t(e.duplicate)}</li>
        <li>${o.userUp} Select parent ${t(e.selectParent)}</li>
        <li>${o.userLeft} Select previous sibling ${t(e.selectPreviousSibling)}</li>
        <li>${o.userRight} Select first child / next sibling ${t(e.selectNextSibling)}</li>
        <li>${o.trash} Delete ${t(e.delete)}</li>
      </ul>`;
  }
};
u = y([
  g("copilot-shortcuts-panel")
], u);
function t(l) {
  return p`<span class="kbds">${m(l)}</span>`;
}
const C = b({
  header: "Keyboard Shortcuts",
  tag: "copilot-shortcuts-panel",
  width: 400,
  height: 550,
  floatingPosition: {
    top: 50,
    left: 50
  }
}), P = {
  init(l) {
    l.addPanel(C);
  }
};
window.Vaadin.copilot.plugins.push(P);
