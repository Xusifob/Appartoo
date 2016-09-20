package com.appartoo.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.misc.MainActivity;
import com.appartoo.utils.ConversationIdReceiver;
import com.appartoo.utils.model.CompleteUserModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;
import com.appartoo.utils.TextValidator;
import com.appartoo.utils.TokenReceiver;
import com.appartoo.utils.view.NavigationDrawerView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
        sharedPreferences = getActivity().getSharedPreferences(Appartoo.APP_NAME, Context.MODE_PRIVATE);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = mailEdit.getText().toString();
                password = passwordEdit.getText().toString();

                if (isFormValid()) {
                    logInButton.setEnabled(false);
                    logIn();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.error_login_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                    sharedPreferences.edit().putString(Appartoo.KEY_TOKEN, Appartoo.TOKEN).apply();
                    if (Appartoo.TOKEN != null && !Appartoo.TOKEN.equals("")) {
                        retrieveUserProfile();
                    }

                    //If the user didn't send the right credentials
                } else if (response.code() == 401) {
                    logInButton.setEnabled(true);
                    Toast.makeText(getActivity().getApplicationContext(), R.string.error_login_password, Toast.LENGTH_SHORT).show();
                    //If the server isn't responding
                } else {
                    logInButton.setEnabled(true);
                    try {
                        Log.v("LoginFragment", "logIn: " + String.valueOf(response.code()));
                        Log.v("LoginFragment", "logIn: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity().getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenReceiver> call, Throwable t) {
                Log.v("LoginFragment", "logIn: " + t.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                logInButton.setEnabled(true);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(passwordEdit.getWindowToken(), 0);
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
        Call<CompleteUserModel> callback = restService.getLoggedUserProfile("Bearer " + Appartoo.TOKEN);

        //Handle the server response
        callback.enqueue(new Callback<CompleteUserModel>() {
            @Override
            public void onResponse(Call<CompleteUserModel> call, Response<CompleteUserModel> response) {

                //If the login is successful
                if(response.isSuccessful()) {
                    Appartoo.LOGGED_USER_PROFILE = response.body();

                    sharedPreferences.edit().putString(Appartoo.KEY_GIVEN_NAME, Appartoo.LOGGED_USER_PROFILE.getGivenName())
                            .putString(Appartoo.KEY_FAMILY_NAME, Appartoo.LOGGED_USER_PROFILE.getFamilyName())
                            .putString(Appartoo.KEY_EMAIL, Appartoo.LOGGED_USER_PROFILE.getUser().getEmail())
                            .apply();
                    
                    NavigationDrawerView.setHeaderInformations(Appartoo.LOGGED_USER_PROFILE.getGivenName() + " " + Appartoo.LOGGED_USER_PROFILE.getFamilyName(),Appartoo.LOGGED_USER_PROFILE.getUser().getEmail());
                    Appartoo.initiateFirebase();

                    if(getActivity().getIntent().getStringExtra("userId") != null) {
                        applyToOffer();
                    } else {
                        if(getActivity().getIntent().getBooleanExtra("connection", false)) {
                            getActivity().setResult(Appartoo.IS_LOGGED);
                        } else {
                            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                        }
                        getActivity().finish();
                    }
                } else {
                    if(getActivity().getIntent().getBooleanExtra("connection", false)) {
                        getActivity().setResult(Appartoo.IS_LOGGED);
                    } else {
                        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<CompleteUserModel> call, Throwable t) {
                t.printStackTrace();
                if(getActivity().getIntent().getBooleanExtra("connection", false)) {
                    getActivity().setResult(Appartoo.IS_LOGGED);
                } else {
                    getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                }
                getActivity().finish();
            }

        });
    }

    private void applyToOffer(){
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Création de la conversation", "Veuillez patienter...", true);

        Call<ConversationIdReceiver> callback = restService.sendMessageToUser("Bearer " + Appartoo.TOKEN, getActivity().getIntent().getStringExtra("userId"));
        callback.enqueue(new Callback<ConversationIdReceiver>() {
            @Override
            public void onResponse(Call<ConversationIdReceiver> call, Response<ConversationIdReceiver> response) {
                logInButton.setEnabled(true);
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    Intent intent = new Intent();
                    intent.putExtra("conversationId", response.body().getIdConversation());
                    getActivity().setResult(Appartoo.IS_LOGGED_FOR_CONVERSATION, intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.error_conversation_creation, Toast.LENGTH_SHORT).show();
                    try {
                        Log.v("LoginActivity", "applyToOffer: " + String.valueOf(response.code()));
                        Log.v("LoginActivity", "applyToOffer: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ConversationIdReceiver> call, Throwable t) {
                logInButton.setEnabled(true);
                progressDialog.dismiss();
                Log.v("LoginActivity", "applyToOffer: " + t.getMessage());
            }
        });
    }
}
