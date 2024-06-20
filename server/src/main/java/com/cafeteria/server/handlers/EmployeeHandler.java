package com.cafeteria.server.handlers;

import com.cafeteria.server.controllers.*;
import com.cafeteria.server.models.*;
import com.cafeteria.server.utils.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

public class EmployeeHandler {
    private final NotificationController notificationController;
    private final OrderController orderController;
    private final FeedbackController feedbackController;
    private final MenuItemController menuItemController;

    public EmployeeHandler(NotificationController notificationController, RecommendationController recommendationController, OrderController orderController, FeedbackController feedbackController, MenuItemController menuItemController, String username) {
        this.notificationController = notificationController;
        this.orderController = orderController;
        this.feedbackController = feedbackController;
        this.menuItemController = menuItemController;
    }

    public void handle(ObjectInputStream in, ObjectOutputStream out, User employeeUser) throws IOException, ClassNotFoundException {
        out.writeObject("\nLogin successful. Welcome, Employee.");
        Logger.log(employeeUser.getUsername(), "Logged in");
        String notifications = notificationController.viewActiveNotifications(employeeUser.getUserId());
        out.writeObject("Notifications: \n" + notifications);

        displayEmployeeMenu(out, in, employeeUser);
    }

    private void displayEmployeeMenu(ObjectOutputStream out, ObjectInputStream in, User employeeUser) throws IOException, ClassNotFoundException {
        while (true) {
            out.writeObject("\nEMPLOYEE MENU:\n1. View Notifications\n2. Order\n3. Give Feedback\n4. Logout");
            int employeeChoice = (int) in.readObject();

            switch (employeeChoice) {
                case 1:
                    viewNotifications(out, employeeUser);
                    break;
                case 2:
                    placeOrder(in, out, employeeUser);
                    break;
                case 3:
                    giveFeedback(in, out, employeeUser);
                    break;
                case 4:
                    logoutEmployee(out, employeeUser);
                    return;
                default:
                    out.writeObject("Invalid choice.");
            }
        }
    }

    private void viewNotifications(ObjectOutputStream out, User employeeUser) throws IOException {
        String notifications = notificationController.viewActiveNotifications(employeeUser.getUserId());
        out.writeObject(notifications);
        Logger.log(employeeUser.getUsername(), "Viewed notifications");
    }

    private void placeOrder(ObjectInputStream in, ObjectOutputStream out, User employeeUser) throws IOException, ClassNotFoundException {
        int orderId = (int) in.readObject();
        if (orderId != 0) {
            orderController.addOrder(new Order(employeeUser.getUserId(), orderId, new Date()));
            out.writeObject("Order placed successfully.");
            Logger.log(employeeUser.getUsername(), "Placed order for item ID: " + orderId);
        }
    }

    private void giveFeedback(ObjectInputStream in, ObjectOutputStream out, User employeeUser) throws IOException, ClassNotFoundException {
        String menu = menuItemController.viewMenu();
        out.writeObject(menu);
        int itemId = (int) in.readObject();
        if (itemId != 0) {
            if (orderController.hasOrdered(employeeUser.getUserId(), itemId)) {
                out.writeObject("Enter rating (1-5):");
                int rating = (int) in.readObject();
                out.writeObject("Enter comment:");
                String comment = (String) in.readObject();
                feedbackController.addFeedback(new Feedback(employeeUser.getUserId(), itemId, rating, comment));
                out.writeObject("Feedback submitted successfully.");
                Logger.log(employeeUser.getUsername(), "Submitted feedback for item ID: " + itemId);
            } else {
                out.writeObject("You can only give feedback on items you have ordered.");
                Logger.log(employeeUser.getUsername(), "Attempted to give feedback on item not ordered: " + itemId);
            }
        }
    }

    private void logoutEmployee(ObjectOutputStream out, User employeeUser) throws IOException {
        out.writeObject("Logged out successfully.");
        Logger.log(employeeUser.getUsername(), "Logged out");
    }
}
