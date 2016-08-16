package com.appartoo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.model.UserModel;

/**
 * Created by alexandre on 16-07-18.
 */
public class UserDetailFragment extends Fragment {
    UserModel user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        user = getActivity().getIntent().getParcelableExtra("user");
        populateView();
    }

    private void populateView(){
        ((TextView) getActivity().findViewById(R.id.userName)).setText(user.getGivenName());
        String job = user.getContract();
        if (user.getSociety() != null) {
            job += ", " + user.getSociety();
        }

        ((TextView) getActivity().findViewById(R.id.userContractAndSociety)).setText(job);
        ((TextView) getActivity().findViewById(R.id.userAge)).setText(Integer.toString(user.getAge()) + " " + getResources().getString(R.string.year_age));
        ((TextView) getActivity().findViewById(R.id.userDescription)).setText(user.getDescription());

        if(user.getSmoker() != null && user.getSmoker() == true) {
            ((ImageView) getActivity().findViewById(R.id.residentIsSmoker)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.is_smoker, null));
        } else if(user.getSmoker() != null){
            ((ImageView) getActivity().findViewById(R.id.residentIsSmoker)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.is_not_smoker, null));
        }

        if(user.getInRelationship() != null && user.getInRelationship() == true) {
            ((ImageView) getActivity().findViewById(R.id.residentIsSingle)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.couple, null));
        }

        if(user.getContract().equals("worker")) {
            ((ImageView) getActivity().findViewById(R.id.residentContract)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.worker, null));
        }
    }
}
