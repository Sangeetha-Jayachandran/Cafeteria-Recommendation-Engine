// src/main/java/controller/FeedbackController.java

package controller;

import model.Feedback;
import service.FeedbackService;

import java.util.List;

public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController() {
        this.feedbackService = new FeedbackService();
    }

    public void addFeedback(Feedback feedback) {
        feedbackService.addFeedback(feedback);
    }

    public List<Feedback> getFeedbacksForItem(int itemId) {
        return feedbackService.getFeedbacksForItem(itemId);
    }
    
    public List<Feedback> getAllFeedback() {
        return feedbackService.getAllFeedback();
    }
}
