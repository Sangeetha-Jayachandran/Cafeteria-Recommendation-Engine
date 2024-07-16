package service;

import model.MenuItem;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    public void notifyEmployees(List<MenuItem> breakfastItems, List<MenuItem> lunchItems, List<MenuItem> dinnerItems) {
        String query = "INSERT INTO Notification (user_id, item_id) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            List<Integer> employeeIds = getEmployeeIds(connection);

            for (int employeeId : employeeIds) {
                notifyEmployee(employeeId, breakfastItems, preparedStatement);
                notifyEmployee(employeeId, lunchItems, preparedStatement);
                notifyEmployee(employeeId, dinnerItems, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> getEmployeeIds(Connection connection) {
        String query = "SELECT user_id FROM User WHERE role_id = (SELECT role_id FROM Role WHERE role_name = 'EMPLOYEE')";
        List<Integer> employeeIds = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                employeeIds.add(resultSet.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeIds;
    }

    private void notifyEmployee(int employeeId, List<MenuItem> items, PreparedStatement preparedStatement) throws SQLException {
        for (MenuItem item : items) {
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setInt(2, item.getItemId());
            preparedStatement.executeUpdate();
        }
    }

    public List<MenuItem> fetchNotifiedItems(String type) {
        String query = "SELECT m.* FROM Notification n JOIN MenuItem m ON n.item_id = m.item_id WHERE m.item_type = ?";
        List<MenuItem> items = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, type);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int itemId = resultSet.getInt("item_id");
                String itemName = resultSet.getString("item_name");
                String itemType = resultSet.getString("item_type");
                double price = resultSet.getDouble("price");
                boolean availability = resultSet.getBoolean("availability");
                MenuItem menuItem = new MenuItem(itemId, itemName, itemType, price, availability);
                items.add(menuItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    
    public List<MenuItem> getNotifications() {
        List<MenuItem> notifications = new ArrayList<>();
        String query = "SELECT mi.item_id, mi.item_name, mi.item_type, mi.price " +
                       "FROM Notification n JOIN MenuItem mi ON n.item_id = mi.item_id";

        try (Connection connection = DatabaseConnection.getConnection();
        	 PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int itemId = resultSet.getInt("item_id");
                String itemName = resultSet.getString("item_name");
                String itemType = resultSet.getString("item_type");
                double price = resultSet.getDouble("price");

                MenuItem menuItem = new MenuItem(itemId, itemName, itemType, price, true);
                notifications.add(menuItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public void clearNotifications() {
        String query = "DELETE FROM Notification WHERE 1";

        try (Connection connection = DatabaseConnection.getConnection();
        	PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
