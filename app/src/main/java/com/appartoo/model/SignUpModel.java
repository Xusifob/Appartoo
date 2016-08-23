package com.appartoo.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alexandre on 16-07-20.
 */
public class SignUpModel {
    private String email;
    private String password;
    private String givenName;
    private String familyName;
    private String birthdate;

    public SignUpModel(String email, String password, String givenName, String familyName, String birthdate) {
        this.email = email;
        this.password = password;
        this.givenName = givenName;
        this.familyName = familyName;
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getAge() {
        SimpleDateFormat jsonFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        Calendar now = Calendar.getInstance();
        Calendar birthdate = Calendar.getInstance();
        try {
            birthdate.setTime(jsonFormat.parse(this.birthdate));
        } catch (ParseException e) {
            return -1;
        }

        int age = now.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
        if (birthdate.get(Calendar.DAY_OF_YEAR) > now.get(Calendar.DAY_OF_YEAR)) {
            age -= 1;
        }
        return age;
    }
}
