package com.appartoo.addoffer;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.utils.ValidationFragment;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddModifyOfferSuccessFragment extends ValidationFragment {

    private TextView successMessage;
    private Integer textReference;
    private Integer textButtonReference;
    private Button finishButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_success, container, false);
        successMessage = (TextView) rootView.findViewById(R.id.endOfAddModifyOffer);
        finishButton = (Button) rootView.findViewById(R.id.endOfAddModifyOfferButton);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(textReference != null) successMessage.setText(textReference);
        if(textButtonReference != null) finishButton.setText(textButtonReference);
    }

    @Override
    public void modifyViews() {
        super.modifyViews();
        textReference = R.string.end_of_modify_offer;
        textButtonReference = R.string.end_of_modify_offer_button;
    }
}
