package mobile.appartoo.model;

/**
 * Created by alexandre on 16-07-08.
 */
public class LogInModel {
    String mail;
    String password;

    public LogInModel(String mail, String password){
        this.mail = mail;
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValid(){
        if(mail != null && password != null){
            return true;
        } else {
            return false;
        }
    }
}
