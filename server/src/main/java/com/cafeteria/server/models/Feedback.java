package com.cafeteria.server.models;

import java.util.Date;

public class Feedback {
    private int feedbackId;
    private int userId;
    private int itemId;
    private String itemName;
    private int rating;
    private String comment;
    private Date dateProvided;
    
    public Feedback(int userId, int itemId, int rating, String comment) {
        this.userId = userId;
        this.itemId = itemId;
        this.rating = rating;
        this.comment = comment;
    }

    public Feedback(int feedbackId, int userId, int itemId, String itemName, int rating, String comment, Date dateProvided) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.rating = rating;
        this.comment = comment;
        this.dateProvided = dateProvided;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public int getUserId() {
        return userId;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Date getDateProvided() {
        return dateProvided;
    }
}
