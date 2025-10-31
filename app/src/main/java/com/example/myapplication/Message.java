package com.example.myapplication;

public class Message {
    private String sender;
    private String content;
    private long timestamp;
    private boolean isMine; // True if the message is from the current user

    public Message(String sender, String content, long timestamp, boolean isMine) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.isMine = isMine;

    }

    // Getters for all properties
    public String getSender() { return sender; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }
    public boolean isMine() { return isMine; }
}
