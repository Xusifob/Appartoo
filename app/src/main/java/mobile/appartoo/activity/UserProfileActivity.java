package mobile.appartoo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import mobile.appartoo.R;
import mobile.appartoo.fragment.UserProfileMainFragment;
import mobile.appartoo.fragment.UserProfileModifyFragment;
import mobile.appartoo.fragment.UserProfileSettingsFragment;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.view.DrawerListView;

/**
 * Created by alexandre on 16-07-15.
 */
public class UserProfileActivity extends FragmentActivity {

    private UserProfileMainFragment mainFragment;
    private UserProfileSettingsFragment settingsFragment;
    private UserProfileModifyFragment modifyFragment;
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
    private DrawerListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Retrieve the drawer elements
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerListView = (DrawerListView) findViewById(R.id.drawerList);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    @Override
    public void onStart(){
        super.onStart();

        //Define the drawer
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        drawerListView.setDrawerLayout(drawerLayout);
        drawerLayout.addDrawerListener(drawerToggle);

        //Define the fragments
        fragmentManager = getSupportFragmentManager();
        mainFragment = new UserProfileMainFragment();
        modifyFragment = new UserProfileModifyFragment();
        settingsFragment = new UserProfileSettingsFragment();

        if(Appartoo.LOGGED_USER_PROFILE != null) {
            ((TextView) drawerLayout.findViewById(R.id.drawerUserName)).setText(Appartoo.LOGGED_USER_PROFILE.getGivenName() + " " + Appartoo.LOGGED_USER_PROFILE.getFamilyName());
        }

        fragmentManager.beginTransaction().add(R.id.userProfileFrame, mainFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if(!mainFragment.isVisible()) {
            fragmentManager.beginTransaction().replace(R.id.userProfileFrame, mainFragment).commit();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Switch between the fragments
     * @param v - the button corresponding to the fragments
     */
    public void switchFragment(View v) {
        if(v.getTag().equals("my_settings")) {
            fragmentManager.beginTransaction().replace(R.id.userProfileFrame, settingsFragment).commit();
        } else if (v.getTag().equals("modify_profile")) {
            fragmentManager.beginTransaction().replace(R.id.userProfileFrame, modifyFragment).commit();
        } else if (v.getTag().equals("my_offers")) {

        }
    }
}
