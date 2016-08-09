package mobile.appartoo.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mobile.appartoo.R;
import mobile.appartoo.activity.MainActivity;
import mobile.appartoo.model.UserWithProfileModel;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.RestService;
import mobile.appartoo.utils.TextValidator;
import mobile.appartoo.utils.TokenReceiver;
import mobile.appartoo.view.NavigationDrawerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexandre on 16-07-28.
 */
public class LogInFragment extends Fragment {

    private Button logInButton;
    private String mail;
    private String password;
    private EditText mailEdit;
    private EditText passwordEdit;
    private RestService restService;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        logInButton = (Button) view.findViewById(R.id.connectButton);
        mailEdit = ((EditText) view.findViewById(R.id.logInMail));
        passwordEdit = ((EditText) view.findViewById(R.id.logInPassword));

        if (container != null) {
            container.removeAllViews();
        }

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        //Build a retrofit request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restService = retrofit.create(RestService.class);

        sharedPreferences = getActivity().getSharedPreferences("Appartoo", Context.MODE_PRIVATE);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = mailEdit.getText().toString();
                password = passwordEdit.getText().toString();

                if (isFormValid()) {
                    logInButton.setEnabled(false);
                    logIn();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Veuillez entrer correctement vos identifiants.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void logIn() {

        //Disable the login button
        logInButton.setEnabled(false);

        Call<TokenReceiver> callback = restService.postLogIn(mail, password);

        //Handle the server response
        callback.enqueue(new Callback<TokenReceiver>() {
            @Override
            public void onResponse(Call<TokenReceiver> call, Response<TokenReceiver> response) {

                //If the login is successful
                if (response.isSuccessful()) {
                    //Retrieve the user token
                    Appartoo.TOKEN = response.body().getToken();

                    //Stock the token in shared preferences
                    sharedPreferences.edit().putString("token", Appartoo.TOKEN).apply();

                    if (Appartoo.TOKEN != null && !Appartoo.TOKEN.equals("")) {
                        retrieveUserProfile();
                    }

                    //If the user didn't send the right credentials
                } else if (response.code() == 401) {
                    Toast.makeText(getActivity().getApplicationContext(), "Mauvais login ou mot de passe.", Toast.LENGTH_SHORT).show();
                    //If the server isn't responding
                } else {
                    System.out.println(response.code());
                    Toast.makeText(getActivity().getApplicationContext(), "Erreur de connection au serveur.", Toast.LENGTH_SHORT).show();
                }

                logInButton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<TokenReceiver> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(), "Erreur de connection avec le serveur.", Toast.LENGTH_SHORT).show();
                logInButton.setEnabled(true);
            }
        });
    }

    /**
     * Check if the login form is valid
     * @return true if the form is correctly filled, false if not
     */
    private boolean isFormValid(){
        //Check that the mail and the password aren't null
        if(TextValidator.haveText(mail) && TextValidator.haveText(password)){
            return TextValidator.isEmail(mail);
        } else {
            return false;
        }
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
                }
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }

            @Override
            public void onFailure(Call<UserWithProfileModel> call, Throwable t) {
                t.printStackTrace();
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
    }
}
