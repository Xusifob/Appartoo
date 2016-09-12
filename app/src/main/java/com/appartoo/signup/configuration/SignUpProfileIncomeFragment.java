package com.appartoo.signup.configuration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.appartoo.R;

/**
 * Created by alexandre on 16-07-12.
 */
public class SignUpProfileIncomeFragment extends Fragment {

    private EditText income;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_profile_income, container, false);

        income = (EditText) rootView.findViewById(R.id.signUpConfigureIncome);

        return rootView;
    }

    public String getIncome() {
        return income.getText().toString();
    }
}
