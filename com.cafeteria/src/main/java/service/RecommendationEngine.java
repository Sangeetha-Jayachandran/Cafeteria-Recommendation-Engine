package service;

import model.Feedback;
import model.MenuItem;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class RecommendationEngine {
    private FeedbackService feedbackService = new FeedbackService();
    private MenuItemService menuItemService = new MenuItemService();
    
    public Map<String, List<MenuItem>> generateRecommendations() {
        Map<String, List<MenuItem>> recommendations = new HashMap<>();

        List<MenuItem> breakfastItems = getTopRatedItems("Breakfast");
        List<MenuItem> lunchItems = getTopRatedItems("Lunch");
        List<MenuItem> dinnerItems = getTopRatedItems("Dinner");

        recommendations.put("Breakfast", breakfastItems);
        recommendations.put("Lunch", lunchItems);
        recommendations.put("Dinner", dinnerItems);

        saveRecommendations(breakfastItems);
        saveRecommendations(lunchItems);
        saveRecommendations(dinnerItems);

        return recommendations;
    }

    private List<MenuItem> getTopRatedItems(String type) {
        List<MenuItem> allItems = menuItemService.getAllMenuItemsByType(type);

        Map<MenuItem, Double> itemRatings = new HashMap<>();
        for (MenuItem item : allItems) {
            double averageRating = getAverageRating(item.getItemId());
            if (!Double.isNaN(averageRating)) {
                itemRatings.put(item, averageRating);
            }
        }

        return itemRatings.entrySet().stream()
                .sorted(Map.Entry.<MenuItem, Double>comparingByValue().reversed())
                .limit(8)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private void saveRecommendations(List<MenuItem> items) {
        String query = "INSERT INTO Recommendation (item_id, average_rating) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (MenuItem item : items) {
                preparedStatement.setInt(1, item.getItemId());
                preparedStatement.setDouble(2, getAverageRating(item.getItemId()));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getAverageRating(int itemId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksForItem(itemId);
        if (feedbacks.isEmpty()) return Double.NaN;
        double sum = 0;
        for (Feedback feedback : feedbacks) {
            sum += feedback.getRating();
        }
        return sum / feedbacks.size();
    }
}
