package com.appartoo.fragment.addoffer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.activity.AddOfferActivity;
import com.appartoo.adapter.PlacesAdapter;
import com.appartoo.model.PlaceModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by alexandre on 16-07-12.
 */
public class AddOfferThirdFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private AutoCompleteTextView placesAutocomplete;
    private PlacesAdapter placesAdapter;
    private ArrayList<PlaceModel> places;
    private PlaceModel selectedPlace;
    private GoogleApiClient googleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_page03, container, false);
        places = new ArrayList<>();
        placesAdapter = new PlacesAdapter(getActivity(), 0, places);
        placesAutocomplete = (AutoCompleteTextView) rootView.findViewById(R.id.addOfferAddress);

        googleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        googleApiClient.connect();
        placesAutocomplete.setAdapter(placesAdapter);

        placesAutocomplete.addTextChangedListener(new TextWatcher() {
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
                                System.out.println(status.getStatusMessage());
                                Toast.makeText(getActivity(), R.string.error_predicate_place, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        placesAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPlace = places.get(position);
                if(getActivity() instanceof AddOfferActivity) {
                    ((AddOfferActivity) getActivity()).setSelectedPlace(selectedPlace);
                }
            }
        });
    }

    @Override
    public void onStop(){
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
