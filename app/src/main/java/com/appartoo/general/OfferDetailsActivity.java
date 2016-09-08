package com.appartoo.general;

import android.app.ProgressDialog;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.message.MessageActivity;
import com.appartoo.utils.adapter.ImageViewPagerAdapter;
import com.appartoo.utils.model.ImageModel;
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

import java.io.IOException;
import java.util.ArrayList;

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
    private ArrayList<ImageModel> images;
    private ImageButton offerDetailOwnerPicture;
    private ViewPager viewPager;
    private Button offerDetailSendMessageButton;
    private ProgressDialog progressDialog;
    private OfferModel offer;
    private RestService restService;
    private String offerId;
    private View offerDetailContainer;
    private ProgressBar progressBar;

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
        offerId = getIntent().getStringExtra("offerId");
        offer = getIntent().getParcelableExtra("offer");
        offerDetailContainer = findViewById(R.id.offerDetailContainer);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Retrieve the others elements
        scrollView = (NestedScrollView) findViewById(R.id.offerDetailsScrollView);
        offerDetailsFragment = (OfferDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.offerDetailsFragment);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onStart(){
        super.onStart();

        if(offer == null) {
            offerDetailContainer.setVisibility(View.GONE);
            offerDetailSendMessageButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Appartoo.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            restService = retrofit.create(RestService.class);

            Call<OfferModelWithDate> callback = restService.getOfferById(RestService.REST_URL + "/offers/" + offerId);

            callback.enqueue(new Callback<OfferModelWithDate>() {
                @Override
                public void onResponse(Call<OfferModelWithDate> call, Response<OfferModelWithDate> response) {
                    if (response.isSuccessful()) {

                        offer = response.body();
                        offerDetailsFragment.bindData(offer);
                        mapFragment.getMapAsync(OfferDetailsActivity.this);
                        bindData();
                        offerDetailContainer.setVisibility(View.VISIBLE);
                        offerDetailSendMessageButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        try {
                            Log.v("OfferDetailsActivity", "onStart: " + String.valueOf(response.code()));
                            Log.v("OfferDetailsActivity", "onStart: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(OfferDetailsActivity.this, R.string.error_retrieve_offer, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<OfferModelWithDate> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(OfferDetailsActivity.this, R.string.error_retrieve_offer, Toast.LENGTH_SHORT).show();
                    Log.v("OfferDetailsActivity", "onStart: " + t.getMessage());
                }
            });
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
            ImageViewPagerAdapter imagesAdapter = new ImageViewPagerAdapter(this, offer.getImages());
            viewPager.setAdapter(imagesAdapter);
        }

        if(Appartoo.LOGGED_USER_PROFILE != null && Appartoo.LOGGED_USER_PROFILE.getId().equals(offer.getOwner().getId())) {

            offerDetailSendMessageButton.setText("Modifier votre annonce");
            offerDetailSendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(OfferDetailsActivity.this)
                            .setMessage("Fonctionnalité bientôt disponible.")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }
            });
        } else if(Appartoo.TOKEN == null || Appartoo.TOKEN.equals("")) {
            new AlertDialog.Builder(OfferDetailsActivity.this)
                    .setMessage("Vous devez être inscrit et connecté pour pouvoir postuler à une annonce.")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
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
                intent.putExtra("user", offer.getOwner());
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
    }

    private void applyToOffer(){
        progressDialog = ProgressDialog.show(this, "Création de la conversation", "Veuillez patienter...", true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestService restService = retrofit.create(RestService.class);

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
                    Toast.makeText(getApplicationContext(), "Erreur lors de la création de la conversation. Veuillez réessayer plus tard.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
