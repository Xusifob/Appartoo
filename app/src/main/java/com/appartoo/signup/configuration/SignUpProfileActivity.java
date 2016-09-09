package com.appartoo.signup.configuration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.appartoo.R;
import com.appartoo.misc.MainActivity;
import com.appartoo.utils.model.CompleteUserModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;
import com.appartoo.utils.TextValidator;
import com.appartoo.utils.view.NonSwipeableViewPager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpProfileActivity extends FragmentActivity {

    private static final int NUM_PAGES = 12;
    private NonSwipeableViewPager pager;
    private PagerAdapter pagerAdapter;
    private RestService restService;
    private Button updateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_profile);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restService = retrofit.create(RestService.class);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        pager = (NonSwipeableViewPager) findViewById(R.id.signup_pager);
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
        } else if (pager.getCurrentItem() < NUM_PAGES-1){
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
            startActivity(new Intent(SignUpProfileActivity.this, MainActivity.class));
            finish();
        } else if(pager.getCurrentItem() == NUM_PAGES - 2) {
            updateProfile = (Button) v;
            updateUserProfile();
        } else {
            pager.setCurrentItem(pager.getCurrentItem()+1);
        }
    }

    public void previousView(View v){
        pager.setCurrentItem(pager.getCurrentItem()-1);
    }

    private void updateUserProfile(){
        final CompleteUserModel profileUpdateModel = getProfileUpdateModel();
        updateProfile.setEnabled(false);

        if(profileUpdateModel != null && Appartoo.LOGGED_USER_PROFILE != null) {

            Call<CompleteUserModel> callback = restService.updateUserProfile(Appartoo.LOGGED_USER_PROFILE.getId(),"Bearer " + Appartoo.TOKEN, profileUpdateModel);
            callback.enqueue(new Callback<CompleteUserModel>() {
                @Override
                public void onResponse(Call<CompleteUserModel> call, Response<CompleteUserModel> response) {
                    if(response.isSuccessful()) {

                        updateUserLoggedModel(profileUpdateModel);
                        pager.setCurrentItem(NUM_PAGES-1);
                        updateProfile.setEnabled(true);

                    } else {
                        try {
                            Log.v("SignUpProfileActivity", "updateUserProfile: " + String.valueOf(response.code()));
                            Log.v("SignUpProfileActivity", "updateUserProfile: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        updateProfile.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<CompleteUserModel> call, Throwable t) {
                    Log.v("SignUpProfileActivity", "updateUserProfile: " + t.getMessage());
                }
            });
        } else {
            updateProfile.setEnabled(true);
        }
    }

    private CompleteUserModel getProfileUpdateModel(){
        CompleteUserModel updateModel = new CompleteUserModel();
        setToggleViewsInformations(updateModel);

        String society = ((EditText) findViewById(R.id.signUpConfigureSociety)).getText().toString();
        String function = ((EditText) findViewById(R.id.signUpConfigureFunction)).getText().toString();
        String contract = findViewById(R.id.signUpConfigureContract).getTag().toString();
        String incomeStr = ((EditText) findViewById(R.id.signUpConfigureIncome)).getText().toString();
        String description = ((EditText) findViewById(R.id.signUpConfigureDescription)).getText().toString();

        if(TextValidator.haveText(society)) updateModel.setSociety(society.trim());
        if(TextValidator.haveText(function)) updateModel.setFunction(function.trim());
        if(TextValidator.haveText(contract)) updateModel.setContract(contract.trim());
        if(TextValidator.haveText(description)) updateModel.setDescription(description.trim());
        if(TextValidator.haveText(incomeStr)) updateModel.setIncome(Double.valueOf(incomeStr));

        return updateModel;
    }

    private void setToggleViewsInformations(CompleteUserModel updateModel){
        boolean man = findViewById(R.id.signUpConfigureMan).isSelected();
        boolean woman = findViewById(R.id.signUpConfigureWoman).isSelected();
        boolean single = findViewById(R.id.signUpConfigureSingle).isSelected();
        boolean inRelationship = findViewById(R.id.signUpConfigureInRelationship).isSelected();
        boolean isSmoker = findViewById(R.id.signUpConfigureIsSmoker).isSelected();
        boolean isNotSmoker = findViewById(R.id.signUpConfigureIsNotSmoker).isSelected();

        if(man) updateModel.setGender("Male");
        if(woman) updateModel.setGender("Female");
        if(single) updateModel.setInRelationship(false);
        if(inRelationship) updateModel.setInRelationship(true);
        if(isSmoker) updateModel.setSmoker(true);
        if(isNotSmoker) updateModel.setSmoker(false);
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

    private void updateUserLoggedModel(CompleteUserModel updateModel){
        if(Appartoo.LOGGED_USER_PROFILE != null) {
            Appartoo.LOGGED_USER_PROFILE.setGender(updateModel.getGender());
            Appartoo.LOGGED_USER_PROFILE.setInRelationship(updateModel.getInRelationship());
            Appartoo.LOGGED_USER_PROFILE.setSmoker(updateModel.getSmoker());
            Appartoo.LOGGED_USER_PROFILE.setSociety(updateModel.getSociety());
            Appartoo.LOGGED_USER_PROFILE.setFunction(updateModel.getFunction());
            Appartoo.LOGGED_USER_PROFILE.setContract(updateModel.getContract());
            Appartoo.LOGGED_USER_PROFILE.setDescription(updateModel.getDescription());
            Appartoo.LOGGED_USER_PROFILE.setIncome(updateModel.getIncome());
        }
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
                case 0: return new SignUpProfileBeginConfFragment();
                case 1: return new SignUpProfileConfTypeFragment();
                case 2: return new SignUpProfileGenderFragment();
                case 3: return new SignUpProfileStatusFragment();
                case 4: return new SignUpProfileSmokerFragment();
                case 5: return new SignUpProfileSocietyFragment();
                case 6: return new SignUpProfileFunctionFragment();
                case 7: return new SignUpProfileContractFragment();
                case 8: return new SignUpProfileIncomeFragment();
                case 9: return new SignUpProfileGarantorFragment();
                case 10: return new SignUpProfileDescriptionFragment();
                case 11: return new SignUpProfileSuccessFragment();
                default: return new SignUpProfileBeginConfFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}