package mobile.appartoo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mobile.appartoo.R;
import mobile.appartoo.adapter.WorkaroundMapFragment;
import mobile.appartoo.model.OfferModel;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.view.DrawerListView;

/**
 * Created by alexandre on 16-07-06.
 */
public class OfferDetailActivity  extends FragmentActivity implements OnMapReadyCallback {

    private ScrollView scrollView;
    private DrawerLayout drawerLayout;
    private DrawerListView drawerListView;
    private OfferModel offer;
    private ActionBarDrawerToggle drawerToggle;
    private SupportMapFragment mapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        //Retrieve the drawer element
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerListView = (DrawerListView) findViewById(R.id.drawerList);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //Retrieve the others elements
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onStart(){
        super.onStart();

        //Define the drawer
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        drawerListView.setDrawerLayout(drawerLayout);
        drawerLayout.addDrawerListener(drawerToggle);
        offer = (OfferModel) getIntent().getSerializableExtra("offer");

        //Disable the scrollview on map interaction
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });

        if(Appartoo.LOGGED_USER_PROFILE != null) {
            ((TextView) drawerLayout.findViewById(R.id.drawerUserName)).setText(Appartoo.LOGGED_USER_PROFILE.getGivenName() + " " + Appartoo.LOGGED_USER_PROFILE.getFamilyName());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Define the action to do according to the selected menu
        switch (item.getItemId()) {
            case android.R.id.home:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Define the latitude and longitude to use with the map fragment
        LatLng offerFlat = new LatLng(offer.getAddress().getLatitude(), offer.getAddress().getLongitude());

        //Add the marker to the map
        googleMap.addMarker(new MarkerOptions().position(offerFlat).title(offer.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(offerFlat, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
}
