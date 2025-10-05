package com.ai.assistant.recyclerview;

public class Message {
    private String sender;
    private String content;
    private long timestamp;
    private boolean isMine;

    public Message(String sender, String content, long timestamp, boolean isMine) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.isMine = isMine;
    }
    public String getSender() { return sender; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }
    public boolean isMine() { return isMine; }
}
