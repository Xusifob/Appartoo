package com.appartoo.signup.configuration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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

    private NonSwipeableViewPager pager;
    private ScreenSlidePagerAdapter pagerAdapter;
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
        pager.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
    }

    @Override
    public void onBackPressed() {
        if(pager.getCurrentItem() == 0) {
            startActivity(new Intent(SignUpProfileActivity.this, MainActivity.class));
            finish();
        } else if (pager.getCurrentItem() < pagerAdapter.getCount()-1){
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
        if(pager.getCurrentItem() == pagerAdapter.getCount() - 1) {
            startActivity(new Intent(SignUpProfileActivity.this, MainActivity.class));
            finish();
        } else if(pager.getCurrentItem() == pagerAdapter.getCount() - 2) {
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
                        pager.setCurrentItem(pagerAdapter.getCount()-1);
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

        updateModel.setInRelationship(pagerAdapter.getItemFromClass(SignUpProfileRelationshipFragment.class).getInRelationship());
        updateModel.setSmoker(pagerAdapter.getItemFromClass(SignUpProfileSmokerFragment.class).getSmoker());
        updateModel.setGender(pagerAdapter.getItemFromClass(SignUpProfileGenderFragment.class).getGender());

        String society = pagerAdapter.getItemFromClass(SignUpProfileSocietyFragment.class).getSociety();
        String function = pagerAdapter.getItemFromClass(SignUpProfileFunctionFragment.class).getFunction();
        String contract = pagerAdapter.getItemFromClass(SignUpProfileContractFragment.class).getContract();
        String incomeStr = pagerAdapter.getItemFromClass(SignUpProfileIncomeFragment.class).getIncome();
        String description = pagerAdapter.getItemFromClass(SignUpProfileDescriptionFragment.class).getDescription();

        System.out.println(society + " " + function);

        if(TextValidator.haveText(society)) updateModel.setSociety(society.trim());
        if(TextValidator.haveText(function)) updateModel.setFunction(function.trim());
        if(TextValidator.haveText(contract)) updateModel.setContract(contract.trim());
        if(TextValidator.haveText(description)) updateModel.setDescription(description.trim());
        if(TextValidator.haveText(incomeStr)) updateModel.setIncome(Double.valueOf(incomeStr));

        return updateModel;
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
        parent.setTag(v.getTag());

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

        private Fragment[] fragments = {new SignUpProfileBeginConfFragment(),
        new SignUpProfileConfTypeFragment(), new SignUpProfileGenderFragment(),
        new SignUpProfileRelationshipFragment(), new SignUpProfileSmokerFragment(),
        new SignUpProfileSocietyFragment(), new SignUpProfileFunctionFragment(),
        new SignUpProfileContractFragment(), new SignUpProfileIncomeFragment(),
        new SignUpProfileGarantorFragment(), new SignUpProfileDescriptionFragment(),
        new SignUpProfileSuccessFragment()};

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        public <T> T getItemFromClass(Class<T> fragmentClass) {
            for(Fragment fragment : fragments) {
                if(fragment.getClass().equals(fragmentClass)) return (T) fragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}