package mobile.appartoo.activity;

import android.app.DatePickerDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobile.appartoo.R;
import mobile.appartoo.fragment.SignUpFifthFragment;
import mobile.appartoo.fragment.SignUpFirstFragment;
import mobile.appartoo.fragment.SignUpFourthFragment;
import mobile.appartoo.fragment.SignUpSecondFragment;
import mobile.appartoo.fragment.SignUpThirdFragment;
import mobile.appartoo.model.SignUpModel;
import mobile.appartoo.model.UserWithProfileModel;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.RestService;
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

    private String password;
    private String email;
    private static final int NUM_PAGES = 5;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener date;
    private SimpleDateFormat dateFormat;
    private RestService restService;
    private Button signUpButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Retreive the resources
        pager = (ViewPager) findViewById(R.id.signup_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        pager.setAdapter(pagerAdapter);
    }

    @Override
    public void onStart(){

        //Define the number of pages to keep in memory
        pager.setOffscreenPageLimit(NUM_PAGES - 1);
        sharedPreferences = getSharedPreferences("Appartoo", Context.MODE_PRIVATE);

        //Define today's date
        calendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBirthDate();
            }

        };

        //Build a retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restService = retrofit.create(RestService.class);
        signUpButton = (Button) findViewById(R.id.buttonFinishSignUp);

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

    public void finishSignUp(View v){
        signUpButton = (Button) v;

        if(isFormValid()){

            signUpButton.setEnabled(false);
            Call<ResponseBody> callback = restService.postUser(new SignUpModel(email, password));

            //Handle the server response
            callback.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //If the login is successful
                    if(response.isSuccessful()) {
                        try {
                            String responseBody = IOUtils.toString(response.body().charStream());
                            JsonObject jsonResponse = new Gson().fromJson(responseBody, JsonObject.class);
                            logUser();
                            startActivity(new Intent(SignUpActivity.this, ConfigureProfileActivity.class));

                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(), "Erreur, identification impossible.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Erreur de connection au serveur.", Toast.LENGTH_SHORT).show();
                    }
                    signUpButton.setEnabled(true);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erreur de connection avec le serveur.", Toast.LENGTH_SHORT).show();
                    signUpButton.setEnabled(true);
                }
            });

        //If the form isn't valid, prevent the user.
        }
    }

    private void logUser() {
        System.out.println("Logging user...");
        Call<ResponseBody> newCallback = restService.postLogIn(email, password);

        newCallback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        String responseBody = IOUtils.toString(response.body().charStream());
                        JsonObject jsonResponse = new Gson().fromJson(responseBody, JsonObject.class);
                        Appartoo.TOKEN = jsonResponse.get("token").getAsString();
                        sharedPreferences.edit().putString("token", Appartoo.TOKEN).commit();

                        if(Appartoo.TOKEN != null && !Appartoo.TOKEN.equals("")) {
                            retrieveUserProfile();
                        }
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Erreur, identification impossible.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "L'inscription s'est bien déroulée, mais un problème est survenu lors de la connection.", Toast.LENGTH_SHORT).show();
                }
                signUpButton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "L'inscription s'est bien déroulée, mais un problème est survenu lors de la connection.", Toast.LENGTH_SHORT).show();
                signUpButton.setEnabled(true);
            }
        });
    }

    private void retrieveUserProfile(){
        System.out.println("Retrieving profile user...");
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
                        System.out.println(Appartoo.LOGGED_USER_PROFILE.toString());
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Erreur, identification impossible.", Toast.LENGTH_SHORT).show();
                    }

                //If the user didn't send the right credentials
                } else {
                    Toast.makeText(getApplicationContext(), "Impossible de récupérer vos informations via le serveur.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Impossible de récupérer vos informations via le serveur.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Update the edit text object that shows the selected date
     */
    private void updateBirthDate(){
        ((EditText) findViewById(R.id.signUpBirthdate)).setText(dateFormat.format(calendar.getTime()));
    }

    /**
     * Open a dialog to pick a date easily
     * @param v
     */
    public void openDatePicker(View v) {
        new DatePickerDialog(SignUpActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Checks if the informations submitted have correct
     * @return true if the informations are correct, false if they are not.
     */
    private boolean isFormValid(){
        //Retrieve the form inputs
        boolean man = findViewById(R.id.signUpMan).isSelected();
        boolean woman = findViewById(R.id.signUpWoman).isSelected();
        boolean single = findViewById(R.id.signUpSingle).isSelected();
        boolean inRelationship = findViewById(R.id.signUpInRelationship).isSelected();
        boolean smoker = findViewById(R.id.signUpSmoker).isSelected();
        boolean nonSmoker = findViewById(R.id.signUpNonSmoker).isSelected();
        String firstName = ((EditText) findViewById(R.id.signupFirstName)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.signupLastName)).getText().toString();
        String password_confirm = ((EditText) findViewById(R.id.signUpPasswordConfirm)).getText().toString();
        String birthdate = ((EditText) findViewById(R.id.signUpBirthdate)).getText().toString();
        email = ((EditText) findViewById(R.id.signUpMail)).getText().toString();
        password = ((EditText) findViewById(R.id.signUpPassword)).getText().toString();

        //Check the toggle buttons
        if((!man && !woman) || (!single && !inRelationship) || (!smoker && !nonSmoker)) {
            Toast.makeText(getApplicationContext(), "Vous devez entrer toutes les informations du formulaires pour finir l'inscription.", Toast.LENGTH_LONG).show();
            return false;
        }

        //Check the edit text input
        if(firstName.replaceAll("\\s+","").equals("") || lastName.replaceAll("\\s+","").equals("") ||
                password.replaceAll("\\s+","").equals("") || password_confirm.replaceAll("\\s+","").equals("") ||
                birthdate.replaceAll("\\s+","").equals("") || email.replaceAll("\\s+","").equals("")) {
            Toast.makeText(getApplicationContext(), "Vous devez entrer toutes les informations du formulaires pour finir l'inscription.", Toast.LENGTH_LONG).show();
            return false;
        }

        //Check if mail is valid
        if(!isEmailValid(email)) {
            Toast.makeText(getApplicationContext(), "Veuillez entrer une adresse électronique correcte.", Toast.LENGTH_LONG).show();
            return false;
        }

        //Check if password is valid
        if(!password.equals(password_confirm)) {
            Toast.makeText(getApplicationContext(), "Les mots de passes doivent être identiques.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
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
     * Switch to the next fragment
     * @param v - the button to switch to the next view
     */
    public void nextView(View v){
        if(pager.getCurrentItem() == NUM_PAGES - 1) {
            finish();
        } else {
            pager.setCurrentItem(pager.getCurrentItem()+1);
        }
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

