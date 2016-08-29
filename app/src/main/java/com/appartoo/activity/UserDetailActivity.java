package com.appartoo.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.model.UserModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.ConversationIdReceiver;
import com.appartoo.utils.ImageManager;
import com.appartoo.utils.RestService;

import java.io.IOException;
import java.net.SocketTimeoutException;

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
    private UserModel userModel;
    private Button sendMessageButton;
    private ImageView userProfilePic;
    private ProgressDialog progressDialog;

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

        userModel = getIntent().getParcelableExtra("user");
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onStart(){
        super.onStart();

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


        if(Appartoo.LOGGED_USER_PROFILE != null && Appartoo.LOGGED_USER_PROFILE.getId().equals(userModel.getId())) {
            sendMessageButton.setVisibility(View.GONE);
        } else if(Appartoo.TOKEN == null || Appartoo.TOKEN.equals("")) {
            new AlertDialog.Builder(UserDetailActivity.this)
                    .setMessage("Vous devez être inscrit pour pouvoir envoyer un message.")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        } else {
            sendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendMessageButton.setEnabled(false);
                    sendMessageToUser();
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

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
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
                sendMessageButton.setEnabled(false);
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    Intent intent = new Intent(UserDetailActivity.this, MessageActivity.class);
                    intent.putExtra("conversationId", response.body().getIdConversation());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Erreur lors de la création de la conversation. Veuillez réessayer plus tard.", Toast.LENGTH_SHORT).show();
                    System.out.println(response.code());
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ConversationIdReceiver> call, Throwable t) {
                sendMessageButton.setEnabled(false);
                progressDialog.dismiss();
                if(t instanceof SocketTimeoutException) {
                    Toast.makeText(getApplicationContext(), "La conversation a bien été créée", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Erreur lors de la création de la conversation. Veuillez réessayer plus tard.", Toast.LENGTH_SHORT).show();
                }
                t.printStackTrace();
            }
        });
    }
}