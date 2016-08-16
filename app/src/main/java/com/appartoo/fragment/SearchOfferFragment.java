package com.appartoo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

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

import com.appartoo.R;
import com.appartoo.adapter.PlacesAdapter;
import com.appartoo.model.PlaceModel;

/**
 * Created by alexandre on 16-07-20.
 */
public class SearchOfferFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private RangeSeekBar<Integer> rangeSeekBar;
    private AutoCompleteTextView searchPlace;
    private TextView minValue;
    private TextView maxValue;
    private GoogleApiClient googleApiClient;
    private PlacesAdapter placesAdapter;
    private ArrayList<PlaceModel> places;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_offer, container, false);

        if (container != null) {
            container.removeAllViews();
        }

        rangeSeekBar = (RangeSeekBar) view.findViewById(R.id.searchOfferPrice);
        searchPlace = (AutoCompleteTextView) view.findViewById(R.id.searchPlace);
        minValue = (TextView) view.findViewById(R.id.searchRangeMinValue);
        maxValue = (TextView) view.findViewById(R.id.searchRangeMaxValue);
        googleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        googleApiClient.connect();
        places = new ArrayList<>();

        placesAdapter = new PlacesAdapter(getActivity(), 0, places);
        searchPlace.setAdapter(placesAdapter);

        searchPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Places.GeoDataApi
                        .getAutocompletePredictions(googleApiClient, String.valueOf(s), null, null)
                        .setResultCallback(new ResultCallbacks<AutocompletePredictionBuffer>(){
                            @Override
                            public void onSuccess(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                                Iterator<AutocompletePrediction> placeIterator = autocompletePredictions.iterator();

                                places.clear();
                                while(places.size() < 5 && placeIterator.hasNext()) {
                                    places.add(new PlaceModel(placeIterator.next()));
                                }

                                placesAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(@NonNull Status status) {
                                Toast.makeText(getActivity(), R.string.error_predicate_place, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

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

    @Override
    public void onStop(){
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}
