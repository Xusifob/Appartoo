package com.appartoo.utils.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandre on 16-07-06.
 */
public class ImageModel implements Parcelable {

    @SerializedName("@id")
    private String id;
    private String caption;
    private String contentUrl;
    @SerializedName("id")
    private String idNumber;

    private static final ClassLoader INTEGER_CLASS_LOADER = Integer.class.getClassLoader();

    protected ImageModel(Parcel in) {
        id = in.readString();
        caption = in.readString();
        contentUrl = in.readString();
        idNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(caption);
        dest.writeString(contentUrl);
        dest.writeString(idNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    @Override
    public String toString(){
        return "ImageModel: {id: " + id + ", " +
                "caption: " + caption + ", " +
                "contentUrl: " + contentUrl + ", ";
    }
}
