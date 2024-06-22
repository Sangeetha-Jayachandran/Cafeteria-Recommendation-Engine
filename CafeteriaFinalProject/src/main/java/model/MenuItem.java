package model;

public class MenuItem {
    private int itemId;
    private String itemName;
    private String itemType;
    private double price;
    private boolean availability;

    public MenuItem(int itemId, String itemName, String itemType, double price, boolean availability) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.price = price;
        this.availability = availability;
    }

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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
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

    @Override
    public String toString() {
        return "MenuItem{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", price=" + price +
                ", availability=" + availability +
                '}';
    }
}
