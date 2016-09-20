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
public class SignUpProfileWorkerFragment extends Fragment {

    View worker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_profile_worker, container, false);

        worker = rootView.findViewById(R.id.signUpConfigureWorker);

        return rootView;
    }

    public String getWorker() {
        return (String) worker.getTag();
    }
}
