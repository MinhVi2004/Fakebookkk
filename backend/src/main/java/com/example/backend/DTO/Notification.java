package com.example.backend.DTO;

public class Notification {
    private String message;
    private Integer senderId;

    public Notification() {}

    public Notification(String message, Integer senderId) {
        this.message = message;
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }
}