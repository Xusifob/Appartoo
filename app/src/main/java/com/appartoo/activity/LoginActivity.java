package com.appartoo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.appartoo.R;
import com.appartoo.fragment.HomeFragment;
import com.appartoo.fragment.LogInFragment;
import com.appartoo.model.CompleteUserModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;
import com.appartoo.view.NavigationDrawerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends FragmentActivity {

    private SharedPreferences sharedPreferences;
    private RestService restService;
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private LogInFragment logInFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //Retrieve login button and shared preferences
        sharedPreferences = getSharedPreferences("Appartoo", Context.MODE_PRIVATE);
    }

    @Override
    protected void onStart(){
        super.onStart();

        //Retrieve the user token
        Appartoo.TOKEN = sharedPreferences.getString("token", "");

        //Build a retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restService = retrofit.create(RestService.class);

        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        logInFragment = new LogInFragment();

        //If the token exist, launch the main activity
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(0);
                } catch (Exception e) {

                } finally {
                    if(Appartoo.TOKEN != null && !Appartoo.TOKEN.equals("")) {
                        NavigationDrawerView.setHeaderInformations(sharedPreferences.getString("givenName", "") + " " + sharedPreferences.getString("familyName", ""), sharedPreferences.getString("email", ""));
                        retrieveUserProfile();
                    } else {

                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                .replace(R.id.logInFragments, logInFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        };
        welcomeThread.start();
    }

    /**
     * Launch the signup activity
     * @param v - the button to launch the activity
     */
    public void signUp(View v){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
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

                    sharedPreferences.edit().putString("givenName", Appartoo.LOGGED_USER_PROFILE.getGivenName())
                            .putString("familyName", Appartoo.LOGGED_USER_PROFILE.getFamilyName())
                            .putString("email", Appartoo.LOGGED_USER_PROFILE.getUser().getEmail())
                            .putString("age", Integer.toString(Appartoo.LOGGED_USER_PROFILE.getAge()))
                            .putString("profilePicUrl", Appartoo.LOGGED_USER_PROFILE.getImage().getContentUrl()).apply();
                    if(Appartoo.LOGGED_USER_PROFILE.getImage().getThumbnail() != null) {
                        sharedPreferences.edit().putString("profilePicUrlThumbnail", Appartoo.LOGGED_USER_PROFILE.getImage().getThumbnail().getContentUrl()).apply();
                    }

                    NavigationDrawerView.setHeaderInformations(Appartoo.LOGGED_USER_PROFILE.getGivenName() + " " + Appartoo.LOGGED_USER_PROFILE.getFamilyName(),Appartoo.LOGGED_USER_PROFILE.getUser().getEmail());
                }
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<CompleteUserModel> call, Throwable t) {

                t.printStackTrace();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}