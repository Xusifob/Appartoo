package com.appartoo.addoffer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.utils.ValidationFragment;
import com.appartoo.utils.model.AddressComponent;
import com.appartoo.utils.model.AddressInformationsModel;
import com.appartoo.utils.model.ImageModel;
import com.appartoo.utils.model.OfferModel;
import com.appartoo.utils.model.OfferToCreateModel;
import com.appartoo.utils.model.PlaceModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.GeocoderResponse;
import com.appartoo.utils.GoogleServices;
import com.appartoo.utils.RestService;
import com.appartoo.utils.view.NonSwipeableViewPager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private NonSwipeableViewPager pager;
    private ScreenSlidePagerAdapter pagerAdapter;
    private Button addOfferButton;
    private GoogleServices googleGeocodingService;
    private RestService restService;
    private ArrayList<File> files;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        pager = (NonSwipeableViewPager) findViewById(R.id.addOfferPager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        addOfferButton = (Button) findViewById(R.id.finishAddOfferButton);

        pager.setAdapter(pagerAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        pager.setOffscreenPageLimit(NUM_PAGES - 1);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
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

    public void toggleView(View v) {
        ImageView animals = (ImageView) v.findViewById(R.id.addOfferImageAnimals);
        ImageView smoker = (ImageView) v.findViewById(R.id.addOfferImageSmoker);

        if(animals != null) {
            Boolean acceptAnimals = Boolean.valueOf(animals.getTag().toString());
            if(acceptAnimals) {
                animals.setTag("false");
                animals.setImageDrawable(ResourcesCompat.getDrawable(v.getResources(), R.drawable.dont_accept_animals_big, null));
                ((TextView) v.findViewById(R.id.addOfferTextAnimals)).setText(R.string.i_dont_accept_animals);
            } else {
                animals.setTag("true");
                animals.setImageDrawable(ResourcesCompat.getDrawable(v.getResources(), R.drawable.accept_animals_big, null));
                ((TextView) v.findViewById(R.id.addOfferTextAnimals)).setText(R.string.i_accept_animals);
            }
        } else if (smoker != null) {
            Boolean acceptSmoker = Boolean.valueOf(smoker.getTag().toString());
            if(acceptSmoker) {
                smoker.setTag("false");
                smoker.setImageDrawable(ResourcesCompat.getDrawable(v.getResources(), R.drawable.is_not_smoker_big, null));
                ((TextView) v.findViewById(R.id.addOfferTextSmoker)).setText(R.string.i_dont_accept_smokers);
            } else {
                smoker.setTag("true");
                smoker.setImageDrawable(ResourcesCompat.getDrawable(v.getResources(), R.drawable.is_smoker_big, null));
                ((TextView) v.findViewById(R.id.addOfferTextSmoker)).setText(R.string.i_accept_smokers);
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
            if (pagerAdapter.getItem(pager.getCurrentItem()).validateFragment(AddOfferActivity.this))
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
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        private ValidationFragment fragments[] = {
                new AddOfferTitleFragment(), new AddOfferKeywordFragment(),
                new AddOfferAddressFragment(), new AddOfferDatesFragment(),
                new AddOfferPriceFragment(), new AddOfferRoomsFragment(),
                new AddOfferPhoneFragment(), new AddOfferDescriptionFragment(),
                new AddOfferSmokerAnimalsFragment(), new AddOfferResidentsFragment(),
                new AddOfferPicturesFragment(), new AddOfferSuccessFragment()
        };

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }



        public <T> T getItemFromClass(Class<T> fragmentClass) {
            for(ValidationFragment fragment : fragments) {
                if(fragment.getClass().equals(fragmentClass)) return (T) fragment;
            }
            return null;
        }

        @Override
        public ValidationFragment getItem(int position) {
            if(position < fragments.length) {
                return fragments[position];
            } else {
                return null;
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

            for(int i = 0 ; i < pagerAdapter.getCount() ; i++) {
                if(!pagerAdapter.getItem(i).validateFragment(AddOfferActivity.this)) {
                    Toast.makeText(AddOfferActivity.this, R.string.error_missing_info_add_offer, Toast.LENGTH_SHORT).show();
                    return null;
                }
            }

            offerModel.setStart(pagerAdapter.getItemFromClass(AddOfferDatesFragment.class).getStartDate());
            offerModel.setEnd(pagerAdapter.getItemFromClass(AddOfferDatesFragment.class).getEndDate());
            offerModel.setOffer_name(pagerAdapter.getItemFromClass(AddOfferTitleFragment.class).getTitle().trim());
            offerModel.setPrice(pagerAdapter.getItemFromClass(AddOfferPriceFragment.class).getPrice());
            offerModel.setRooms(pagerAdapter.getItemFromClass(AddOfferRoomsFragment.class).getRooms());
            offerModel.setAcceptAnimal(pagerAdapter.getItemFromClass(AddOfferSmokerAnimalsFragment.class).getAcceptAnimals());
            offerModel.setSmoker(pagerAdapter.getItemFromClass(AddOfferSmokerAnimalsFragment.class).getAllowSmoker());
            offerModel.setDescription(pagerAdapter.getItemFromClass(AddOfferDescriptionFragment.class).getDescription());
            offerModel.setPhone(pagerAdapter.getItemFromClass(AddOfferPhoneFragment.class).getTelephone());
            offerModel.setKeyword(pagerAdapter.getItemFromClass(AddOfferKeywordFragment.class).getKeyword());

            return offerModel;
        }

        public void setAddress(final OfferToCreateModel offerModel) {

            final PlaceModel selectedPlace = pagerAdapter.getItemFromClass(AddOfferAddressFragment.class).getSelectedPlace();

            if(selectedPlace != null && selectedPlace.getPlaceId() != null) {
                progressDialog = ProgressDialog.show(AddOfferActivity.this, "Création de l'annonce", "Veuillez patienter...", true);
                Call<GeocoderResponse> callback = googleGeocodingService.getAddressFromPlaceId(selectedPlace.getPlaceId(), getResources().getString(R.string.google_maps_key));

                callback.enqueue(new Callback<GeocoderResponse>() {
                    @Override
                    public void onResponse(Call<GeocoderResponse> call, Response<GeocoderResponse> response) {
                        if (response.isSuccessful()) {
                            if(response.body().getResults().size() > 0) {
                                AddressInformationsModel addressInformations = response.body().getResults().get(0);

                                for (AddressComponent component : addressInformations.getAddress_components()) {
                                    if (component.getTypes().contains("locality")) offerModel.setAddressLocality(component.getLong_name());
                                    if (component.getTypes().contains("country")) offerModel.setCountry(component.getLong_name());
                                    if (component.getTypes().contains("administrative_area_level_2") && offerModel.getAddressRegion() == null) offerModel.setAddressRegion(component.getLong_name());
                                    if (component.getTypes().contains("administrative_area_level_1")) offerModel.setAddressRegion(component.getLong_name());
                                    if (component.getTypes().contains("postal_code")) offerModel.setPostalCode(component.getLong_name());
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

                Call<OfferModel> callback = restService.addOffer("Bearer " + Appartoo.TOKEN, offerModelMap);
                callback.enqueue(new Callback<OfferModel>() {
                    @Override
                    public void onResponse(Call<OfferModel> call, Response<OfferModel> response) {

                        if(response.isSuccessful()){
                            String offerId = response.body().getId();
                            files = pagerAdapter.getItemFromClass(AddOfferPicturesFragment.class).getFiles();
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
                                Log.v("AddOfferActivity", "sendOfferToServer: " + String.valueOf(response.code()));
                                Log.v("AddOfferActivity", "sendOfferToServer: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OfferModel> call, Throwable t) {
                        Log.v("AddOfferActivity", "sendOfferToServer: " + t.getMessage());
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
            String url = RestService.REST_URL + offerId + "/images";
            File file = files.remove(0);

            progressDialog.setMessage("Annonce enregistrée, envoi des images : " + String.valueOf(currentImage) + " sur " + String.valueOf(numberOfImages) + "...");

            BitmapFactory.Options options = new BitmapFactory.Options();

            try {
                BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body =  MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            Call<ImageModel> callback = restService.addImageToServer(url, "Bearer " + Appartoo.TOKEN, body);

            callback.enqueue(new Callback<ImageModel>() {
                @Override
                public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {

                    if(!response.isSuccessful()){
                        Toast.makeText(getApplicationContext().getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                        try {
                            Log.v("AddOfferActivity", "sendImagesToServer: " + String.valueOf(response.code()));
                            Log.v("AddOfferActivity", "sendImagesToServer: " + response.errorBody().string());
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
                    Log.v("AddOfferActivity", "sendImagesToServer: " + String.valueOf(t.getMessage()));
                    progressDialog.dismiss();
                    addOfferButton.setEnabled(true);
                }
            });
        }

    }

    public void alertUser(View v) {
        new AlertDialog.Builder(new ContextThemeWrapper(AddOfferActivity.this, R.style.AppThemeDialog))
                .setMessage("Fonctionnalité bientôt disponible.")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}
