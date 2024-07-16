package model;

import java.util.Date;

public class Notification {
    private int notificationId;
    private int userId;
    private String message;
    private Date dateSent;
    private boolean isActive;

    public Notification(int notificationId, int userId, String message, Date dateSent, boolean isActive) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.dateSent = dateSent;
        this.isActive = isActive;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public int getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public boolean isActive() {
        return isActive;
    }
}
