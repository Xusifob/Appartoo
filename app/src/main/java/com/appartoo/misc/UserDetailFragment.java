package com.appartoo.misc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.utils.model.UserModel;
import com.appartoo.utils.TextValidator;

/**
 * Created by alexandre on 16-07-18.
 */
public class UserDetailFragment extends Fragment {

    private TextView userContractAndSociety;
    private TextView userAge;
    private TextView userDescription;
    private TextView userName;
    private ImageView userIsSmoker;
    private ImageView userIsInRelationship;
    private ImageView userIsWorker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_detail, container, false);

        userContractAndSociety = (TextView) rootView.findViewById(R.id.userDetailContractAndSociety);
        userAge = (TextView) rootView.findViewById(R.id.userDetailAge);
        userName = (TextView) rootView.findViewById(R.id.userDetailName);
        userDescription = (TextView) rootView.findViewById(R.id.userDetailDescription);
        userIsSmoker = (ImageView) rootView.findViewById(R.id.userDetailSmoker);
        userIsInRelationship = (ImageView) rootView.findViewById(R.id.userDetailRelationship);
        userIsWorker = (ImageView) rootView.findViewById(R.id.userDetailIsWorker);

        return rootView;
    }

    public void bindData(UserModel user){
        userName.setText(user.getGivenName());


        String job = "";
        if(user.getSociety() != null) job += user.getSociety() + ", ";
        if(user.getSociety() != null) job += user.getFunction() + ", ";
        if (user.getContract() != null) job += user.getContract();
        else if(job.length() > 2) job = job.substring(0, job.length()-2);

        if(!TextValidator.haveText(job)) userContractAndSociety.setText(R.string.no_job_refered);
        else userContractAndSociety.setText(job);

        if(user.getAge() > 0) {
            userAge.setText(Integer.toString(user.getAge()) + " " + getResources().getString(R.string.year_age));
        }

        if (user.getDescription() == null || !TextValidator.haveText(user.getDescription())) userDescription.setText(R.string.no_description);
        else userDescription.setText(user.getDescription());

        if(user.getSmoker() != null && user.getSmoker()) userIsSmoker.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.is_smoker_big, null));
        else if(user.getSmoker() != null) userIsSmoker.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.is_not_smoker_big, null));

        if(user.getInRelationship() != null && user.getInRelationship()) userIsInRelationship.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.couple_big, null));
        else if (user.getInRelationship() != null) userIsInRelationship.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.single_big, null));

        if(user.getContract() != null && user.getContract().equals("worker")) userIsWorker.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.worker_big, null));
    }
}