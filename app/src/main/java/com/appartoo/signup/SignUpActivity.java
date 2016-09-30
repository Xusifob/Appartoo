package com.appartoo.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.misc.MainActivity;
import com.appartoo.utils.ConversationIdReceiver;
import com.appartoo.utils.ValidationFragment;
import com.appartoo.utils.model.CompleteUserModel;
import com.appartoo.utils.model.SignUpModel;
import com.appartoo.signup.configuration.SignUpProfileActivity;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;
import com.appartoo.utils.TokenReceiver;
import com.appartoo.utils.view.NavigationDrawerView;
import com.appartoo.utils.view.NonSwipeableViewPager;

import java.io.IOException;

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
    private NonSwipeableViewPager pager;
    private ScreenSlidePagerAdapter pagerAdapter;
    private RestService restService;
    private SharedPreferences sharedPreferences;
    private SignUpModel newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        System.out.println(getIntent().getBooleanExtra("connection", false));

        //Retreive the resources
        pager = (NonSwipeableViewPager) findViewById(R.id.signup_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
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

        for(int i = 0 ; i < pagerAdapter.getCount() ; i++) {
            if (!pagerAdapter.getItem(i).validateFragment(SignUpActivity.this)) {
                Toast.makeText(SignUpActivity.this, R.string.error_missing_info_sign_up, Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        return new SignUpModel(
                ((SignUpEmailFragment) pagerAdapter.getItem(1)).getEmail(),
                ((SignUpPasswordFragment) pagerAdapter.getItem(2)).getPassword(),
                ((SignUpNameFragment) pagerAdapter.getItem(0)).getGivenName(),
                ((SignUpNameFragment) pagerAdapter.getItem(0)).getFamilyName());
    }

    /**
     * Switch to the next fragment
     * @param v - the button to switch to the next view
     */
    public void nextView(View v){
        if(pager.getCurrentItem() == NUM_PAGES - 1) {
            finishSignUp(v);
        } else {
            if(pagerAdapter.getItem(pager.getCurrentItem()).validateFragment(SignUpActivity.this)) {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        }
    }

    public void finishSignUp(View v){

        newUser = getSignUpModel();
        if(getSignUpModel() != null){

            ((SignUpFinishFragment) pagerAdapter.getItem(NUM_PAGES-1)).setButtonEnabled(false);

            Call<ResponseBody> callback = restService.registerUser(newUser.getEmail(), newUser.getPassword(), newUser.getGivenName(), newUser.getFamilyName());

            //Handle the server response
            callback.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //If the login is successful
                    if(response.isSuccessful()) {
                        logUser();
                    } else if(response.code() == 402) {
                        ((SignUpFinishFragment) pagerAdapter.getItem(NUM_PAGES-1)).setButtonEnabled(true);
                        Toast.makeText(getApplicationContext(), R.string.user_already_exists, Toast.LENGTH_SHORT).show();
                    } else {
                        ((SignUpFinishFragment) pagerAdapter.getItem(NUM_PAGES-1)).setButtonEnabled(true);
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
                    ((SignUpFinishFragment) pagerAdapter.getItem(NUM_PAGES-1)).setButtonEnabled(true);
                }
            });
        }
    }

    private void logUser() {
        Call<TokenReceiver> newCallback = restService.logInWithAPI(newUser.getEmail(), newUser.getPassword());

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
                            .putString(Appartoo.KEY_EMAIL, Appartoo.LOGGED_USER_PROFILE.getEmail())
                            .putString(Appartoo.KEY_PROFILE_PICTURE, Appartoo.LOGGED_USER_PROFILE.getImage().getContentUrl()).apply();

                    NavigationDrawerView.setHeaderInformations(Appartoo.LOGGED_USER_PROFILE.getGivenName() + " " + Appartoo.LOGGED_USER_PROFILE.getFamilyName(),Appartoo.LOGGED_USER_PROFILE.getEmail());
                    Appartoo.initiateFirebase();

                    if (getIntent().getStringExtra("userId") != null) {
                        applyToOffer();
                    } else if(getIntent().getBooleanExtra("connection", false)) {
                        setResult(Appartoo.IS_LOGGED);
                        finish();
                    } else {
                        launchActivityWithoutHistory(SignUpProfileActivity.class);
                    }
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

                    if(getIntent().getBooleanExtra("connection", false)) {
                        setResult(Appartoo.IS_LOGGED);
                        finish();
                    } else {
                        launchActivityWithoutHistory(MainActivity.class);
                    }
                }
            }

            @Override
            public void onFailure(Call<CompleteUserModel> call, Throwable t) {
                if(getIntent().getBooleanExtra("connection", false)) {
                    setResult(Appartoo.IS_LOGGED);
                    finish();
                } else {
                    launchActivityWithoutHistory(MainActivity.class);
                }
                Log.v("SignUpActivity", "retrieveUserProfile: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pager.getWindowToken(), 0);
    }

    private void launchActivityWithoutHistory(Class activityClass){
        Intent intent = new Intent(SignUpActivity.this, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * A simple adapter to associate with the view pager
     */
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        ValidationFragment[] fragments = {new SignUpNameFragment(), new SignUpEmailFragment(), new SignUpPasswordFragment(), new SignUpFinishFragment()};

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public ValidationFragment getItem(int position) {
            if(position < fragments.length) {
                return fragments[position];
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private void applyToOffer(){
        final ProgressDialog progressDialog = ProgressDialog.show(SignUpActivity.this, "Création de la conversation", "Veuillez patienter...", true);

        Call<ConversationIdReceiver> callback = restService.sendMessageToUser("Bearer " + Appartoo.TOKEN, getIntent().getStringExtra("userId"));
        callback.enqueue(new Callback<ConversationIdReceiver>() {
            @Override
            public void onResponse(Call<ConversationIdReceiver> call, Response<ConversationIdReceiver> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    Intent intent = new Intent();
                    intent.putExtra("conversationId", response.body().getIdConversation());
                    setResult(Appartoo.IS_LOGGED_FOR_CONVERSATION, intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, R.string.error_conversation_creation, Toast.LENGTH_SHORT).show();
                    try {
                        Log.v("LoginActivity", "applyToOffer: " + String.valueOf(response.code()));
                        Log.v("LoginActivity", "applyToOffer: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ConversationIdReceiver> call, Throwable t) {
                progressDialog.dismiss();
                finish();
                Log.v("LoginActivity", "applyToOffer: " + t.getMessage());
            }
        });
    }
}

