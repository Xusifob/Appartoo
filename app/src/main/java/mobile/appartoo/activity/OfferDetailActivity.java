package mobile.appartoo.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mobile.appartoo.R;
import mobile.appartoo.adapter.WorkaroundMapFragment;
import mobile.appartoo.model.OfferModel;

/**
 * Created by alexandre on 16-07-06.
 */
public class OfferDetailActivity  extends AppCompatActivity implements OnMapReadyCallback {

    private NestedScrollView scrollView;
    private Toolbar toolbar;
    private SupportMapFragment mapFragment;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private OfferModel offer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        //Retrieve the drawer element
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.offerDetailAppBar);

        offer = getIntent().getParcelableExtra("offer");

        //Retrieve the others elements
        scrollView = (NestedScrollView) findViewById(R.id.offerDetailScrollView);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onStart(){
        super.onStart();

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
                    collapsingToolbarLayout.setTitle("Détail de l'annonce");
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if(offer == null) {
            offer = getIntent().getParcelableExtra("offer");
        }
        //Define the latitude and longitude to use with the map fragment
        LatLng offerFlat = new LatLng(offer.getAddress().getLatitude(), offer.getAddress().getLongitude());

        //Add the marker to the map
        googleMap.addMarker(new MarkerOptions().position(offerFlat).title(offer.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(offerFlat, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
