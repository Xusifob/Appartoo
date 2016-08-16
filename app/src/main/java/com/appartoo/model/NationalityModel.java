package com.appartoo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandre on 16-07-22.
 */
public class NationalityModel implements Parcelable {
    @SerializedName("@id")
    private String id;
    private String name;

    protected NationalityModel(Parcel in) {
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

    public static final Creator<NationalityModel> CREATOR = new Creator<NationalityModel>() {
        @Override
        public NationalityModel createFromParcel(Parcel in) {
            return new NationalityModel(in);
        }

        @Override
        public NationalityModel[] newArray(int size) {
            return new NationalityModel[size];
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
        return "NationalityModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
