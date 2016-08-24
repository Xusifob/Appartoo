package com.appartoo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.model.ConversationModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;
import com.appartoo.view.NavigationDrawerView;

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
    private HashMap<String, String> conversationOffer;
    private ConversationModel conversationModel;
    private RestService restService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        conversationOwner = getIntent().getStringExtra("conversationOwner");
        conversationType = getIntent().getIntExtra("conversationType", -1);
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
    }

    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void invalidateMenu(){
        this.invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(conversationOffer != null) {
            if(conversationOwner != null && !Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString().equals(conversationOwner) && conversationType == 1) {
                getMenuInflater().inflate(R.menu.candidate, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.candidate_accept:
                if(conversationModel != null) {
                    acceptUser();
                }
            case R.id.candidate_delete:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setConversationModel(ConversationModel conversationModel) {
        this.conversationModel = conversationModel;
    }

    private void acceptUser(){
        String url = Appartoo.SERVER_URL + "/organization/" + conversationModel.getOffer().get("id") + "/accept";

        String candidateId = "";

        if(conversationModel.getParticipants().size() == 2) {
            for(String key : conversationModel.getParticipants().keySet()) {
                if (key.equals(conversationOwner)) {
                    candidateId = key;
                    break;
                }
            }
        }

        System.out.println("url " + url);
        System.out.println("candidate_id " + candidateId);
        System.out.println("conversation_id " + conversationModel.getId());

        Call<ResponseBody> callback = restService.acceptCandidateToOffer(url, "Bearer " + Appartoo.TOKEN, candidateId, conversationModel.getId());

        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Added !", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println(response.code());
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
