package com.appartoo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by alexandre on 16-07-22.
 */
public class UserModel implements Parcelable {
    @SerializedName("@id")
    private String id;
    private String society;
    private String function;
    private String contract;
    private String description;
    private String familyName;
    private String gender;
    private String givenName;
    private String telephone;
    private Boolean isSmoker;
    private Boolean inRelationship;
    private Integer plus;
    private Double income;
    private Date birthDate;
    private AddressModel address;
    private ImageModel image;

    private static final ClassLoader BOOLEAN_CLASS_LOADER = Boolean.class.getClassLoader();
    private static final ClassLoader INTEGER_CLASS_LOADER = Integer.class.getClassLoader();
    private static final ClassLoader DOUBLE_CLASS_LOADER = Double.class.getClassLoader();
    private static final ClassLoader DATE_CLASS_LOADER = Date.class.getClassLoader();
    private static final ClassLoader IMAGEMODEL_CLASS_LOADER = ImageModel.class.getClassLoader();
    private static final ClassLoader ADDRESSMODEL_CLASS_LOADER = AddressModel.class.getClassLoader();

    public UserModel() {}

    protected UserModel(Parcel in) {
        id = in.readString();
        society = in.readString();
        function = in.readString();
        contract = in.readString();
        description = in.readString();
        familyName = in.readString();
        gender = in.readString();
        givenName = in.readString();
        telephone = in.readString();
        isSmoker = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        inRelationship = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        plus = (Integer) in.readValue(INTEGER_CLASS_LOADER);
        income = (Double) in.readValue(DOUBLE_CLASS_LOADER);
        birthDate = (Date) in.readValue(DATE_CLASS_LOADER);
        address = (AddressModel) in.readValue(ADDRESSMODEL_CLASS_LOADER);
        image = (ImageModel) in.readValue(IMAGEMODEL_CLASS_LOADER);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(society);
        dest.writeString(function);
        dest.writeString(contract);
        dest.writeString(description);
        dest.writeString(familyName);
        dest.writeString(gender);
        dest.writeString(givenName);
        dest.writeString(telephone);
        dest.writeValue(isSmoker);
        dest.writeValue(inRelationship);
        dest.writeValue(plus);
        dest.writeValue(income);
        dest.writeValue(birthDate);
        dest.writeValue(address);
        dest.writeValue(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getPlus() {
        return plus;
    }

    public void setPlus(Integer plus) {
        this.plus = plus;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
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

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    @Override
    public String toString() {
        return "UserModel{" +
                "id='" + id + '\'' +
                ", isSmoker=" + isSmoker +
                ", inRelationship=" + inRelationship +
                ", society='" + society + '\'' +
                ", function='" + function + '\'' +
                ", contract='" + contract + '\'' +
                ", plus=" + plus +
                ", income=" + income +
                ", address=" + address +
                ", birthDate=" + birthDate +
                ", description='" + description + '\'' +
                ", familyName='" + familyName + '\'' +
                ", gender='" + gender + '\'' +
                ", givenName='" + givenName + '\'' +
                ", image=" + image +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
