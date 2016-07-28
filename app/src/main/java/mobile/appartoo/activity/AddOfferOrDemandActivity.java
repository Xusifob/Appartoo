package mobile.appartoo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import mobile.appartoo.R;
import mobile.appartoo.adapter.AddOffersAndDemandsAdapter;
import mobile.appartoo.view.NavigationDrawerView;
import mobile.appartoo.view.NonSwipeableViewPager;

public class AddOfferOrDemandActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationDrawerView navigationView;
    private TabLayout tabLayout;
    private NonSwipeableViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer_or_demand);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //Retrieve the drawer elements
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationDrawerView) findViewById(R.id.navigationDrawer);

        tabLayout = (TabLayout) findViewById(R.id.offersAndDemandsTabs);
        viewPager = (NonSwipeableViewPager) findViewById(R.id.offersAndDemandsList);
    }

    @Override
    public void onStart(){
        super.onStart();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Appartoo");
        navigationView.setDrawerLayout(drawerLayout);

        viewPager.setAdapter(new AddOffersAndDemandsAdapter(getSupportFragmentManager(), AddOfferOrDemandActivity.this));
        tabLayout.setupWithViewPager(viewPager);

        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
    }
}
