package mobile.appartoo.model;

import com.google.gson.annotations.SerializedName;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexandre on 16-07-22.
 */
public class AddressModel {
    @SerializedName("@id")
    private String id;
    private AddressCountryModel addressCountry;
    private String addressLocality;
    private String addressRegion;
    private String postalCode;
    private String postOfficeBoxNumber;
    private String streetAddress;
    private Double latitude;
    private Double longitude;
    private String name;
    private String placeId;
    private String formattedAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AddressCountryModel getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(AddressCountryModel addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getAddressLocality() {
        return addressLocality;
    }

    public void setAddressLocality(String addressLocality) {
        this.addressLocality = addressLocality;
    }

    public String getAddressRegion() {
        return addressRegion;
    }

    public void setAddressRegion(String addressRegion) {
        this.addressRegion = addressRegion;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostOfficeBoxNumber() {
        return postOfficeBoxNumber;
    }

    public void setPostOfficeBoxNumber(String postOfficeBoxNumber) {
        this.postOfficeBoxNumber = postOfficeBoxNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
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
    public String toString() {
        return "AddressModel{" +
                "id='" + id + '\'' +
                ", addressCountry=" + addressCountry +
                ", addressLocality='" + addressLocality + '\'' +
                ", addressRegion='" + addressRegion + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", postOfficeBoxNumber='" + postOfficeBoxNumber + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", name='" + name + '\'' +
                ", placeId='" + placeId + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                '}';
    }
}
