package org.stockmaster3000.stockmaster3000.views;

import java.time.LocalDate;

public class InventoryItem {
    private String name;
    private String type;
    private int quantity;
    private LocalDate expirationDate;
    
    public InventoryItem(String name, String type, int quantity, LocalDate expirationDate) {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }
    
    // Getters
    public String getName() { return name; }
    public String getType() { return type; }
    public int getQuantity() { return quantity; }
    public LocalDate getExpirationDate() { return expirationDate; }
    
    // Setters
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }
}
