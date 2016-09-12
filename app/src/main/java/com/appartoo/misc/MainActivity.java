package com.appartoo.misc;

import android.content.Context;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.appartoo.R;
import com.appartoo.search.SearchActivity;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.view.NavigationDrawerView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {



    private final int ID_MENU_SEARCH = 1;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationDrawerView navigationDrawerView;
    private HashMap<String, Object> query;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //Retrieve the drawer elements
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationDrawerView = (NavigationDrawerView) findViewById(R.id.navigationDrawer);

        query = (HashMap<String, Object>) getIntent().getSerializableExtra("query");
    }

    @Override
    public void onStart(){
        super.onStart();

        setSupportActionBar(toolbar);

        if(query == null) {
            getSupportActionBar().setTitle(Appartoo.APP_NAME);
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
        } else {
            getSupportActionBar().setTitle(R.string.drawer_search);
            toolbar.setNavigationIcon(R.drawable.left_arrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if(query == null) menu.add(Menu.NONE, ID_MENU_SEARCH, Menu.NONE, "Search").setIcon(R.drawable.search_white).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Define the action to do according to the selected menu
        switch (item.getItemId()) {
            case ID_MENU_SEARCH:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
