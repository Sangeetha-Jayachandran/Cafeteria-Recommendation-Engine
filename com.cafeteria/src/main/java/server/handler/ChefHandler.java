package server.handler;

import controller.MenuItemController;
import controller.FeedbackController;
import model.MenuItem;
import model.Feedback;
import util.Logger;

import java.io.PrintWriter;
import java.util.List;

public class ChefHandler {
    private MenuItemController menuItemController = new MenuItemController();
    private FeedbackController feedbackController = new FeedbackController();

    public void handle(String command, PrintWriter writer) {
        String[] parts = command.split(" ", 2);
        String action = parts[1];

        switch (action) {
            case "VIEW_MENU":
                viewMenu(writer);
                break;
            case "VIEW_FEEDBACK":
                viewFeedback(writer);
                break;
            case "VIEW_RECOMMENDATION":
                writer.println("Viewing recommendations...");
                Logger.log("Chef viewed recommendations.");
                writer.println("END_OF_RESPONSE");
                break;
            case "ROLL_OUT_MENU":
                writer.println("Menu rolled out for next day.");
                Logger.log("Chef rolled out menu for the next day.");
                writer.println("END_OF_RESPONSE");
                break;
            case "VIEW_ROLL_OUT_MENU":
                writer.println("Viewing rolled out menu...");
                Logger.log("Chef viewed rolled out menu.");
                writer.println("END_OF_RESPONSE");
                break;
            default:
                writer.println("Invalid chef command.");
                writer.println("END_OF_RESPONSE");
                break;
        }
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
        Logger.log("Chef viewed the menu.");
    }

    private void viewFeedback(PrintWriter writer) {
        List<Feedback> feedbacks = feedbackController.getAllFeedback();
        if (feedbacks.isEmpty()) {
            writer.println("No feedback available.");
        } else {
            StringBuilder response = new StringBuilder("Feedback:\n");
            response.append(String.format("\n%-5s %-10s %-40s %-5s\n", "ID", "ItemID", "Comment", "Rating"));
            response.append("------------------------------------------------------------------\n");
            for (Feedback feedback : feedbacks) {
                response.append(String.format("%-5d %-10d %-40s %-5d\n",
                        feedback.getFeedbackId(), feedback.getItemId(),
                        feedback.getComment(), feedback.getRating()));
            }
            writer.println(response.toString());
        }
        writer.println("END_OF_RESPONSE");
        Logger.log("Chef viewed feedback.");
    }
}
