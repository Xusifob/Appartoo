package mobile.appartoo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alexandre on 16-07-05.
 */
public class OfferModel implements Parcelable {

    @SerializedName("@id")
    private String id;
    private String description;
    private String name;
    private String keyword;
    private Boolean isActive;
    private Boolean isSmoker;
    private Boolean acceptAnimal;
    private Integer price;
    private Integer rooms;
    private Date availabilityEnds;
    private Date availabilityStarts;
    private UserModel owner;
    private AddressModel address;
    private ArrayList<ImageModel> images;
    private ArrayList<UserModel> resident;

    private static final ClassLoader BOOLEAN_CLASS_LOADER = Boolean.class.getClassLoader();
    private static final ClassLoader INTEGER_CLASS_LOADER = Integer.class.getClassLoader();
    private static final ClassLoader USERMODEL_CLASS_LOADER = UserModel.class.getClassLoader();
    private static final ClassLoader ADDRESSMODEL_CLASS_LOADER = AddressModel.class.getClassLoader();
    private static final ClassLoader IMAGEMODEL_CLASS_LOADER = ImageModel.class.getClassLoader();

    protected OfferModel(Parcel in) {
        id = in.readString();
        description = in.readString();
        name = in.readString();
        keyword = in.readString();
        isActive = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        isSmoker = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        acceptAnimal = (Boolean) in.readValue(BOOLEAN_CLASS_LOADER);
        price = (Integer) in.readValue(INTEGER_CLASS_LOADER);
        rooms = (Integer) in.readValue(INTEGER_CLASS_LOADER);
        availabilityEnds = (Date) in.readSerializable();
        availabilityStarts = (Date) in.readSerializable();
        owner = in.readParcelable(USERMODEL_CLASS_LOADER);
        address = in.readParcelable(ADDRESSMODEL_CLASS_LOADER);
        images = in.readArrayList(IMAGEMODEL_CLASS_LOADER);
        resident = in.readArrayList(USERMODEL_CLASS_LOADER);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(name);
        dest.writeString(keyword);
        dest.writeValue(isActive);
        dest.writeValue(isSmoker);
        dest.writeValue(acceptAnimal);
        dest.writeValue(price);
        dest.writeValue(rooms);
        dest.writeSerializable(availabilityEnds);
        dest.writeSerializable(availabilityStarts);
        dest.writeParcelable(owner, flags);
        dest.writeParcelable(address, flags);
        dest.writeList(images);
        dest.writeList(resident);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OfferModel> CREATOR = new Creator<OfferModel>() {
        @Override
        public OfferModel createFromParcel(Parcel in) {
            return new OfferModel(in);
        }

        @Override
        public OfferModel[] newArray(int size) {
            return new OfferModel[size];
        }
    };

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
