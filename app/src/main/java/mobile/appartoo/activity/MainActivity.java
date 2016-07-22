package mobile.appartoo.activity;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import mobile.appartoo.R;
import mobile.appartoo.fragment.OfferListFragment;
import mobile.appartoo.fragment.SearchOfferFragment;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.view.DrawerListView;

public class MainActivity extends FragmentActivity {

    private final int ID_MENU_SEARCH = 1;
    private DrawerLayout drawerLayout;
    private DrawerListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;
    private OfferListFragment offersFragment;
    private SearchOfferFragment searchOfferFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Retrieve the drawer elements
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerListView = (DrawerListView) findViewById(R.id.drawerList);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        offersFragment = new OfferListFragment();
        searchOfferFragment = new SearchOfferFragment();

    }

    @Override
    public void onStart(){
        super.onStart();

        //Define the drawer
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getActionBar().setIcon(R.drawable.ic_launcher);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        drawerListView.setDrawerLayout(drawerLayout);
        drawerLayout.addDrawerListener(drawerToggle);

        if(Appartoo.LOGGED_USER_PROFILE != null) {
            ((TextView) drawerLayout.findViewById(R.id.drawerUserName)).setText(Appartoo.LOGGED_USER_PROFILE.getGivenName() + " " + Appartoo.LOGGED_USER_PROFILE.getFamilyName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, ID_MENU_SEARCH, Menu.NONE, "Search").setIcon(R.drawable.search).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!offersFragment.isVisible()){
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFragments, offersFragment).commit();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Define the action to do according to the selected menu
        switch (item.getItemId()) {
            case ID_MENU_SEARCH :
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragments, searchOfferFragment).commit();
                return true;
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
}
