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
public class SignUpPasswordFragment extends ValidationFragment {

    private EditText password;
    private EditText passwordConfirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_signup_password, container, false);

        password = (EditText) rootView.findViewById(R.id.signUpPassword);
        passwordConfirm = (EditText) rootView.findViewById(R.id.signUpPasswordConfirm);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        passwordConfirm.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        passwordConfirm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        String passwordStr = password.getText().toString();
        String passwordConfirmStr = passwordConfirm.getText().toString();
        if(!TextValidator.haveText(passwordStr) || !TextValidator.haveText(passwordConfirmStr)) {
            Toast.makeText(context, R.string.please_enter_passwords, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(passwordStr.length() < 8) {
            Toast.makeText(context, R.string.error_password_length, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!passwordStr.equals(passwordConfirmStr)) {
            Toast.makeText(context, R.string.error_password_not_identical, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public String getPassword() {
        return password.getText().toString().trim();
    }
}
