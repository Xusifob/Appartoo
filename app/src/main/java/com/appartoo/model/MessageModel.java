package com.appartoo.model;

/**
 * Created by alexandre on 16-08-16.
 */
public class MessageModel {
    private String id;
    private Long createdTime;
    private String message;
    private String senderName;
    private String senderId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "id='" + id + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", message='" + message + '\'' +
                ", senderId='" + senderId + '\'' +
                '}';
    }
}
