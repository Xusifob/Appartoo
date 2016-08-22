package com.appartoo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appartoo.R;
import com.appartoo.activity.MessageActivity;
import com.appartoo.adapter.ConversationsAdapter;
import com.appartoo.model.ConversationModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.ImageManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by alexandre on 16-07-28.
 */
public class MessagesListFragment extends Fragment{

    private DatabaseReference databaseReference;
    private ArrayList<ConversationModel> userConversations;
    private ListView conversationsList;
    private ConversationsAdapter conversationsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages_list, container, false);

        conversationsList = (ListView) view.findViewById(R.id.messagesConversationList);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userConversations = new ArrayList<>();
        conversationsAdapter = new ConversationsAdapter(getActivity(), userConversations);

        if (container != null) {
            container.removeAllViews();
        }

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        conversationsList.setAdapter(conversationsAdapter);

        conversationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ConversationModel conversationModel = userConversations.get(i);

                String convName = "";
                if(conversationModel.getOffer() != null){
                    convName = conversationModel.getOffer().get("name");
                } else {
                    for (String participant : conversationModel.getParticipants().values()){
                        convName += participant + ", ";
                    }
                    convName = convName.substring(0, convName.length() - 2);
                }

                Intent intent = new Intent(getActivity().getApplicationContext(), MessageActivity.class);
                intent.putExtra("conversationName", convName);
                intent.putExtra("conversationId", conversationModel.getId());
                startActivity(intent);
            }
        });

        databaseReference.getRoot().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, String>> hashMapType = new GenericTypeIndicator<HashMap<String, String>>() {};
                GenericTypeIndicator<ConversationModel> convType = new GenericTypeIndicator<ConversationModel>() {};

                HashMap<String, String> conversations = dataSnapshot
                        .child("profiles/" + Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString() + "/conversations")
                        .getValue(hashMapType);

                userConversations.clear();

                if(conversations != null) {
                    for (String conversationId : conversations.values()) {
                        if(dataSnapshot.child("conversations/").child(conversationId).getValue(true) != null) {
                            String json = dataSnapshot.child("conversations/")
                                    .child(conversationId)
                                    .getValue(true)
                                    .toString()
                                    .replace("={", ":{")
                                    .replace("=", ":\"")
                                    .replaceAll("([^}]), ", "$1\", ")
                                    .replaceAll("([^}])\\}", "$1\"}");

                            ConversationModel conversationModel = new Gson().fromJson(json, ConversationModel.class);
                            conversationModel.setId(conversationId);

                            if (conversationModel != null) {
                                userConversations.add(conversationModel);
                            }
                        }
                    }
                }
                conversationsAdapter.notifyDataSetChanged();
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
}
