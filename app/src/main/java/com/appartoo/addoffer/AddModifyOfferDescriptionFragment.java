package com.appartoo.addoffer;


import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.utils.TextValidator;
import com.appartoo.utils.ValidationFragment;
import com.appartoo.utils.model.OfferModel;

import org.w3c.dom.Text;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddModifyOfferDescriptionFragment extends ValidationFragment {

    private EditText description;
    private String descriptionStr;
    private TextView fragmentTitle;
    private Integer textReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_description, container, false);

        description = (EditText) rootView.findViewById(R.id.addOfferDescription);
        fragmentTitle = (TextView) rootView.findViewById(R.id.addOfferDescriptionTitle);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(descriptionStr != null) description.setText(descriptionStr);
        if(textReference != null) fragmentTitle.setText(textReference);
    }

    public String getDescription() {
        return description.getText().toString();
    }

    @Override
    public boolean validateFragment(Context context) {
        if(!TextValidator.haveText(description.getText().toString())) {
            Toast.makeText(context, R.string.error_missing_description, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void setData(OfferModel offerModel) {
        super.setData(offerModel);
        this.descriptionStr = offerModel.getDescription();
    }

    @Override
    public void modifyViews() {
        super.modifyViews();
        textReference = R.string.add_offer_description_modify;
    }
}
