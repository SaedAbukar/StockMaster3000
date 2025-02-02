package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import org.springframework.beans.factory.annotation.Autowired;
import org.stockmaster3000.stockmaster3000.security.SecurityService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.html.Paragraph;

@Route("/main")
@AnonymousAllowed
public class MainView extends VerticalLayout {
    private Div mainContent;
    private InventoryView inventoryView;
    private InsightsView insightsView;
    private Tabs tabs;

    public MainView(@Autowired SecurityService securityService) {
        setPadding(false);
        setSpacing(false);
        setSizeFull();

        // Show popup on reload
        showPopupOnReload();

        // Create header
        createHeader();

        // Create tabs
        createTabs();

        // Create content area
        createContent();

        // Show inventory by default
        showInventory();
    }

    private void showPopupOnReload() {
        Dialog reloadDialog = new Dialog();

        // Add content to the dialog
        H1 dialogTitle = new H1("Welcome Back!");
        Paragraph dialogMessage = new Paragraph("You have reloaded the page.");
        Button closeButton = new Button("Got it", event -> reloadDialog.close());

        // Style and layout
        reloadDialog.add(dialogTitle, dialogMessage, closeButton);
        reloadDialog.setWidth("400px");
        reloadDialog.setHeight("200px");
        reloadDialog.setModal(true);

        // Open dialog
        reloadDialog.open();
    }

    private void createHeader() {
        // Header container
        Div header = new Div();
        header.addClassName("header");

        // Logo/title on the left
        H1 title = new H1();
        title.addClassName("logo");
        title.getElement().setProperty("innerHTML", "StockMaster<span style='color: #00E676;'>3000</span>");

        // Settings button on the right
        Button settingsButton = new Button(VaadinIcon.COG.create());
        settingsButton.addClassName("settings-button");
        settingsButton.getStyle().set("margin-left", "auto");

        header.add(title, settingsButton);
        add(header);
    }

    private void createTabs() {
        // Create tabs
        Tab inventoryTab = new Tab("Inventory");
        Tab insightsTab = new Tab("Insights");
        Tab aboutTab = new Tab("About");

        tabs = new Tabs(inventoryTab, insightsTab, aboutTab);
        tabs.setWidthFull(); // Stretch tabs across the screen
        tabs.addClassName("nav-tabs");

        // Align tabs to the center
        tabs.getStyle().set("justify-content", "center");
        tabs.getStyle().set("border-bottom", "1px solid var(--primary-color)"); // Add line under tabs
        tabs.getStyle().set("margin-bottom", "0"); // Remove unnecessary spacing

        // Add tab selection listener
        tabs.addSelectedChangeListener(event -> {
            Tab selectedTab = event.getSelectedTab();
            if (selectedTab.equals(inventoryTab)) {
                showInventory();
            } else if (selectedTab.equals(insightsTab)) {
                showInsights();
            } else if (selectedTab.equals(aboutTab)) {
                showAbout();
            }
        });

        // Add tabs to layout
        add(tabs);
    }

    private void createContent() {
        mainContent = new Div();
        mainContent.addClassName("main-content");
        mainContent.setSizeFull();

        // Initialize views
        inventoryView = new InventoryView();
        insightsView = new InsightsView();

        add(mainContent);
    }

    private void showInventory() {
        mainContent.removeAll();
        mainContent.add(inventoryView);
    }

    private void showInsights() {
        mainContent.removeAll();
        mainContent.add(insightsView);
    }

    private void showAbout() {
        mainContent.removeAll();
        Div aboutContent = new Div();
        aboutContent.setText("About Coming Soon");
        mainContent.add(aboutContent);
    }
}
