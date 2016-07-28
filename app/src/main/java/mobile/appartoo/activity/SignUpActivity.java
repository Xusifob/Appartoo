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
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.text.ParseException;
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
import mobile.appartoo.utils.TokenReceiver;
import mobile.appartoo.view.NavigationDrawerView;
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

    private static final int NUM_PAGES = 5;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener date;
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
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        jsonFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
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
    private SignUpModel getSignUpModel(){
        //Retrieve the form inputs
        String firstName = ((EditText) findViewById(R.id.signupFirstName)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.signupLastName)).getText().toString();
        String password_confirm = ((EditText) findViewById(R.id.signUpPasswordConfirm)).getText().toString();
        String birthdate = ((EditText) findViewById(R.id.signUpBirthdate)).getText().toString();
        String email = ((EditText) findViewById(R.id.signUpMail)).getText().toString();
        String password = ((EditText) findViewById(R.id.signUpPassword)).getText().toString();

        //Check the edit text input
        if(firstName.replaceAll("\\s+","").equals("") || lastName.replaceAll("\\s+","").equals("") ||
                password.replaceAll("\\s+","").equals("") || password_confirm.replaceAll("\\s+","").equals("") ||
                birthdate.replaceAll("\\s+","").equals("") || email.replaceAll("\\s+","").equals("")) {
            Toast.makeText(getApplicationContext(), "Vous devez entrer toutes les informations du formulaires pour finir l'inscription.", Toast.LENGTH_LONG).show();
            return null;
        }

        //Check if mail is valid
        if(!isEmailValid(email)) {
            Toast.makeText(getApplicationContext(), "Veuillez entrer une adresse électronique correcte.", Toast.LENGTH_LONG).show();
            return null;
        }

        //Check if password is valid
        if(!password.equals(password_confirm)) {
            Toast.makeText(getApplicationContext(), "Les mots de passes doivent être identiques.", Toast.LENGTH_LONG).show();
            return null;
        }

        try {
            birthdate = jsonFormat.format(dateFormat.parse(birthdate));
        } catch (ParseException e) {
            return null;
        }


        return new SignUpModel(email, password, firstName, lastName, birthdate);
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
            Call<ResponseBody> callback = restService.postUser(newUser.getEmail(), newUser.getPassword(), newUser.getGivenName(), newUser.getFamilyName(), newUser.getBirthdate());

            //Handle the server response
            callback.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    signUpButton.setEnabled(true);
                    //If the login is successful
                    if(response.isSuccessful()) {
                        logUser();
                    } else if(response.code() == 402) {
                        Toast.makeText(getApplicationContext(), "L'utilisateur existe déjà.", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println("finishSignUp response code " + response.code());
                        Toast.makeText(getApplicationContext(), "Erreur de connection au serveur. Veuillez réessayer plus tard.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    System.out.println("finishSignUp Failure");
                    Toast.makeText(getApplicationContext(), "Erreur de connection avec le serveur. Veuillez réessayer plus tard.", Toast.LENGTH_SHORT).show();
                    signUpButton.setEnabled(true);
                }
            });
        }
    }

    private void logUser() {
        System.out.println("Logging user...");
        Call<TokenReceiver> newCallback = restService.postLogIn(newUser.getEmail(), newUser.getPassword());

        newCallback.enqueue(new Callback<TokenReceiver>() {
            @Override
            public void onResponse(Call<TokenReceiver> call, Response<TokenReceiver> response) {
                if(response.isSuccessful()) {
                    Appartoo.TOKEN = response.body().getToken();
                    sharedPreferences.edit().putString("token", Appartoo.TOKEN).apply();
                    retrieveUserProfile();
                } else {
                    finish();
                    System.out.println("logUser response code " + response.code());
                    Toast.makeText(getApplicationContext(), "Votre inscription a été finalisée avec succès !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenReceiver> call, Throwable t) {
                finish();
                System.out.println("logUser Failure");
                Toast.makeText(getApplicationContext(), "Votre inscription a été finalisée avec succès !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveUserProfile(){
        Call<UserWithProfileModel> callback = restService.getLoggedUserProfile("Bearer (" + Appartoo.TOKEN + ")");

        //Handle the server response
        callback.enqueue(new Callback<UserWithProfileModel>() {
            @Override
            public void onResponse(Call<UserWithProfileModel> call, Response<UserWithProfileModel> response) {

                //If the login is successful
                if(response.isSuccessful()) {
                    Appartoo.LOGGED_USER_PROFILE = response.body();

                    sharedPreferences.edit().putString("givenName", Appartoo.LOGGED_USER_PROFILE.getGivenName())
                            .putString("familyName", Appartoo.LOGGED_USER_PROFILE.getFamilyName())
                            .putString("email", Appartoo.LOGGED_USER_PROFILE.getUser().getEmail())
                            .putString("age", Integer.toString(Appartoo.LOGGED_USER_PROFILE.getAge())).apply();

                    NavigationDrawerView.setHeaderInformations(Appartoo.LOGGED_USER_PROFILE.getGivenName() + " " + Appartoo.LOGGED_USER_PROFILE.getFamilyName(),Appartoo.LOGGED_USER_PROFILE.getUser().getEmail());

                    launchActivityWithoutHistory(SignUpProfileActivity.class);
                } else {
                    System.out.println("retrieveUserProfile response code " + response.code());
                    launchActivityWithoutHistory(MainActivity.class);
                }
            }

            @Override
            public void onFailure(Call<UserWithProfileModel> call, Throwable t) {
                System.out.println("retrieveUserProfile Failure");
                launchActivityWithoutHistory(MainActivity.class);
                t.printStackTrace();
            }
        });
    }


    private void launchActivityWithoutHistory(Class activityClass){
        Intent intent = new Intent(SignUpActivity.this, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

