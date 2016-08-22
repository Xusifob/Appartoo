package com.appartoo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.appartoo.R;
import com.appartoo.fragment.OffersListFragment;
import com.appartoo.view.NavigationDrawerView;

public class MainActivity extends AppCompatActivity {

    private final int ID_MENU_MESSAGES = 1;
    private final int ID_MENU_REFRESH = 2;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationDrawerView navigationDrawerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //Retrieve the drawer elements
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationDrawerView = (NavigationDrawerView) findViewById(R.id.navigationDrawer);
    }

    @Override
    public void onStart(){
        super.onStart();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Appartoo");
        navigationDrawerView.setDrawerLayout(drawerLayout);

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

    @Override
    public void onResume(){
        super.onResume();
        navigationDrawerView.updateHeader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, ID_MENU_REFRESH, Menu.NONE, "Refresh").setIcon(R.drawable.refresh).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(Menu.NONE, ID_MENU_MESSAGES, Menu.NONE, "Messages").setIcon(R.drawable.speech).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Define the action to do according to the selected menu
        switch (item.getItemId()) {
            case ID_MENU_MESSAGES:
                startActivity(new Intent(MainActivity.this, MessagesListActivity.class));
                return true;
            case ID_MENU_REFRESH:
                OffersListFragment offersListFragment = (OffersListFragment) getSupportFragmentManager().findFragmentById(R.id.offerListFragment);
                offersListFragment.refreshOffers();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
