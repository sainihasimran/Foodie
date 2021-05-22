package com.cegep.foodie.model;

class Message {

    private String email;

    private String message;

    private String id;

    public Message() {

    }

    public Message(String email, String message, String id) {
        this.email = email;
        this.message = message;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
