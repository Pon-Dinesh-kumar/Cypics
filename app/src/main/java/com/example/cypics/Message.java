package com.example.cypics;

public class Message {
    public static final int TYPE_SENT = 0 ;
    public static final int TYPE_RECEIVED = 1 ;
    private String messageText;
    private int messageUser;
    private long messageTime;
    private String message;

    public Message(String messageText, int messageUser, long messageTime) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = messageTime;
    }

    public Message(String botReply, boolean b) {}

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public int getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(int messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public int getMessageType() {
        return 0;
    }
    public String getMessage() {
        return message;
    }
}

