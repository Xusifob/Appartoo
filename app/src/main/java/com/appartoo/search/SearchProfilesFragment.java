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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by alexandre on 16-07-20.
 */
public class SearchProfilesFragment extends Fragment {

    private RangeSeekBar<Integer> rangeSeekBar;
    private TextView minValue;
    private TextView maxValue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_profile, container, false);

        rangeSeekBar = (RangeSeekBar) view.findViewById(R.id.searchOfferPrice);
        minValue = (TextView) view.findViewById(R.id.searchRangeMinValue);
        maxValue = (TextView) view.findViewById(R.id.searchRangeMaxValue);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        rangeSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    int min = rangeSeekBar.getSelectedMinValue() - rangeSeekBar.getSelectedMinValue()%10;
                    int max = rangeSeekBar.getSelectedMaxValue() - rangeSeekBar.getSelectedMaxValue()%10;
                    minValue.setText(Integer.toString(min));
                    maxValue.setText(Integer.toString(max));
                }
                return rangeSeekBar.onTouchEvent(event);
            }
        });
    }


}
