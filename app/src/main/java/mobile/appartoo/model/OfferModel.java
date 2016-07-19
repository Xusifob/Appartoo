package mobile.appartoo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alexandre on 16-07-05.
 */
public class OfferModel implements Serializable {

    private int price;
    private int rooms;
    private int conversationId;

    @SerializedName("@id")
    private String id;
    private String description;
    private String name;
    private String keyword;
    private AddressModel address;
    private ArrayList<ImageModel> images;
    private ArrayList<UserModel> resident;
    private UserModel owner;
    private Date availabilityEnds;
    private Date availabilityStarts;
    private Boolean isActive;
    private Boolean isSmoker;
    private Boolean acceptAnimal;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public ArrayList<ImageModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageModel> images) {
        this.images = images;
    }

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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

    public ArrayList<UserModel> getResident() {
        return resident;
    }

    public void setResident(ArrayList<UserModel> resident) {
        this.resident = resident;
    }

    @Override
    public String toString() {
        String str = "OfferModel: {price: " + price + ", " +
                "rooms: " + rooms + ", " +
                "conversationId: " + conversationId + ", " +
                "id: " + id + ", " +
                "description: " + description + ", " +
                "name: " + name + ", " +
                "keyword: " + keyword + ", " +
                "address: " + address.toString() + ", " +
                "images: " + images + ", " +
                "resident: " + resident + ", " +
                "owner: " + owner.toString() + ", " +
                "availabilityEnds: " + availabilityEnds + ", " +
                "availabilityStarts: " + availabilityStarts + ", " +
                "isActive: " + isActive + ", " +
                "isSmoker: " + isSmoker + ", " +
                "acceptAnimal: " + acceptAnimal + "}";
        return str;
    }
}
