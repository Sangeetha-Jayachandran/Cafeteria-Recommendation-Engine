package com.cafeteria.server;

import com.cafeteria.server.controllers.*;
import com.cafeteria.server.services.RecommendationService;
import com.cafeteria.server.handlers.*;
import com.cafeteria.server.models.User;
import com.cafeteria.server.utils.Logger;

import java.io.*;
import java.net.Socket;
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
                out.writeObject("\nWelcome to Cafeteria!\n\nChoose your role:\n1. ADMIN\n2. CHEF\n3. EMPLOYEE\n4. EXIT");
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
        User adminUser = authenticateUser(in, out);
        if (adminUser == null || !"admin".equals(adminUser.getRole())) {
            out.writeObject("Invalid admin credentials.");
            Logger.log("Unknown Admin", "Failed login attempt");
            return;
        }
        AdminHandler adminHandler = new AdminHandler(userController, menuItemController, notificationController, adminUser.getUsername());
        adminHandler.handle(in, out, adminUser);
        Logger.log(adminUser.getUsername(), "Logged out");
    }

    private void handleChef(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        User chefUser = authenticateUser(in, out);
        if (chefUser == null || !"chef".equals(chefUser.getRole())) {
            out.writeObject("Invalid chef credentials.");
            Logger.log("Unknown Chef", "Failed login attempt");
            return;
        }
        ChefHandler chefHandler = new ChefHandler(recommendationController, feedbackController, notificationController, menuItemController, chefUser.getUsername());
        chefHandler.handle(in, out, chefUser);
        Logger.log(chefUser.getUsername(), "Logged out");
    }

    private void handleEmployee(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        User employeeUser = authenticateUser(in, out);
        if (employeeUser == null || !"employee".equals(employeeUser.getRole())) {
            out.writeObject("Invalid employee credentials.");
            Logger.log("Unknown Employee", "Failed login attempt");
            return;
        }
        EmployeeHandler employeeHandler = new EmployeeHandler(notificationController, recommendationController, orderController, feedbackController, menuItemController, employeeUser.getUsername());
        employeeHandler.handle(in, out, employeeUser);
        Logger.log(employeeUser.getUsername(), "Logged out");
    }

    private User authenticateUser(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        out.writeObject("Enter username:");
        String username = (String) in.readObject();
        out.writeObject("Enter password:");
        String password = (String) in.readObject();
        return userController.authenticateUser(username, password);
    }

    public static void startCleanupTask() {
        scheduler.scheduleAtFixedRate(ServerThread::deleteOldRecommendations, 0, 24, TimeUnit.HOURS);
    }

    private static void deleteOldRecommendations() {
        RecommendationService.cleanupOldRecommendations();
    }
}
