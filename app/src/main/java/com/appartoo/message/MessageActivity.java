package com.appartoo.message;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.appartoo.R;
import com.appartoo.utils.model.ConversationModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexandre on 16-08-17.
 */
public class MessageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String conversationOwner;
    private Integer conversationType;
    private Integer conversationStatus;
    private HashMap<String, String> conversationOffer;
    private ConversationModel conversationModel;
    private RestService restService;
    private ProgressDialog loading;
    private AlertDialog.Builder preExecutionDialog;
    private AlertDialog.Builder postExecutionDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        conversationOwner = getIntent().getStringExtra("conversationOwner");
        conversationType = getIntent().getIntExtra("conversationType", -1);
        conversationStatus = getIntent().getIntExtra("conversationStatus", -1);
        conversationOffer = (HashMap<String, String>) getIntent().getSerializableExtra("conversationOffer");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restService = retrofit.create(RestService.class);

        setSupportActionBar(toolbar);
    }

    @Override
    public void onStart() {
        super.onStart();

        toolbar.setNavigationIcon(R.drawable.left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        postExecutionDialog = new AlertDialog.Builder(MessageActivity.this)
                .setPositiveButton(R.string.ok, null);

        preExecutionDialog = new AlertDialog.Builder(MessageActivity.this)
                .setNegativeButton(R.string.cancel, null);
    }

    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(toolbar.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(conversationOffer != null) {
            if(conversationOwner != null && !Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString().equals(conversationOwner) && conversationType == 1 && conversationStatus == 0) {
                getMenuInflater().inflate(R.menu.candidate, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void invalidateMenu(){
        this.invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.candidate_accept:
                if(conversationModel != null) {
                    preExecutionDialog
                            .setMessage(R.string.confirm_accept_candidate)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    acceptUser();
                                }
                            }).show();
                }
                return true;
            case R.id.candidate_delete:
                preExecutionDialog
                        .setMessage(R.string.confirm_refuse_candidate)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                refuseUser();
                            }
                        }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setConversationModel(ConversationModel conversationModel) {
        this.conversationModel = conversationModel;
    }

    private void refuseUser(){

        loading = ProgressDialog.show(this, "Refus du participant", "Veuillez patienter...", true);
        String url = Appartoo.SERVER_URL + RestService.REST_URL + "/organization/" + conversationModel.getOffer().get("id") + "/candidate";
        String candidateId = getCandidateId();

        Call<ResponseBody> callback = restService.refuseCandidateToOffer(url, "Bearer " + Appartoo.TOKEN, candidateId, conversationModel.getId());

        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                if(response.isSuccessful()) {
                    invalidateMenu();
                    postExecutionDialog.setMessage(R.string.candidate_refused).show();
                } else {
                    postExecutionDialog.setMessage(R.string.connection_error).show();

                    try {
                        Log.v("MessageActivity", "refuseUser: " + String.valueOf(response.code()));
                        Log.v("MessageActivity", "refuseUser: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Log.v("MessageActivity", "refuseUser: " + t.getMessage());
                postExecutionDialog.setMessage(R.string.connection_error).show();
            }
        });
    }

    private void acceptUser(){

        loading = ProgressDialog.show(this, "Ajout du participant", "Veuillez patienter...", true);
        String url = Appartoo.SERVER_URL + RestService.REST_URL + "/organization/" + conversationModel.getOffer().get("id") + "/accept";
        String candidateId = getCandidateId();

        Call<ResponseBody> callback = restService.acceptCandidateToOffer(url, "Bearer " + Appartoo.TOKEN, candidateId, conversationModel.getId());

        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                if(response.isSuccessful()) {
                    postExecutionDialog.setMessage(R.string.candidate_accepted).show();
                    invalidateMenu();
                } else {
                    postExecutionDialog.setMessage(R.string.connection_error).show();

                    try {
                        Log.v("MessageActivity", "acceptUser: " + String.valueOf(response.code()));
                        Log.v("MessageActivity", "acceptUser: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                postExecutionDialog.setMessage(R.string.connection_error).show();
                Log.v("MessageActivity", "acceptUser: " + String.valueOf(t.getMessage()));
            }
        });
    }

    public String getCandidateId(){
        String candidateId = "";
        if(conversationModel.getParticipants().size() == 2) {
            for(String key : conversationModel.getParticipants().keySet()) {
                if (key.equals(conversationOwner)) {
                    candidateId = key;
                    break;
                }
            }
        }

        return candidateId;
    }
}
