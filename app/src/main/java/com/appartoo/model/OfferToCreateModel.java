package com.appartoo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alexandre on 16-08-01.
 */
public class OfferToCreateModel extends AddressModel implements Parcelable{

    private String description;
    private String offer_name;
    private String keyword;
    private String phone;
    private String country;
    private Date start;
    private Date end;
    private Integer price;
    private Integer rooms;
    private Boolean acceptAnimal;
    private Boolean isSmoker;
    private ArrayList<UserModel> residents;

    private static final ClassLoader BOOLEAN_CLASS_LOADER = Boolean.class.getClassLoader();
    private static final ClassLoader INTEGER_CLASS_LOADER = Integer.class.getClassLoader();

    public OfferToCreateModel() {super();}

    protected OfferToCreateModel(Parcel in) {
        super(in);
        description = in.readString();
        offer_name = in.readString();
        keyword = in.readString();
        phone = in.readString();
        country = in.readString();
        start = (Date) in.readSerializable();
        end = (Date) in.readSerializable();
        price = (Integer) in.readValue(INTEGER_CLASS_LOADER);
        rooms = (Integer) in.readValue(INTEGER_CLASS_LOADER);
        acceptAnimal = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isSmoker = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        residents = in.createTypedArrayList(UserModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(description);
        dest.writeString(offer_name);
        dest.writeString(keyword);
        dest.writeString(phone);
        dest.writeString(country);
        dest.writeValue(start);
        dest.writeValue(end);
        dest.writeValue(price);
        dest.writeValue(rooms);
        dest.writeValue(acceptAnimal);
        dest.writeValue(isSmoker);
        dest.writeTypedList(residents);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OfferToCreateModel> CREATOR = new Creator<OfferToCreateModel>() {
        @Override
        public OfferToCreateModel createFromParcel(Parcel in) {
            return new OfferToCreateModel(in);
        }

        @Override
        public OfferToCreateModel[] newArray(int size) {
            return new OfferToCreateModel[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Boolean getAcceptAnimal() {
        return acceptAnimal;
    }

    public void setAcceptAnimal(Boolean acceptAnimal) {
        this.acceptAnimal = acceptAnimal;
    }

    public Boolean getSmoker() {
        return isSmoker;
    }

    public void setSmoker(Boolean smoker) {
        isSmoker = smoker;
    }

    public ArrayList<UserModel> getResidents() {
        return residents;
    }

    public void setResidents(ArrayList<UserModel> residents) {
        this.residents = residents;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "OfferToCreateModel{" + super.toString() +
                ", description='" + description + '\'' +
                ", offer_name='" + offer_name + '\'' +
                ", keyword='" + keyword + '\'' +
                ", phone='" + phone + '\'' +
                ", country='" + country + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", price=" + price +
                ", rooms=" + rooms +
                ", acceptAnimal=" + acceptAnimal +
                ", isSmoker=" + isSmoker +
                ", residents=" + residents +
                '}';
    }
}
