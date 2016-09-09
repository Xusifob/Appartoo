package com.appartoo.addoffer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appartoo.R;
import com.appartoo.utils.ValidationFragment;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferSmokerAnimalsFragment extends ValidationFragment {

    View acceptAnimals;
    View allowSmoker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_smoker_animals, container, false);

        acceptAnimals = rootView.findViewById(R.id.addOfferAcceptAnimals);
        allowSmoker = rootView.findViewById(R.id.addOfferAllowSmoker);

        return rootView;
    }

    public Boolean getAcceptAnimals() {
        return (Boolean) acceptAnimals.getTag();
    }

    public Boolean getAllowSmoker() {
        return (Boolean) allowSmoker.getTag();
    }


}
