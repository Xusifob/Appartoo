package com.appartoo.addoffer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.utils.ValidationFragment;
import com.appartoo.utils.adapter.PlacesAdapter;
import com.appartoo.utils.model.OfferModel;
import com.appartoo.utils.model.PlaceModel;
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
public class AddOfferAddressFragment extends ValidationFragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private AutoCompleteTextView placesAutocomplete;
    private PlacesAdapter placesAdapter;
    private ArrayList<PlaceModel> places;
    private PlaceModel selectedPlace;
    private GoogleApiClient googleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_offer_address, container, false);

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

        if(selectedPlace != null) {
            placesAutocomplete.setText(selectedPlace.getFullText());
        }

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
            }
        });

        placesAutocomplete.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        placesAutocomplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_NEXT && getActivity() instanceof AddOfferActivity) ((AddOfferActivity) getActivity()).nextView(textView);
                return true;
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

    @Override
    public boolean validateFragment(Context context) {
        if(selectedPlace == null) {
            Toast.makeText(context, R.string.error_missing_place, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public PlaceModel getSelectedPlace() {
        return selectedPlace;
    }

    @Override
    public void setData(OfferModel offerModel) {
        super.setData(offerModel);

        this.selectedPlace = new PlaceModel();

        selectedPlace.setPlaceId(offerModel.getAddress().getPlaceId());
        selectedPlace.setFullText(offerModel.getAddress().getFormattedAddress());
    }
}
