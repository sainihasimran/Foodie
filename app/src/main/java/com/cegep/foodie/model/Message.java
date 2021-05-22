package com.cegep.foodie.model;

public class Message {

    private String email;

    private String message;

    private long timestamp;

    public Message() {

    }

    public Message(String email, String message,  long timestamp) {
        this.email = email;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
