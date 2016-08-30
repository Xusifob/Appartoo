package com.appartoo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-08-30.
 */
public class UserPresentationModel {

    @SerializedName("@id")
    private String id;
    private String relationshipStatus;
    private String familyName;
    private String givenName;
    private String address;
    private String description;
    private String gender;
    private String jobTitle;
    private Boolean isSmoker;
    private Boolean isActive;
    private Boolean acceptAnimal;
    private Integer percentCompleteProfile;
    private ArrayList<ImageModel> images;
    private DetailledDateModel lastConnectedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Boolean getSmoker() {
        return isSmoker;
    }

    public void setSmoker(Boolean smoker) {
        isSmoker = smoker;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getAcceptAnimal() {
        return acceptAnimal;
    }

    public void setAcceptAnimal(Boolean acceptAnimal) {
        this.acceptAnimal = acceptAnimal;
    }

    public Integer getPercentCompleteProfile() {
        return percentCompleteProfile;
    }

    public void setPercentCompleteProfile(Integer percentCompleteProfile) {
        this.percentCompleteProfile = percentCompleteProfile;
    }

    public ArrayList<ImageModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageModel> images) {
        this.images = images;
    }

    public DetailledDateModel getLastConnectedTime() {
        return lastConnectedTime;
    }

    public void setLastConnectedTime(DetailledDateModel lastConnectedTime) {
        this.lastConnectedTime = lastConnectedTime;
    }
}
