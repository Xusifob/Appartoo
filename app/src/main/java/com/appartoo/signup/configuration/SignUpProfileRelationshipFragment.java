package com.appartoo.signup.configuration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appartoo.R;

/**
 * Created by alexandre on 16-07-12.
 */
public class SignUpProfileRelationshipFragment extends Fragment {

    public View inRelationship;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_profile_status, container, false);

        inRelationship = rootView.findViewById(R.id.signUpConfigureStatus);

        return rootView;
    }

    public Boolean getInRelationship() {
        return Boolean.valueOf((String) inRelationship.getTag());
    }
}
