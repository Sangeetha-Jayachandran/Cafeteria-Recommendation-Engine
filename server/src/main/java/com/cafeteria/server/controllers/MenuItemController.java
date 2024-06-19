package com.cafeteria.server.controllers;

import java.util.List;

import com.cafeteria.server.models.MenuItem;
import com.cafeteria.server.services.MenuItemService;

public class MenuItemController {
    private final MenuItemService menuItemService;

    public MenuItemController() {
        this.menuItemService = new MenuItemService();
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItemService.addMenuItem(menuItem);
    }

    public void updateMenuItem(MenuItem menuItem) {
        menuItemService.updateMenuItem(menuItem);
    }

    public void deleteMenuItem(int itemId) {
        menuItemService.deleteMenuItem(itemId);
    }

    public String viewMenu() {
        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
        StringBuilder table = new StringBuilder();
        table.append(String.format("%-10s %-20s %-15s %-10s %-15s\n", "Item ID", "Item Name", "Category", "Price", "Availability"));
        table.append("----------------------------------------------------------------------------\n");
        for (MenuItem item : menuItems) {
            table.append(String.format("%-10d %-20s %-15s %-10.2f %-15s\n",
                    item.getItemId(),
                    item.getItemName(),
                    item.getCategory(),
                    item.getPrice(),
                    item.isAvailability() ? "Available" : "Not Available"));
        }
        return table.toString();
    }
}
