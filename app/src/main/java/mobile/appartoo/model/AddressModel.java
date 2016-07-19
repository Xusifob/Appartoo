package mobile.appartoo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexandre on 16-07-06.
 */
public class AddressModel implements Serializable {

    @SerializedName("@id")
    private String id;
    private double latitude;
    private double longitude;
    private String name;
    private String placeId;
    private String formattedAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getCity(){
        Pattern p = Pattern.compile("^([0-9\\s]*(?<!\\s))\\s(.*)$");
        Matcher m = p.matcher(this.formattedAddress.split("\n")[1]);
        if(m.matches()){
            return m.group(2);
        }

        return null;
    }

    @Override
    public String toString(){
        String str = "AddressModel: {id: " + id + ", " +
                "latitude: " + latitude + ", " +
                "longitude: " + longitude + ", " +
                "name: " + name + ", " +
                "placeId: " + placeId + ", " +
                "formattedAddress: " + formattedAddress + "}";
        return str;
    }
}
