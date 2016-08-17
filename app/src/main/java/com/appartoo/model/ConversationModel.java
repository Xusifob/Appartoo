package com.appartoo.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by alexandre on 16-08-16.
 */
public class ConversationModel {

    private String id;
    private String owner;
    private Long createdTime;
    private Long lastMessageTime;
//    private HashMap<String, Boolean> hasAnswered;
//    private HashMap<String, Boolean> isRead;
//    private HashMap<String, Boolean> isTyping;
//    private HashMap<String, Boolean> isOnline;
    private HashMap<String, String> participants;
    private HashMap<String, MessageModel> messages;
    private Integer type;
    private Boolean enabled;

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

//    public HashMap<String, Boolean> getHasAnswered() {
//        return hasAnswered;
//    }
//
//    public void setHasAnswered(HashMap<String, Boolean> hasAnswered) {
//        this.hasAnswered = hasAnswered;
//    }
//
//    public HashMap<String, Boolean> getIsRead() {
//        return isRead;
//    }
//
//    public void setIsRead(HashMap<String, Boolean> isRead) {
//        this.isRead = isRead;
//    }
//
//    public HashMap<String, Boolean> getIsTyping() {
//        return isTyping;
//    }
//
//    public void setIsTyping(HashMap<String, Boolean> isTyping) {
//        this.isTyping = isTyping;
//    }
//
//    public HashMap<String, Boolean> getIsOnline() {
//        return isOnline;
//    }
//
//    public void setIsOnline(HashMap<String, Boolean> isOnline) {
//        this.isOnline = isOnline;
//    }

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

    public MessageModel getLastMessage(){
        if(messages != null && messages.size() > 0) {
            Long tempCreatedTime = 0L;
            MessageModel lastMessage = null;
            for (MessageModel message : messages.values()) {
                if(tempCreatedTime < message.getCreatedTime()) {
                    tempCreatedTime = message.getCreatedTime();
                    lastMessage = message;
                }
            }
            return lastMessage;
        } else {
            return null;
        }
    }

    public Date getLastMessageDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTimeInMillis(createdTime);
        return calendar.getTime();
    }

    public String getOwnerName(){
        for(String key : participants.keySet()){
            if(key.equals(owner)) {
                return participants.get(key);
            }
        }

        return "";
    }

    @Override
    public String toString() {
        return "ConversationModel{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", createdTime=" + createdTime +
//                ", hasAnswered=" + hasAnswered +
//                ", isRead=" + isRead +
//                ", isTyping=" + isTyping +
//                ", isOnline=" + isOnline +
                ", participants=" + participants +
                ", messages=" + messages +
                ", lastMessageTime=" + lastMessageTime +
                ", type=" + type +
                ", enabled=" + enabled +
                '}';
    }
}
