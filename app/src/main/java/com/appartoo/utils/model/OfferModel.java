package com.appartoo.utils.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-07-05.
 */
public class OfferModel implements Parcelable {

    @SerializedName("@id")
    private String id;
    private String description;
    private String name;
    private String keyword;
    private Boolean isActive;
    private Boolean isSmoker;
    private Boolean acceptAnimal;

    @SerializedName("id")
    private Integer idNumber;
    private Integer price;
    private Integer rooms;
    private UserModel owner;
    private AddressModel address;
    private ArrayList<ImageModel> images;
    private ArrayList<UserModel> resident;

    private static final ClassLoader BOOLEAN_CLASS_LOADER = Boolean.class.getClassLoader();
    private static final ClassLoader INTEGER_CLASS_LOADER = Integer.class.getClassLoader();
    private static final ClassLoader USERMODEL_CLASS_LOADER = UserModel.class.getClassLoader();
    private static final ClassLoader ADDRESSMODEL_CLASS_LOADER = AddressModel.class.getClassLoader();
    private static final ClassLoader IMAGEMODEL_CLASS_LOADER = ImageModel.class.getClassLoader();

    public OfferModel() {};

    protected OfferModel(Parcel in) {
        id = in.readString();
        description = in.readString();
        name = in.readString();
        keyword = in.readString();
        isActive = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isSmoker = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        acceptAnimal = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        idNumber = (Integer) in.readValue(INTEGER_CLASS_LOADER);
        price = (Integer) in.readValue(INTEGER_CLASS_LOADER);
        rooms = (Integer) in.readValue(INTEGER_CLASS_LOADER);
        owner = in.readParcelable(USERMODEL_CLASS_LOADER);
        address = in.readParcelable(ADDRESSMODEL_CLASS_LOADER);
        images = in.readArrayList(IMAGEMODEL_CLASS_LOADER);
        resident = in.readArrayList(USERMODEL_CLASS_LOADER);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(name);
        dest.writeString(keyword);
        dest.writeValue(isActive);
        dest.writeValue(isSmoker);
        dest.writeValue(acceptAnimal);
        dest.writeValue(idNumber);
        dest.writeValue(price);
        dest.writeValue(rooms);
        dest.writeParcelable(owner, flags);
        dest.writeParcelable(address, flags);
        dest.writeList(images);
        dest.writeList(resident);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OfferModel> CREATOR = new Creator<OfferModel>() {
        @Override
        public OfferModel createFromParcel(Parcel in) {
            return new OfferModel(in);
        }

        @Override
        public OfferModel[] newArray(int size) {
            return new OfferModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public ArrayList<ImageModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageModel> images) {
        this.images = images;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Boolean getSmoker() {
        return isSmoker;
    }

    public void setSmoker(Boolean smoker) {
        isSmoker = smoker;
    }

    public Boolean getAcceptAnimal() {
        return acceptAnimal;
    }

    public void setAcceptAnimal(Boolean acceptAnimal) {
        this.acceptAnimal = acceptAnimal;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ArrayList<UserModel> getResident() {
        return resident;
    }

    public void setResident(ArrayList<UserModel> resident) {
        this.resident = resident;
    }

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }

    public Integer getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(Integer idNumber) {
        this.idNumber = idNumber;
    }

    public OfferModel getOfferModel() {
        return this;
    }

    public void setOfferModel(OfferModel offerModel) {
        this.id = offerModel.getId();
        this.description = offerModel.getDescription();
        this.name = offerModel.getName();
        this.keyword = offerModel.getKeyword();
        this.isActive = offerModel.getActive();
        this.isSmoker = offerModel.getSmoker();
        this.acceptAnimal = offerModel.getAcceptAnimal();
        this.idNumber = offerModel.getIdNumber();
        this.price = offerModel.getPrice();
        this.rooms = offerModel.getRooms();
        this.owner = offerModel.getOwner();
        this.address = offerModel.getAddress();
        this.images = offerModel.getImages();
        this.resident = offerModel.getResident();
    }

    @Override
    public String toString() {
        return "OfferModel{" +
                "id='" + id + '\'' +
                ", idNumber=" + idNumber +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", address=" + address +
                ", isActive=" + isActive +
                ", images=" + images +
                ", rooms=" + rooms +
                ", isSmoker=" + isSmoker +
                ", acceptAnimal=" + acceptAnimal +
                ", keyword='" + keyword + '\'' +
                ", owner='" + owner + '\'' +
                ", resident=" + resident +
                '}';
    }
}
