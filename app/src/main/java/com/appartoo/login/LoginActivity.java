package com.appartoo.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.appartoo.R;
import com.appartoo.misc.MainActivity;
import com.appartoo.utils.model.CompleteUserModel;
import com.appartoo.signup.SignUpActivity;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;
import com.appartoo.utils.view.NavigationDrawerView;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

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
        sharedPreferences = getSharedPreferences(Appartoo.APP_NAME, Context.MODE_PRIVATE);

        //Retrieve the user token
        Appartoo.TOKEN = sharedPreferences.getString(Appartoo.KEY_TOKEN, "");

        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        logInFragment = new LogInFragment();

        new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(2000);
                } catch (Exception e) {

                } finally {
                    if(Appartoo.TOKEN != null && !Appartoo.TOKEN.equals("")) {
                        NavigationDrawerView.setHeaderInformations(sharedPreferences.getString(Appartoo.KEY_GIVEN_NAME, "") + " " + sharedPreferences.getString(Appartoo.KEY_FAMILY_NAME, ""), sharedPreferences.getString(Appartoo.KEY_EMAIL, ""));
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
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
        //Build a retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restService = retrofit.create(RestService.class);

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
                } else {
                    try {
                        Log.v("LoginActivity", "retrieveUserProfile: " + String.valueOf(response.code()));
                        Log.v("LoginActivity", "retrieveUserProfile: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<CompleteUserModel> call, Throwable t) {

                Log.v("LoginActivity", "retrieveUserProfile: " + t.getMessage());
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public void alertUser(View v) {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppThemeDialog))
                .setMessage("Fonctionnalité bientôt disponible.")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}