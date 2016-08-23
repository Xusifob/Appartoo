package com.appartoo.model;

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
    private ImageModel thumbnail;

    public ImageModel() {}


    protected ImageModel(Parcel in) {
        id = in.readString();
        caption = in.readString();
        contentUrl = in.readString();
        thumbnail = in.readParcelable(ImageModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(caption);
        dest.writeString(contentUrl);
        dest.writeParcelable(thumbnail, flags);
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

    public ImageModel getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageModel thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString(){
        String str;

        if(thumbnail != null) {
            str = "ImageModel: {id: " + id + ", " +
                    "caption: " + caption + ", " +
                    "contentUrl: " + contentUrl + ", " +
                    "thumbnail: " + thumbnail.toString() + "}";
        } else {
            str = "ImageModel: {id: " + id + ", " +
                    "caption: " + caption + ", " +
                    "contentUrl: " + contentUrl + ", " +
                    "thumbnail: null}";
        }

        return str;
    }
}
