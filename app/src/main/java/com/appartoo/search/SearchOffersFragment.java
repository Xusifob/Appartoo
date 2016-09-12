package com.appartoo.search;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.utils.GeocoderResponse;
import com.appartoo.utils.GoogleServices;
import com.appartoo.utils.adapter.PlacesAdapter;
import com.appartoo.utils.model.AddressInformationsModel;
import com.appartoo.utils.model.PlaceModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexandre on 16-07-20.
 */
public class SearchOffersFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private RangeSeekBar<Integer> rangePriceSeekBar;
    private RangeSeekBar<Integer> rangeRoomsSeekBar;
    private AutoCompleteTextView searchPlace;
    private TextView priceMinValue;
    private TextView priceMaxValue;
    private TextView roomsMinValue;
    private TextView roomsMaxValue;
    private EditText availabilityStart;
    private EditText availabilityEnd;
    private LinearLayout acceptAnimals;
    private LinearLayout acceptSmokers;
    private GoogleApiClient googleApiClient;
    private PlacesAdapter placesAdapter;
    private AddressInformationsModel searchPlaceTag;
    private ArrayList<PlaceModel> places;
    private View rootView;
    private HashMap<String, Integer> toggledViews;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_offer, container, false);
        rootView = view;

        rangePriceSeekBar = (RangeSeekBar) view.findViewById(R.id.searchOfferPrice);
        rangeRoomsSeekBar = (RangeSeekBar) view.findViewById(R.id.searchOfferRooms);
        searchPlace = (AutoCompleteTextView) view.findViewById(R.id.searchPlace);
        priceMinValue = (TextView) view.findViewById(R.id.searchPriceMinValue);
        priceMaxValue = (TextView) view.findViewById(R.id.searchPriceMaxValue);
        roomsMinValue = (TextView) view.findViewById(R.id.searchRoomsMinValue);
        roomsMaxValue = (TextView) view.findViewById(R.id.searchRoomsMaxValue);

        acceptAnimals = (LinearLayout) view.findViewById(R.id.searchAcceptAnimals);
        acceptSmokers = (LinearLayout) view.findViewById(R.id.searchAcceptSmokers);

        availabilityEnd = (EditText) view.findViewById(R.id.searchAvailabilityEnds);
        availabilityStart = (EditText) view.findViewById(R.id.searchAvailabilityStarts);

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
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
        places = new ArrayList<>();

        placesAdapter = new PlacesAdapter(getActivity(), 0, places);
        searchPlace.setAdapter(placesAdapter);
        searchPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Places.GeoDataApi
                        .getAutocompletePredictions(googleApiClient, String.valueOf(s), null, null)
                        .setResultCallback(new ResultCallbacks<AutocompletePredictionBuffer>() {
                            @Override
                            public void onSuccess(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                                Iterator<AutocompletePrediction> placeIterator = autocompletePredictions.iterator();

                                places.clear();
                                while (places.size() < 5 && placeIterator.hasNext()) {
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

        searchPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                retrievePlaceLatLon(places.get(i).getPlaceId());
            }
        });

        rangePriceSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    int min = rangePriceSeekBar.getSelectedMinValue() - rangePriceSeekBar.getSelectedMinValue() % 10;
                    int max = rangePriceSeekBar.getSelectedMaxValue() - rangePriceSeekBar.getSelectedMaxValue() % 10;
                    priceMinValue.setText(Integer.toString(min));
                    priceMaxValue.setText(Integer.toString(max));
                }
                return rangePriceSeekBar.onTouchEvent(event);
            }
        });

        rangeRoomsSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    int max = rangeRoomsSeekBar.getSelectedMaxValue();
                    int min = rangeRoomsSeekBar.getSelectedMinValue();
                    roomsMinValue.setText(Integer.toString(min));
                    roomsMaxValue.setText(Integer.toString(max));
                }
                return rangeRoomsSeekBar.onTouchEvent(event);
            }
        });

        availabilityEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(availabilityEnd);
            }
        });

        availabilityStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(availabilityStart);
            }
        });

    }

    private void openDatePicker(final EditText v) {

        final Calendar calendar = Calendar.getInstance();

        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                v.setText(dateFormat.format(calendar.getTime()));
                v.setTag(calendar.getTime());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public HashMap<String, Object> getOfferQuery() {

        if(rootView == null || !((CheckBox) rootView.findViewById(R.id.searchOffersCheckbox)).isChecked()) {
            return null;
        }

        HashMap<String, Object> map = new HashMap<>();

        if(acceptAnimals.getTag() != null) map.put("animal", Boolean.valueOf((String) acceptAnimals.getTag()));
        if(acceptSmokers.getTag() != null) map.put("smoker", Boolean.valueOf((String) acceptSmokers.getTag()));
        if(availabilityStart.getTag() != null) map.put("availabilityStartsMin", availabilityStart.getTag());
        if(availabilityEnd.getTag() != null) map.put("availabilityStartsMax", availabilityEnd.getTag());

        if(searchPlace.getTag() != null) {
            double lat = ((AddressInformationsModel) searchPlace.getTag()).getLatitude();
            double lon = ((AddressInformationsModel) searchPlace.getTag()).getLongitude();

            map.put("latitudeCenter", lat);
            map.put("latitudeMin", lat - 10/110.574);
            map.put("latitudeMax", lat + 10/110.574);
            map.put("longitudeCenter", lon);
            map.put("longitudeMin", lon - 10/(111.320*Math.cos(Math.toRadians(lat))));
            map.put("longitudeMax", lon + 10/(111.320*Math.cos(Math.toRadians(lat))));
        }

        map.put("priceMin", Integer.valueOf(priceMinValue.getText().toString()));
        map.put("priceMax", Integer.valueOf(priceMaxValue.getText().toString()));
        map.put("roomsMin", Integer.valueOf(roomsMinValue.getText().toString()));
        map.put("roomsMax", Integer.valueOf(roomsMaxValue.getText().toString()));

        return map;
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

    private void retrievePlaceLatLon(String placeId) {
        Retrofit geocodingRetrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GoogleServices googleGeocodingService = geocodingRetrofit.create(GoogleServices.class);
        Call<GeocoderResponse> callback = googleGeocodingService.getAddressFromPlaceId(placeId, getResources().getString(R.string.google_maps_key));

        callback.enqueue(new Callback<GeocoderResponse>() {
            @Override
            public void onResponse(Call<GeocoderResponse> call, Response<GeocoderResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getResults().size() > 0) {
                        searchPlace.setTag(response.body().getResults().get(0));
                    } else {
                        try {
                            Log.v("SearchOffersFragment", "searchPlace.onItemClickListener");
                            Log.v("SearchOffersFragment", String.valueOf(response.code()));
                            Log.v("SearchOffersFragment", String.valueOf(response.errorBody().string()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getActivity().getApplicationContext(), R.string.error_predicate_place, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GeocoderResponse> call, Throwable t) {
                Log.v("SearchOffersFragment", "searchPlace.onItemClickListener");
                Log.v("SearchOffersFragment", t.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_predicate_place, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();

        toggledViews = new HashMap<>();
        searchPlaceTag = (AddressInformationsModel) searchPlace.getTag();

        if(acceptAnimals.getTag() != null) {
            for (int i = 0 ; i < acceptAnimals.getChildCount(); i++) {
                if (acceptAnimals.getTag().equals(acceptAnimals.getChildAt(i).getTag())) {
                    toggledViews.put("animal", i);
                    break;
                }
            }
        }

        if(acceptSmokers.getTag() != null) {
            for(int i = 0 ; i < acceptSmokers.getChildCount() ; i++) {
                if(acceptSmokers.getTag().equals(acceptSmokers.getChildAt(i).getTag())) {
                    toggledViews.put("smoker", i);
                    break;
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        priceMaxValue.setText(String.valueOf(rangePriceSeekBar.getSelectedMaxValue() - rangePriceSeekBar.getSelectedMaxValue() % 10));
        priceMinValue.setText(String.valueOf(rangePriceSeekBar.getSelectedMinValue() - rangePriceSeekBar.getSelectedMinValue() % 10));
        roomsMaxValue.setText(String.valueOf(rangeRoomsSeekBar.getSelectedMaxValue()));
        roomsMinValue.setText(String.valueOf(rangeRoomsSeekBar.getSelectedMinValue()));

        searchPlace.setTag(searchPlaceTag);

        if(getActivity() instanceof SearchActivity && toggledViews != null) {
            if(toggledViews.get("animal") != null) ((SearchActivity) getActivity()).toggleView(acceptAnimals.getChildAt(toggledViews.get("animal")));
            if(toggledViews.get("smoker") != null) ((SearchActivity) getActivity()).toggleView(acceptSmokers.getChildAt(toggledViews.get("smoker")));
        }
    }
}
