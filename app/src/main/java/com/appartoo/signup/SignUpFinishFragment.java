package com.appartoo.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appartoo.R;
import com.appartoo.utils.ValidationFragment;

/**
 * Created by alexandre on 16-07-12.
 */
public class SignUpFinishFragment extends ValidationFragment {

    private Button signUpButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_signup_finish, container, false);

        signUpButton = (Button) rootView.findViewById(R.id.finishSignUpButton);

        return rootView;
    }

    public void setButtonEnabled(boolean enabled){
        signUpButton.setEnabled(enabled);
    }
}