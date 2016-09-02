package com.appartoo.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appartoo.R;
import com.appartoo.activity.MessageActivity;
import com.appartoo.adapter.ConversationsAdapter;
import com.appartoo.model.CompleteUserModel;
import com.appartoo.model.ConversationModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexandre on 16-07-28.
 */
public class MessagesListFragment extends Fragment{
    
    private ArrayList<ConversationModel> userConversations;
    private RecyclerView conversationsList;
    private ConversationsAdapter conversationsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages_list, container, false);

        conversationsList = (RecyclerView) view.findViewById(R.id.messagesConversationList);
        Appartoo.databaseReference = FirebaseDatabase.getInstance().getReference();
        userConversations = new ArrayList<>();
        conversationsAdapter = new ConversationsAdapter(getActivity(), userConversations);

        conversationsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        conversationsList.setLayoutManager(linearLayoutManager);

        if (container != null) {
            container.removeAllViews();
        }

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        conversationsList.setAdapter(conversationsAdapter);

        Appartoo.databaseReference.getRoot().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("onDataChange");

                new DataChangedTask().execute(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        conversationsAdapter.notifyDataSetChanged();
    }

    private class DataChangedTask extends AsyncTask<DataSnapshot, Void, ArrayList<ConversationModel>> {

        @Override
        protected ArrayList<ConversationModel> doInBackground(DataSnapshot... dataSnapshots) {
            if(Appartoo.LOGGED_USER_PROFILE != null) {
                GenericTypeIndicator<HashMap<String, String>> hashMapType = new GenericTypeIndicator<HashMap<String, String>>() {
                };

                HashMap<String, String> conversations = dataSnapshots[0]
                        .child("profiles/" + Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString() + "/conversations")
                        .getValue(hashMapType);

                ArrayList<ConversationModel> conversationModels = new ArrayList<>();

                if (conversations != null) {
                    for (String conversationId : conversations.values()) {
                        if (dataSnapshots[0].child("conversations/").child(conversationId).getValue(true) != null) {

                            HashMap<String, ?> json = (HashMap<String, ?>) dataSnapshots[0].child("conversations/")
                                    .child(conversationId)
                                    .getValue(true);

                            Gson gson = new Gson();
                            ConversationModel conversationModel = gson.fromJson(gson.toJson(gson.toJsonTree(json)), ConversationModel.class);
                            conversationModel.setId(conversationId);

                            conversationModels.add(conversationModel);
                        }
                    }
                }

                return conversationModels;
            }

            return null;
        }

        protected void onPostExecute(ArrayList<ConversationModel> conversationModels) {
            if(conversationModels != null) {
                userConversations.clear();
                userConversations.addAll(conversationModels);
                conversationsAdapter.notifyDataSetChanged();
            }
        }
    }
}
