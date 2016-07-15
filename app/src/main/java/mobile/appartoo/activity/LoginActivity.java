package mobile.appartoo.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CharSequenceInputStream;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mobile.appartoo.R;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.FeedReaderCredentials;
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

public class LoginActivity extends Activity {

    private Button logInButton;
    private String mail;
    private String password;
    private FeedReaderCredentials databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInButton = (Button) findViewById(R.id.connectButton);
        databaseHelper = new FeedReaderCredentials(LoginActivity.this);
    }

    @Override
    protected void onStart(){
        super.onStart();

        databaseHelper.setLastUserLoggedCredentials();

        if(Appartoo.TOKEN != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private boolean isFormValid(){
        mail = ((EditText) findViewById(R.id.logInMail)).getText().toString();
        password = ((EditText) findViewById(R.id.logInPassword)).getText().toString();

        if(mail != null && password != null && !mail.equals("") && !password.equals("")){
            if(isEmailValid(mail)){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public void logIn(View v){
        if(isFormValid()){

            logInButton.setEnabled(false);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Appartoo.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RestService restService = retrofit.create(RestService.class);
            Call<ResponseBody> callback = restService.postLogIn(mail, password);

            callback.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        try {
                            String responseBody = IOUtils.toString(response.body().charStream());
                            JsonObject jsonResponse = new Gson().fromJson(responseBody, JsonObject.class);

                            Appartoo.TOKEN = jsonResponse.get("token").getAsString();
                            Appartoo.LOGGED_USER_MAIL = mail;

                            if (!databaseHelper.userIsInDatabase(mail)) {
                                databaseHelper.addUserToDatabase(mail, Appartoo.TOKEN);
                            } else {
                                databaseHelper.updateUserLoggedState(mail, true);
                            }

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                        } catch (IOException e) {
                            logInButton.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "Erreur, identification impossible.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        logInButton.setEnabled(true);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Mauvais login ou mot de passe.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    logInButton.setEnabled(true);
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erreur de connexion avec le serveur.", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            Toast.makeText(getApplicationContext(), "Veuillez entrer correctement vos identifiants.", Toast.LENGTH_SHORT).show();
        }
    }

    public void signUp(View v){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}

