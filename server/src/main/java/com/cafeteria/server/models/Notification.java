package com.cafeteria.server.models;

import java.io.Serializable;
import java.util.Date;

public class Notification implements Serializable {
	private int notificationId;
	private int userId;
	private String message;
	private Date dateSent;
	private boolean isActive;

	public Notification() {
	}

	public Notification(int userId, String message) {
		this.userId = userId;
		this.message = message;
		this.isActive = true;
	}

	public Notification(int notificationId, int userId, String message, Date dateSent, boolean isActive) {
		super();
		this.notificationId = notificationId;
		this.userId = userId;
		this.message = message;
		this.dateSent = dateSent;
		this.isActive = isActive;
	}

	public Notification(int userId, String message, boolean isActive) {
		super();
		this.userId = userId;
		this.message = message;
		this.isActive = isActive;
	}
	public int getNotificationId() {
		return notificationId;
	}
	
	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDateSent() {
		return dateSent;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}
}
