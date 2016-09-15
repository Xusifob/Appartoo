package com.appartoo.misc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.message.MessageActivity;
import com.appartoo.utils.model.UserModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.ConversationIdReceiver;
import com.appartoo.utils.ImageManager;
import com.appartoo.utils.RestService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexandre on 16-07-06.
 */
public class UserDetailActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private UserDetailFragment userDetailFragment;
    private UserModel userModel;
    private Button sendMessageButton;
    private ImageView userProfilePic;
    private ProgressDialog progressDialog;
    private String userId;
    private RestService restService;
    private ProgressBar progressBar;
    private View userDetailContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);


        //Retrieve the drawer element
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.userDetailAppBar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        userProfilePic = (ImageView) findViewById(R.id.userDetailProfilePic);
        sendMessageButton = (Button) findViewById(R.id.userDetailSendMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        userDetailContainer = findViewById(R.id.userDetailContainer);
        userDetailFragment = (UserDetailFragment) getSupportFragmentManager().findFragmentById(R.id.userDetailFragment);

        userModel = getIntent().getParcelableExtra("user");
        userId = getIntent().getStringExtra("profileId");

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onStart(){
        super.onStart();

        progressBar.setVisibility(View.VISIBLE);
        userDetailContainer.setVisibility(View.GONE);
        sendMessageButton.setVisibility(View.GONE);

        if(userModel != null) {
            bindData(userModel);
            userDetailFragment.bindData(userModel);
            progressBar.setVisibility(View.GONE);
            userDetailContainer.setVisibility(View.VISIBLE);
        } else if (userId != null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Appartoo.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            restService = retrofit.create(RestService.class);

            Call<UserModel> callback = restService.getUserInformationsById(RestService.REST_URL + "/profiles/" + userId);

            callback.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if(response.isSuccessful()) {

                        userModel = response.body();
                        bindData(userModel);
                        userDetailFragment.bindData(userModel);
                        userDetailContainer.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(UserDetailActivity.this, R.string.error_retrieve_user_informations, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Toast.makeText(UserDetailActivity.this, R.string.error_retrieve_user_informations, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    t.printStackTrace();
                }
            });
        }

        //Define the drawer
        toolbar.setNavigationIcon(R.drawable.left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void bindData(final UserModel userModel) {
        ImageManager.downloadPictureIntoView(getApplicationContext(), userProfilePic, userModel.getImage().getContentUrl(), ImageManager.TRANFORM_SQUARE);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Détail de l'utilisateur");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });


        if(userModel.getId() == null || (Appartoo.LOGGED_USER_PROFILE != null && Appartoo.LOGGED_USER_PROFILE.getId().equals(userModel.getId()))) {
            sendMessageButton.setVisibility(View.GONE);
        } else if(Appartoo.TOKEN == null || Appartoo.TOKEN.equals("")) {
            sendMessageButton.setVisibility(View.VISIBLE);
            sendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent = new Intent(UserDetailActivity.this, MessageActivity.class);
                        intent.putExtra("userId", userModel.getIdNumber().toString());
                        intent.putExtra("conversationName", userModel.getGivenName());
                        startActivity(intent);
                }
            });
        } else {
            sendMessageButton.setVisibility(View.VISIBLE);
            sendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendMessageButton.setEnabled(false);
                    sendMessageToUser();
                }
            });
        }
    }

    private void sendMessageToUser(){
        progressDialog = ProgressDialog.show(this, "Création de la conversation", "Veuillez patienter...", true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestService restService = retrofit.create(RestService.class);

        Call<ConversationIdReceiver> callback = restService.sendMessageToUser("Bearer " + Appartoo.TOKEN, userModel.getIdNumber().toString());

        callback.enqueue(new Callback<ConversationIdReceiver>() {
            @Override
            public void onResponse(Call<ConversationIdReceiver> call, Response<ConversationIdReceiver> response) {
                sendMessageButton.setEnabled(true);
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    Intent intent = new Intent(UserDetailActivity.this, MessageActivity.class);
                    intent.putExtra("conversationId", response.body().getIdConversation());
                    intent.putExtra("conversationName", userModel.getGivenName());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Erreur lors de la création de la conversation. Veuillez réessayer plus tard.", Toast.LENGTH_SHORT).show();
                    try {
                        Log.v("UserDetailActivity", "sendMessageToUser: " + String.valueOf(response.code()));
                        Log.v("UserDetailActivity", "sendMessageToUser: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ConversationIdReceiver> call, Throwable t) {
                sendMessageButton.setEnabled(true);
                progressDialog.dismiss();
                Log.v("UserDetailActivity", "sendMessageToUser: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}