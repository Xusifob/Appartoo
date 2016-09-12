package com.appartoo.signup;

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

/**
 * Created by alexandre on 16-07-12.
 */

public class SignUpNameFragment extends ValidationFragment {

    private EditText givenName;
    private EditText familyName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_signup_name, container, false);

        givenName = (EditText) rootView.findViewById(R.id.signUpGivenName);
        familyName = (EditText) rootView.findViewById(R.id.signUpFamilyName);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        familyName.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        familyName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_NEXT && getActivity() instanceof SignUpActivity) {
                    ((SignUpActivity) getActivity()).nextView(textView);

                }

                return true;
            }
        });
    }

    @Override
    public boolean validateFragment(Context context) {
        if(givenName == null || !TextValidator.haveText(givenName.getText().toString())) {
            Toast.makeText(context, R.string.please_enter_valid_givenname, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(familyName == null || !TextValidator.haveText(familyName.getText().toString())) {
            Toast.makeText(context, R.string.please_enter_valid_familyname, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    public String getGivenName() {
        return givenName.getText().toString().trim();
    }

    public String getFamilyName() {
        return familyName.getText().toString().trim();
    }
}
