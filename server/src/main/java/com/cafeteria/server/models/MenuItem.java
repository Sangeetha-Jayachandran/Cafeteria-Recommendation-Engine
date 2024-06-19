package com.cafeteria.server.models;

public class MenuItem {
    private int itemId;
    private String itemName;
    private String category;
    private double price;
    private boolean availability;
    private String sentiment;

    public MenuItem(int itemId, String itemName, String category, double price, boolean availability) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.availability = availability;
    }

    
	public MenuItem(String itemName, String category, double price, boolean availability) {
		super();
		this.itemName = itemName;
		this.category = category;
		this.price = price;
		this.availability = availability;
	}


	public MenuItem(int itemId, String itemName, String category, double price, String sentiment, boolean availability) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.sentiment = sentiment;
        this.availability = availability;
    }

    // Getters and Setters

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
}
