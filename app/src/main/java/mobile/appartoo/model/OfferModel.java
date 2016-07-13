package mobile.appartoo.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alexandre on 16-07-05.
 */
public class OfferModel implements JsonModel {

    private int price;
    private int rooms;
    private int conversationId;
    private String id;
    private String description;
    private String name;
    private String keyword;
    private AddressModel address;
    private ArrayList<ImageModel> images;
    private ArrayList<UserModel> residents;
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

    public ArrayList<UserModel> getResidents() {
        return residents;
    }

    public void setResidents(ArrayList<UserModel> residents) {
        this.residents = residents;
    }

    @Override
    public OfferModel createFromJSON(JSONObject jsonObject) throws JSONException {
        this.price = Integer.valueOf(jsonObject.getString("price"));
        this.rooms = Integer.valueOf(jsonObject.getString("rooms"));
        this.conversationId = Integer.valueOf(jsonObject.getString("conversationId"));
        this.id = jsonObject.getString("@id");
        this.description = jsonObject.getString("description");
        this.name = jsonObject.getString("name");
        this.keyword = jsonObject.getString("keyword");

        String availabilityEndsStr = jsonObject.getString("availabilityEnds");
        String availabilityStartsStr = jsonObject.getString("availabilityStarts");

        try {
            this.availabilityEnds = format.parse(availabilityEndsStr);
            this.availabilityStarts = format.parse(availabilityStartsStr);
        } catch (Exception e) {
            this.availabilityEnds = null;
            this.availabilityStarts = null;
            e.printStackTrace();
        }

        this.isActive = Boolean.valueOf(jsonObject.getString("isActive"));
        this.isSmoker = Boolean.valueOf(jsonObject.getString("isSmoker"));
        this.acceptAnimal = Boolean.valueOf(jsonObject.getString("acceptAnimal"));
        this.owner = new UserModel().createFromJSON(jsonObject.getJSONObject("owner"));

        this.images = new ArrayList<>();
        JSONArray jsonImages = jsonObject.getJSONArray("images");
        for (int i = 0 ; i < jsonImages.length() ; i++){
            images.add(new ImageModel().createFromJSON(jsonImages.getJSONObject(i)));
        }

        this.residents = new ArrayList<>();
        JSONArray jsonResidents = jsonObject.getJSONArray("resident");
        for (int i = 0 ; i < jsonResidents.length() ; i++) {
            residents.add(new UserModel().createFromJSON(jsonResidents.getJSONObject(i)));
        }

        this.address = new AddressModel().createFromJSON(jsonObject.getJSONObject("address"));
        return this;
    }
}
