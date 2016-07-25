package mobile.appartoo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandre on 16-07-22.
 */
public class SimpleUserModel implements Parcelable{
    @SerializedName("@id")
    String id;
    String email;
    String facebookId;

    protected SimpleUserModel(Parcel in) {
        id = in.readString();
        email = in.readString();
        facebookId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(facebookId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SimpleUserModel> CREATOR = new Creator<SimpleUserModel>() {
        @Override
        public SimpleUserModel createFromParcel(Parcel in) {
            return new SimpleUserModel(in);
        }

        @Override
        public SimpleUserModel[] newArray(int size) {
            return new SimpleUserModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    @Override
    public String toString() {
        return "SimpleUserModel{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", facebookId='" + facebookId + '\'' +
                '}';
    }
}
