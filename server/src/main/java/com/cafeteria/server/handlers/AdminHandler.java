package com.cafeteria.server.handlers;

import com.cafeteria.server.controllers.MenuItemController;
import com.cafeteria.server.controllers.NotificationController;
import com.cafeteria.server.controllers.UserController;
import com.cafeteria.server.models.MenuItem;
import com.cafeteria.server.models.Notification;
import com.cafeteria.server.models.User;
import com.cafeteria.server.utils.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class AdminHandler {
    private final UserController userController;
    private final MenuItemController menuItemController;
    private final NotificationController notificationController;
    private final String username;

    public AdminHandler(UserController userController, MenuItemController menuItemController, NotificationController notificationController, String username) {
        this.userController = userController;
        this.menuItemController = menuItemController;
        this.notificationController = notificationController;
        this.username = username;
    }

    public void handle(ObjectInputStream in, ObjectOutputStream out, User adminUser) throws IOException, ClassNotFoundException {
        out.writeObject("\nLogin successful. Welcome, Admin.");
        Logger.log(adminUser.getUsername(), "Logged in");

        while (true) {
            out.writeObject("\nADMIN MENU:\n1. Register a user\n2. Add Menu Item\n3. Update Menu Item\n4. Delete Menu Item\n5. View Menu\n6. Logout");
            int adminChoice = (Integer) in.readObject();

            switch (adminChoice) {
                case 1:
                    registerUser(in, out);
                    break;
                case 2:
                    addMenuItem(in, out);
                    break;
                case 3:
                    updateMenuItem(in, out);
                    break;
                case 4:
                    deleteMenuItem(in, out);
                    break;
                case 5:
                    viewMenu(out);
                    break;
                case 6:
                    out.writeObject("Logged out successfully.");
                    return;
                default:
                    out.writeObject("Invalid choice.");
            }
        }
    }

    private void registerUser(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        out.writeObject("Enter new username:");
        String newUsername = (String) in.readObject();
        out.writeObject("Enter new password:");
        String newPassword = (String) in.readObject();
        out.writeObject("Enter role (admin/chef/employee):");
        String newRole = (String) in.readObject();
        userController.addUser(new User(newUsername, newPassword, newRole));
        out.writeObject("User registered successfully.");
        Logger.log(username, "Registered new user: " + newUsername + " with role: " + newRole);
    }

    private void addMenuItem(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        out.writeObject("Enter item name:");
        String itemName = (String) in.readObject();
        out.writeObject("Enter category (breakfast/lunch/dinner):");
        String category = (String) in.readObject();
        out.writeObject("Enter price:");
        Double price = (Double) in.readObject();
        out.writeObject("Enter availability (true/false):");
        Boolean availability = (Boolean) in.readObject();
        menuItemController.addMenuItem(new MenuItem(itemName, category, price, availability));
        out.writeObject("Menu item added successfully.");
        notifyChefs("New food item added ", itemName);
        Logger.log(username, "Added menu item: " + itemName + " in category: " + category);
    }

    private void updateMenuItem(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        out.writeObject("Enter item ID to update:");
        Integer updateItemId = (Integer) in.readObject();
        out.writeObject("Enter new item name:");
        String updateItemName = (String) in.readObject();
        out.writeObject("Enter new category (breakfast/lunch/dinner):");
        String updateCategory = (String) in.readObject();
        out.writeObject("Enter new price:");
        Double updatePrice = (Double) in.readObject();
        out.writeObject("Enter new availability (true/false):");
        Boolean updateAvailability = (Boolean) in.readObject();
        menuItemController.updateMenuItem(new MenuItem(updateItemId, updateItemName, updateCategory, updatePrice, updateAvailability));
        out.writeObject("Menu item updated successfully.");
        notifyChefs("New food item added ", updateItemName);
        Logger.log(username, "Updated menu item ID: " + updateItemId + " to new name: " + updateItemName);
    }

    private void deleteMenuItem(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        out.writeObject("Enter item ID to delete:");
        Integer deleteItemId = (Integer) in.readObject();
        menuItemController.deleteMenuItem(deleteItemId);
        out.writeObject("Menu item deleted successfully.");
        Logger.log(username, "Deleted menu item ID: " + deleteItemId);
    }

    private void viewMenu(ObjectOutputStream out) throws IOException {
        String menu = menuItemController.viewMenu();
        out.writeObject(menu);
        Logger.log(username, "Viewed menu");
    }

    private void notifyChefs(String message, String itemName) {
        List<Integer> chefIds = notificationController.getChefIds();
        Logger.log(username, "Chef IDs: " + chefIds.toString());

        for (Integer chefId : chefIds) {
            Notification notification = new Notification(chefId, (message + itemName), true);
            notificationController.addNotification(notification);
            Logger.log(username, "Notification sent to chef ID: " + chefId + " with message: " + message + itemName);
        }
    }
}
