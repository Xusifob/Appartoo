package mobile.appartoo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alexandre on 16-07-22.
 */
public class UserWithProfileModel extends UserModel implements Serializable {

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
    private String relationshipStatus;
    private SimpleUserModel user;
    private Boolean emailChecked;
    private Integer percentCompleteProfile;
    private String honorificPrefix;
    private String jobTitle;
    private NationalityModel nationality;

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

    public SimpleUserModel getUser() {
        return user;
    }

    public void setUser(SimpleUserModel user) {
        this.user = user;
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
        return "UserWithProfileModel{" +
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
                ", user=" + user +
                ", emailChecked=" + emailChecked +
                ", percentCompleteProfile=" + percentCompleteProfile +
                ", honorificPrefix='" + honorificPrefix + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", nationality=" + nationality +
                '}';
    }
}
