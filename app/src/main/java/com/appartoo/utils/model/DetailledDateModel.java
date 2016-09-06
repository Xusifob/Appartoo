package com.appartoo.utils.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by alexandre on 16-07-27.
 */
public class DetailledDateModel implements Parcelable{
    private Date date;
    private Integer timezone_type;
    private String timezone;

    private static final ClassLoader INTEGER_CLASS_LOADER = Integer.class.getClassLoader();

    protected DetailledDateModel(Parcel in) {
        date = (Date) in.readSerializable();
        timezone_type = (Integer) in.readValue(INTEGER_CLASS_LOADER);
        timezone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(date);
        dest.writeValue(timezone_type);
        dest.writeString(timezone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DetailledDateModel> CREATOR = new Creator<DetailledDateModel>() {
        @Override
        public DetailledDateModel createFromParcel(Parcel in) {
            return new DetailledDateModel(in);
        }

        @Override
        public DetailledDateModel[] newArray(int size) {
            return new DetailledDateModel[size];
        }
    };

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getTimezone_type() {
        return timezone_type;
    }

    public void setTimezone_type(Integer timezone_type) {
        this.timezone_type = timezone_type;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
