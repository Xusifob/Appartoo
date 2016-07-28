package mobile.appartoo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.ParseException;

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
import mobile.appartoo.model.ProfileUpdateModel;
import mobile.appartoo.model.SignUpModel;
import mobile.appartoo.model.UserWithProfileModel;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.RestService;
import mobile.appartoo.view.DisableLastSwipeViewPager;
import mobile.appartoo.view.NavigationDrawerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpProfileActivity extends FragmentActivity {

    private static final int NUM_PAGES = 13;
    private DisableLastSwipeViewPager pager;
    private PagerAdapter pagerAdapter;
    private RestService restService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_profile);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restService = retrofit.create(RestService.class);
        sharedPreferences = getSharedPreferences("Appartoo", Context.MODE_PRIVATE);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


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
            startActivity(new Intent(SignUpProfileActivity.this, MainActivity.class));
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
        startActivity(new Intent(SignUpProfileActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Switch to the next fragment
     * @param v - the button to switch to the next view
     */
    public void nextView(View v){
        if(pager.getCurrentItem() == NUM_PAGES - 1) {
            finish();
        } else if(pager.getCurrentItem() == NUM_PAGES - 2) {
            updateUserProfile();
            pager.setCurrentItem(pager.getCurrentItem()+1);
        } else {
            pager.setCurrentItem(pager.getCurrentItem()+1);
        }
    }

    private void updateUserProfile(){
        ProfileUpdateModel profileUpdateModel = getProfileUpdateModel();
        if(profileUpdateModel != null) {
            System.out.println(profileUpdateModel.toString());
        }

    }

    private ProfileUpdateModel getProfileUpdateModel(){
        ProfileUpdateModel updateModel = new ProfileUpdateModel();
        setToggleViewsInformations(updateModel);

        String society = ((EditText) findViewById(R.id.signUpConfigureSociety)).getText().toString();
        String function = ((EditText) findViewById(R.id.signUpConfigureFunction)).getText().toString();
        String contract = ((EditText) findViewById(R.id.signUpConfigureContract)).getText().toString();
        String incomeStr = ((EditText) findViewById(R.id.signUpConfigureContract)).getText().toString();
        String description = ((EditText) findViewById(R.id.signUpConfigureDescription)).getText().toString();

        Double income = null;
        if(!incomeStr.replaceAll("\\s+","").equals("")) {
            try {
                income = Double.valueOf(incomeStr);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Salaire non reconnu", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        if(!society.replaceAll("\\s+","").equals("")) updateModel.setSociety(society);
        if(!function.replaceAll("\\s+","").equals("")) updateModel.setFunction(function);
        if(!contract.replaceAll("\\s+","").equals("")) updateModel.setContract(contract);
        if(!description.replaceAll("\\s+","").equals("")) updateModel.setDescription(description);
        if(income != null) updateModel.setIncome(income);

        return updateModel;
    }

    private void setToggleViewsInformations(ProfileUpdateModel updateModel){
        boolean man = findViewById(R.id.signUpConfigureMan).isSelected();
        boolean woman = findViewById(R.id.signUpConfigureWoman).isSelected();
        boolean single = findViewById(R.id.signUpConfigureSingle).isSelected();
        boolean inRelationship = findViewById(R.id.signUpConfigureInRelationship).isSelected();
        boolean isSmoker = findViewById(R.id.signUpConfigureIsSmoker).isSelected();
        boolean isNotSmoker = findViewById(R.id.signUpConfigureIsNotSmoker).isSelected();
        boolean student = findViewById(R.id.signUpConfigureStudent).isSelected();
        boolean worker = findViewById(R.id.signUpConfigureWorker).isSelected();
//        boolean searchFlatmate = findViewById(R.id.signUpConfigureSearchFlatmate).isSelected();
//        boolean searchSharedFlat = findViewById(R.id.signUpConfigureSearchSharedFlat).isSelected();
//        boolean shareFlat = findViewById(R.id.signUpConfigureShareFlat).isSelected();

        if(man) updateModel.setGender("Male");
        if(woman) updateModel.setGender("Female");
        if(single) updateModel.setRelationshipStatus("Célibataire");
        if(inRelationship) updateModel.setRelationshipStatus("En couple");
        if(isSmoker) updateModel.setSmoker(true);
        if(isNotSmoker) updateModel.setSmoker(false);
        if(student) updateModel.setContract("student");
        if(worker) updateModel.setContract("worker");
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