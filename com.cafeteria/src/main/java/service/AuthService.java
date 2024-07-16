package service;

import model.User;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    public User authenticate(String username, String password, String role) {
        String query = "SELECT u.user_id, u.username, u.password, u.role_id, r.role_name " +
                       "FROM User u " +
                       "JOIN Role r ON u.role_id = r.role_id " +
                       "WHERE u.username = ? AND u.password = ? AND r.role_name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String retrievedUsername = resultSet.getString("username");
                String retrievedPassword = resultSet.getString("password");
                int roleId = resultSet.getInt("role_id");
                String roleName = resultSet.getString("role_name");
                return new User(userId, retrievedUsername, retrievedPassword, roleId, roleName);
            } else {
                System.out.println("User not found or incorrect credentials.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
