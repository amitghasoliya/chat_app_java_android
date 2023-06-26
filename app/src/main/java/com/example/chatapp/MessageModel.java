package com.example.chatapp;

public class MessageModel {
    private String msgId;
    private String senderId;
    private String message;
    private long timeStamp;

    public MessageModel(String msgId, String senderId, String message, long timeStamp) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public MessageModel() {
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
