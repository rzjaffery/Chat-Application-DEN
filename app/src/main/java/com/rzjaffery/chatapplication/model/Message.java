package com.rzjaffery.chatapplication.model;

public class Message {
    public String messageId;   // unique id of the message (push key from Firebase)
    public String senderId;    // who sent the message
    public String receiverId;  // who receives it
    public String text;        // actual message text
    public long timestamp;     // time in millis

    public Message() {}

    public Message(String senderId, String receiverId, String text, long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Message(String messageId, String senderId, String receiverId, String text, long timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.timestamp = timestamp;
    }
}
