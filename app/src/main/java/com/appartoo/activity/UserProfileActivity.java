package com.appartoo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.appartoo.R;
import com.appartoo.fragment.userprofile.UserProfileMainFragment;
import com.appartoo.fragment.userprofile.UserProfileModifyFragment;
import com.appartoo.fragment.userprofile.UserProfileOffersFragment;
import com.appartoo.fragment.userprofile.UserProfileSettingsFragment;
import com.appartoo.utils.Appartoo;
import com.appartoo.view.NavigationDrawerView;

/**
 * Created by alexandre on 16-07-15.
 */
public class UserProfileActivity extends AppCompatActivity {

    private UserProfileMainFragment mainFragment;
    private UserProfileSettingsFragment settingsFragment;
    private UserProfileModifyFragment modifyFragment;
    private UserProfileOffersFragment offersFragment;
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationDrawerView navigationDrawerView;
    private Fragment currentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        mainFragment = new UserProfileMainFragment();
        modifyFragment = new UserProfileModifyFragment();
        settingsFragment = new UserProfileSettingsFragment();
        offersFragment = new UserProfileOffersFragment();

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            System.out.println("RESTORING FRAGMENT");
            currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");
            fragmentManager.beginTransaction().replace(R.id.userProfileFrame, currentFragment).commit();
        } else {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.userProfileFrame, mainFragment).commit();
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //Retrieve the drawer elements
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationDrawerView = (NavigationDrawerView) findViewById(R.id.navigationDrawer);
    }

    @Override
    public void onStart(){
        super.onStart();

        //Define the drawer
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Appartoo.APP_NAME);
        navigationDrawerView.setDrawerLayout(drawerLayout);

        if(settingsFragment.isVisible() || modifyFragment.isVisible() || offersFragment.isVisible()) {
            toolbar.setNavigationIcon(R.drawable.left_arrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_drawer);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        if(!mainFragment.isVisible()) {
            fragmentManager.beginTransaction().replace(R.id.userProfileFrame, mainFragment).commit();
            toolbar.setNavigationIcon(R.drawable.ic_drawer);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    }
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Switch between the fragments
     * @param v - the button corresponding to the fragments
     */
    public void switchFragment(View v) {
        if(Appartoo.LOGGED_USER_PROFILE != null) {
            if (v.getTag().equals("my_settings")) {
                fragmentManager.beginTransaction().replace(R.id.userProfileFrame, settingsFragment).commit();
            } else if (v.getTag().equals("modify_profile")) {
                fragmentManager.beginTransaction().replace(R.id.userProfileFrame, modifyFragment).commit();
            } else if (v.getTag().equals("my_offers")) {
                fragmentManager.beginTransaction().replace(R.id.userProfileFrame, offersFragment).commit();
            }
            toolbar.setNavigationIcon(R.drawable.left_arrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(settingsFragment.isVisible()) {
            getSupportFragmentManager().putFragment(outState, "currentFragment", settingsFragment);
        } else if (modifyFragment.isVisible()) {
            getSupportFragmentManager().putFragment(outState, "currentFragment", modifyFragment);
        } else if (offersFragment.isVisible()){
            getSupportFragmentManager().putFragment(outState, "currentFragment", offersFragment);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume(){
        super.onResume();
        navigationDrawerView.updateHeader();
    }
}
