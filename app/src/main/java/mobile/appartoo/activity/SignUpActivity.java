package mobile.appartoo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import mobile.appartoo.R;
import mobile.appartoo.fragment.SignUpFifthFragment;
import mobile.appartoo.fragment.SignUpFirstFragment;
import mobile.appartoo.fragment.SignUpFourthFragment;
import mobile.appartoo.fragment.SignUpSecondFragment;
import mobile.appartoo.fragment.SignUpThirdFragment;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SignUpActivity extends FragmentActivity {

    private static final int NUM_PAGES = 5;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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
            finish();
        } else {
            pager.setCurrentItem(pager.getCurrentItem()-1);
        }
    }

    public void toggleView(View v) {

        int sdk = android.os.Build.VERSION.SDK_INT;
        LinearLayout parent = (LinearLayout) v.getParent();

        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            for(int i = 0 ; i < parent.getChildCount() ; i++){
                System.out.println(parent.getChildAt(i).equals(v));
                if(parent.getChildAt(i).equals(v)){
                    parent.getChildAt(i).setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_bordered_square_light_green));
                } else {
                    parent.getChildAt(i).setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_bordered_square));
                }
            }
        } else {
            for(int i = 0 ; i < parent.getChildCount() ; i++){
                System.out.println(parent.getChildAt(i).equals(v));
                if(parent.getChildAt(i).equals(v)){
                    parent.getChildAt(i).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_bordered_square_light_green));
                } else {
                    parent.getChildAt(i).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_bordered_square));
                }
            }
        }
    }

    public void finishSignup(View v){
        String firstName = ((EditText) findViewById(R.id.signupFirstName)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.signupFirstName)).getText().toString();

        if(firstName.equals("") || lastName.equals("")){
            pager.setCurrentItem(1);
            Toast.makeText(getApplicationContext(), "Vous devez entrer toutes les informations du formulaires pour finir l'inscription.", Toast.LENGTH_LONG).show();
        }
    }

    public void nextView(View v){
        if(pager.getCurrentItem() == NUM_PAGES - 1) {
            finish();
        } else {
            pager.setCurrentItem(pager.getCurrentItem()+1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0: return new SignUpFirstFragment();
                case 1: return new SignUpSecondFragment();
                case 2: return new SignUpThirdFragment();
                case 3: return new SignUpFourthFragment();
                case 4: return new SignUpFifthFragment();
                default: return new SignUpFirstFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}

