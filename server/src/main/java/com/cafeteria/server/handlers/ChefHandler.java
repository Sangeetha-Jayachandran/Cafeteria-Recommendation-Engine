package com.cafeteria.server.handlers;

import com.cafeteria.server.controllers.*;
import com.cafeteria.server.models.*;
import com.cafeteria.server.utils.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ChefHandler {
    private final RecommendationController recommendationController;
    private final FeedbackController feedbackController;
    private final NotificationController notificationController;
    private final MenuItemController menuItemController;

    public ChefHandler(RecommendationController recommendationController, FeedbackController feedbackController, NotificationController notificationController, MenuItemController menuItemController, String username) {
        this.recommendationController = recommendationController;
        this.feedbackController = feedbackController;
        this.notificationController = notificationController;
        this.menuItemController = menuItemController;
    }

    public void handle(ObjectInputStream in, ObjectOutputStream out, User chefUser) throws IOException, ClassNotFoundException {
        out.writeObject("\nLogin successful. Welcome, Chef.");
        Logger.log(chefUser.getUsername(), "Logged in");
        
        String notifications = notificationController.viewActiveNotifications(chefUser.getUserId());
        out.writeObject("Notifications: \n" + notifications);
        
        while (true) {
            out.writeObject("\nChef Menu:\n1. Add Recommendation\n2. View Rolled Out Menu items\n3. View Feedback\n4. View Menu\n5. Logout");
            int chefChoice = (int) in.readObject();

            switch (chefChoice) {
                case 1:
                    addRecommendation(in, out, chefUser);
                    break;
                case 2:
                    viewRolledOutMenuItems(out, chefUser);
                    break;
                case 3:
                    viewFeedback(out, chefUser);
                    break;
                case 4:
                    viewMenu(out, chefUser);
                    break;
                case 5:
                    logoutChef(out, chefUser);
                    return;
                default:
                    out.writeObject("Invalid choice.");
                    Logger.log(chefUser.getUsername(), "Invalid choice in chef menu");
            }
        }
    }

    private void addRecommendation(ObjectInputStream in, ObjectOutputStream out, User chefUser) throws IOException, ClassNotFoundException {
        notificationController.clearEmployeeNotifications();
        out.writeObject("Top items for recommendation...");

        List<MenuItem> topBreakfastItems = recommendationController.getTopItems("breakfast");
        List<MenuItem> topLunchItems = recommendationController.getTopItems("lunch");
        List<MenuItem> topDinnerItems = recommendationController.getTopItems("dinner");

        out.writeObject("Top Breakfast Items:\n" + menuItemsToString(topBreakfastItems));
        List<Integer> selectedBreakfastItems = readItemIds(in);

        out.writeObject("Top Lunch Items:\n" + menuItemsToString(topLunchItems));
        List<Integer> selectedLunchItems = readItemIds(in);

        out.writeObject("Top Dinner Items:\n" + menuItemsToString(topDinnerItems));
        List<Integer> selectedDinnerItems = readItemIds(in);

        List<Integer> selectedItems = new ArrayList<>();
        selectedItems.addAll(selectedBreakfastItems);
        selectedItems.addAll(selectedLunchItems);
        selectedItems.addAll(selectedDinnerItems);

        recommendationController.addRecommendation(selectedItems, chefUser.getUserId());
        out.writeObject("Recommendations added successfully.");
        notifyEmployees();
        Logger.log(chefUser.getUsername(), "Added recommendations");
    }

    private void viewRolledOutMenuItems(ObjectOutputStream out, User chefUser) throws IOException {
        String recommendations = recommendationController.viewRecommendations(chefUser.getUserId());
        out.writeObject(recommendations);
        Logger.log(chefUser.getUsername(), "Viewed rolled out menu items");
    }

    private void viewFeedback(ObjectOutputStream out, User chefUser) throws IOException {
        String feedback = feedbackController.viewFeedback();
        out.writeObject(feedback);
        Logger.log(chefUser.getUsername(), "Viewed feedback");
    }

    private void viewMenu(ObjectOutputStream out, User chefUser) throws IOException {
        String menu = menuItemController.viewMenu();
        out.writeObject(menu);
        Logger.log(chefUser.getUsername(), "Viewed menu");
    }

    private void logoutChef(ObjectOutputStream out, User chefUser) throws IOException {
        out.writeObject("Logged out successfully.");
        notificationController.deactivateChefNotifications(chefUser.getUserId());
        Logger.log(chefUser.getUsername(), "Logged out");
    }

    private List<Integer> readItemIds(ObjectInputStream in) throws IOException, ClassNotFoundException {
        @SuppressWarnings("unchecked")
        List<Integer> itemIds = (List<Integer>) in.readObject();
        return itemIds;
    }

    private String menuItemsToString(List<MenuItem> items) {
        StringBuilder sb = new StringBuilder();
        for (MenuItem item : items) {
            sb.append("ID: ").append(item.getItemId())
                    .append(", Name: ").append(item.getItemName())
                    .append(", Price: ").append(item.getPrice())
                    .append(", Availability: ").append(item.isAvailability()).append("\n");
        }
        return sb.toString();
    }

    private void notifyEmployees() {
        List<Integer> employeeIds = notificationController.getEmployeeIds();
        List<MenuItem> recommendedItems = recommendationController.getLatestRecommendations();

        StringBuilder notificationMessage = new StringBuilder("Next Day Menu Recommendation:\n");
        for (MenuItem item : recommendedItems) {
            notificationMessage.append("ID: ").append(item.getItemId())
                    .append(", Price: ").append(item.getPrice())
                    .append(", Category: ").append(item.getCategory()).append("\n");
        }

        for (int employeeId : employeeIds) {
            notificationController.addNotification(new Notification(employeeId, notificationMessage.toString(), true));
        }
    }
}
