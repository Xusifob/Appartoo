package com.appartoo.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.misc.MainActivity;
import com.appartoo.misc.OffersAndProfilesListFragment;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.view.NavigationDrawerView;
import com.appartoo.utils.view.NonSwipeableViewPager;

import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private NonSwipeableViewPager viewPager;
    private Toolbar toolbar;
    private NavigationDrawerView navigationDrawerView;
    private Integer currentPage;
    private SearchViewPagerAdapter adapter;
    private int colorBlue;
    private int colorWhite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //Retrieve the drawer elements
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationDrawerView = (NavigationDrawerView) findViewById(R.id.navigationDrawer);
        tabLayout = (TabLayout) findViewById(R.id.searchTabs);
        viewPager = (NonSwipeableViewPager) findViewById(R.id.searchViewPager);

        colorBlue = ContextCompat.getColor(SearchActivity.this, R.color.colorBlue);
        colorWhite = ContextCompat.getColor(SearchActivity.this, R.color.colorWhite);
        adapter = new SearchViewPagerAdapter(getSupportFragmentManager());
    }

    @Override
    public void onStart(){
        super.onStart();

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.drawer_search);

        if(Appartoo.TOKEN != null && !Appartoo.TOKEN.equals("")) {
            navigationDrawerView.setDrawerLayout(drawerLayout);

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
            toolbar.setNavigationIcon(R.drawable.left_arrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public void launchSearch(View v) {

        SearchOffersFragment offersFragment = (SearchOffersFragment) adapter.getItem(0);
        SearchProfilesFragment profilesFragment = (SearchProfilesFragment) adapter.getItem(1);

        HashMap<String, Object> offerQuery = offersFragment.getOfferQuery();
        HashMap<String, Object> profileQuery = profilesFragment.getProfileQuery();
        HashMap<String, Object> query = new HashMap<>();

        if(offerQuery != null) query.putAll(offerQuery);
        else query.put("type", "profile");

        if(profileQuery != null) query.putAll(profileQuery);
        else if (offerQuery != null) query.put("type", "offer");
        else query = null;

        if(query != null) {
            Intent intent = new Intent(SearchActivity.this, MainActivity.class);
            query.put("start", 0);
            query.put("limit", OffersAndProfilesListFragment.LIMIT);
            intent.putExtra("query", query);
            startActivity(intent);
        }
    }

    public void toggleView(View v) {

        TextView toggled = (TextView) v;
        LinearLayout layout = (LinearLayout) v.getParent();

        for(int i = 0 ; i < layout.getChildCount() ; i++) {
            TextView child = (TextView) layout.getChildAt(i);
            child.setBackgroundColor(colorBlue);
            child.setTextColor(colorWhite);
        }

        toggled.setBackgroundColor(colorWhite);
        toggled.setTextColor(colorBlue);

        layout.setTag(toggled.getTag());
    }

    @Override
    public void onResume(){
        super.onResume();

        if(currentPage != null) viewPager.setCurrentItem(currentPage);
        navigationDrawerView.updateHeader();
    }

    @Override
    public void onPause() {
        super.onPause();
        currentPage = viewPager.getCurrentItem();
    }

    private class SearchViewPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;

        public String[] titles = {getString(R.string.offers), getString(R.string.profiles)};
        public Fragment[] fragments = {new SearchOffersFragment(), new SearchProfilesFragment()};

        public SearchViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position < fragments.length) {
                return fragments[position];
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }
}
