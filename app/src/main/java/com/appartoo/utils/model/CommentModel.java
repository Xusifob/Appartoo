package com.appartoo.utils.model;

import com.appartoo.utils.Appartoo;

import java.util.Calendar;

/**
 * Created by alexandre on 16-09-16.
 */
public class CommentModel {
    private Integer note;
    private String description;
    private Long createdTime;
    private String givenName;

    public CommentModel(Integer note, String description) {
        this.note = note;
        this.description = description;
        if(Appartoo.LOGGED_USER_PROFILE != null) this.givenName = Appartoo.LOGGED_USER_PROFILE.getGivenName();
        this.createdTime = Calendar.getInstance().getTimeInMillis()/1000;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }
}
