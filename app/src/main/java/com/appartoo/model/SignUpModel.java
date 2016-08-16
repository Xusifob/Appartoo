package com.appartoo.model;

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
}
