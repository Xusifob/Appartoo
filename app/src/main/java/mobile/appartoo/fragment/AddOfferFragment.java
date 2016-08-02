package mobile.appartoo.fragment;

import android.app.DatePickerDialog;
import android.location.Geocoder;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.Void;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import mobile.appartoo.R;
import mobile.appartoo.adapter.PlacesAdapter;
import mobile.appartoo.model.AddressComponent;
import mobile.appartoo.model.AddressInformationsModel;
import mobile.appartoo.model.OfferToCreateModel;
import mobile.appartoo.model.PlaceModel;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.GeocoderResponse;
import mobile.appartoo.utils.GoogleMapsService;
import mobile.appartoo.utils.RestService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexandre on 16-07-20.
 */
public class AddOfferFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private EditText availaibilityStart;
    private EditText availaibilityEnd;
    private GoogleApiClient googleApiClient;
    private Button addOfferButton;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private RestService restService;
    private GoogleMapsService googleGeocodingService;
    private View rootView;
    private Geocoder geocoder;
    private AutoCompleteTextView placesAutocomplete;
    private PlacesAdapter placesAdapter;
    private ArrayList<PlaceModel> places;
    private PlaceModel selectedPlace;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_offer, container, false);

        availaibilityStart = (EditText) rootView.findViewById(R.id.addOfferAvailabilityStarts);
        availaibilityEnd = (EditText) rootView.findViewById(R.id.addOfferAvailabilityEnds);
        addOfferButton = (Button) rootView.findViewById(R.id.addOfferSaveButton);

        places = new ArrayList<>();
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

        placesAutocomplete = (AutoCompleteTextView) rootView.findViewById(R.id.addOfferAddress);
        googleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (container != null) {
            container.removeAllViews();
        }

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        //Build a retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restService = retrofit.create(RestService.class);

        Retrofit geocodingRetrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        googleGeocodingService = geocodingRetrofit.create(GoogleMapsService.class);

        availaibilityStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });

        availaibilityEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });

        addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOfferButton.setEnabled(false);
                new AsyncOffer().execute();
            }
        });


        googleApiClient.connect();
        places = new ArrayList<>();

        placesAdapter = new PlacesAdapter(getActivity(), 0, places);
        placesAutocomplete.setAdapter(placesAdapter);

        placesAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPlace = places.get(position);

            }
        });

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
                                Toast.makeText(getActivity(), "Impossible de prédire un endroit", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void openDatePicker(View v) {

        final EditText dateEditText = (EditText) v;
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateEditText.setText(dateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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

    private class AsyncOffer extends AsyncTask<Void, Void, OfferToCreateModel> {

        @Override
        protected OfferToCreateModel doInBackground(Void... params) {
            return getOfferModel();
        }

        @Override
        public void onPostExecute(OfferToCreateModel offerModel){
            if(offerModel == null) {
                addOfferButton.setEnabled(true);
            } else {
                setAddress(offerModel);
            }
        }

        private OfferToCreateModel getOfferModel(){
            OfferToCreateModel offerModel = new OfferToCreateModel();

            String name = ((EditText) rootView.findViewById(R.id.addOfferTitle)).getText().toString();
            String keyword = ((EditText) rootView.findViewById(R.id.addOfferKeyword)).getText().toString();
            String availibityStartsStr = availaibilityStart.getText().toString();
            String availibityEndsStr = availaibilityEnd.getText().toString();
            String price = ((EditText) rootView.findViewById(R.id.addOfferPrice)).getText().toString();
            String rooms = ((EditText) rootView.findViewById(R.id.addOfferRooms)).getText().toString();
            String phone = ((EditText) rootView.findViewById(R.id.addOfferPhone)).getText().toString();
            String description = ((EditText) rootView.findViewById(R.id.addOfferDescription)).getText().toString();
            boolean acceptAnimals = Boolean.valueOf(rootView.findViewById(R.id.addOfferImageAnimals).getTag().toString());
            boolean acceptSmoker = Boolean.valueOf(rootView.findViewById(R.id.addOfferImageSmoker).getTag().toString());

            if(!name.replaceAll("\\s+","").equals("")) offerModel.setOffer_name(name.trim()); else return null;
            if(!description.replaceAll("\\s+","").equals("")) offerModel.setDescription(description.trim()); else return null;
            if(!phone.replaceAll("\\s+","").equals("")) offerModel.setPhone(phone.trim()); else return null;
            if(!keyword.replaceAll("\\s+","").equals("")) offerModel.setKeyword(keyword.trim()); else return null;

            try {
                offerModel.setStart(dateFormat.parse(availibityStartsStr));
                offerModel.setEnd(dateFormat.parse(availibityEndsStr));
            } catch (Exception e){
                return null;
            }

            if(!price.replaceAll("\\s+","").equals("")) offerModel.setPrice(Integer.valueOf(price)); else return null;
            if(!rooms.replaceAll("\\s+","").equals("")) offerModel.setRooms(Integer.valueOf(rooms)); else return null;

            offerModel.setAcceptAnimal(acceptAnimals);
            offerModel.setSmoker(acceptSmoker);

            return offerModel;
        }

        public void setAddress(final OfferToCreateModel offerModel) {
            if(selectedPlace.getPlaceId() != null) {
                Call<GeocoderResponse> callback = googleGeocodingService.getAddressFromPlaceId(selectedPlace.getPlaceId(), getResources().getString(R.string.google_maps_key));

                callback.enqueue(new Callback<GeocoderResponse>() {
                    @Override
                    public void onResponse(Call<GeocoderResponse> call, Response<GeocoderResponse> response) {
                        if (response.isSuccessful()) {
                            AddressInformationsModel addressInformations = response.body().getResults().get(0);
                            for (AddressComponent component : addressInformations.getAddress_components()) {
                                if (component.getTypes().contains("locality"))
                                    offerModel.setAddressLocality(component.getLong_name());
                                if (component.getTypes().contains("country"))
                                    offerModel.setCountry(component.getLong_name());
                                if(component.getTypes().contains("administrative_area_level_2"))
                                    offerModel.setAddressRegion(component.getLong_name());
                                if (component.getTypes().contains("administrative_area_level_1"))
                                    offerModel.setAddressRegion(component.getLong_name());
                                if (component.getTypes().contains("postal_code"))
                                    offerModel.setPostalCode(component.getLong_name());
                            }
                            offerModel.setLatitude(addressInformations.getLatitude());
                            offerModel.setLongitude(addressInformations.getLongitude());
                            offerModel.setFormattedAddress(addressInformations.getFormatted_address());
                            offerModel.setName("No name");
                            offerModel.setStreetAddress(selectedPlace.getPrimaryText());
                            offerModel.setPlaceId(selectedPlace.getPlaceId());
                            sendOfferToServer(offerModel);
                        }
                    }

                    @Override
                    public void onFailure(Call<GeocoderResponse> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), "Impossible de définir l'addresse.", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Impossible de définir l'addresse.", Toast.LENGTH_SHORT).show();
            }
        }

        private void sendOfferToServer(OfferToCreateModel offerModel) {
            if(offerModel != null) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                Type stringStringMap = new TypeToken<Map<String, String>>(){}.getType();
                Map<String,String> offerModelMap = gson.fromJson(gson.toJson(offerModel), stringStringMap);

                while(offerModelMap.values().contains(null)) {
                    offerModelMap.values().remove(null);
                }

                System.out.println(offerModelMap.toString());
                Call<ResponseBody> callback = restService.addOffer("Bearer (" + Appartoo.TOKEN + ")", offerModelMap);
                callback.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        addOfferButton.setEnabled(true);

                        if(response.isSuccessful()){
                            Toast.makeText(getActivity().getApplicationContext(), "Votre annonce a été ajoutée avec succès", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        } else {
                            try {
                                System.out.println(response.code());
                                System.out.println(response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(), "Erreur de connexion au serveur.", Toast.LENGTH_SHORT).show();
                        addOfferButton.setEnabled(true);
                    }
                });
            } else {
                addOfferButton.setEnabled(true);
            }
        }
    }
}
