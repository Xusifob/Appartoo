package com.appartoo.addoffer;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.TextValidator;
import com.appartoo.utils.ValidationFragment;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferPhoneFragment extends ValidationFragment {

    private TextView phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_phone, container, false);

        phone = (TextView) rootView.findViewById(R.id.addOfferPhone);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(Appartoo.LOGGED_USER_PROFILE != null && Appartoo.LOGGED_USER_PROFILE.getTelephone() != null)
            phone.setText(Appartoo.LOGGED_USER_PROFILE.getTelephone());

        phone.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_NEXT && getActivity() instanceof AddOfferActivity) ((AddOfferActivity) getActivity()).nextView(textView);
                return true;
            }
        });
    }

    @Override
    public boolean validateFragment(Context context) {
        if (!TextValidator.haveText(phone.getText().toString())) {
            Toast.makeText(context, R.string.error_missing_phone, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!TextValidator.isPhone(phone.getText().toString())) {
            Toast.makeText(context, R.string.error_correct_phone, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public String getTelephone() {
        return phone.getText().toString();
    }
}
