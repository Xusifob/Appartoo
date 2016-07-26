package mobile.appartoo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.LinearLayout;

import mobile.appartoo.R;
import mobile.appartoo.fragment.ConfigureProfileEighthFragment;
import mobile.appartoo.fragment.ConfigureProfileEleventhFragment;
import mobile.appartoo.fragment.ConfigureProfileFifthFragment;
import mobile.appartoo.fragment.ConfigureProfileFirstFragment;
import mobile.appartoo.fragment.ConfigureProfileFourthFragment;
import mobile.appartoo.fragment.ConfigureProfileNinthFragment;
import mobile.appartoo.fragment.ConfigureProfileSecondFragment;
import mobile.appartoo.fragment.ConfigureProfileSeventhFragment;
import mobile.appartoo.fragment.ConfigureProfileSixthFragment;
import mobile.appartoo.fragment.ConfigureProfileTenthFragment;
import mobile.appartoo.fragment.ConfigureProfileThirdFragment;
import mobile.appartoo.fragment.ConfigureProfileThirteenthFragment;
import mobile.appartoo.fragment.ConfigureProfileTwelfthFragment;
import mobile.appartoo.view.DisableLastSwipeViewPager;

public class ConfigureProfileActivity extends FragmentActivity {

    private static final int NUM_PAGES = 13;
    private DisableLastSwipeViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_profile);

        pager = (DisableLastSwipeViewPager) findViewById(R.id.signup_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        pager.setOffscreenPageLimit(NUM_PAGES - 1);
    }

    @Override
    public void onBackPressed() {
        if(pager.getCurrentItem() == 0) {
            startActivity(new Intent(ConfigureProfileActivity.this, MainActivity.class));
            finish();
        } else if (pager.getCurrentItem() < 9){
            pager.setCurrentItem(pager.getCurrentItem()-1);
        }
    }

    /**
     * Launch the main activity
     * @param v - the button pressed to launch the activity
     */
    public void goToMainActivity(View v) {
        startActivity(new Intent(ConfigureProfileActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Switch to the next fragment
     * @param v - the button to switch to the next view
     */
    public void nextView(View v){
        if(pager.getCurrentItem() == NUM_PAGES - 1) {
            startActivity(new Intent(ConfigureProfileActivity.this, MainActivity.class));
            finish();
        } else {
            pager.setCurrentItem(pager.getCurrentItem()+1);
        }
    }

    /**
     * Modify the layout of the toggle buttons
     * @param v - the clicked button
     */
    public void toggleView(View v) {
        LinearLayout parent = (LinearLayout) v.getParent();

        //Set all buttons as unselected
        for(int i = 0 ; i < parent.getChildCount() ; i++) {
            if(!parent.getChildAt(i).equals(v)) {
                parent.getChildAt(i).setSelected(false);
            }
        }

        //Set the clicked button as selected
        v.setSelected(true);
    }

    /**
     * A simple adapter to associate with the view pager
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0: return new ConfigureProfileFirstFragment();
                case 1: return new ConfigureProfileSecondFragment();
                case 2: return new ConfigureProfileThirdFragment();
                case 3: return new ConfigureProfileFourthFragment();
                case 4: return new ConfigureProfileFifthFragment();
                case 5: return new ConfigureProfileSixthFragment();
                case 6: return new ConfigureProfileSeventhFragment();
                case 7: return new ConfigureProfileEighthFragment();
                case 8: return new ConfigureProfileNinthFragment();
                case 9: return new ConfigureProfileTenthFragment();
                case 10: return new ConfigureProfileEleventhFragment();
                case 11: return new ConfigureProfileTwelfthFragment();
                case 12: return new ConfigureProfileThirteenthFragment();
                default: return new ConfigureProfileFirstFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}