package mobile.appartoo.model;

/**
 * Created by alexandre on 16-07-20.
 */
public class SignUpModel {
    private String email;
    private String password;
    private String facebookId;

    public SignUpModel(String email, String password, String facebookId) {
        this.email = email;
        this.password = password;
        this.facebookId = facebookId;
    }

    public SignUpModel(String email, String password) {
        this.email = email;
        this.password = password;
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

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
}
