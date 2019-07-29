package com.pulkit.imagesteganography.model;

public class MessageDecodingStatus {

    private String message;
    private boolean ended;

    public MessageDecodingStatus() {
        message = "";
        ended = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded() {
        this.ended = true;
    }
}
