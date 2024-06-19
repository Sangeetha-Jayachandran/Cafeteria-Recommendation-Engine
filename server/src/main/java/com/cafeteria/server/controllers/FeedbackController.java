package com.cafeteria.server.controllers;

import java.util.List;

import com.cafeteria.server.models.Feedback;
import com.cafeteria.server.services.FeedbackService;

public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController() {
        this.feedbackService = new FeedbackService();
    }

    public void addFeedback(Feedback feedback) {
        feedbackService.addFeedback(feedback);
    }

    public String viewFeedback() {
        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        StringBuilder table = new StringBuilder();
        table.append(String.format("%-20s %-25s %-10s %-25s %-25s\n", "Item ID", "Item Name", "Rating", "Comment", "Date Provided"));
        table.append("--------------------------------------------------------------------------------------------------\n");
        for (Feedback feedback : feedbackList) {
            table.append(String.format("%-20s %-25s %-10s %-25s %-25s\n",
                    feedback.getItemId(),
                    feedback.getItemName(),
                    feedback.getRating(),
                    feedback.getComment(),
                    feedback.getDateProvided().toString()));
        }
        return table.toString();
    }
}
