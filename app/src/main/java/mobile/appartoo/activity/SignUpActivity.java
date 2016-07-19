package mobile.appartoo.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Retreive the resources
        pager = (ViewPager) findViewById(R.id.signup_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }

    @Override
    public void onStart(){

        //Define the number of pages to keep in memory
        pager.setOffscreenPageLimit(NUM_PAGES - 1);

        //Define today's date
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBirthDate();
            }

        };

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

    public void finishSignup(View v){
        //When form is valid,
        if(isFormValid()){
            // TODO request API to add user
            startActivity(new Intent(SignUpActivity.this, ConfigureProfileActivity.class));
            finish();
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
        String password = ((EditText) findViewById(R.id.signupFirstName)).getText().toString();
        String password_confirm = ((EditText) findViewById(R.id.signupFirstName)).getText().toString();
        String birthdate = ((EditText) findViewById(R.id.signUpBirthdate)).getText().toString();
        String email = ((EditText) findViewById(R.id.signupFirstName)).getText().toString();

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

