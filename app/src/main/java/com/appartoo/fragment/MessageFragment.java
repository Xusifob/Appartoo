package com.appartoo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.appartoo.R;
import com.appartoo.activity.MessageActivity;
import com.appartoo.adapter.ConversationsAdapter;
import com.appartoo.adapter.MessageAdapter;
import com.appartoo.model.ConversationModel;
import com.appartoo.model.MessageModel;
import com.appartoo.utils.Appartoo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by alexandre on 16-07-28.
 */
public class MessageFragment extends Fragment{

    private DatabaseReference databaseReference;
    private ArrayList<MessageModel> messages;
    private ListView messageList;
    private MessageAdapter messageAdapter;
    private String conversationId;
    private ImageView sendMessage;
    private ConversationModel conversationModel;
    private EditText messageEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        messageList = (ListView) view.findViewById(R.id.messageList);
        messageEdit = (EditText) view.findViewById(R.id.conversationNewMessage);
        sendMessage = (ImageView) view.findViewById(R.id.conversationSendMessage);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        conversationId = getActivity().getIntent().getStringExtra("conversationId");

        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(getActivity().getApplicationContext(), messages, Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString());

        if (container != null) {
            container.removeAllViews();
        }

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        messageList.setAdapter(messageAdapter);

        databaseReference.getRoot()
                .child("conversations/" + conversationId + "/messages")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String json = dataSnapshot
                                .getValue(true)
                                .toString()
                                .replace("={", ":{")
                                .replace("=", ":\"")
                                .replaceAll("([^}]), ", "$1\", ")
                                .replaceAll("([^}])\\}", "$1\"}");

                        messages.add(new Gson().fromJson(json, MessageModel.class));
                        messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        databaseReference.getRoot()
                .child("conversations/" + conversationId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue(true) != null) {
                            String json = dataSnapshot
                                    .getValue(true)
                                    .toString()
                                    .replace("={", ":{")
                                    .replace("=", ":\"")
                                    .replaceAll("([^}]), ", "$1\", ")
                                    .replaceAll("([^}])\\}", "$1\"}");

                            conversationModel = new Gson().fromJson(json, ConversationModel.class);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Long time = Calendar.getInstance().getTimeInMillis();
                ConversationModel conversation = getConversationModel(time);
                MessageModel messageModel = getMessageModel(time);

                if (conversation != null && messageModel != null) {
                    Map<String, Object> updates = new HashMap<>();

                    updates.put("/isTyping", conversation.getIsTyping());
                    updates.put("/hasAnswered", conversation.getHasAnswered());
                    updates.put("/isRead", conversation.getIsRead());
                    updates.put("/isOnline", conversation.getIsOnline());
                    updates.put("/lastMessageTime", conversation.getLastMessageTime());

                    databaseReference.getRoot().child("conversations/" + conversationId).updateChildren(updates);
                    databaseReference.getRoot().child("conversations/" + conversationId + "/messages").push().setValue(messageModel);

                    messageEdit.setText("");
                }
            }
        });
    }

    public ConversationModel getConversationModel(Long time){

        if(conversationModel == null) {
            return null;
        }

        ConversationModel conversation = new ConversationModel();

        HashMap<String, Boolean> participantsMapFalse = new HashMap<String, Boolean>();
        for(String participant : conversationModel.getParticipants().keySet()) {
            participantsMapFalse.put(participant, false);
            if(participant.equals(Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString())) participantsMapFalse.put(participant, true);
        }

        conversation.setIsOnline(conversationModel.getIsOnline());
        conversation.setIsTyping(conversationModel.getIsTyping());
        conversation.getIsTyping().put(Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString(), false);

        conversation.setHasAnswered(participantsMapFalse);
        conversation.setIsRead(participantsMapFalse);
        conversation.setLastMessageTime(time);

        return conversation;
    }

    public MessageModel getMessageModel(Long time) {

        if(messageEdit.getText().toString().equals("")) {
            return null;
        }

        MessageModel messageModel = new MessageModel();
        messageModel.setCreatedTime(time);
        messageModel.setMessage(messageEdit.getText().toString());

        messageModel.setSenderId(Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString());
        messageModel.setSenderName(Appartoo.LOGGED_USER_PROFILE.getGivenName());

        return messageModel;
    }
}
