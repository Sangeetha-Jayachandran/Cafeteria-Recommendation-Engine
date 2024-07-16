// src/main/java/model/Feedback.java

package model;

public class Feedback {
    private int feedbackId;
    private int itemId;
    private int userId;
    private String comment;
    private int rating;

    public Feedback(int feedbackId, int itemId, int userId, String comment, int rating) {
        this.feedbackId = feedbackId;
        this.itemId = itemId;
        this.userId = userId;
        this.comment = comment;
        this.rating = rating;
    }

    // getters and setters

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
