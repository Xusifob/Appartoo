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

/**
 * Created by alexandre on 16-07-12.
 */
public class AddModifyOfferTitleFragment extends ValidationFragment {

    private EditText title;
    private TextView fragmentTitle;
    private Integer textReference;
    private String titleStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_title, container, false);

        fragmentTitle = (TextView) rootView.findViewById(R.id.addOfferTitleFragTitle);
        title = (EditText) rootView.findViewById(R.id.addOfferTitle);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(titleStr != null) title.setText(titleStr);
        if(textReference != null) fragmentTitle.setText(textReference);

        title.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_NEXT && getActivity() instanceof AddModifyOfferActivity) ((AddModifyOfferActivity) getActivity()).nextView(textView);
                return true;
            }
        });
    }

    @Override
    public boolean validateFragment(Context context) {
        if(!TextValidator.haveText(title.getText().toString())) {
            Toast.makeText(context, R.string.error_missing_title, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(title.getText().toString().length() > 255) {
            Toast.makeText(context, R.string.error_title_over_255_characters, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public String getTitle() {
        return title.getText().toString();
    }

    @Override
    public void setData(OfferModel offerModel) {
        super.setData(offerModel);

        this.titleStr = offerModel.getName();
    }

    @Override
    public void modifyViews() {
        super.modifyViews();
        textReference = R.string.add_offer_title_modify;
    }
}