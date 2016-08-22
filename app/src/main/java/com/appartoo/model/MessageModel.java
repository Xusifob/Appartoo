package com.appartoo.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    public String getFormattedDate(){

        Long timestamp = createdTime;

        if(String.valueOf(timestamp).length() > 10) {
            int uselessDigits = String.valueOf(timestamp).length() - 10;
            timestamp = (timestamp - (timestamp % ((long) Math.pow(10, uselessDigits))))/((long) Math.pow(10, uselessDigits));
        }

        Calendar now = Calendar.getInstance();

        Calendar messageTime = Calendar.getInstance();
        messageTime.setTimeInMillis(timestamp * 1000);

        SimpleDateFormat timeFormatString = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateTimeFormatString = new SimpleDateFormat("EEE d MMM", Locale.getDefault());

        if(now.get(Calendar.DATE) == messageTime.get(Calendar.DATE)) {
            return timeFormatString.format(messageTime.getTime());
        } else {
            return dateTimeFormatString.format(messageTime.getTime());
        }
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
