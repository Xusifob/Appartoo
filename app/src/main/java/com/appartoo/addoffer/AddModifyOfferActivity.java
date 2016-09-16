package com.appartoo.addoffer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.misc.OfferDetailsActivity;
import com.appartoo.utils.ValidationFragment;
import com.appartoo.utils.model.AddressComponent;
import com.appartoo.utils.model.AddressInformationsModel;
import com.appartoo.utils.model.AddressModel;
import com.appartoo.utils.model.CompleteUserModel;
import com.appartoo.utils.model.ImageModel;
import com.appartoo.utils.model.OfferModel;
import com.appartoo.utils.model.OfferModelWithDate;
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
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddModifyOfferActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 12;
    private NonSwipeableViewPager pager;
    private AddOfferPagerAdapter pagerAdapter;
    private Button addOfferButton;
    private GoogleServices googleGeocodingService;
    private RestService restService;
    private ProgressDialog progressDialog;
    private OfferModel offerModel;
    private ArrayList<File> files;
    private ArrayList<String> imageIdsToDelete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        pager = (NonSwipeableViewPager) findViewById(R.id.addOfferPager);
        pagerAdapter = new AddOfferPagerAdapter(getSupportFragmentManager());
        addOfferButton = (Button) findViewById(R.id.finishaddOfferButton);

        offerModel = getIntent().getParcelableExtra("offer");

        pager.setAdapter(pagerAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();

        pager.setOffscreenPageLimit(NUM_PAGES - 1);

        if(offerModel != null) {
            for (int i = 0; i < pagerAdapter.getCount(); i++) {
                pagerAdapter.getItem(i).setData(offerModel);
                pagerAdapter.getItem(i).modifyViews();
            }
        }

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
            if(offerModel != null) {
                Intent intent = new Intent();
                intent.putExtra("offerId", String.valueOf(offerModel.getIdNumber()));
                setResult(OfferDetailsActivity.IS_UPDATED, intent);
            }
            finish();
        } else if (pager.getCurrentItem() < NUM_PAGES-1){
            pager.setCurrentItem(pager.getCurrentItem()-1);
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
            if(offerModel == null) {
                progressDialog = ProgressDialog.show(AddModifyOfferActivity.this, "Création de l'annonce", "Veuillez patienter...", true);
                createOffer();
            } else {
                progressDialog = ProgressDialog.show(AddModifyOfferActivity.this, "Modification de l'annonce", "Veuillez patienter...", true);
                updateOffer();
            }
        } else {
            if(pagerAdapter.getItem(pager.getCurrentItem()) instanceof AddModifyOfferAddressFragment || pagerAdapter.getItem(pager.getCurrentItem()) instanceof AddModifyOfferDescriptionFragment) {
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
            if (pagerAdapter.getItem(pager.getCurrentItem()).validateFragment(AddModifyOfferActivity.this))
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
    private class AddOfferPagerAdapter extends FragmentPagerAdapter {

        private ValidationFragment fragments[] = {
                new AddModifyOfferTitleFragment(), new AddModifyOfferAddressFragment(),
                new AddModifyOfferKeywordFragment(), new AddModifyOfferDatesFragment(),
                new AddModifyOfferPriceFragment(), new AddModifyOfferRoomsFragment(),
                new AddModifyOfferPhoneFragment(), new AddModifyOfferDescriptionFragment(),
                new AddModifyOfferSmokerAnimalsFragment(), new AddModifyOfferResidentsFragment(),
                new AddModifyOfferPicturesFragment(), new AddModifyOfferSuccessFragment()
        };

        public AddOfferPagerAdapter(FragmentManager fm) {
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

    private void updateOffer() {
        OfferModelWithDate offerModelWithDate = getOfferModelWithDate();
        if(offerModelWithDate != null) {
            updateOfferModel(offerModelWithDate);
        } else {
            addOfferButton.setEnabled(true);
        }
    }


    private void updateOfferModel(OfferModelWithDate offerUpdatesModel){

        if(offerUpdatesModel != null) {
            progressDialog.setMessage(getString(R.string.updating_offer));

            Call<OfferModelWithDate> callback = restService.updateOffer(RestService.REST_URL + "/offers/" + String.valueOf(offerModel.getIdNumber()), "Bearer " + Appartoo.TOKEN, offerUpdatesModel);

            callback.enqueue(new Callback<OfferModelWithDate>() {
                @Override
                public void onResponse(Call<OfferModelWithDate> call, Response<OfferModelWithDate> response) {
                    if(response.isSuccessful()) {
                        modifyAddress();
                    } else {
                        progressDialog.dismiss();
                        addOfferButton.setEnabled(true);
                        Toast.makeText(AddModifyOfferActivity.this, R.string.error_updating_offer, Toast.LENGTH_SHORT).show();
                        try {
                            Log.v("AddModifyOfferActivity", "updateOfferModel");
                            Log.v("AddModifyOfferActivity", String.valueOf(response.code()));
                            Log.v("AddModifyOfferActivity", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<OfferModelWithDate> call, Throwable t) {
                    progressDialog.dismiss();
                    addOfferButton.setEnabled(true);
                    Toast.makeText(AddModifyOfferActivity.this, R.string.error_updating_offer, Toast.LENGTH_SHORT).show();
                    Log.v("AddModifyOfferActivity", "updateOfferModel");
                    Log.v("AddModifyOfferActivity", t.getMessage());
                }
            });

        }
    }

    private void modifyAddress() {
//            final PlaceModel selectedPlace = pagerAdapter.getItemFromClass(AddModifyOfferAddressFragment.class).getSelectedPlace();

        //TODO modify when API is great...
        final PlaceModel selectedPlace = null;
        if(selectedPlace != null && !offerModel.getAddress().getPlaceId().equals(selectedPlace.getPlaceId())) {
            progressDialog.setMessage(getString(R.string.updating_address));

            Call<GeocoderResponse> callback = googleGeocodingService.getAddressFromPlaceId(selectedPlace.getPlaceId(), getResources().getString(R.string.google_maps_key));

            callback.enqueue(new Callback<GeocoderResponse>() {
                @Override
                public void onResponse(Call<GeocoderResponse> call, Response<GeocoderResponse> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getResults().size() > 0) {
                            AddressInformationsModel addressInformations = response.body().getResults().get(0);
                            HashMap<String, String> newAddress = new HashMap<>();

                            for (AddressComponent component : addressInformations.getAddress_components()) {
                                if (component.getTypes().contains("locality")) newAddress.put("addressLocatity", component.getLong_name());
                                if (component.getTypes().contains("country")) newAddress.put("country", component.getLong_name());
                                if (component.getTypes().contains("postal_code")) newAddress.put("postalCode", component.getLong_name());
                            }

                            newAddress.put("latitude", addressInformations.getLatitude().toString());
                            newAddress.put("longitude", addressInformations.getLongitude().toString());
                            newAddress.put("formattedAddress", addressInformations.getFormatted_address());
                            newAddress.put("name", "No name");
                            newAddress.put("streetAddress", selectedPlace.getPrimaryText());
                            newAddress.put("placeId", selectedPlace.getPlaceId());

                            addAddressToServer(newAddress);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.error_predicate_place, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            addOfferButton.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeocoderResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext().getApplicationContext(), R.string.error_identify_address, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    addOfferButton.setEnabled(true);
                }
            });
        } else {
            deletePhotos(imageIdsToDelete.size(), 1);
        }
    }

    private void addAddressToServer(HashMap<String, String> newAddress) {
        Call<ResponseBody> callback = restService.addAddressToServer(RestService.REST_URL + "/offers/" + offerModel.getIdNumber().toString() + "/address", "Bearer " + Appartoo.TOKEN, newAddress);

        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    deletePhotos(imageIdsToDelete.size(), 1);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_updating_address, Toast.LENGTH_SHORT).show();
                    try {
                        System.out.println(response.code());
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                    addOfferButton.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext().getApplicationContext(), R.string.error_updating_address, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                progressDialog.dismiss();
                addOfferButton.setEnabled(true);
            }
        });
    }

    private void deletePhotos(final int numberOfPhotos, final int currentPhoto) {
        if(imageIdsToDelete.size() > 0) {
            progressDialog.setMessage("Suppression des photos " + currentPhoto + " sur " + numberOfPhotos);

            String imageId = imageIdsToDelete.remove(0);

            Call<ResponseBody> callback = restService.deleteImage(RestService.REST_URL + "/image_objects/" + imageId, "Bearer " + Appartoo.TOKEN);

            callback.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        deletePhotos(numberOfPhotos, currentPhoto+1);
                    } else {
                        progressDialog.dismiss();
                        addOfferButton.setEnabled(true);
                        Toast.makeText(AddModifyOfferActivity.this, R.string.error_deleting_photos, Toast.LENGTH_SHORT).show();
                        try {
                            Log.v("AddModifyOfferActivity", "deletePhotos");
                            Log.v("AddModifyOfferActivity", String.valueOf(response.code()));
                            Log.v("AddModifyOfferActivity", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(AddModifyOfferActivity.this, R.string.error_deleting_photos, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    addOfferButton.setEnabled(true);
                    Log.v("AddModifyOfferActivity", "deletePhotos");
                    Log.v("AddModifyOfferActivity", t.getMessage());
                }
            });
        } else {
            if(files.size() > 0) {
                sendImagesToServer("/offers/" + String.valueOf(offerModel.getIdNumber()), files.size(), 1);
            } else {
                String phone = pagerAdapter.getItemFromClass(AddModifyOfferPhoneFragment.class).getTelephone();
                if(Appartoo.LOGGED_USER_PROFILE.getTelephone() != null && !Appartoo.LOGGED_USER_PROFILE.getTelephone().equals(phone)) {
                    updateUserPhone(phone);
                } else {
                    progressDialog.dismiss();
                    pager.setCurrentItem(NUM_PAGES - 1);
                }
            }
        }
    }

    private void updateUserPhone(final String phone) {
        progressDialog.setMessage(getString(R.string.updating_user_phone));
        CompleteUserModel modelUpdate = new CompleteUserModel();
        modelUpdate.setTelephone(phone);
        Call<CompleteUserModel> callback = restService.updateUserProfile(Appartoo.LOGGED_USER_PROFILE.getId(), "Bearer " + Appartoo.TOKEN, modelUpdate);

        callback.enqueue(new Callback<CompleteUserModel>() {
            @Override
            public void onResponse(Call<CompleteUserModel> call, Response<CompleteUserModel> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(AddModifyOfferActivity.this, R.string.error_updating_phone, Toast.LENGTH_SHORT).show();
                } else {
                    Appartoo.LOGGED_USER_PROFILE.setTelephone(phone);
                }
                progressDialog.dismiss();
                pager.setCurrentItem(NUM_PAGES - 1);
            }

            @Override
            public void onFailure(Call<CompleteUserModel> call, Throwable t) {
                Toast.makeText(AddModifyOfferActivity.this, R.string.error_updating_phone, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                progressDialog.dismiss();
                pager.setCurrentItem(NUM_PAGES - 1);
            }
        });
    }

    private OfferModelWithDate getOfferModelWithDate() {
        addOfferButton.setEnabled(false);

        for(int i = 0 ; i < pagerAdapter.getCount() ; i++) {
            if(!pagerAdapter.getItem(i).validateFragment(AddModifyOfferActivity.this)) {
                Toast.makeText(AddModifyOfferActivity.this, R.string.error_missing_info_add_offer, Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        OfferModelWithDate offerUpdatesModel = new OfferModelWithDate();

        offerUpdatesModel.setAvailabilityStarts(pagerAdapter.getItemFromClass(AddModifyOfferDatesFragment.class).getStartDate());
        offerUpdatesModel.setAvailabilityEnds(pagerAdapter.getItemFromClass(AddModifyOfferDatesFragment.class).getEndDate());
        offerUpdatesModel.setName(pagerAdapter.getItemFromClass(AddModifyOfferTitleFragment.class).getTitle().trim());
        offerUpdatesModel.setPrice(pagerAdapter.getItemFromClass(AddModifyOfferPriceFragment.class).getPrice());
        offerUpdatesModel.setRooms(pagerAdapter.getItemFromClass(AddModifyOfferRoomsFragment.class).getRooms());
        offerUpdatesModel.setAcceptAnimal(pagerAdapter.getItemFromClass(AddModifyOfferSmokerAnimalsFragment.class).getAcceptAnimals());
        offerUpdatesModel.setSmoker(pagerAdapter.getItemFromClass(AddModifyOfferSmokerAnimalsFragment.class).getAllowSmoker());
        offerUpdatesModel.setDescription(pagerAdapter.getItemFromClass(AddModifyOfferDescriptionFragment.class).getDescription());
        offerUpdatesModel.setKeyword(pagerAdapter.getItemFromClass(AddModifyOfferKeywordFragment.class).getKeyword());

        AddModifyOfferPicturesFragment fragment = pagerAdapter.getItemFromClass(AddModifyOfferPicturesFragment.class);

        imageIdsToDelete = fragment.getImageIdsToDelete();
        files = fragment.getFilesToAdd();

        return offerUpdatesModel;
    }

    public void createOffer(){
        OfferToCreateModel offerToCreateModel = getOfferModel();
        if(offerToCreateModel != null) {
            setAddress(offerToCreateModel);
        } else {
            addOfferButton.setEnabled(true);
        }
    }

    private OfferToCreateModel getOfferModel(){
        OfferToCreateModel offerToCreateModel = new OfferToCreateModel();

        for(int i = 0 ; i < pagerAdapter.getCount() ; i++) {
            if(!pagerAdapter.getItem(i).validateFragment(AddModifyOfferActivity.this)) {
                Toast.makeText(AddModifyOfferActivity.this, R.string.error_missing_info_add_offer, Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        offerToCreateModel.setStart(pagerAdapter.getItemFromClass(AddModifyOfferDatesFragment.class).getStartDate());
        offerToCreateModel.setEnd(pagerAdapter.getItemFromClass(AddModifyOfferDatesFragment.class).getEndDate());
        offerToCreateModel.setOffer_name(pagerAdapter.getItemFromClass(AddModifyOfferTitleFragment.class).getTitle().trim());
        offerToCreateModel.setPrice(pagerAdapter.getItemFromClass(AddModifyOfferPriceFragment.class).getPrice());
        offerToCreateModel.setRooms(pagerAdapter.getItemFromClass(AddModifyOfferRoomsFragment.class).getRooms());
        offerToCreateModel.setAcceptAnimal(pagerAdapter.getItemFromClass(AddModifyOfferSmokerAnimalsFragment.class).getAcceptAnimals());
        offerToCreateModel.setSmoker(pagerAdapter.getItemFromClass(AddModifyOfferSmokerAnimalsFragment.class).getAllowSmoker());
        offerToCreateModel.setDescription(pagerAdapter.getItemFromClass(AddModifyOfferDescriptionFragment.class).getDescription());
        offerToCreateModel.setPhone(pagerAdapter.getItemFromClass(AddModifyOfferPhoneFragment.class).getTelephone());
        offerToCreateModel.setKeyword(pagerAdapter.getItemFromClass(AddModifyOfferKeywordFragment.class).getKeyword());

        return offerToCreateModel;
    }

    public void setAddress(final OfferToCreateModel offerToCreateModel) {

        final PlaceModel selectedPlace = pagerAdapter.getItemFromClass(AddModifyOfferAddressFragment.class).getSelectedPlace();

        if(selectedPlace != null && selectedPlace.getPlaceId() != null) {
            Call<GeocoderResponse> callback = googleGeocodingService.getAddressFromPlaceId(selectedPlace.getPlaceId(), getResources().getString(R.string.google_maps_key));

            callback.enqueue(new Callback<GeocoderResponse>() {
                @Override
                public void onResponse(Call<GeocoderResponse> call, Response<GeocoderResponse> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getResults().size() > 0) {
                            AddressInformationsModel addressInformations = response.body().getResults().get(0);

                            for (AddressComponent component : addressInformations.getAddress_components()) {
                                if (component.getTypes().contains("locality")) offerToCreateModel.setAddressLocality(component.getLong_name());
                                if (component.getTypes().contains("country")) offerToCreateModel.setCountry(component.getLong_name());
                                if (component.getTypes().contains("administrative_area_level_2") && offerToCreateModel.getAddressRegion() == null) offerToCreateModel.setAddressRegion(component.getLong_name());
                                if (component.getTypes().contains("administrative_area_level_1")) offerToCreateModel.setAddressRegion(component.getLong_name());
                                if (component.getTypes().contains("postal_code")) offerToCreateModel.setPostalCode(component.getLong_name());
                            }

                            offerToCreateModel.setLatitude(addressInformations.getLatitude());
                            offerToCreateModel.setLongitude(addressInformations.getLongitude());
                            offerToCreateModel.setFormattedAddress(addressInformations.getFormatted_address());
                            offerToCreateModel.setName("No name");
                            offerToCreateModel.setStreetAddress(selectedPlace.getPrimaryText());
                            offerToCreateModel.setPlaceId(selectedPlace.getPlaceId());

                            sendOfferToServer(offerToCreateModel);
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

    private void sendOfferToServer(OfferToCreateModel offerToCreateModel) {
        if(offerToCreateModel != null) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            Type stringStringMap = new TypeToken<Map<String, String>>(){}.getType();
            Map<String,String> offerModelMap = gson.fromJson(gson.toJson(offerToCreateModel), stringStringMap);

            while(offerModelMap.values().contains(null)) {
                offerModelMap.values().remove(null);
            }

            Call<OfferModel> callback = restService.addOffer("Bearer " + Appartoo.TOKEN, offerModelMap);
            callback.enqueue(new Callback<OfferModel>() {
                @Override
                public void onResponse(Call<OfferModel> call, Response<OfferModel> response) {

                    if(response.isSuccessful()){
                        String offerId = response.body().getId();
                        files = pagerAdapter.getItemFromClass(AddModifyOfferPicturesFragment.class).getFilesToAdd();
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
                            Log.v("AddModifyOfferActivity", "sendOfferToServer: " + String.valueOf(response.code()));
                            Log.v("AddModifyOfferActivity", "sendOfferToServer: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<OfferModel> call, Throwable t) {
                    Log.v("AddModifyOfferActivity", "sendOfferToServer: " + t.getMessage());
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

    public void alertUser(View v) {
        new AlertDialog.Builder(new ContextThemeWrapper(AddModifyOfferActivity.this, R.style.AppThemeDialog))
                .setMessage("Fonctionnalité bientôt disponible.")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
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
                        Log.v("AddModifyOfferActivity", "sendImagesToServer: " + String.valueOf(response.code()));
                        Log.v("AddModifyOfferActivity", "sendImagesToServer: " + response.errorBody().string());
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
                Toast.makeText(getApplicationContext().getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                Log.v("AddModifyOfferActivity", "sendImagesToServer: " + String.valueOf(t.getMessage()));
                progressDialog.dismiss();
                addOfferButton.setEnabled(true);
            }
        });
    }
}
