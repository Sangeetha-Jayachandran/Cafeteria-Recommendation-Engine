package com.cafeteria.server;

import com.cafeteria.server.controllers.*;
import com.cafeteria.server.models.*;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerThread extends Thread {
    private final Socket socket;
    private final UserController userController;
    private final MenuItemController menuItemController;
    private final FeedbackController feedbackController;
    private final RecommendationController recommendationController;
    private final NotificationController notificationController;
    private final OrderController orderController;

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ServerThread(Socket socket) {
        this.socket = socket;
        this.userController = new UserController();
        this.menuItemController = new MenuItemController();
        this.feedbackController = new FeedbackController();
        this.recommendationController = new RecommendationController();
        this.notificationController = new NotificationController();
        this.orderController = new OrderController();
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            while (true) {
                out.writeObject("Welcome! Choose your role:\n1. Admin\n2. Chef\n3. Employee\n4. Exit");
                int roleChoice = (int) in.readObject();

                switch (roleChoice) {
                    case 1:
                        handleAdmin(in, out);
                        break;
                    case 2:
                        handleChef(in, out);
                        break;
                    case 3:
                        handleEmployee(in, out);
                        break;
                    case 4:
                        out.writeObject("Goodbye!");
                        return;
                    default:
                        out.writeObject("Invalid choice. Goodbye!");
                        return;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handleAdmin(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        out.writeObject("Admin Menu:\n1. Login\n2. Exit");
        int adminChoice = (int) in.readObject();

        switch (adminChoice) {
            case 1:
                User adminUser = authenticateUser(in, out);
                if (adminUser != null && "admin".equals(adminUser.getRole())) {
                    out.writeObject("Login successful. Welcome, Admin.");
                    handleAdminActions(in, out, adminUser);
                } else {
                    out.writeObject("Invalid admin credentials.");
                }
                break;
            case 2:
                return;
        }
    }

    private void handleAdminActions(ObjectInputStream in, ObjectOutputStream out, User adminUser) throws IOException, ClassNotFoundException {
        while (true) {
            out.writeObject("Admin Actions:\n1. Register a user\n2. Add Menu Item\n3. Update Menu Item\n4. Delete Menu Item\n5. View Menu\n6. Logout");
            int actionChoice = (int) in.readObject();

            switch (actionChoice) {
                case 1:
                    out.writeObject("Enter new username:");
                    String newUsername = (String) in.readObject();
                    out.writeObject("Enter new password:");
                    String newPassword = (String) in.readObject();
                    out.writeObject("Enter role (admin/chef/employee):");
                    String newRole = (String) in.readObject();
                    userController.addUser(new User(newUsername, newPassword, newRole));
                    out.writeObject("User registered successfully.");
                    break;
                case 2:
                    out.writeObject("Enter item name:");
                    String itemName = (String) in.readObject();
                    out.writeObject("Enter category (breakfast/lunch/dinner):");
                    String category = (String) in.readObject();
                    out.writeObject("Enter price:");
                    double price = (double) in.readObject();
                    out.writeObject("Enter availability (true/false):");
                    boolean availability = (boolean) in.readObject();
                    menuItemController.addMenuItem(new MenuItem(itemName, category, price, availability));
                    out.writeObject("Menu item added successfully.");
                    List<Integer> chefIds = notificationController.getChefIds();
                    for (int chefId : chefIds) {
                        notificationController.addNotification(new Notification(chefId, "New food item added: " + itemName, true));
                    }
                    break;
                case 3:
                    out.writeObject("Enter item ID to update:");
                    int updateItemId = (int) in.readObject();
                    out.writeObject("Enter new item name:");
                    String updateItemName = (String) in.readObject();
                    out.writeObject("Enter new category (breakfast/lunch/dinner):");
                    String updateCategory = (String) in.readObject();
                    out.writeObject("Enter new price:");
                    double updatePrice = (double) in.readObject();
                    out.writeObject("Enter new availability (true/false):");
                    boolean updateAvailability = (boolean) in.readObject();
                    menuItemController.updateMenuItem(new MenuItem(updateItemId, updateItemName, updateCategory, updatePrice, updateAvailability));
                    out.writeObject("Menu item updated successfully.");
                    List<Integer> chefIds1 = notificationController.getChefIds();
                    for (int chefId : chefIds1) {
                        notificationController.addNotification(new Notification(chefId, "Food item availability updated: " + updateItemName, true));
                    }
                    break;
                case 4:
                    out.writeObject("Enter item ID to delete:");
                    int deleteItemId = (int) in.readObject();
                    menuItemController.deleteMenuItem(deleteItemId);
                    out.writeObject("Menu item deleted successfully.");
                    break;
                case 5:
                    String menu = menuItemController.viewMenu();
                    out.writeObject(menu);
                    break;
                case 6:
                    out.writeObject("Logged out successfully.");
                    return;
            }
        }
    }

    private void handleChef(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        User chefUser = authenticateUser(in, out);
        if (chefUser == null || !"chef".equals(chefUser.getRole())) {
            out.writeObject("Invalid chef credentials.");
            return;
        }

        out.writeObject("Login successful. Welcome, Chef.");

        while (true) {
            out.writeObject("Chef Menu:\n1. Add Recommendation\n2. View Rolled Out Menu items\n3. View Feedback\n4. View Menu\n5. Logout");
            int chefChoice = (int) in.readObject();

            switch (chefChoice) {
                case 1:
                    notificationController.clearEmployeeNotifications();
                    out.writeObject("Top items for recommendation...");

                    // Fetch top items for breakfast, lunch, and dinner
                    List<MenuItem> topBreakfastItems = recommendationController.getTopItems("breakfast");
                    List<MenuItem> topLunchItems = recommendationController.getTopItems("lunch");
                    List<MenuItem> topDinnerItems = recommendationController.getTopItems("dinner");

                    // Display top items to the chef
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
                    break;
                case 2:
                    String recommendations = recommendationController.viewRecommendations(chefUser.getUserId());
                    out.writeObject(recommendations);
                    break;
                case 3:
                    String feedback = feedbackController.viewFeedback();
                    out.writeObject(feedback);
                    break;
                case 4:
                    String menu = menuItemController.viewMenu();
                    out.writeObject(menu);
                    break;
                case 5:
                    out.writeObject("Logged out successfully.");
                    notificationController.deactivateChefNotifications(chefUser.getUserId());
                    return;
                default:
                    out.writeObject("Invalid choice.");
            }
        }
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

    private void handleEmployee(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        User employeeUser = authenticateUser(in, out);
        if (employeeUser == null || !"employee".equals(employeeUser.getRole())) {
            out.writeObject("Invalid employee credentials.");
            return;
        }

        out.writeObject("Login successful. Welcome, Employee.");
        String notifications = notificationController.viewActiveNotifications(employeeUser.getUserId());
        out.writeObject("Notifications: " + notifications);

        displayEmployeeMenu(out, in, employeeUser);
    }

    private void displayEmployeeMenu(ObjectOutputStream out, ObjectInputStream in, User employeeUser) throws IOException, ClassNotFoundException {
        while (true) {
            out.writeObject("Employee Menu:\n1. View Notifications\n2. Order\n3. Give Feedback\n4. Logout");
            int employeeChoice = (int) in.readObject();

            switch (employeeChoice) {
                case 1:
                    String notifications = notificationController.viewActiveNotifications(employeeUser.getUserId());
                    out.writeObject(notifications);
                    break;
                case 2:
                    String recommendations = recommendationController.viewRecommendations(employeeUser.getUserId());
                    out.writeObject(recommendations);
                    int orderId = (int) in.readObject();
                    if (orderId != 0) {
                        orderController.addOrder(new Order(employeeUser.getUserId(), orderId, new Date()));
                        out.writeObject("Order placed successfully.");
                    }
                    break;
                case 3:
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
                        } else {
                            out.writeObject("You can only give feedback on items you have ordered.");
                        }
                    }
                    break;
                case 4:
                    out.writeObject("Logged out successfully.");
                    return;
                default:
                    out.writeObject("Invalid choice.");
            }
        }
    }

    private User authenticateUser(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        out.writeObject("Enter username:");
        String username = (String) in.readObject();
        out.writeObject("Enter password:");
        String password = (String) in.readObject();
        return userController.authenticateUser(username, password);
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

    public static void startCleanupTask() {
        scheduler.scheduleAtFixedRate(ServerThread::deleteOldRecommendations, 0, 24, TimeUnit.HOURS);
    }

    private static void deleteOldRecommendations() {
        String query = "DELETE FROM recommendations WHERE date_recommended < CURDATE()";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Deleted " + rowsAffected + " old recommendations.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
