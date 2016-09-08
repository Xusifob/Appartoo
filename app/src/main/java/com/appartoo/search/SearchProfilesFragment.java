package com.appartoo.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.signup.SignUpFirstFragment;
import com.appartoo.signup.SignUpFourthFragment;
import com.appartoo.signup.SignUpSecondFragment;
import com.appartoo.signup.SignUpThirdFragment;
import com.appartoo.utils.adapter.PlacesAdapter;
import com.appartoo.utils.model.PlaceModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by alexandre on 16-07-20.
 */
public class SearchProfilesFragment extends Fragment {

    private View rootView;
    private LinearLayout genderToggle;
    private LinearLayout smokerToggle;
    private LinearLayout coupleToggle;
    private LinearLayout workerToggle;
    private HashMap<String, Integer> toggledViews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_profile, container, false);
        rootView = view;

        genderToggle = (LinearLayout) view.findViewById(R.id.searchGender);
        smokerToggle = (LinearLayout) view.findViewById(R.id.searchSmoker);
        coupleToggle = (LinearLayout) view.findViewById(R.id.searchCouple);
        workerToggle = (LinearLayout) view.findViewById(R.id.searchWorker);

        return view;
    }

    public HashMap<String, Object> getProfileQuery() {
        if(rootView == null || !((CheckBox) rootView.findViewById(R.id.searchProfilesCheckbox)).isChecked()) {
            return null;
        }

        HashMap<String, Object> map = new HashMap<>();

        if(genderToggle.getTag() != null) map.put("gender", String.valueOf(genderToggle.getTag()));
        if(smokerToggle.getTag() != null) map.put("smoker", Boolean.valueOf(smokerToggle.getTag().toString()));
        if(coupleToggle.getTag() != null) map.put("relationship", Boolean.valueOf(coupleToggle.getTag().toString()));
        if(workerToggle.getTag() != null) map.put("job", Boolean.valueOf(workerToggle.getTag().toString()));

        return map;
    }

    @Override
    public void onPause() {
        super.onPause();

        toggledViews = new HashMap<>();

        if(genderToggle.getTag() != null) {
            for (int i = 0 ; i < genderToggle.getChildCount(); i++) {
                if (genderToggle.getTag().equals(genderToggle.getChildAt(i).getTag())) {
                    toggledViews.put("gender", i);
                    break;
                }
            }
        }

        if(smokerToggle.getTag() != null) {
            for(int i = 0 ; i < smokerToggle.getChildCount() ; i++) {
                if(smokerToggle.getTag().equals(smokerToggle.getChildAt(i).getTag())) {
                    toggledViews.put("smoker", i);
                    break;
                }
            }
        }

        if(coupleToggle.getTag() != null) {
            for (int i = 0 ; i < coupleToggle.getChildCount(); i++) {
                if (coupleToggle.getTag().equals(coupleToggle.getChildAt(i).getTag())) {
                    toggledViews.put("couple", i);
                    break;
                }
            }
        }

        if(workerToggle.getTag() != null) {
            for(int i = 0 ; i < workerToggle.getChildCount() ; i++) {
                if(workerToggle.getTag().equals(workerToggle.getChildAt(i).getTag())) {
                    toggledViews.put("worker", i);
                    break;
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity() instanceof SearchActivity && toggledViews != null) {
            if(toggledViews.get("gender") != null) ((SearchActivity) getActivity()).toggleView(genderToggle.getChildAt(toggledViews.get("gender")));
            if(toggledViews.get("smoker") != null) ((SearchActivity) getActivity()).toggleView(smokerToggle.getChildAt(toggledViews.get("smoker")));
            if(toggledViews.get("worker") != null) ((SearchActivity) getActivity()).toggleView(workerToggle.getChildAt(toggledViews.get("worker")));
            if(toggledViews.get("couple") != null) ((SearchActivity) getActivity()).toggleView(coupleToggle.getChildAt(toggledViews.get("couple")));
        }
    }
}
