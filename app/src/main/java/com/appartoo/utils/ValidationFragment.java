package com.appartoo.utils;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.appartoo.utils.model.OfferModel;

/**
 * Created by alexandre on 16-09-09.
 */
public abstract class ValidationFragment extends Fragment {

    public boolean validateFragment(Context context) {
        return true;
    }
    public void modifyViews() {}
    public void setData(OfferModel offerModel){}
}
