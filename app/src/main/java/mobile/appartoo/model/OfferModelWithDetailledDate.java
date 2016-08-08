package mobile.appartoo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandre on 16-07-05.
 */
public class OfferModelWithDetailledDate extends OfferModel implements Parcelable {

    @SerializedName("owner")
    private Integer ownerId;
    private DetailledDateModel availabilityEnds;
    private DetailledDateModel availabilityStarts;

    private static final ClassLoader INTEGER_CLASS_LOADER = Integer.class.getClassLoader();

    protected OfferModelWithDetailledDate(Parcel in) {
        super(in);
        ownerId = (Integer) in.readValue(INTEGER_CLASS_LOADER);
        availabilityEnds = in.readParcelable(DetailledDateModel.class.getClassLoader());
        availabilityStarts = in.readParcelable(DetailledDateModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(ownerId);
        dest.writeParcelable(availabilityEnds, flags);
        dest.writeParcelable(availabilityStarts, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OfferModelWithDetailledDate> CREATOR = new Creator<OfferModelWithDetailledDate>() {
        @Override
        public OfferModelWithDetailledDate createFromParcel(Parcel in) {
            return new OfferModelWithDetailledDate(in);
        }

        @Override
        public OfferModelWithDetailledDate[] newArray(int size) {
            return new OfferModelWithDetailledDate[size];
        }
    };

    public DetailledDateModel getAvailabilityEnds() {
        return availabilityEnds;
    }

    public void setAvailabilityEnds(DetailledDateModel availabilityEnds) {
        this.availabilityEnds = availabilityEnds;
    }

    public DetailledDateModel getAvailabilityStarts() {
        return availabilityStarts;
    }

    public void setAvailabilityStarts(DetailledDateModel availabilityStarts) {
        this.availabilityStarts = availabilityStarts;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
}
