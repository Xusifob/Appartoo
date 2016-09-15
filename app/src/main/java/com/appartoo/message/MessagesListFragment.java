package com.appartoo.message;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appartoo.R;
import com.appartoo.utils.adapter.ConversationsAdapter;
import com.appartoo.utils.model.ConversationModel;
import com.appartoo.utils.Appartoo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

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

                    Collections.sort(conversationModels, new Comparator<ConversationModel>() {
                        @Override
                        public int compare(ConversationModel conversationModel, ConversationModel t1) {
                            if(t1 != null && conversationModel != null && t1.getLastMessageTime() != null && conversationModel.getLastMessageTime() != null)
                                return t1.getLastMessageTime().compareTo(conversationModel.getLastMessageTime());
                            else
                                return 0;
                        }
                    });
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
