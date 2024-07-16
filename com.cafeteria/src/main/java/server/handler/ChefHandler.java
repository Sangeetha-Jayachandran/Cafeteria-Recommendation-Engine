// src/main/java/server/handler/ChefHandler.java

package server.handler;

import controller.MenuItemController;
import controller.FeedbackController;
import model.MenuItem;
import model.Feedback;
import service.NotificationService;
import service.RecommendationEngine;
import util.Logger;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChefHandler {
    private MenuItemController menuItemController = new MenuItemController();
    private FeedbackController feedbackController = new FeedbackController();
    private RecommendationEngine recommendationEngine = new RecommendationEngine();
    private NotificationService notificationService = new NotificationService();

    public void handle(String command, PrintWriter writer) {
        String[] parts = command.split(",", 2);
        String action = parts[0].split(" ")[1];

        switch (action) {
            case "VIEW_MENU":
                viewMenu(writer);
                break;
            case "VIEW_FEEDBACK":
                viewFeedback(writer);
                break;
            case "VIEW_RECOMMENDATION":
                viewRecommendation(writer);
                break;
            case "ROLL_OUT_MENU":
                if (parts.length == 2) {
                    rollOutMenu(parts[1], writer);
                } else {
                    writer.println("Invalid command format. Use: ROLL_OUT_MENU,<breakfast_items>,<lunch_items>,<dinner_items>");
                    writer.println("END_OF_RESPONSE");
                }
                break;
            case "VIEW_ROLL_OUT_MENU":
                viewRollOutMenu(writer);
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
            response.append(String.format("\n%-5s %-10s %-50s %-5s\n", "ID", "ItemID", "Comment", "Rating"));
            response.append("---------------------------------------------------------------\n");
            for (Feedback feedback : feedbacks) {
                response.append(String.format("%-5d %-10d %-50s %-5d\n",
                        feedback.getFeedbackId(), feedback.getItemId(),
                        feedback.getComment(), feedback.getRating()));
            }
            writer.println(response.toString());
        }
        writer.println("END_OF_RESPONSE");
        Logger.log("Chef viewed feedback.");
    }

    private void viewRecommendation(PrintWriter writer) {
        Map<String, List<MenuItem>> recommendations = recommendationEngine.generateRecommendations();
        StringBuilder response = new StringBuilder("Recommendations:\n");

        for (String mealType : recommendations.keySet()) {
            response.append(mealType).append(":\n");
            List<MenuItem> items = recommendations.get(mealType);
            if (items.isEmpty()) {
                response.append("No recommendations available.\n");
            } else {
                response.append(String.format("\n%-5s %-20s %-10s %-10s\n", "ID", "Name", "Avg Rating", "Type"));
                response.append("---------------------------------------------------------------\n");
                for (MenuItem menuItem : items) {
                    response.append(String.format("%-5d %-20s %-10.2f %-10s\n",
                            menuItem.getItemId(), menuItem.getItemName(),
                            recommendationEngine.getAverageRating(menuItem.getItemId()), menuItem.getItemType()));
                }
                response.append("\n");
            }
        }

        writer.println(response.toString());
        writer.println("END_OF_RESPONSE");
        Logger.log("Chef viewed recommendations.");
    }

    private void rollOutMenu(String commandPart, PrintWriter writer) {
        String[] parts = commandPart.split(",", 3);
        if (parts.length != 3) {
            writer.println("Invalid command format. Use: ROLL_OUT_MENU,<breakfast_items>,<lunch_items>,<dinner_items>");
            writer.println("END_OF_RESPONSE");
            return;
        }

        List<MenuItem> breakfastItems = getItemsByIds(parts[0]);
        List<MenuItem> lunchItems = getItemsByIds(parts[1]);
        List<MenuItem> dinnerItems = getItemsByIds(parts[2]);

        if (breakfastItems.size() > 5 || lunchItems.size() > 5 || dinnerItems.size() > 5) {
            writer.println("Too many items. Please select up to 5 items for each meal.");
            writer.println("END_OF_RESPONSE");
            return;
        }

        notificationService.notifyEmployees(breakfastItems, lunchItems, dinnerItems);

        writer.println("Menu rolled out for the next day.");
        writer.println("END_OF_RESPONSE");
        Logger.log("Chef rolled out menu for the next day.");
    }

    private List<MenuItem> getItemsByIds(String ids) {
        List<MenuItem> items = new ArrayList<>();
        String[] itemIds = ids.split("\\s*,\\s*");
        for (String itemIdStr : itemIds) {
            try {
                int itemId = Integer.parseInt(itemIdStr);
                MenuItem item = menuItemController.getMenuItemById(itemId);
                if (item != null) {
                    items.add(item);
                }
            } catch (NumberFormatException e) {
                Logger.log("Invalid item ID: " + itemIdStr);
            }
        }
        return items;
    }

    private void viewRollOutMenu(PrintWriter writer) {
        List<MenuItem> breakfastItems = notificationService.fetchNotifiedItems("Breakfast");
        List<MenuItem> lunchItems = notificationService.fetchNotifiedItems("Lunch");
        List<MenuItem> dinnerItems = notificationService.fetchNotifiedItems("Dinner");

        StringBuilder response = new StringBuilder("Rolled Out Menu:\n");

        response.append("Breakfast:\n");
        response.append(displayItems(breakfastItems));

        response.append("Lunch:\n");
        response.append(displayItems(lunchItems));

        response.append("Dinner:\n");
        response.append(displayItems(dinnerItems));

        writer.println(response.toString());
        writer.println("END_OF_RESPONSE");
        Logger.log("Chef viewed rolled out menu.");
    }

    private String displayItems(List<MenuItem> items) {
        StringBuilder response = new StringBuilder();
        if (items.isEmpty()) {
            response.append("No items available.\n");
        } else {
            response.append(String.format("\n%-5s %-20s %-10s %-10s\n", "ID", "Name", "Type", "Price"));
            response.append("---------------------------------------------------------------\n");
            for (MenuItem menuItem : items) {
                response.append(String.format("%-5d %-20s %-10s %-10.2f\n",
                        menuItem.getItemId(), menuItem.getItemName(),
                        menuItem.getItemType(), menuItem.getPrice()));
            }
            response.append("\n");
        }
        return response.toString();
    }
}