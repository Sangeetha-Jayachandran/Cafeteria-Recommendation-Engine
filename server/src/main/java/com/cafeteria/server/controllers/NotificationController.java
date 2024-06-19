package com.cafeteria.server.controllers;

import com.cafeteria.server.models.Notification;
import com.cafeteria.server.services.NotificationService;

import java.util.List;

public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController() {
        this.notificationService = new NotificationService();
    }

    public void addNotification(Notification notification) {
        notificationService.addNotification(notification);
    }

    public String viewActiveNotifications(int userId) {
        List<Notification> notifications = notificationService.getActiveNotifications(userId);
        StringBuilder notificationMessages = new StringBuilder();
        for (Notification notification : notifications) {
            notificationMessages.append(notification.getMessage()).append("\n");
        }
        return notificationMessages.toString();
    }

    public void deleteOldNotifications() {
        notificationService.deleteOldNotifications();
    }

    public List<Integer> getChefIds() {
        return notificationService.getChefIds();
    }

    public List<Integer> getEmployeeIds() {
        return notificationService.getEmployeeIds();
    }

    public void deactivateChefNotifications(int chefId) {
        notificationService.deactivateChefNotifications(chefId);
    }

    public void clearEmployeeNotifications() {
        notificationService.clearEmployeeNotifications();
    }
}
