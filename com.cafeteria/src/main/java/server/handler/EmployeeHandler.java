package server.handler;

import controller.FeedbackController;
import model.Feedback;
import util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class EmployeeHandler {
    private final FeedbackController feedbackController = new FeedbackController();

    public void handle(String command, PrintWriter writer, BufferedReader reader, int userId) throws IOException {
        String[] parts = command.split(",", 2);
        String action = parts[0];

        switch (action) {
            case "EMPLOYEE VIEW_RECOMMENDATION":
                writer.println("Viewing recommendations for the next day...");
                Logger.log("Employee viewed recommendations for the next day.");
                break;
            case "EMPLOYEE VOTE_MENU_ITEM":
                writer.println("Voted for menu item.");
                Logger.log("Employee voted for menu item.");
                break;
            case "EMPLOYEE GIVE_FEEDBACK":
                handleGiveFeedback(parts[1], writer, userId);
                break;
            default:
                writer.println("Invalid employee command.");
                writer.println("END_OF_RESPONSE");
                break;
        }
    }

    private void handleGiveFeedback(String feedbackData, PrintWriter writer, int userId) {
        String[] parts = feedbackData.split(",");
        if (parts.length != 3) {
            writer.println("Invalid feedback command format. Use: GIVE_FEEDBACK,<item_id>,<comment>,<rating>");
            writer.println("END_OF_RESPONSE");
            return;
        }

        int itemId = Integer.parseInt(parts[0].trim());
        String comment = parts[1].trim();
        int rating = Integer.parseInt(parts[2].trim());

        Feedback feedback = new Feedback(0, itemId, userId, comment, rating);
        feedbackController.addFeedback(feedback);

        writer.println("Feedback submitted successfully.");
        writer.println("END_OF_RESPONSE");
        Logger.log("Employee submitted feedback for item ID: " + itemId);
    }
}
