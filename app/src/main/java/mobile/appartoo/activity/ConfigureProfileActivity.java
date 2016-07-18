package mobile.appartoo.activity;

/**
 * Created by alexandre on 16-07-18.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import mobile.appartoo.R;
import mobile.appartoo.fragment.ConfigureProfileFirstFragment;
import mobile.appartoo.fragment.ConfigureProfileSecondFragment;
import mobile.appartoo.fragment.ConfigureProfileThirdFragment;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ConfigureProfileActivity extends FragmentActivity {

    private static final int NUM_PAGES = 3;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_profile);

        pager = (ViewPager) findViewById(R.id.signup_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }

    @Override
    public void onStart(){
        pager.setOffscreenPageLimit(NUM_PAGES - 1);
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if(pager.getCurrentItem() == 0) {
            startActivity(new Intent(ConfigureProfileActivity.this, MainActivity.class));
            finish();
        } else {
            pager.setCurrentItem(pager.getCurrentItem()-1);
        }
    }

    public void goToMainActivity(View v) {
        startActivity(new Intent(ConfigureProfileActivity.this, MainActivity.class));
        finish();
    }

    public void nextView(View v){
        if(pager.getCurrentItem() == NUM_PAGES - 1) {
            startActivity(new Intent(ConfigureProfileActivity.this, MainActivity.class));
            finish();
        } else {
            pager.setCurrentItem(pager.getCurrentItem()+1);
        }
    }

    public void toggleView(View v) {
        LinearLayout parent = (LinearLayout) v.getParent();

        for(int i = 0 ; i < parent.getChildCount() ; i++) {
            if(parent.getChildAt(i).equals(v)) {
                parent.getChildAt(i).getBackground().setState(new int[] {android.R.attr.state_checked});
            } else {
                parent.getChildAt(i).getBackground().setState(new int[] {});
            }
        }
    }

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
                default: return new ConfigureProfileFirstFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}

