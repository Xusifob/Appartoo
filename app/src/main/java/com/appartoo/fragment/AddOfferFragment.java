package com.appartoo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by alexandre on 16-07-20.
 */
public class AddOfferFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    private EditText availabilityStart;
//    private EditText availabilityEnd;
//    private EditText keyword;
//    private Button addOfferButton;
//    private SimpleDateFormat dateFormat;
//
//    private View rootView;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.fragment_add_offer, container, false);
//
//        keyword = (EditText) rootView.findViewById(R.id.addOfferKeyword);
//        addOfferButton = (Button) rootView.findViewById(R.id.addOfferSaveButton);
//
//
//
//        if (container != null) {
//            container.removeAllViews();
//        }
//
//        return rootView;
//    }
//
//    @Override
//    public void onStart(){
//        super.onStart();
//
//        addOfferButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addOfferButton.setEnabled(false);
//                new AsyncOffer().execute();
//            }
//        });
//
//        setRetrofitRequests();
//        setDateListeners();
//    }
//
//
//
//    private void setRetrofitRequests() {
////        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Appartoo.SERVER_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        restService = retrofit.create(RestService.class);
//
//        Retrofit geocodingRetrofit = new Retrofit.Builder()
//                .baseUrl("https://maps.googleapis.com/maps/api/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        googleGeocodingService = geocodingRetrofit.create(GoogleMapsService.class);
//
//
//
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {}
//
//    @Override
//    public void onConnectionSuspended(int i) {}
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}