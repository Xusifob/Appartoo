package com.appartoo.utils.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandre on 16-07-22.
 */
public class AddressCountryModel implements Parcelable {

    @SerializedName("@id")
    String id;
    String name;

    protected AddressCountryModel(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressCountryModel> CREATOR = new Creator<AddressCountryModel>() {
        @Override
        public AddressCountryModel createFromParcel(Parcel in) {
            return new AddressCountryModel(in);
        }

        @Override
        public AddressCountryModel[] newArray(int size) {
            return new AddressCountryModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AddressCountryModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
