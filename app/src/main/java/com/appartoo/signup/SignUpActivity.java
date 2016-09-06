package com.appartoo.signup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.general.MainActivity;
import com.appartoo.utils.model.CompleteUserModel;
import com.appartoo.utils.model.SignUpModel;
import com.appartoo.signup.configuration.SignUpProfileActivity;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;
import com.appartoo.utils.TextValidator;
import com.appartoo.utils.TokenReceiver;
import com.appartoo.utils.view.NavigationDrawerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SignUpActivity extends FragmentActivity {

    private static final int NUM_PAGES = 4;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat jsonFormat;
    private RestService restService;
    private Button signUpButton;
    private SharedPreferences sharedPreferences;
    private SignUpModel newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //Retreive the resources
        pager = (ViewPager) findViewById(R.id.signup_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        jsonFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        pager.setAdapter(pagerAdapter);
    }

    @Override
    public void onStart(){

        //Define the number of pages to keep in memory
        pager.setOffscreenPageLimit(NUM_PAGES - 1);
        sharedPreferences = getSharedPreferences(Appartoo.APP_NAME, Context.MODE_PRIVATE);

        //Build a retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restService = retrofit.create(RestService.class);

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

    public void previousView(View v){
        if(pager.getCurrentItem() == 0) {
            finish();
        } else {
            pager.setCurrentItem(pager.getCurrentItem()-1);
        }
    }

    /**
     * Checks if the informations submitted have correct
     * @return true if the informations are correct, false if they are not.
     */
    private SignUpModel getSignUpModel(){
        //Retrieve the form inputs
        String firstName = ((EditText) findViewById(R.id.signupFirstName)).getText().toString().trim();
        String lastName = ((EditText) findViewById(R.id.signupLastName)).getText().toString().trim();
        String email = ((EditText) findViewById(R.id.signUpMail)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.signUpPassword)).getText().toString();
        String password_confirm = ((EditText) findViewById(R.id.signUpPasswordConfirm)).getText().toString();

        //Check the edit text input
        if(!TextValidator.haveText(new String[] {firstName, lastName, password, password_confirm, email})) {
            Toast.makeText(getApplicationContext(), R.string.error_missing_info_sign_up, Toast.LENGTH_LONG).show();
            return null;
        }

        //Check if mail is valid
        if(!TextValidator.isEmail(email)) {
            Toast.makeText(getApplicationContext(), R.string.error_mail, Toast.LENGTH_LONG).show();
            return null;
        }

        //Check if password is valid
        if(!password.equals(password_confirm)) {
            Toast.makeText(getApplicationContext(), R.string.error_password_not_identical, Toast.LENGTH_LONG).show();
            return null;
        }

        return new SignUpModel(email, password, firstName, lastName);
    }

    /**
     * Switch to the next fragment
     * @param v - the button to switch to the next view
     */
    public void nextView(View v){
        if(pager.getCurrentItem() == NUM_PAGES - 1) {
            finishSignUp(v);
        } else {
            pager.setCurrentItem(pager.getCurrentItem()+1);
        }
    }

    public void finishSignUp(View v){
        signUpButton = (Button) v;

        newUser = getSignUpModel();
        if(getSignUpModel() != null){

            signUpButton.setEnabled(false);
            Call<ResponseBody> callback = restService.postUser(newUser.getEmail(), newUser.getPassword(), newUser.getGivenName(), newUser.getFamilyName());

            //Handle the server response
            callback.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //If the login is successful
                    if(response.isSuccessful()) {
                        logUser();
                    } else if(response.code() == 402) {
                        signUpButton.setEnabled(true);
                        Toast.makeText(getApplicationContext(), R.string.user_already_exists, Toast.LENGTH_SHORT).show();
                    } else {
                        signUpButton.setEnabled(true);
                        try {
                            Log.v("SignUpActivity", "finishSignUp: " + String.valueOf(response.code()));
                            Log.v("SignUpActivity", "finishSignUp: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Log.v("SignUpActivity", "finishSignUp: " + t.getMessage());
                    Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                    signUpButton.setEnabled(true);
                }
            });
        }
    }

    private void logUser() {
        Call<TokenReceiver> newCallback = restService.postLogIn(newUser.getEmail(), newUser.getPassword());

        newCallback.enqueue(new Callback<TokenReceiver>() {
            @Override
            public void onResponse(Call<TokenReceiver> call, Response<TokenReceiver> response) {
                if(response.isSuccessful()) {
                    Appartoo.TOKEN = response.body().getToken();
                    sharedPreferences.edit().putString(Appartoo.KEY_TOKEN, Appartoo.TOKEN).apply();
                    retrieveUserProfile();
                } else {
                    finish();
                    try {
                        Log.v("SignUpActivity", "logUser: " + String.valueOf(response.code()));
                        Log.v("SignUpActivity", "logUser: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), R.string.success_sign_up, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenReceiver> call, Throwable t) {
                finish();
                Log.v("SignUpActivity", "logUser: " + t.getMessage());
                Toast.makeText(getApplicationContext(), R.string.success_sign_up, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveUserProfile(){
        Call<CompleteUserModel> callback = restService.getLoggedUserProfile("Bearer " + Appartoo.TOKEN);

        //Handle the server response
        callback.enqueue(new Callback<CompleteUserModel>() {
            @Override
            public void onResponse(Call<CompleteUserModel> call, Response<CompleteUserModel> response) {

                //If the login is successful
                if(response.isSuccessful()) {
                    Appartoo.LOGGED_USER_PROFILE = response.body();

                    sharedPreferences.edit().putString(Appartoo.KEY_GIVEN_NAME, Appartoo.LOGGED_USER_PROFILE.getGivenName())
                            .putString(Appartoo.KEY_FAMILY_NAME, Appartoo.LOGGED_USER_PROFILE.getFamilyName())
                            .putString(Appartoo.KEY_EMAIL, Appartoo.LOGGED_USER_PROFILE.getUser().getEmail())
                            .putString(Appartoo.KEY_PROFILE_PICTURE, Appartoo.LOGGED_USER_PROFILE.getImage().getContentUrl()).apply();

                    NavigationDrawerView.setHeaderInformations(Appartoo.LOGGED_USER_PROFILE.getGivenName() + " " + Appartoo.LOGGED_USER_PROFILE.getFamilyName(),Appartoo.LOGGED_USER_PROFILE.getUser().getEmail());
                    Appartoo.initiateFirebase();

                    launchActivityWithoutHistory(SignUpProfileActivity.class);
                } else {

                    try {
                        Log.v("SignUpActivity", "retrieveUserProfile: " + String.valueOf(response.code()));
                        Log.v("SignUpActivity", "retrieveUserProfile: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sharedPreferences.edit().putString(Appartoo.KEY_GIVEN_NAME, newUser.getGivenName())
                            .putString(Appartoo.KEY_FAMILY_NAME, newUser.getFamilyName())
                            .putString(Appartoo.KEY_EMAIL, newUser.getEmail())
                            .putString(Appartoo.KEY_PROFILE_PICTURE, "images/profile.png").apply();

                    NavigationDrawerView.setHeaderInformations(newUser.getGivenName() + " " + newUser.getFamilyName(), newUser.getEmail());

                    launchActivityWithoutHistory(MainActivity.class);
                }
            }

            @Override
            public void onFailure(Call<CompleteUserModel> call, Throwable t) {
                launchActivityWithoutHistory(MainActivity.class);
                Log.v("SignUpActivity", "retrieveUserProfile: " + t.getMessage());
            }
        });
    }


    private void launchActivityWithoutHistory(Class activityClass){
        Intent intent = new Intent(SignUpActivity.this, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
                case 0: return new SignUpFirstFragment();
                case 1: return new SignUpSecondFragment();
                case 2: return new SignUpThirdFragment();
                case 3: return new SignUpFourthFragment();
                default: return new SignUpFourthFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}

