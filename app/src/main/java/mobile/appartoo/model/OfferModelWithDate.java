package mobile.appartoo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by alexandre on 16-07-05.
 */
public class OfferModelWithDate extends OfferModel implements Parcelable {

    private UserModel owner;
    private Date availabilityEnds;
    private Date availabilityStarts;

    private static final ClassLoader USERMODEL_CLASS_LOADER = UserModel.class.getClassLoader();

    public OfferModelWithDate() {
        super();
    };

    protected OfferModelWithDate(Parcel in) {
        super(in);
        owner = in.readParcelable(USERMODEL_CLASS_LOADER);
        availabilityEnds = (Date) in.readSerializable();
        availabilityStarts = (Date) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(owner, flags);
        dest.writeSerializable(availabilityEnds);
        dest.writeSerializable(availabilityStarts);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OfferModelWithDate> CREATOR = new Creator<OfferModelWithDate>() {
        @Override
        public OfferModelWithDate createFromParcel(Parcel in) {
            return new OfferModelWithDate(in);
        }

        @Override
        public OfferModelWithDate[] newArray(int size) {
            return new OfferModelWithDate[size];
        }
    };

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

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }
}