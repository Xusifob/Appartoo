package com.appartoo.signup.configuration;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.utils.ValidationFragment;

/**
 * Created by alexandre on 16-07-12.
 */
public class SignUpProfileConfTypeFragment extends Fragment {

    View configurationType;
    public static final int SEARCH_OFFER_CONFIGURATION = 0;
    public static final int SEARCH_FLATMATE_CONFIGURATION = 1;
    public static final int PROPOSE_OFFER_CONFIGURATION = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_profile_configuration_type, container, false);

        configurationType = rootView.findViewById(R.id.signUpConfigurationType);

        return rootView;
    }

    public Integer getConfigurationType() {
        if(configurationType.getTag() == null) {
            return null;
        }

        return Integer.valueOf((String) configurationType.getTag());
    }
}
