package controller;

import model.MenuItem;
import service.NotificationService;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationController {
    NotificationService notificationService = new NotificationService();
	
    public void notifyEmployees(List<MenuItem> breakfastItems, List<MenuItem> lunchItems, List<MenuItem> dinnerItems) {
        notificationService.notifyEmployees(breakfastItems, lunchItems, dinnerItems);
    }

    public List<MenuItem> fetchNotifiedItems(String type) {
        return notificationService.fetchNotifiedItems(type);
    }
    
    public List<MenuItem> getNotifications() {
        return notificationService.getNotifications();
    }
}
