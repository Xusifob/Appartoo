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
public class SignUpEmailFragment extends ValidationFragment {

    private TextView email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_signup_email, container, false);

        email = (EditText) rootView.findViewById(R.id.signUpMail);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        email.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        if(email == null || !TextValidator.isEmail(email.getText().toString())) {
            Toast.makeText(context, R.string.please_enter_valid_email, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    public String getEmail() {
        return email.getText().toString().trim();
    }
}
