package mobile.appartoo.model;

import android.os.Parcel;

/**
 * Created by alexandre on 16-07-22.
 */
public class UserWithProfileModel extends UserModel {

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
    private SimpleUserModel user;
    private NationalityModel nationality;

    private static final ClassLoader BOOLEAN_CLASS_LOADER = Boolean.class.getClassLoader();
    private static final ClassLoader INTEGER_CLASS_LOADER = Integer.class.getClassLoader();
    private static final ClassLoader SIMPLEUSERMODEL_CLASS_LOADER = SimpleUserModel.class.getClassLoader();
    private static final ClassLoader NATIONALITYMODEL_CLASS_LOADER = NationalityModel.class.getClassLoader();

    protected UserWithProfileModel(Parcel in) {
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
        user = in.readParcelable(SIMPLEUSERMODEL_CLASS_LOADER);
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
        dest.writeParcelable(user, flags);
        dest.writeParcelable(nationality, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserWithProfileModel> CREATOR = new Creator<UserWithProfileModel>() {
        @Override
        public UserWithProfileModel createFromParcel(Parcel in) {
            return new UserWithProfileModel(in);
        }

        @Override
        public UserWithProfileModel[] newArray(int size) {
            return new UserWithProfileModel[size];
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
