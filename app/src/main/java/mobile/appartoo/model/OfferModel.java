package mobile.appartoo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by alexandre on 16-07-05.
 */
public class OfferModel implements Serializable {

    @SerializedName("@id")
    private String id;
    private Date availabilityEnds;
    private Date availabilityStarts;
    private String description;
    private String name;
    private Integer price;
    private UserModel owner;
    private AddressModel address;
    private Boolean isActive;
    private ArrayList<ImageModel> images;
    private Integer rooms;
    private Boolean isSmoker;
    private Boolean acceptAnimal;
    private String keyword;
    private ArrayList<UserModel> resident;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getAvailabilityEnds() {
        return availabilityEnds;
    }

    public void setAvailabilityEnds(Date availabilityEnds) {
        this.availabilityEnds = availabilityEnds;
    }

    public Date getAvailabilityStarts() {
        return availabilityStarts;
    }

    public void setAvailabilityStarts(Date availabilityStarts) {
        this.availabilityStarts = availabilityStarts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public ArrayList<ImageModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageModel> images) {
        this.images = images;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Boolean getSmoker() {
        return isSmoker;
    }

    public void setSmoker(Boolean smoker) {
        isSmoker = smoker;
    }

    public Boolean getAcceptAnimal() {
        return acceptAnimal;
    }

    public void setAcceptAnimal(Boolean acceptAnimal) {
        this.acceptAnimal = acceptAnimal;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ArrayList<UserModel> getResident() {
        return resident;
    }

    public void setResident(ArrayList<UserModel> resident) {
        this.resident = resident;
    }

    @Override
    public String toString() {
        return "OfferModel{" +
                "id='" + id + '\'' +
                ", availabilityEnds=" + availabilityEnds +
                ", availabilityStarts=" + availabilityStarts +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", owner=" + owner +
                ", address=" + address +
                ", isActive=" + isActive +
                ", images=" + images +
                ", rooms=" + rooms +
                ", isSmoker=" + isSmoker +
                ", acceptAnimal=" + acceptAnimal +
                ", keyword='" + keyword + '\'' +
                ", resident=" + resident +
                '}';
    }
}
