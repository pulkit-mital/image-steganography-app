package com.pulkit.imagesteganography.model;

public class MessageEncodingStatus {

    private boolean messageEncoded;
    private int currentMessageIndex;
    private byte[] byteArrayMessage;
    private String message;

    public MessageEncodingStatus(String message, byte[] byteArrayMessage) {
        this.messageEncoded = false;
        this.currentMessageIndex = 0;
        this.byteArrayMessage = byteArrayMessage;
        this.message = message;
    }

    public boolean isMessageEncoded() {
        return messageEncoded;
    }

    public void setMessageEncoded(boolean messageEncoded) {
        this.messageEncoded = messageEncoded;
    }

    public int getCurrentMessageIndex() {
        return currentMessageIndex;
    }

    public void setCurrentMessageIndex(int currentMessageIndex) {
        this.currentMessageIndex = currentMessageIndex;
    }

    public byte[] getByteArrayMessage() {
        return byteArrayMessage;
    }

    public void setByteArrayMessage(byte[] byteArrayMessage) {
        this.byteArrayMessage = byteArrayMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void incrementMessageIndex() {
        currentMessageIndex++;
    }
}
