package com.cafeteria.server.services;

import com.cafeteria.server.DatabaseConnection;
import com.cafeteria.server.models.Feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackService {

    public void addFeedback(Feedback feedback) {
        String query = "INSERT INTO feedback (user_id, item_id, rating, comment, date_provided) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, feedback.getUserId());
            pstmt.setInt(2, feedback.getItemId());
            pstmt.setInt(3, feedback.getRating());
            pstmt.setString(4, feedback.getComment());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Feedback> getAllFeedback() {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT f.feedback_id, f.user_id, f.item_id, mi.item_name, f.rating, f.comment, f.date_provided " +
                       "FROM feedback f " +
                       "JOIN menu_items mi ON f.item_id = mi.item_id";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Feedback feedback = new Feedback(
                        rs.getInt("feedback_id"),
                        rs.getInt("user_id"),
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("date_provided")
                );
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }
}
