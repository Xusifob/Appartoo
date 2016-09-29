package com.appartoo.misc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.addoffer.AddModifyOfferActivity;
import com.appartoo.message.MessageActivity;
import com.appartoo.utils.adapter.ImageModelViewPagerAdapter;
import com.appartoo.utils.model.OfferModel;
import com.appartoo.utils.model.OfferModelWithDate;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.ConversationIdReceiver;
import com.appartoo.utils.ImageManager;
import com.appartoo.utils.RestService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexandre on 16-07-06.
 */
public class OfferDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private NestedScrollView scrollView;
    private Toolbar toolbar;
    private SupportMapFragment mapFragment;
    private OfferDetailsFragment offerDetailsFragment;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageButton offerDetailOwnerPicture;
    private ViewPager viewPager;
    private Button offerDetailSendMessageButton;
    private Button offerDetailDesactivate;
    private ProgressDialog progressDialog;
    private OfferModel offer;
    private RestService restService;
    private String offerId;
    private View offerDetailContainer;
    private ProgressBar progressBar;
    private boolean isOwner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);


        //Retrieve the drawer element
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.offerDetailsAppBar);

        viewPager = (ViewPager) findViewById(R.id.offerFlatImagesPager);
        offerDetailOwnerPicture = (ImageButton) findViewById(R.id.offerDetailOwnerPicture);
        offerDetailSendMessageButton = (Button) findViewById(R.id.offerDetailSendMessage);
        offerDetailDesactivate = (Button) findViewById(R.id.offerDetailDesactivate);
        offerId = getIntent().getStringExtra("offerId");
        offer = getIntent().getParcelableExtra("offer");
        isOwner = getIntent().getBooleanExtra("isOwner", false);

        offerDetailContainer = findViewById(R.id.offerDetailContainer);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);



        //Retrieve the others elements
        scrollView = (NestedScrollView) findViewById(R.id.offerDetailsScrollView);
        offerDetailsFragment = (OfferDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.offerDetailsFragment);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Appartoo.REQUEST_MODIFY_OFFER && resultCode == Appartoo.HAS_UPDATED_OFFER) {
            offerId = offer.getIdNumber().toString();
            progressDialog = ProgressDialog.show(this, "Mise à jour des informations de l'annonce", "Veuillez patienter...", true);
            getOfferDetails();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        restService = retrofit.create(RestService.class);

        if(offer == null) {
            getOfferDetails();
        } else {
            offerDetailsFragment.bindData(offer);
            mapFragment.getMapAsync(OfferDetailsActivity.this);
            bindData();
            offerDetailContainer.setVisibility(View.VISIBLE);
            offerDetailSendMessageButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        toolbar.setNavigationIcon(R.drawable.left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Disable the scrollview on map interaction
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
    }

    private void getOfferDetails() {
        offerDetailContainer.setVisibility(View.GONE);
        offerDetailSendMessageButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Call<OfferModelWithDate> callback = restService.getOfferById(RestService.REST_URL + "/offers/" + offerId);

        callback.enqueue(new Callback<OfferModelWithDate>() {
            @Override
            public void onResponse(Call<OfferModelWithDate> call, Response<OfferModelWithDate> response) {

                if(progressDialog != null) progressDialog.dismiss();

                if (response.isSuccessful()) {

                    offer = response.body();
                    bindData();
                    offerDetailsFragment.bindData(offer);
                    mapFragment.getMapAsync(OfferDetailsActivity.this);
                    offerDetailContainer.setVisibility(View.VISIBLE);
                    offerDetailSendMessageButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    try {
                        Log.v("OfferDetailsActivity", "getOfferDetails: " + String.valueOf(response.code()));
                        Log.v("OfferDetailsActivity", "getOfferDetails: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(OfferDetailsActivity.this, R.string.error_retrieve_offer, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<OfferModelWithDate> call, Throwable t) {
                if(progressDialog != null) progressDialog.dismiss();
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OfferDetailsActivity.this, R.string.error_retrieve_offer, Toast.LENGTH_SHORT).show();
                Log.v("OfferDetailsActivity", "getOfferDetails: " + t.getMessage());
            }
        });
    }

    private void bindData() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(offer.getName());
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });

        if(offer.getImages().size() > 0) {
            ImageModelViewPagerAdapter imagesAdapter = new ImageModelViewPagerAdapter(this, offer.getImages());
            viewPager.setAdapter(imagesAdapter);
        }

        if((Appartoo.LOGGED_USER_PROFILE != null && Appartoo.LOGGED_USER_PROFILE.getId().equals(offer.getOwner().getId())) || isOwner) {

            offerDetailSendMessageButton.setText(R.string.modify_offer);
            offerDetailSendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(OfferDetailsActivity.this, AddModifyOfferActivity.class);
                    intent.putExtra("offer", offer);
                    startActivityForResult(intent, Appartoo.REQUEST_MODIFY_OFFER);
                }
            });

            offerDetailDesactivate.setVisibility(View.VISIBLE);
            if(!offer.getActive()) offerDetailDesactivate.setText(R.string.activate_offer);

            offerDetailDesactivate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(OfferDetailsActivity.this, R.style.AppThemeDialog))
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                    if(offer.getActive()) {
                        dialog.setMessage(getString(R.string.confirm_desactivate)).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                activateOffer(false);
                            }
                        });
                    } else {
                        dialog.setMessage(getString(R.string.confirm_activate)).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                activateOffer(true);
                            }
                        });
                    }

                    dialog.show();
                }
            });

        } else if(Appartoo.TOKEN == null || Appartoo.TOKEN.equals("")) {
            offerDetailSendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(OfferDetailsActivity.this, MessageActivity.class);
                    intent.putExtra("userId", offer.getOwner().getIdNumber().toString());
                    intent.putExtra("conversationName", offer.getOwner().getGivenName() + " (" + offer.getName() + ")");
                    startActivity(intent);
                }
            });
        } else {
            offerDetailSendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        offerDetailSendMessageButton.setEnabled(false);
                        applyToOffer();
                }
            });
        }

        if (offer.getOwner().getImage() != null) {
            ImageManager.downloadPictureIntoView(getApplicationContext(), offerDetailOwnerPicture, offer.getOwner().getImage().getContentUrl(), ImageManager.TRANFORM_CIRCLE);
        } else {
            offerDetailOwnerPicture.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_profile_picture));
        }

        offerDetailOwnerPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OfferDetailsActivity.this, UserDetailActivity.class);
                intent.putExtra("profileId", offer.getOwner().getIdNumber().toString());
                startActivity(intent);
            }
        });
    }

    private void applyToOffer(){
        progressDialog = ProgressDialog.show(this, "Création de la conversation", "Veuillez patienter...", true);

        Call<ConversationIdReceiver> callback = restService.sendMessageToUser("Bearer " + Appartoo.TOKEN, offer.getOwner().getIdNumber().toString());
        callback.enqueue(new Callback<ConversationIdReceiver>() {
            @Override
            public void onResponse(Call<ConversationIdReceiver> call, Response<ConversationIdReceiver> response) {
                progressDialog.dismiss();
                offerDetailSendMessageButton.setEnabled(true);
                if(response.isSuccessful()){
                    Intent intent = new Intent(OfferDetailsActivity.this, MessageActivity.class);
                    intent.putExtra("conversationId", response.body().getIdConversation());
                    intent.putExtra("conversationName", offer.getOwner().getGivenName() + " (" + offer.getName() + ")");
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_conversation_creation, Toast.LENGTH_SHORT).show();
                    try {
                        Log.v("OfferDetailsActivity", "applyToOffer: " + String.valueOf(response.code()));
                        Log.v("OfferDetailsActivity", "applyToOffer: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ConversationIdReceiver> call, Throwable t) {
                offerDetailSendMessageButton.setEnabled(true);
                progressDialog.dismiss();
                Log.v("OfferDetailsActivity", "applyToOffer: " + t.getMessage());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Define the latitude and longitude to use with the map fragment
        if (offer.getAddress() != null){
            LatLng offerFlat = new LatLng(offer.getAddress().getLatitude(), offer.getAddress().getLongitude());

            //Add the marker to the map
            googleMap.addMarker(new MarkerOptions().position(offerFlat).title(offer.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(offerFlat, 15));
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
    }

    private void activateOffer(final boolean activate) {

        OfferModelWithDate offerToUpdate = new OfferModelWithDate();
        offerToUpdate.setActive(activate);

        Call<OfferModelWithDate> callback = restService.updateOffer(RestService.REST_URL + "/offers/" + String.valueOf(offer.getIdNumber()), "Bearer " + Appartoo.TOKEN, offerToUpdate);

        callback.enqueue(new Callback<OfferModelWithDate>() {
            @Override
            public void onResponse(Call<OfferModelWithDate> call, Response<OfferModelWithDate> response) {
                if(response.isSuccessful()) {
                    offer.setActive(activate);

                    if(activate) {
                        Toast.makeText(OfferDetailsActivity.this, R.string.offer_is_now_activated, Toast.LENGTH_SHORT).show();
                        offerDetailDesactivate.setText(R.string.desactivate_offer);
                    } else {
                        Toast.makeText(OfferDetailsActivity.this, R.string.offer_is_now_desactivated, Toast.LENGTH_SHORT).show();
                        offerDetailDesactivate.setText(R.string.activate_offer);
                    }
                } else {
                    Toast.makeText(OfferDetailsActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
                    try {
                        Log.v("OfferDetailsActivity", "activateOffer");
                        Log.v("OfferDetailsActivity", String.valueOf(response.code()));
                        Log.v("OfferDetailsActivity", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<OfferModelWithDate> call, Throwable t) {
                Toast.makeText(OfferDetailsActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
                Log.v("OfferDetailsActivity", String.valueOf("activateOffer"));
                Log.v("OfferDetailsActivity", String.valueOf(t.getMessage()));
            }
        });
    }
}
