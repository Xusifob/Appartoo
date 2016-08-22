package com.appartoo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.appartoo.utils.ImageManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.appartoo.R;
import com.appartoo.adapter.ImageViewPagerAdapter;
import com.appartoo.fragment.WorkaroundMapFragment;
import com.appartoo.model.OfferModel;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alexandre on 16-07-06.
 */
public class OfferDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private NestedScrollView scrollView;
    private Toolbar toolbar;
    private SupportMapFragment mapFragment;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private OfferModel offer;
    private ImageButton offerDetailOwnerPicture;
    private ViewPager viewPager;

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
        offer = getIntent().getParcelableExtra("offer");

        //Retrieve the others elements
        scrollView = (NestedScrollView) findViewById(R.id.offerDetailsScrollView);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onStart(){
        super.onStart();


        if(offer.getImages().size() > 0) {
            ImageViewPagerAdapter imagesAdapter = new ImageViewPagerAdapter(this, offer.getImages());
            viewPager.setAdapter(imagesAdapter);
        } else {
            findViewById(R.id.noPictureIndicator).setVisibility(View.VISIBLE);
        }

        //Define the drawer
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

        if(offer.getOwner().getImage().getThumbnail() != null) {
            ImageManager.downloadPictureIntoView(getApplicationContext(), offerDetailOwnerPicture, offer.getOwner().getImage().getThumbnail().getContentUrl(), ImageManager.TRANFORM_CIRCLE);
        } else if (offer.getOwner().getImage() != null) {
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
