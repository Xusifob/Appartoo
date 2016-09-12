package com.appartoo.utils.model;

import android.os.Parcel;

/**
 * Created by alexandre on 16-07-22.
 */
public class UserProfileModel extends UserModel {

    private String relationshipStatus;
    private String honorificPrefix;
    private String jobTitle;
    private Boolean isCook;
    private Boolean isMusician;
    private Boolean isSpendthrift;
    private Boolean isPartyGoer;
    private Boolean isLayabout;
    private Boolean isGeek;
    private Boolean isTraveller;
    private Boolean isGenerous;
    private Boolean isAnimalFriend;
    private Boolean isMessy;
    private Boolean isManiac;
    private Boolean acceptAnimal;
    private Boolean emailChecked;
    private Integer percentCompleteProfile;
    private NationalityModel nationality;

    private static final ClassLoader BOOLEAN_CLASS_LOADER = Boolean.class.getClassLoader();
    private static final ClassLoader INTEGER_CLASS_LOADER = Integer.class.getClassLoader();
    private static final ClassLoader NATIONALITYMODEL_CLASS_LOADER = NationalityModel.class.getClassLoader();

    public UserProfileModel() {
        super();
    }

    protected UserProfileModel(Parcel in) {
        super(in);
        relationshipStatus = in.readString();
        honorificPrefix = in.readString();
        jobTitle = in.readString();
        isCook = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isMusician = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isSpendthrift = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isPartyGoer = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isLayabout = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isGeek = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isTraveller = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isGenerous = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isAnimalFriend = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isMessy = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isManiac = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        acceptAnimal = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        emailChecked = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        percentCompleteProfile = (Integer) in.readValue(INTEGER_CLASS_LOADER);
        nationality = in.readParcelable(NATIONALITYMODEL_CLASS_LOADER);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(relationshipStatus);
        dest.writeString(honorificPrefix);
        dest.writeString(jobTitle);
        dest.writeValue(isCook);
        dest.writeValue(isMusician);
        dest.writeValue(isSpendthrift);
        dest.writeValue(isPartyGoer);
        dest.writeValue(isLayabout);
        dest.writeValue(isGeek);
        dest.writeValue(isTraveller);
        dest.writeValue(isGenerous);
        dest.writeValue(isAnimalFriend);
        dest.writeValue(isMessy);
        dest.writeValue(isManiac);
        dest.writeValue(acceptAnimal);
        dest.writeValue(emailChecked);
        dest.writeValue(percentCompleteProfile);
        dest.writeParcelable(nationality, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserProfileModel> CREATOR = new Creator<UserProfileModel>() {
        @Override
        public UserProfileModel createFromParcel(Parcel in) {
            return new UserProfileModel(in);
        }

        @Override
        public UserProfileModel[] newArray(int size) {
            return new UserProfileModel[size];
        }
    };

    public Boolean getCook() {
        return isCook;
    }

    public void setCook(Boolean cook) {
        isCook = cook;
    }

    public Boolean getMusician() {
        return isMusician;
    }

    public void setMusician(Boolean musician) {
        isMusician = musician;
    }

    public Boolean getSpendthrift() {
        return isSpendthrift;
    }

    public void setSpendthrift(Boolean spendthrift) {
        isSpendthrift = spendthrift;
    }

    public Boolean getPartyGoer() {
        return isPartyGoer;
    }

    public void setPartyGoer(Boolean partyGoer) {
        isPartyGoer = partyGoer;
    }

    public Boolean getLayabout() {
        return isLayabout;
    }

    public void setLayabout(Boolean layabout) {
        isLayabout = layabout;
    }

    public Boolean getGeek() {
        return isGeek;
    }

    public void setGeek(Boolean geek) {
        isGeek = geek;
    }

    public Boolean getTraveller() {
        return isTraveller;
    }

    public void setTraveller(Boolean traveller) {
        isTraveller = traveller;
    }

    public Boolean getGenerous() {
        return isGenerous;
    }

    public void setGenerous(Boolean generous) {
        isGenerous = generous;
    }

    public Boolean getAnimalFriend() {
        return isAnimalFriend;
    }

    public void setAnimalFriend(Boolean animalFriend) {
        isAnimalFriend = animalFriend;
    }

    public Boolean getMessy() {
        return isMessy;
    }

    public void setMessy(Boolean messy) {
        isMessy = messy;
    }

    public Boolean getManiac() {
        return isManiac;
    }

    public void setManiac(Boolean maniac) {
        isManiac = maniac;
    }

    public Boolean getAcceptAnimal() {
        return acceptAnimal;
    }

    public void setAcceptAnimal(Boolean acceptAnimal) {
        this.acceptAnimal = acceptAnimal;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public Boolean getEmailChecked() {
        return emailChecked;
    }

    public void setEmailChecked(Boolean emailChecked) {
        this.emailChecked = emailChecked;
    }

    public Integer getPercentCompleteProfile() {
        return percentCompleteProfile;
    }

    public void setPercentCompleteProfile(Integer percentCompleteProfile) {
        this.percentCompleteProfile = percentCompleteProfile;
    }

    public String getHonorificPrefix() {
        return honorificPrefix;
    }

    public void setHonorificPrefix(String honorificPrefix) {
        this.honorificPrefix = honorificPrefix;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public NationalityModel getNationality() {
        return nationality;
    }

    public void setNationality(NationalityModel nationality) {
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return "CompleteUserModel{" +
                "isCook=" + isCook +
                ", isMusician=" + isMusician +
                ", isSpendthrift=" + isSpendthrift +
                ", isPartyGoer=" + isPartyGoer +
                ", isLayabout=" + isLayabout +
                ", isGeek=" + isGeek +
                ", isTraveller=" + isTraveller +
                ", isGenerous=" + isGenerous +
                ", isAnimalFriend=" + isAnimalFriend +
                ", isMessy=" + isMessy +
                ", isManiac=" + isManiac +
                ", acceptAnimal=" + acceptAnimal +
                ", relationshipStatus='" + relationshipStatus + '\'' +
                ", emailChecked=" + emailChecked +
                ", percentCompleteProfile=" + percentCompleteProfile +
                ", honorificPrefix='" + honorificPrefix + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", nationality=" + nationality +
                '}' + super.toString();
    }
}
