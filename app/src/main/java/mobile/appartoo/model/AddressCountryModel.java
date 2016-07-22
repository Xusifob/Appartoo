package mobile.appartoo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandre on 16-07-22.
 */
public class AddressCountryModel {

    @SerializedName("@id")
    String id;
    String name;

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
