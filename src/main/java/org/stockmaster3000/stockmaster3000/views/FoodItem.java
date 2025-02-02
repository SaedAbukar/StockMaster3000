package org.stockmaster3000.stockmaster3000.views;

public class FoodItem {
    private String name;
    private String type;
    private String location;
    private String quantity;
    private String lastUpdated;

    public FoodItem(String name, String type, String location, String quantity, String lastUpdated) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.quantity = quantity;
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }
}