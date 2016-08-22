package com.appartoo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by alexandre on 16-08-16.
 */
public class ConversationModel {

    private String id;
    private String owner;
    private Long createdTime;
    private Long lastMessageTime;
    private Integer type;
    private Boolean enabled;
    private HashMap<String, String> offer;
    private HashMap<String, Boolean> hasAnswered;
    private HashMap<String, Boolean> isRead;
    private HashMap<String, Boolean> isTyping;
    private HashMap<String, Boolean> isOnline;
    private HashMap<String, String> participants;
    private HashMap<String, MessageModel> messages;

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

    public HashMap<String, Boolean> getHasAnswered() {
        return hasAnswered;
    }

    public void setHasAnswered(HashMap<String, Boolean> hasAnswered) {
        this.hasAnswered = hasAnswered;
    }

    public HashMap<String, Boolean> getIsRead() {
        return isRead;
    }

    public void setIsRead(HashMap<String, Boolean> isRead) {
        this.isRead = isRead;
    }

    public HashMap<String, Boolean> getIsTyping() {
        return isTyping;
    }

    public void setIsTyping(HashMap<String, Boolean> isTyping) {
        this.isTyping = isTyping;
    }

    public HashMap<String, Boolean> getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(HashMap<String, Boolean> isOnline) {
        this.isOnline = isOnline;
    }

    public HashMap<String, String> getParticipants() {
        return participants;
    }

    public void setParticipants(HashMap<String, String> participants) {
        this.participants = participants;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public HashMap<String, MessageModel> getMessages() {
        return messages;
    }

    public void setMessages(HashMap<String, MessageModel> messages) {
        this.messages = messages;
    }

    public Long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public HashMap<String, String> getOffer() {
        return offer;
    }

    public void setOffer(HashMap<String, String> offer) {
        this.offer = offer;
    }

    public MessageModel getLastMessage() {
        if(messages == null) {
            return null;
        }
        Long tempCreatedTime = 0L;
        MessageModel lastMessage = null;
        for (MessageModel message : messages.values()) {
            if(tempCreatedTime < message.getCreatedTime()) {
                tempCreatedTime = message.getCreatedTime();
                lastMessage = message;
            }
        }
        return lastMessage;
    }

    public String getLastMessageFormattedDate(){
        Long timestamp = lastMessageTime;

        if(String.valueOf(timestamp).length() > 10) {
            int uselessDigits = String.valueOf(timestamp).length() - 10;
            timestamp = (timestamp - (timestamp % ((long) Math.pow(10, uselessDigits))))/((long) Math.pow(10, uselessDigits));
        }

        Calendar messageTime = Calendar.getInstance();
        messageTime.setTimeInMillis(timestamp * 1000);

        Calendar now = Calendar.getInstance();

        SimpleDateFormat timeFormatString = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateTimeFormatString = new SimpleDateFormat("EEE d MMM", Locale.getDefault());

        if(now.get(Calendar.DATE) == messageTime.get(Calendar.DATE)) {
            return timeFormatString.format(messageTime.getTime());
        } else {
            return dateTimeFormatString.format(messageTime.getTime());
        }
    }

    @JsonIgnore
    public String getOwnerName(){
        return participants.get(owner);
    }

    @Override
    public String toString() {
        return "ConversationModel{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", createdTime=" + createdTime +
                ", hasAnswered=" + hasAnswered +
                ", isRead=" + isRead +
                ", isTyping=" + isTyping +
                ", isOnline=" + isOnline +
                ", participants=" + participants +
                ", messages=" + messages +
                ", lastMessageTime=" + lastMessageTime +
                ", type=" + type +
                ", enabled=" + enabled +
                '}';
    }
}
