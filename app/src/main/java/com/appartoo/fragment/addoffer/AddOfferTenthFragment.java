package com.appartoo.fragment.addoffer;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.appartoo.R;
import com.appartoo.adapter.GarantorsAdapter;
import com.appartoo.model.GarantorModel;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferTenthFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_page10, container, false);
        return rootView;
    }
}
