package mobile.appartoo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobile.appartoo.R;
import mobile.appartoo.model.UserWithProfileModel;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.RestService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends Activity {

    private Button logInButton;
    private String mail;
    private String password;
    private SharedPreferences sharedPreferences;
    private RestService restService;

    private String givenName;
    private String familyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Retrieve login button and shared preferences
        logInButton = (Button) findViewById(R.id.connectButton);
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

        //If the token exist, launch the main activity
        if(Appartoo.TOKEN != null && !Appartoo.TOKEN.equals("")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            //retrieveUserProfile();
        }
    }

    /**
     * Check if the login form is valid
     * @return true if the form is correctly filled, false if not
     */
    private boolean isFormValid(){
        //Get the mail and password typed
        mail = ((EditText) findViewById(R.id.logInMail)).getText().toString();
        password = ((EditText) findViewById(R.id.logInPassword)).getText().toString();

        //Check that the mail and the password aren't null
        if(mail != null && password != null && !mail.equals("") && !password.equals("")){

            //If the mail has a proper form, return true
            if(isEmailValid(mail)){
                return true;

            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * Check if the string is an email
     * @param email
     * @return true if the string is an email, false if not
     */
    public static boolean isEmailValid(String email) {
        //Regex defining an email
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        //Return the match result of the regex
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Launch the login action
     * @param v - the button that launched the login
     */
    public void logIn(View v){
        //If the form is valid
        if(isFormValid()){

            //Disable the login button
            logInButton.setEnabled(false);

            Call<ResponseBody> callback = restService.postLogIn(mail, password);

            //Handle the server response
            callback.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    //If the login is successful
                    if(response.isSuccessful()) {
                        try {
                            //Retrieve the user token
                            String responseBody = IOUtils.toString(response.body().charStream());
                            JsonObject jsonResponse = new Gson().fromJson(responseBody, JsonObject.class);
                            Appartoo.TOKEN = jsonResponse.get("token").getAsString();

                            //Stock the token in shared preferences
                            sharedPreferences.edit().putString("token", Appartoo.TOKEN).apply();

                            if(Appartoo.TOKEN != null && !Appartoo.TOKEN.equals("")) {
                                retrieveUserProfile();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Erreur, identification impossible.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(), "Erreur, identification impossible.", Toast.LENGTH_SHORT).show();
                        }

                    //If the user didn't send the right credentials
                    } else if(response.code() == 401){
                        Toast.makeText(getApplicationContext(), "Mauvais login ou mot de passe.", Toast.LENGTH_SHORT).show();

                    //If the server isn't responding
                    } else {
                        Toast.makeText(getApplicationContext(), "Erreur de connection au serveur.", Toast.LENGTH_SHORT).show();
                    }

                    logInButton.setEnabled(true);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erreur de connection avec le serveur.", Toast.LENGTH_SHORT).show();
                    logInButton.setEnabled(true);
                }
            });

        //If the form isn't valid, prevent the user.
        } else {
            Toast.makeText(getApplicationContext(), "Veuillez entrer correctement vos identifiants.", Toast.LENGTH_SHORT).show();
        }
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
        Call<ResponseBody> callback = restService.getLoggedUserProfile("Bearer (" + Appartoo.TOKEN + ")");

        //Handle the server response
        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                //If the login is successful
                if(response.isSuccessful()) {
                    try {
                        String responseBody = IOUtils.toString(response.body().charStream());
                        Appartoo.LOGGED_USER_PROFILE = new Gson().fromJson(responseBody, UserWithProfileModel.class);

                        sharedPreferences.edit().putString("givenName", Appartoo.LOGGED_USER_PROFILE.getGivenName()).apply();
                        sharedPreferences.edit().putString("familyName", Appartoo.LOGGED_USER_PROFILE.getFamilyName()).apply();
                        sharedPreferences.edit().putString("email", Appartoo.LOGGED_USER_PROFILE.getUser().getEmail()).apply();

                        //NavigationDrawerView.setHeaderInformations(Appartoo.LOGGED_USER_PROFILE.getGivenName() + " " + Appartoo.LOGGED_USER_PROFILE.getFamilyName(),
                            //    Appartoo.LOGGED_USER_PROFILE.getUser().getEmail());
                    } catch (IOException e) {
                        System.out.println("Can't retrieve user informations.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                givenName = sharedPreferences.getString("givenName", "");
                familyName = sharedPreferences.getString("familyName", "");
                mail = sharedPreferences.getString("email", "");

                if(!givenName.equals("") && !familyName.equals("") && !mail.equals("")) {
                   // NavigationDrawerView.setHeaderInformations(givenName + " " + familyName, mail);
                }

                t.printStackTrace();
            }
        });
    }
}

