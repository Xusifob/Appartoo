package com.appartoo.addoffer;

import android.content.Context;
import android.inputmethodservice.Keyboard;
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

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferTitleFragment extends ValidationFragment {

    private EditText title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_title, container, false);

        title = (EditText) rootView.findViewById(R.id.addOfferTitle);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        title.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_NEXT && getActivity() instanceof AddOfferActivity) ((AddOfferActivity) getActivity()).nextView(textView);
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

        return true;
    }

    public String getTitle() {
        return title.getText().toString();
    }
}