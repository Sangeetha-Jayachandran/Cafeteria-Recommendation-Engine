package com.cafeteria.server.services;

import com.cafeteria.server.DatabaseConnection;
import com.cafeteria.server.models.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    public void addNotification(Notification notification) {
        String query = "INSERT INTO notifications (user_id, message, date_sent, is_active) VALUES (?, ?, CURDATE(), ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, notification.getUserId());
            pstmt.setString(2, notification.getMessage());
            pstmt.setBoolean(3, notification.isActive());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getActiveNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notifications WHERE user_id = ? AND date_sent = CURDATE() AND is_active = TRUE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(new Notification(
                            rs.getInt("notification_id"),
                            rs.getInt("user_id"),
                            rs.getString("message"),
                            rs.getDate("date_sent"),
                            rs.getBoolean("is_active")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public void deleteOldNotifications() {
        String query = "DELETE FROM notifications WHERE date_sent < CURDATE()";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getChefIds() {
        List<Integer> chefIds = new ArrayList<>();
        String query = "SELECT user_id FROM users WHERE role = 'chef'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                chefIds.add(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chefIds;
    }

    public List<Integer> getEmployeeIds() {
        List<Integer> employeeIds = new ArrayList<>();
        String query = "SELECT user_id FROM users WHERE role = 'employee'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                employeeIds.add(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeIds;
    }

    public void deactivateChefNotifications(int chefId) {
        String query = "UPDATE notifications SET is_active = FALSE WHERE user_id = ? AND is_active = TRUE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, chefId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearEmployeeNotifications() {
        String query = "DELETE FROM notifications WHERE user_id IN (SELECT user_id FROM users WHERE role = 'employee')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
