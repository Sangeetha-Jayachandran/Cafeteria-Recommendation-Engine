package com.cafeteria.server.services;

import com.cafeteria.server.DatabaseConnection;
import com.cafeteria.server.models.MenuItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RecommendationService {

    public void addRecommendation(List<Integer> itemIds, int chefId) {
        String query = "INSERT INTO recommendations (chef_id, item_id, date_recommended) VALUES (?, ?, CURDATE())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int itemId : itemIds) {
                pstmt.setInt(1, chefId);
                pstmt.setInt(2, itemId);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String viewRecommendations(Integer chefId) {
        StringBuilder recommendations = new StringBuilder();
        String breakfast = "Breakfast Recommendations:\n";
        String lunch = "Lunch Recommendations:\n";
        String dinner = "Dinner Recommendations:\n";

        Set<Integer> seenItemIds = new HashSet<>();
        Set<String> seenItemNames = new HashSet<>();

        String query = "SELECT r.recommendation_id, r.date_recommended, mi.item_id, mi.item_name, mi.category " +
                       "FROM recommendations r " +
                       "JOIN menu_items mi ON r.item_id = mi.item_id " +
                       "WHERE r.date_recommended = CURDATE() AND r.chef_id = ? " +
                       "ORDER BY mi.category";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, chefId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    String itemName = rs.getString("item_name");

                    if (seenItemIds.contains(itemId) || seenItemNames.contains(itemName)) {
                        continue;
                    }

                    seenItemIds.add(itemId);
                    seenItemNames.add(itemName);

                    String item = "Recommendation ID: " + rs.getInt("recommendation_id") +
                                  ", Date: " + rs.getDate("date_recommended") +
                                  ", Item: " + itemName +
                                  "\n";

                    switch (rs.getString("category")) {
                        case "breakfast":
                            breakfast += item;
                            break;
                        case "lunch":
                            lunch += item;
                            break;
                        case "dinner":
                            dinner += item;
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        recommendations.append(breakfast).append("\n")
                       .append(lunch).append("\n")
                       .append(dinner);
        return recommendations.toString();
    }

    public String viewTopVotedItems() {
        StringBuilder topVotedItems = new StringBuilder();
        String query = "SELECT mi.item_name, mi.category, COUNT(r.recommendation_id) AS votes " +
                       "FROM recommendations r " +
                       "JOIN menu_items mi ON r.item_id = mi.item_id " +
                       "GROUP BY r.item_id " +
                       "ORDER BY votes DESC " +
                       "LIMIT 2";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                topVotedItems.append("Item: ").append(rs.getString("item_name"))
                        .append(", Category: ").append(rs.getString("category"))
                        .append(", Votes: ").append(rs.getInt("votes")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topVotedItems.toString();
    }

    public void voteForItem(int userId, int itemId) {
        String query = "INSERT INTO votes (user_id, item_id, date_voted) VALUES (?, ?, CURDATE())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, itemId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MenuItem> getTopItems(String category) {
        List<MenuItem> topItems = new ArrayList<>();
        String query = "SELECT f.item_id, m.item_name, AVG(f.rating) as avg_rating, COUNT(f.comment) as comment_count, " +
                       "GROUP_CONCAT(f.comment SEPARATOR ' ') as comments " +
                       "FROM feedback f " +
                       "JOIN menu_items m ON f.item_id = m.item_id " +
                       "WHERE m.category = ? " +
                       "GROUP BY f.item_id, m.item_name " +
                       "ORDER BY avg_rating DESC, comment_count DESC " +
                       "LIMIT 10";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, category);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    String itemName = rs.getString("item_name");
                    double avgRating = rs.getDouble("avg_rating");
                    String comments = rs.getString("comments");
                    String sentiment = analyzeSentiment(comments);

                    // Add the MenuItem to the list
                    topItems.add(new MenuItem(itemId, itemName, category, avgRating, sentiment, true));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topItems;
    }

    public List<MenuItem> getLatestRecommendations() {
        List<MenuItem> latestRecommendations = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT m.item_id, m.item_name, m.category, m.price " +
                           "FROM recommendations r " +
                           "JOIN menu_items m ON r.item_id = m.item_id " +
                           "WHERE r.date_recommended = CURDATE()";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                latestRecommendations.add(new MenuItem(resultSet.getInt("item_id"),
                                                       resultSet.getString("item_name"),
                                                       resultSet.getString("category"),
                                                       resultSet.getDouble("price"),
                                                       true));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return latestRecommendations;
    }

    private String analyzeSentiment(String comments) {
        String[] positiveWords = {"good", "great", "excellent", "amazing", "fantastic"};
        String[] negativeWords = {"bad", "poor", "terrible", "awful", "disappointing"};
        int positiveCount = 0;
        int negativeCount = 0;

        for (String word : positiveWords) {
            positiveCount += countOccurrences(comments, word);
        }

        for (String word : negativeWords) {
            negativeCount += countOccurrences(comments, word);
        }

        if (positiveCount > negativeCount) {
            return "Positive";
        } else if (negativeCount > positiveCount) {
            return "Negative";
        } else {
            return "Neutral";
        }
    }

    private int countOccurrences(String text, String word) {
        String[] words = text.split("\\s+");
        int count = 0;
        for (String w : words) {
            if (w.equalsIgnoreCase(word)) {
                count++;
            }
        }
        return count;
    }
}
