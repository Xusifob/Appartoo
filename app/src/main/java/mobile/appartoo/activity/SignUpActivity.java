package mobile.appartoo.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobile.appartoo.R;
import mobile.appartoo.fragment.ConfigureProfileFirstFragment;
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

        pager = (ViewPager) findViewById(R.id.signup_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }

    @Override
    public void onStart(){
        pager.setOffscreenPageLimit(NUM_PAGES - 1);
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
//        if(isFormValid()){
            startActivity(new Intent(SignUpActivity.this, ConfigureProfileActivity.class));
            finish();
//        }
    }

    private void updateBirthDate(){
        ((EditText) findViewById(R.id.signUpBirthdate)).setText(dateFormat.format(calendar.getTime()));
    }

    public void openDatePicker(View v) {
        new DatePickerDialog(SignUpActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean isFormValid(){
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

        if((!man && !woman) || (!single && !inRelationship) || (!smoker && !nonSmoker)) {
            Toast.makeText(getApplicationContext(), "Vous devez entrer toutes les informations du formulaires pour finir l'inscription.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(firstName.replaceAll("\\s+","").equals("") || lastName.replaceAll("\\s+","").equals("") ||
                password.replaceAll("\\s+","").equals("") || password_confirm.replaceAll("\\s+","").equals("") ||
                birthdate.replaceAll("\\s+","").equals("") || email.replaceAll("\\s+","").equals("")) {
            Toast.makeText(getApplicationContext(), "Vous devez entrer toutes les informations du formulaires pour finir l'inscription.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!isEmailValid(email)) {
            Toast.makeText(getApplicationContext(), "Veuillez entrer une adresse électronique correcte.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!password.equals(password_confirm)) {
            Toast.makeText(getApplicationContext(), "Les mots de passes doivent être identiques.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public void nextView(View v){
        if(pager.getCurrentItem() == NUM_PAGES - 1) {
            finish();
        } else {
            pager.setCurrentItem(pager.getCurrentItem()+1);
        }
    }

    public void toggleView(View v) {
        LinearLayout parent = (LinearLayout) v.getParent();

        for(int i = 0 ; i < parent.getChildCount() ; i++) {
            if(!parent.getChildAt(i).equals(v)) {
                parent.getChildAt(i).setSelected(false);
            }
        }

        v.setSelected(true);
    }

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

