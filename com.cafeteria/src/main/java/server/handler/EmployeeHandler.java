package server.handler;

import controller.MenuItemController;
import controller.FeedbackController;
import controller.NotificationController;
import model.MenuItem;
import model.Feedback;
import util.Logger;

import java.io.PrintWriter;
import java.util.List;

public class EmployeeHandler {
    private MenuItemController menuItemController = new MenuItemController();
    private FeedbackController feedbackController = new FeedbackController();
    private NotificationController notificationController = new NotificationController();

    public void handle(String command, PrintWriter writer) {
        String[] parts = command.split(",", 2);
        String action = parts[0].split(" ")[1];

        switch (action) {
            case "VIEW_NOTIFICATION":
                viewNotification(writer);
                break;
            case "VIEW_MENU":
                viewMenu(writer);
                break;
            case "GIVE_FEEDBACK":
                if (parts.length == 2) {
                    giveFeedback(parts[1], writer);
                } else {
                    writer.println("Invalid command format. Use: GIVE_FEEDBACK,<item_id>,<comment>,<rating>");
                    writer.println("END_OF_RESPONSE");
                }
                break;
            default:
                writer.println("Invalid employee command.");
                writer.println("END_OF_RESPONSE");
                break;
        }
    }

    private void viewNotification(PrintWriter writer) {
        List<MenuItem> notifications = notificationController.getNotifications();
        if (notifications.isEmpty()) {
            writer.println("No notifications available.");
        } else {
            StringBuilder response = new StringBuilder("Notifications:\n");
            response.append(String.format("\n%-5s %-20s %-10s %-10s\n", "ID", "Name", "Type", "Price"));
            response.append("---------------------------------------------------------------\n");
            for (MenuItem menuItem : notifications) {
                response.append(String.format("%-5d %-20s %-10s %-10.2f\n",
                        menuItem.getItemId(), menuItem.getItemName(),
                        menuItem.getItemType(), menuItem.getPrice()));
            }
            writer.println(response.toString());
        }
        writer.println("END_OF_RESPONSE");
        Logger.log("Employee viewed notifications.");
    }

    private void viewMenu(PrintWriter writer) {
        List<MenuItem> menuItems = menuItemController.getAllMenuItems();
        if (menuItems.isEmpty()) {
            writer.println("No menu items available.");
        } else {
            StringBuilder response = new StringBuilder("Menu Items:\n");
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
        Logger.log("Employee viewed the menu.");
    }

    private void giveFeedback(String commandPart, PrintWriter writer) {
        String[] parts = commandPart.split(",");
        if (parts.length != 3) {
            writer.println("Invalid command format. Use: GIVE_FEEDBACK,<item_id>,<comment>,<rating>");
            writer.println("END_OF_RESPONSE");
            return;
        }

        int itemId;
        int rating;
        try {
            itemId = Integer.parseInt(parts[0]);
            rating = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            writer.println("Invalid item ID or rating. They should be numeric.");
            writer.println("END_OF_RESPONSE");
            return;
        }

        String comment = parts[1];

        Feedback feedback = new Feedback(0, itemId, rating, comment, 0);
        feedbackController.addFeedback(feedback);

        writer.println("Feedback submitted successfully.");
        writer.println("END_OF_RESPONSE");
        Logger.log("Employee gave feedback.");
    }
}
