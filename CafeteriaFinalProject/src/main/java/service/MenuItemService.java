package service;

import model.MenuItem;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemService {
	public void createMenuItem(MenuItem menuItem) {
		String query = "INSERT INTO MenuItems (item_name, item_type, price, availability) VALUES (?, ?, ?, ?)";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, menuItem.getItemName());
			preparedStatement.setString(2, menuItem.getItemType());
			preparedStatement.setDouble(3, menuItem.getPrice());
			preparedStatement.setBoolean(4, menuItem.isAvailability());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<MenuItem> getAllMenuItems() {
		List<MenuItem> menuItems = new ArrayList<>();
		String query = "SELECT * FROM MenuItems";

		try (Connection connection = DatabaseConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {
				int itemId = resultSet.getInt("item_id");
				String itemName = resultSet.getString("item_name");
				String itemType = resultSet.getString("item_type");
				double price = resultSet.getDouble("price");
				boolean availability = resultSet.getBoolean("availability");
				MenuItem menuItem = new MenuItem(itemId, itemName, itemType, price, availability);
				menuItems.add(menuItem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return menuItems;
	}

	public void updateMenuItem(MenuItem menuItem) {
		String query = "UPDATE MenuItems SET item_name = ?, item_type = ?, price = ?, availability = ? WHERE item_id = ?";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, menuItem.getItemName());
			preparedStatement.setString(2, menuItem.getItemType());
			preparedStatement.setDouble(3, menuItem.getPrice());
			preparedStatement.setBoolean(4, menuItem.isAvailability());
			preparedStatement.setInt(5, menuItem.getItemId());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteMenuItem(int itemId) {
		String query = "DELETE FROM MenuItems WHERE item_id = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setInt(1, itemId);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
