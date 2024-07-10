// src/main/java/service/FeedbackService.java

package service;

import model.Feedback;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackService {
    public void addFeedback(Feedback feedback) {
        String query = "INSERT INTO Feedback (item_id, user_id, comment, rating) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, feedback.getItemId());
            preparedStatement.setInt(2, feedback.getUserId());
            preparedStatement.setString(3, feedback.getComment());
            preparedStatement.setInt(4, feedback.getRating());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Feedback> getFeedbacksForItem(int itemId) {
        List<Feedback> feedbacks = new ArrayList<>();
        String query = "SELECT * FROM Feedback WHERE item_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, itemId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int feedbackId = resultSet.getInt("feedback_id");
                int userId = resultSet.getInt("user_id");
                String comment = resultSet.getString("comment");
                int rating = resultSet.getInt("rating");

                Feedback feedback = new Feedback(feedbackId, itemId, userId, comment, rating);
                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }
    
    public List<Feedback> getAllFeedback() {
        List<Feedback> feedbacks = new ArrayList<>();
        String query = "SELECT * FROM Feedback";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int feedbackId = resultSet.getInt("feedback_id");
                int itemId = resultSet.getInt("item_id");
                int userId = resultSet.getInt("user_id");
                String comment = resultSet.getString("comment");
                int rating = resultSet.getInt("rating");
                Feedback feedback = new Feedback(feedbackId, itemId, userId, comment, rating);
                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }
}
