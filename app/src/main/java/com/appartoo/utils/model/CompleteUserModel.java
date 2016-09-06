package com.appartoo.utils.model;

import android.os.Parcel;

/**
 * Created by alexandre on 16-07-22.
 */
public class CompleteUserModel extends UserProfileModel {

    private SimpleUserModel user;

    private static final ClassLoader SIMPLEUSERMODEL_CLASS_LOADER = SimpleUserModel.class.getClassLoader();

    public CompleteUserModel() {
        super();
    }

    protected CompleteUserModel(Parcel in) {
        super(in);
        user = in.readParcelable(SIMPLEUSERMODEL_CLASS_LOADER);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(user, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CompleteUserModel> CREATOR = new Creator<CompleteUserModel>() {
        @Override
        public CompleteUserModel createFromParcel(Parcel in) {
            return new CompleteUserModel(in);
        }

        @Override
        public CompleteUserModel[] newArray(int size) {
            return new CompleteUserModel[size];
        }
    };

    public SimpleUserModel getUser() {
        return user;
    }

    public void setUser(SimpleUserModel user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CompleteUserModel{" +
                "user=" + user +
                '}';
    }
}
