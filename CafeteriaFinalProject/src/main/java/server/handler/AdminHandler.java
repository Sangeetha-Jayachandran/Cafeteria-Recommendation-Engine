package server.handler;

import controller.MenuItemController;
import controller.UserController;
import model.MenuItem;
import model.User;
import util.Logger;

import java.io.PrintWriter;
import java.util.List;

public class AdminHandler {
    private MenuItemController menuItemController= new MenuItemController();
    private UserController userController = new UserController();

    public void handle(String command, PrintWriter writer) {
        String[] parts = command.split(",", 2);
        String action = parts[0].split(" ")[1];

        switch (action) {
            case "REGISTER_USER":
            	registerUser(parts[1], writer);
			    break;
		    case "VIEW_USERS":
			    viewUsers(writer);
			    break;
		     case "VIEW_MENU":
                viewMenu(writer);
                break;
             case "ADD_MENU_ITEM":
                addMenuItem(parts[1], writer);
                break;
             case "UPDATE_MENU_ITEM":
                updateMenuItem(parts[1], writer);
                break;
             case "DELETE_MENU_ITEM":
                deleteMenuItem(parts[1], writer);
                break;
             default:
                writer.println("Invalid admin command.");
                writer.println("END_OF_RESPONSE");
                break;
        }
    }
    
    private void registerUser(String commandPart, PrintWriter writer) {
        String[] userParts = commandPart.split(",");
        if (userParts.length < 3) {
            sendInvalidCommandFormatMessage(writer, "REGISTER_USER <username> <password> <role_id>");
            return;
        }
        String username = userParts[0];
        String password = userParts[1];
        int roleId = Integer.parseInt(userParts[2]);
        User newUser = new User(0, username, password, roleId);
        userController.createUser(newUser);
        sendSuccessMessage(writer, "User registered");
        Logger.log("Admin registered a new user.");
    }

    private void viewUsers(PrintWriter writer) {
        List<User> users = userController.getAllUsers();
        if (users.isEmpty()) {
            writer.println("No users available.");
        } else {
            StringBuilder response = new StringBuilder("\nUsers:\n");
            response.append(String.format("\n%-5s %-20s %-20s\n", "ID", "Username", "Role Name"));
            response.append("-----------------------------------------\n");
            for (User user : users) {
                response.append(String.format("%-5d %-20s %-20s\n", user.getUserId(), user.getUsername(), user.getRoleName()));
            }
            writer.println(response.toString());
        }
        writer.println("END_OF_RESPONSE");
        Logger.log("Admin viewed the users.");
    }

    private void viewMenu(PrintWriter writer) {
        List<MenuItem> menuItems = menuItemController.getAllMenuItems();
        if (menuItems.isEmpty()) {
            writer.println("No menu items available.");
        } else {
            StringBuilder response = new StringBuilder("\nMenu Items:\n");
            response.append(String.format("\n%-5s %-20s %-10s %-10s %-15s\n", "ID", "Name", "Type", "Price", "Availability"));
            response.append("---------------------------------------------------------------\n");
            for (MenuItem menuItem : menuItems) {
                response.append(String.format("%-5d %-20s %-10s %-10.2f %-15s\n",
                        menuItem.getItemId(), menuItem.getItemName(), menuItem.getItemType(),
                        menuItem.getPrice(), menuItem.isAvailability() ? "Available" : "Not Available"));
            }
            writer.println(response.toString());
        }
        writer.println("END_OF_RESPONSE");
        Logger.log("Admin viewed the menu.");
    }

    private void addMenuItem(String commandPart, PrintWriter writer) {
        String[] parts = commandPart.split(",");
        if (parts.length < 4) {
            sendInvalidCommandFormatMessage(writer, "ADD_MENU_ITEM <name> <type> <price> <availability>");
            return;
        }
        String itemName = parts[0];
        String itemType = parts[1];
        double price = Double.parseDouble(parts[2]);
        boolean availability = Boolean.parseBoolean(parts[3]);
        MenuItem newMenuItem = new MenuItem(0, itemName, itemType, price, availability);
        menuItemController.createMenuItem(newMenuItem);
        sendSuccessMessage(writer, "Menu item added");
        Logger.log("Admin added a new menu item.");
    }

    private void updateMenuItem(String commandPart, PrintWriter writer) {
        String[] parts = commandPart.split(",");
        if (parts.length < 5) {
            sendInvalidCommandFormatMessage(writer, "UPDATE_MENU_ITEM <id> <name> <type> <price> <availability>");
            return;
        }
        int itemId = Integer.parseInt(parts[0]);
        String itemName = parts[1];
        String itemType = parts[2];
        double price = Double.parseDouble(parts[3]);
        boolean availability = Boolean.parseBoolean(parts[4]);
        MenuItem updatedMenuItem = new MenuItem(itemId, itemName, itemType, price, availability);
        menuItemController.updateMenuItem(updatedMenuItem);
        sendSuccessMessage(writer, "Menu item updated");
        Logger.log("Admin updated a menu item.");
    }

    private void deleteMenuItem(String commandPart, PrintWriter writer) {
        try {
            int itemId = Integer.parseInt(commandPart);
            menuItemController.deleteMenuItem(itemId);
            sendSuccessMessage(writer, "Menu item deleted");
            Logger.log("Admin deleted a menu item.");
        } catch (NumberFormatException e) {
            sendInvalidCommandFormatMessage(writer, "DELETE_MENU_ITEM <id>");
        }
    }
    
    private void sendInvalidCommandFormatMessage(PrintWriter writer, String correctFormat) {
        writer.println("Invalid command format. Use: " + correctFormat);
        writer.println("END_OF_RESPONSE");
    }

    private void sendSuccessMessage(PrintWriter writer, String action) {
        writer.println(action + " successful.");
        writer.println("END_OF_RESPONSE");
    }
}
