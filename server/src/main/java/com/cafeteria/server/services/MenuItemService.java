package com.cafeteria.server.services;

import com.cafeteria.server.models.Feedback;
import com.cafeteria.server.models.MenuItem;
import com.cafeteria.server.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemService {

    public void addMenuItem(MenuItem menuItem) {
        String query = "INSERT INTO menu_items (item_name, category, price, availability, date_added) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, menuItem.getItemName());
            pstmt.setString(2, menuItem.getCategory());
            pstmt.setDouble(3, menuItem.getPrice());
            pstmt.setBoolean(4, menuItem.isAvailability());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMenuItem(MenuItem menuItem) {
        String query = "UPDATE menu_items SET item_name = ?, category = ?, price = ?, availability = ? WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, menuItem.getItemName());
            pstmt.setString(2, menuItem.getCategory());
            pstmt.setDouble(3, menuItem.getPrice());
            pstmt.setBoolean(4, menuItem.isAvailability());
            pstmt.setInt(5, menuItem.getItemId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMenuItem(int itemId) {
        String query = "DELETE FROM menu_items WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, itemId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MenuItem> getAllMenuItems() {
    	List<MenuItem> menuList = new ArrayList<>();
        String query = "SELECT * FROM menu_items WHERE availability = TRUE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
            	MenuItem menu = new MenuItem(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getBoolean("availability")
                );
            	menuList.add(menu);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuList;
    }
}
