package mobile.appartoo.fragment;

import android.app.DatePickerDialog;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import mobile.appartoo.R;
import mobile.appartoo.adapter.PlacesAdapter;
import mobile.appartoo.model.OfferToCreateModel;
import mobile.appartoo.model.PlaceModel;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.RestService;
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
    private View rootView;
    private Geocoder geocoder;
    private AutoCompleteTextView placesAutocomplete;
    private AutoCompleteTextView offerAddressLocation;
    private PlacesAdapter placesAdapter;
    private ArrayList<PlaceModel> places;
    private PlaceModel selectedPlace;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_offer, container, false);

        availaibilityEnd = (EditText) rootView.findViewById(R.id.addOfferAvailabilityEnds);
        availaibilityStart = (EditText) rootView.findViewById(R.id.addOfferAvailabilityStarts);
        addOfferButton = (Button) rootView.findViewById(R.id.addOfferSaveButton);
        offerAddressLocation = (AutoCompleteTextView) rootView.findViewById(R.id.addOfferAddress);

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

        availaibilityEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });
        availaibilityStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });
        addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOffer();
            }
        });


        googleApiClient.connect();
        places = new ArrayList<>();

        placesAdapter = new PlacesAdapter(getActivity(), 0, places);
        placesAutocomplete.setAdapter(placesAdapter);

        placesAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    System.out.println(geocoder.getFromLocationName(places.get(position).toString(), 1).get(0).toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

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

    private void addOffer(){
        addOfferButton.setEnabled(false);
        OfferToCreateModel offerModel = getOfferModel();
        if(offerModel != null) {
            Call<OfferToCreateModel> callback = restService.addOffer("Bearer (" + Appartoo.TOKEN + ")", offerModel);
            callback.enqueue(new Callback<OfferToCreateModel>() {
                @Override
                public void onResponse(Call<OfferToCreateModel> call, Response<OfferToCreateModel> response) {
                    addOfferButton.setEnabled(true);

                    if(response.isSuccessful()){
                        Toast.makeText(getActivity().getApplicationContext(), "Votre annonce a été ajoutée avec succès", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    } else {
                        System.out.println(response.code());
                    }
                }

                @Override
                public void onFailure(Call<OfferToCreateModel> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Erreur de connexion au serveur.", Toast.LENGTH_SHORT).show();
                    addOfferButton.setEnabled(true);
                }
            });
        } else {
            addOfferButton.setEnabled(true);
        }
    }

    private OfferToCreateModel getOfferModel(){
        OfferToCreateModel offerModel = new OfferToCreateModel();
        Address address = null;
        String addressStr = ((AutoCompleteTextView) rootView.findViewById(R.id.addOfferAddress)).getText().toString();
        try {
            address = geocoder.getFromLocationName(addressStr, 1).get(0);
        } catch (Exception e) {
            return null;
        }

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
        offerModel.setAddressLocality(address.getLocality());
        offerModel.setCountry(address.getCountryName());
        offerModel.setLatitude(address.getLatitude());
        offerModel.setLongitude(address.getLongitude());
        offerModel.setName("Anonymous");
        offerModel.setAddressRegion(null);
        if(address.getMaxAddressLineIndex() > 1) {
            offerModel.setFormattedAddress(address.getAddressLine(0) + "\n" + address.getAddressLine(1));
        } else {
            offerModel.setFormattedAddress(address.getAddressLine(0));
        }

        offerModel.setStreetAddress(address.getThoroughfare());
        offerModel.setPostalCode(address.getPostalCode());
        if (selectedPlace != null && addressStr.trim().equals(selectedPlace.getFullText().trim())) offerModel.setPlaceId(selectedPlace.getPlaceId());

        System.out.println(offerModel.toString());
        return offerModel;

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
}
