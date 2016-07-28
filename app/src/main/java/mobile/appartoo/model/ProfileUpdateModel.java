package mobile.appartoo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by alexandre on 16-07-28.
 */
public class ProfileUpdateModel implements Parcelable {

    private Date birthDate;
    private String relationshipStatus;
    private String society;
    private String function;
    private String contract;
    private String description;
    private String familyName;
    private String gender;
    private String givenName;
    private String honorificPrefix;
    private String jobTitle;
    private String telephone;
    private Double income;
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

    private static final ClassLoader BOOLEAN_CLASS_LOADER = Boolean.class.getClassLoader();
    private static final ClassLoader DOUBLE_CLASS_LOADER = Double.class.getClassLoader();

    public ProfileUpdateModel(){}

    protected ProfileUpdateModel(Parcel in) {
        relationshipStatus = in.readString();
        society = in.readString();
        function = in.readString();
        contract = in.readString();
        description = in.readString();
        familyName = in.readString();
        gender = in.readString();
        givenName = in.readString();
        honorificPrefix = in.readString();
        jobTitle = in.readString();
        telephone = in.readString();
        birthDate = (Date) in.readSerializable();
        income = (Double) in.readValue(DOUBLE_CLASS_LOADER);
        isSmoker = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isCook = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isMusician = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isSpendthrift = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isPartyGoer = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isLayabout = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        inRelationship = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isGeek = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isTraveller = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isGenerous = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isAnimalFriend = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isMessy = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isManiac = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        acceptAnimal = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(relationshipStatus);
        dest.writeString(society);
        dest.writeString(function);
        dest.writeString(contract);
        dest.writeString(description);
        dest.writeString(familyName);
        dest.writeString(gender);
        dest.writeString(givenName);
        dest.writeString(honorificPrefix);
        dest.writeString(jobTitle);
        dest.writeString(telephone);
        dest.writeSerializable(birthDate);
        dest.writeValue(income);
        dest.writeValue(isSmoker);
        dest.writeValue(isCook);
        dest.writeValue(isMusician);
        dest.writeValue(isSpendthrift);
        dest.writeValue(isPartyGoer);
        dest.writeValue(isLayabout);
        dest.writeValue(inRelationship);
        dest.writeValue(isGeek);
        dest.writeValue(isTraveller);
        dest.writeValue(isGenerous);
        dest.writeValue(isAnimalFriend);
        dest.writeValue(isMessy);
        dest.writeValue(isManiac);
        dest.writeValue(acceptAnimal);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProfileUpdateModel> CREATOR = new Creator<ProfileUpdateModel>() {
        @Override
        public ProfileUpdateModel createFromParcel(Parcel in) {
            return new ProfileUpdateModel(in);
        }

        @Override
        public ProfileUpdateModel[] newArray(int size) {
            return new ProfileUpdateModel[size];
        }
    };

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

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

    @Override
    public String toString() {
        return "ProfileUpdateModel{" +
                "birthDate=" + birthDate +
                ", relationshipStatus='" + relationshipStatus + '\'' +
                ", society='" + society + '\'' +
                ", function='" + function + '\'' +
                ", contract='" + contract + '\'' +
                ", description='" + description + '\'' +
                ", familyName='" + familyName + '\'' +
                ", gender='" + gender + '\'' +
                ", givenName='" + givenName + '\'' +
                ", honorificPrefix='" + honorificPrefix + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", telephone='" + telephone + '\'' +
                ", income=" + income +
                ", isSmoker=" + isSmoker +
                ", isCook=" + isCook +
                ", isMusician=" + isMusician +
                ", isSpendthrift=" + isSpendthrift +
                ", isPartyGoer=" + isPartyGoer +
                ", isLayabout=" + isLayabout +
                ", inRelationship=" + inRelationship +
                ", isGeek=" + isGeek +
                ", isTraveller=" + isTraveller +
                ", isGenerous=" + isGenerous +
                ", isAnimalFriend=" + isAnimalFriend +
                ", isMessy=" + isMessy +
                ", isManiac=" + isManiac +
                ", acceptAnimal=" + acceptAnimal +
                '}';
    }
}
