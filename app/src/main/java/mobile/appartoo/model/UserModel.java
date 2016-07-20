package mobile.appartoo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alexandre on 16-07-05.
 */
public class UserModel implements Serializable {
    private Boolean isSmoker;
    private Boolean inRelationship;
    private Double income;
    private Date birthDate;

    @SerializedName("@id")
    private String id;
    private String user;
    private String society;
    private String function;
    private String contract;
    private String plus;
    private String description;
    private String familyName;
    private String gender;
    private String telephone;
    private String givenName;
    private ImageModel image;
    private AddressModel address;
    private ProfileModel profileModel;

    public ProfileModel getProfileModel() {
        return profileModel;
    }

    public void setProfileModel(ProfileModel profileModel) {
        this.profileModel = profileModel;
    }

    public Boolean getSmoker() {
        return isSmoker;
    }

    public void setSmoker(Boolean smoker) {
        isSmoker = smoker;
    }

    public Boolean getInRelationship() {
        return inRelationship;
    }

    public void setInRelationship(Boolean inRelationship) {
        this.inRelationship = inRelationship;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public int getAge() {
        Calendar now = Calendar.getInstance();
        Calendar birthdate = Calendar.getInstance();
        birthdate.setTime(this.birthDate);
        int age = now.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
        if (birthdate.get(Calendar.DAY_OF_YEAR) > now.get(Calendar.DAY_OF_YEAR)) {
            age -= 1;
        }
        return age;
    }

    public String toString() {
        String str = "UserModel: {isSmoker: " + isSmoker + ", " +
                "inRelationship: " + inRelationship + ", " +
                "income: " + income + ", " +
                "birthDate: " + birthDate + ", " +
                "id: " + id + ", " +
                "user: " + user + ", " +
                "society: " + society + ", " +
                "function: " + function + ", " +
                "contract: " + contract + ", " +
                "plus: " + plus + ", " +
                "description: " + description + ", " +
                "familyName: " + familyName + ", " +
                "gender: " + gender + ", " +
                "telephone: " + telephone + ", " +
                "givenName: " + givenName + ", " +
                "image: " + image.toString() + ", " +
                "address: " + address.toString() + "}";

        return str;
    }
}
