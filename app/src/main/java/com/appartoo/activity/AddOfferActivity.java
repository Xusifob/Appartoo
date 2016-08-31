package com.appartoo.activity;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.fragment.addoffer.AddOfferEighthFragment;
import com.appartoo.fragment.addoffer.AddOfferEleventhFragment;
import com.appartoo.fragment.addoffer.AddOfferFifthFragment;
import com.appartoo.fragment.addoffer.AddOfferFirstFragment;
import com.appartoo.fragment.addoffer.AddOfferFourthFragment;
import com.appartoo.fragment.addoffer.AddOfferNinthFragment;
import com.appartoo.fragment.addoffer.AddOfferSecondFragment;
import com.appartoo.fragment.addoffer.AddOfferSeventhFragment;
import com.appartoo.fragment.addoffer.AddOfferSixthFragment;
import com.appartoo.fragment.addoffer.AddOfferTenthFragment;
import com.appartoo.fragment.addoffer.AddOfferThirdFragment;
import com.appartoo.fragment.addoffer.AddOfferTwelfthFragment;
import com.appartoo.model.AddressComponent;
import com.appartoo.model.AddressInformationsModel;
import com.appartoo.model.ImageModel;
import com.appartoo.model.OfferModel;
import com.appartoo.model.OfferToCreateModel;
import com.appartoo.model.PlaceModel;
import com.appartoo.model.UserModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.GeocoderResponse;
import com.appartoo.utils.GoogleServices;
import com.appartoo.utils.RestService;
import com.appartoo.utils.TextValidator;
import com.appartoo.view.DisableLastSwipeViewPager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddOfferActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 12;
    private DisableLastSwipeViewPager pager;
    private PagerAdapter pagerAdapter;
    private Button addOfferButton;
    private GoogleServices googleGeocodingService;
    private SimpleDateFormat dateFormat;
    private RestService restService;
    private ArrayList<File> files;
    private PlaceModel selectedPlace;
    private AddOfferFirstFragment addOfferFirstFragment;
    private AddOfferSecondFragment addOfferSecondFragment;
    private AddOfferThirdFragment addOfferThirdFragment;
    private AddOfferFourthFragment addOfferFourthFragment;
    private AddOfferFifthFragment addOfferFifthFragment;
    private AddOfferSixthFragment addOfferSixthFragment;
    private AddOfferSeventhFragment addOfferSeventhFragment;
    private AddOfferEighthFragment addOfferEighthFragment;
    private AddOfferNinthFragment addOfferNinthFragment;
    private AddOfferTenthFragment addOfferTenthFragment;
    private AddOfferEleventhFragment addOfferEleventhFragment;
    private AddOfferTwelfthFragment addOfferTwelfthFragment;
    private ProgressDialog progressDialog;
    private ArrayList<UserModel> residents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        pager = (DisableLastSwipeViewPager) findViewById(R.id.addOfferPager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        addOfferButton = (Button) findViewById(R.id.finishAddOfferButton);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        addOfferFirstFragment = new AddOfferFirstFragment();
        addOfferSecondFragment = new AddOfferSecondFragment();
        addOfferThirdFragment = new AddOfferThirdFragment();
        addOfferFourthFragment = new AddOfferFourthFragment();
        addOfferFifthFragment = new AddOfferFifthFragment();
        addOfferSixthFragment = new AddOfferSixthFragment();
        addOfferSeventhFragment = new AddOfferSeventhFragment();
        addOfferEighthFragment = new AddOfferEighthFragment();
        addOfferNinthFragment = new AddOfferNinthFragment();
        addOfferTenthFragment = new AddOfferTenthFragment();
        addOfferEleventhFragment = new AddOfferEleventhFragment();
        addOfferTwelfthFragment = new AddOfferTwelfthFragment();

        pager.setAdapter(pagerAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        pager.setOffscreenPageLimit(NUM_PAGES - 1);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restService = retrofit.create(RestService.class);

        Retrofit geocodingRetrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        googleGeocodingService = geocodingRetrofit.create(GoogleServices.class);
    }

    @Override
    public void onBackPressed() {
        if(pager.getCurrentItem() == 0) {
            finish();
        } else if (pager.getCurrentItem() < NUM_PAGES-1){
            pager.setCurrentItem(pager.getCurrentItem()-1);
        }
    }

    public void setSelectedPlace(PlaceModel selectedPlace) {
        this.selectedPlace = selectedPlace;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public void setResidents(ArrayList<UserModel> residents) {
        this.residents = residents;
    }

    public void toggleView(View v) {
        ImageView animals = (ImageView) v.findViewById(R.id.addOfferImageAnimals);
        ImageView smoker = (ImageView) v.findViewById(R.id.addOfferImageSmoker);

        if(animals != null) {
            Boolean acceptAnimals = Boolean.valueOf(animals.getTag().toString());
            if(acceptAnimals) {
                animals.setTag("false");
                animals.setImageDrawable(ResourcesCompat.getDrawable(v.getResources(), R.drawable.dont_accept_animals, null));
                ((TextView) v.findViewById(R.id.addOfferTextAnimals)).setText(R.string.dont_accept_animals);
            } else {
                animals.setTag("true");
                animals.setImageDrawable(ResourcesCompat.getDrawable(v.getResources(), R.drawable.accept_animals, null));
                ((TextView) v.findViewById(R.id.addOfferTextAnimals)).setText(R.string.accept_animals);
            }
        } else if (smoker != null) {
            Boolean acceptSmoker = Boolean.valueOf(smoker.getTag().toString());
            if(acceptSmoker) {
                smoker.setTag("false");
                smoker.setImageDrawable(ResourcesCompat.getDrawable(v.getResources(), R.drawable.is_not_smoker, null));
                ((TextView) v.findViewById(R.id.addOfferTextSmoker)).setText(R.string.dont_accept_smokers);
            } else {
                smoker.setTag("true");
                smoker.setImageDrawable(ResourcesCompat.getDrawable(v.getResources(), R.drawable.is_smoker, null));
                ((TextView) v.findViewById(R.id.addOfferTextSmoker)).setText(R.string.accept_smokers);
            }
        }
    }

    /**
     * Switch to the next fragment
     * @param v - the button to switch to the next view
     */

    public void nextView(View v){
        if(pager.getCurrentItem() == NUM_PAGES - 1) {
            finish();
        } else if(pager.getCurrentItem() == NUM_PAGES - 2) {
            addOfferButton = (Button) v;
            addOfferButton.setEnabled(false);
            new AsyncOffer().execute();
        } else {
            pager.setCurrentItem(pager.getCurrentItem()+1);
        }
    }

    public void previousView(View v){
        if(pager.getCurrentItem() == 0) {
            finish();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple adapter to associate with the view pager
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0: return addOfferFirstFragment;
                case 1: return addOfferSecondFragment;
                case 2: return addOfferThirdFragment;
                case 3: return addOfferFourthFragment;
                case 4: return addOfferFifthFragment;
                case 5: return addOfferSixthFragment;
                case 6: return addOfferSeventhFragment;
                case 7: return addOfferEighthFragment;
                case 8: return addOfferNinthFragment;
                case 9: return addOfferTenthFragment;
                case 10: return addOfferEleventhFragment;
                case 11: return addOfferTwelfthFragment;
                default: return addOfferFirstFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private class AsyncOffer extends AsyncTask<Void, Void, OfferToCreateModel> {

        @Override
        protected OfferToCreateModel doInBackground(Void... params) {
            return getOfferModel();
        }

        @Override
        public void onPostExecute(OfferToCreateModel offerModel){
            if(offerModel != null) {
                setAddress(offerModel);
            } else {
                addOfferButton.setEnabled(true);
            }
        }

        private OfferToCreateModel getOfferModel(){
            OfferToCreateModel offerModel = new OfferToCreateModel();

            String name = ((EditText) findViewById(R.id.addOfferTitle)).getText().toString();
            String keyword = ((EditText) findViewById(R.id.addOfferKeyword)).getText().toString();
            String availabilityStartsStr = ((EditText) findViewById(R.id.addOfferAvailabilityStarts)).getText().toString();
            String availabilityEndsStr = ((EditText) findViewById(R.id.addOfferAvailabilityEnds)).getText().toString();
            String price = ((EditText) findViewById(R.id.addOfferPrice)).getText().toString();
            String rooms = ((EditText) findViewById(R.id.addOfferRooms)).getText().toString();
            String phone = ((EditText) findViewById(R.id.addOfferPhone)).getText().toString();
            String description = ((EditText) findViewById(R.id.addOfferDescription)).getText().toString();
            boolean acceptAnimals = Boolean.valueOf(findViewById(R.id.addOfferImageAnimals).getTag().toString());
            boolean acceptSmoker = Boolean.valueOf(findViewById(R.id.addOfferImageSmoker).getTag().toString());

            if(!TextValidator.haveText(new String[] {price, rooms, name, description, phone, keyword})) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), R.string.error_missing_info_add_offer, Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }

            try {
                offerModel.setStart(dateFormat.parse(availabilityStartsStr));
                offerModel.setEnd(dateFormat.parse(availabilityEndsStr));

                if(offerModel.getStart().compareTo(offerModel.getEnd()) >= 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.error_dates_add_offer, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return null;
                }
            } catch (Exception e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), R.string.error_dates_add_offer, Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }

            offerModel.setOffer_name(name.trim());
            offerModel.setPrice(Integer.valueOf(price));
            offerModel.setRooms(Integer.valueOf(rooms));
            offerModel.setAcceptAnimal(acceptAnimals);
            offerModel.setSmoker(acceptSmoker);
            offerModel.setDescription(description.trim());
            offerModel.setPhone(phone.trim());
            offerModel.setKeyword(keyword.trim());

            if(residents != null) {
                offerModel.setResidents(residents);
            }

            return offerModel;
        }

        public void setAddress(final OfferToCreateModel offerModel) {
            if(selectedPlace.getPlaceId() != null) {
                progressDialog = ProgressDialog.show(AddOfferActivity.this, "Création de l'annonce", "Veuillez patienter...", true);
                Call<GeocoderResponse> callback = googleGeocodingService.getAddressFromPlaceId(selectedPlace.getPlaceId(), getResources().getString(R.string.google_maps_key));

                callback.enqueue(new Callback<GeocoderResponse>() {
                    @Override
                    public void onResponse(Call<GeocoderResponse> call, Response<GeocoderResponse> response) {
                        if (response.isSuccessful()) {
                            if(response.body().getResults().size() > 0) {
                                AddressInformationsModel addressInformations = response.body().getResults().get(0);
                                for (AddressComponent component : addressInformations.getAddress_components()) {
                                    if (component.getTypes().contains("locality"))
                                        offerModel.setAddressLocality(component.getLong_name());
                                    if (component.getTypes().contains("country"))
                                        offerModel.setCountry(component.getLong_name());
                                    if (component.getTypes().contains("administrative_area_level_2") && offerModel.getAddressRegion() == null)
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
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.error_predicate_place, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GeocoderResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext().getApplicationContext(), R.string.error_identify_address, Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(getApplicationContext().getApplicationContext(), R.string.error_address_unselected, Toast.LENGTH_SHORT).show();
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
                Call<OfferModel> callback = restService.addOffer("Bearer " + Appartoo.TOKEN, offerModelMap);
                callback.enqueue(new Callback<OfferModel>() {
                    @Override
                    public void onResponse(Call<OfferModel> call, Response<OfferModel> response) {

                        if(response.isSuccessful()){
                            String offerId = response.body().getId();
                            System.out.println(response.body().toString());
                            System.out.println(offerId);
                            if(files != null && files.size() > 0) {
                                sendImagesToServer(offerId, files.size(), 1);
                            } else {
                                progressDialog.dismiss();
                                pager.setCurrentItem(NUM_PAGES-1);
                            }
                        } else {
                            progressDialog.dismiss();
                            addOfferButton.setEnabled(true);
                            Toast.makeText(getApplicationContext().getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                            try {
                                System.out.println(response.code());
                                System.out.println(response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OfferModel> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext().getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        addOfferButton.setEnabled(true);
                    }
                });
            } else {
                addOfferButton.setEnabled(true);
                progressDialog.dismiss();
            }
        }

        private void sendImagesToServer(final String offerId, final int numberOfImages, final int currentImage) {
            String url = offerId + "/images";
            File file = files.remove(0);

            progressDialog.setMessage("Annonce enregistrée, envoi des images : " + String.valueOf(currentImage) + " sur " + String.valueOf(numberOfImages) + "...");

            System.out.println(url);

            BitmapFactory.Options options = new BitmapFactory.Options();

            try {
                BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body =  MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            Call<ImageModel> callback = restService.addImageToServer(RestService.REST_URL + url, "Bearer " + Appartoo.TOKEN, body);

            callback.enqueue(new Callback<ImageModel>() {
                @Override
                public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(getApplicationContext().getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                        try {
                            System.out.println(response.code());
                            System.out.println(response.errorBody().string().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if(files.size() > 0) {
                        sendImagesToServer(offerId, numberOfImages, currentImage+1);
                    } else {
                        progressDialog.dismiss();
                        addOfferButton.setEnabled(true);
                        pager.setCurrentItem(NUM_PAGES-1);
                    }
                }

                @Override
                public void onFailure(Call<ImageModel> call, Throwable t) {
                    t.printStackTrace();
                    progressDialog.dismiss();
                    addOfferButton.setEnabled(true);
                }
            });
        }

    }
}
