package controller;

import java.util.List;

import model.MenuItem;
import service.MenuItemService;

public class MenuItemController {

	MenuItemService menuItemService;
	
	public MenuItemController() {
		this.menuItemService = new MenuItemService();
	}

	public void createMenuItem(MenuItem menuItem) {
		menuItemService.createMenuItem(menuItem);
    }

    public List<MenuItem> getAllMenuItems() {
    	return menuItemService.getAllMenuItems();
    }

    public void updateMenuItem(MenuItem menuItem) {
    	menuItemService.updateMenuItem(menuItem);
    }

    public void deleteMenuItem(int itemId) {
    	menuItemService.deleteMenuItem(itemId);
    }
}
