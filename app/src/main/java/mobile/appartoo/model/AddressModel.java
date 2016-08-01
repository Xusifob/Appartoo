package mobile.appartoo.model;

import android.location.Geocoder;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexandre on 16-07-22.
 */
public class AddressModel implements Parcelable{
    @SerializedName("@id")
    private String id;
    private String addressLocality;
    private String addressRegion;
    private String postalCode;
    private String postOfficeBoxNumber;
    private String streetAddress;
    private String name;
    private String placeId;
    private String formattedAddress;
    private Double latitude;
    private Double longitude;
    private AddressCountryModel addressCountry;

    private static final ClassLoader DOUBLE_CLASS_LOADER = Double.class.getClassLoader();
    private static final ClassLoader ADDRESSCOUNTRYMODEL_CLASS_LOADER = AddressCountryModel.class.getClassLoader();

    public AddressModel () {};

    protected AddressModel(Parcel in) {
        id = in.readString();
        addressLocality = in.readString();
        addressRegion = in.readString();
        postalCode = in.readString();
        postOfficeBoxNumber = in.readString();
        streetAddress = in.readString();
        name = in.readString();
        placeId = in.readString();
        formattedAddress = in.readString();
        latitude = (Double) in.readValue(DOUBLE_CLASS_LOADER);
        longitude = (Double) in.readValue(DOUBLE_CLASS_LOADER);
        addressCountry = in.readParcelable(ADDRESSCOUNTRYMODEL_CLASS_LOADER);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(addressLocality);
        dest.writeString(addressRegion);
        dest.writeString(postalCode);
        dest.writeString(postOfficeBoxNumber);
        dest.writeString(streetAddress);
        dest.writeString(name);
        dest.writeString(placeId);
        dest.writeString(formattedAddress);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
        dest.writeParcelable(addressCountry, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel in) {
            return new AddressModel(in);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };

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
