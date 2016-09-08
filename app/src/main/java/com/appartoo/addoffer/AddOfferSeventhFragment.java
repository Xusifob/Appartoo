package com.appartoo.addoffer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.utils.Appartoo;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferSeventhFragment extends Fragment {

    private TextView phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_page07, container, false);

        phone = (TextView) rootView.findViewById(R.id.addOfferPhone);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(Appartoo.LOGGED_USER_PROFILE != null && Appartoo.LOGGED_USER_PROFILE.getTelephone() != null)
            phone.setText(Appartoo.LOGGED_USER_PROFILE.getTelephone());
    }
}
