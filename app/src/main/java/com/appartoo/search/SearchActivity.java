package com.appartoo.search;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.appartoo.R;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.view.NavigationDrawerView;
import com.appartoo.utils.view.NonSwipeableViewPager;

public class SearchActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private NonSwipeableViewPager viewPager;
    private Toolbar toolbar;
    private NavigationDrawerView navigationDrawerView;


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
    }

    @Override
    public void onStart(){
        super.onStart();

        viewPager.setAdapter(new SearchViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        setSupportActionBar(toolbar);
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
    }

    @Override
    public void onResume(){
        super.onResume();
        navigationDrawerView.updateHeader();
    }

    private class SearchViewPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;

        public String[] titles = {getString(R.string.offers), getString(R.string.profiles)};
        public SearchViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            System.out.println(position);

            switch(position) {
                case 0:
                    return new SearchOfferFragment();
                case 1:
                    return new SearchProfilesFragment();
                default:
                    return new SearchOfferFragment();
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
