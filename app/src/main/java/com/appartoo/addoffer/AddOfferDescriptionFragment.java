package com.appartoo.addoffer;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.utils.ValidationFragment;
import com.appartoo.utils.model.OfferModel;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferDescriptionFragment extends ValidationFragment {

    private EditText description;
    private String descriptionStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_description, container, false);

        description = (EditText) rootView.findViewById(R.id.addOfferDescription);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(descriptionStr != null) description.setText(descriptionStr);
    }

    public String getDescription() {
        return description.getText().toString();
    }

    @Override
    public void setData(OfferModel offerModel) {
        super.setData(offerModel);
        this.descriptionStr = offerModel.getDescription();
    }
}
