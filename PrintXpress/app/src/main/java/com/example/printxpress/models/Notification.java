package com.example.printxpress.models;

public class Notification {
    private int notifId, userId;
    private String title, message, date;
    private boolean isRead;

    public Notification() {}

    public Notification(int userId, String title, String message, String date) {
        this.userId = userId; this.title = title; this.message = message; this.date = date;
    }

    public int getNotifId() { return notifId; }
    public void setNotifId(int notifId) { this.notifId = notifId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
