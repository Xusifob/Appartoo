package mobile.appartoo.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by alexandre on 16-07-20.
 */
public class ProfileModel implements Serializable {
    private Boolean isSmoker;
    private Boolean isCook;
    private Boolean isMusician;
    private Boolean isSpendthrift;
    private Boolean isPartyGoer;
    private Boolean isLayabout;
    private Boolean inRelationship;
    private Boolean isGeek;
    private Boolean isTraveller;
    private Boolean isGenerous;
    private Boolean isAnimalFriend;
    private Boolean isMessy;
    private Boolean isManiac;
    private Boolean acceptAnimal;
    private String relationshipStatus;
    private String society;
    private String function;
    private String contract;
    private String plus;
    private Boolean emailChecked;
    private Double income;
    private Date birthDate;
    private String description;
    private String familyName;
    private String gender;
    private String givenName;
    private String honorificPrefix;
    private String jobTitle;
    private String telephone;

    public Boolean getSmoker() {
        return isSmoker;
    }

    public void setSmoker(Boolean smoker) {
        isSmoker = smoker;
    }

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

    public Boolean getInRelationship() {
        return inRelationship;
    }

    public void setInRelationship(Boolean inRelationship) {
        this.inRelationship = inRelationship;
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

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getPlus() {
        return plus;
    }

    public void setPlus(String plus) {
        this.plus = plus;
    }

    public Boolean getEmailChecked() {
        return emailChecked;
    }

    public void setEmailChecked(Boolean emailChecked) {
        this.emailChecked = emailChecked;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
