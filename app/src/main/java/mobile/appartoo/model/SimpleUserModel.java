package mobile.appartoo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandre on 16-07-22.
 */
public class SimpleUserModel {
    @SerializedName("@id")
    String id;
    String email;
    String facebookId;

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
