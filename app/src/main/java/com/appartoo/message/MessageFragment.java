package com.appartoo.message;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.utils.adapter.MessageAdapter;
import com.appartoo.utils.model.ConversationModel;
import com.appartoo.utils.model.MessageModel;
import com.appartoo.utils.Appartoo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexandre on 16-07-28.
 */
public class MessageFragment extends Fragment{

    private ArrayList<MessageModel> messages;
    private ListView messageList;
    private MessageAdapter messageAdapter;
    private String conversationId;
    private ImageView sendMessage;
    private ConversationModel conversationModel;
    private EditText messageEdit;
    private String conversationName;
    private View messageFooter;
    private LinearLayout messageLayout;
    private DatabaseReference conversationReference;
    private ValueEventListener valueEventListener;
    private ChildEventListener childEventListener;
    private TextWatcher textWatcher;
    private boolean isTyping;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        messageList = (ListView) rootView.findViewById(R.id.messageList);
        messageEdit = (EditText) rootView.findViewById(R.id.conversationNewMessage);
        sendMessage = (ImageView) rootView.findViewById(R.id.conversationSendMessage);
        messageLayout = (LinearLayout) rootView.findViewById(R.id.messageSendMessage);

        messageFooter = inflater.inflate(R.layout.footer_message, container, false);

        conversationId = getActivity().getIntent().getStringExtra("conversationId");
        conversationName = getActivity().getIntent().getStringExtra("conversationName");
        conversationReference = Appartoo.databaseReference.getRoot().child("conversations/" + conversationId);
        isTyping = false;

        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(getActivity().getApplicationContext(), messages, Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString());

        if (container != null) {
            container.removeAllViews();
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() instanceof MessageActivity){
            ((MessageActivity) getActivity()).setToolbarTitle(conversationName);
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        messageFooter.setEnabled(false);

        messageList.setOnItemClickListener(null);
        messageList.addFooterView(messageFooter);
        messageList.setAdapter(messageAdapter);

        setValueEventListener();
        setChildEventListener();
        setTextWatcher();
        conversationReference.child("/messages").addChildEventListener(childEventListener);
        conversationReference.addValueEventListener(valueEventListener);
        messageEdit.addTextChangedListener(textWatcher);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Long time = Calendar.getInstance().getTimeInMillis();
                ConversationModel conversation = getConversationModel(time);
                MessageModel messageModel = getMessageModel(time);

                if (conversation != null && messageModel != null) {
                    Map<String, Object> updates = new HashMap<>();

                    updates.put("/hasAnswered", conversation.getHasAnswered());
                    updates.put("/isRead", conversation.getIsRead());
                    updates.put("/lastMessageTime", conversation.getLastMessageTime());

                    conversationReference.updateChildren(updates);
                    conversationReference.child("/messages").push().setValue(messageModel);

                    messageEdit.setText("");
                }
            }
        });
    }

    private void setValueEventListener() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                new DataChangedTask().execute(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void setChildEventListener(){
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap<String, ?> json = (HashMap<String, ?>) dataSnapshot.getValue(true);

                Gson gson = new Gson();
                MessageModel message = gson.fromJson(gson.toJson(gson.toJsonTree(json)), MessageModel.class);

                messages.add(message);
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
        };
    }

    private void setTextWatcher(){
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!messageEdit.getText().toString().equals("") && !isTyping) {
                    isTyping = true;
                    setUserIsTyping(true);
                } else if (messageEdit.getText().toString().equals("") && isTyping) {
                    isTyping = false;
                    setUserIsTyping(false);
                }
            }
        };
    }

    private void setUserIsTyping(boolean userIsTyping) {
        HashMap<String, Object> isTyping = new HashMap<>();
        isTyping.put("/isTyping/" + Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString(), userIsTyping);
        conversationReference.updateChildren(isTyping);
    }

    private void setOtherIsTyping() {
        TextView typingTextView = ((TextView) messageFooter.findViewById(R.id.messageTyping));
        String isTyping = "";
        for(String participant : conversationModel.getIsTyping().keySet()) {
            if(!participant.equals(Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString()) && conversationModel.getIsTyping().get(participant)) {
                isTyping += conversationModel.getParticipants().get(participant) + " est en train d'écrire...\n";
            }
        }

        if(isTyping.equals("")) {
            typingTextView.setVisibility(View.GONE);
        } else {
            isTyping = isTyping.substring(0, isTyping.length()-1);
            typingTextView.setText(isTyping);
            typingTextView.setVisibility(View.VISIBLE);
        }
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

    public String getCandidateId(){
        String candidateId = "";
        if(conversationModel.getParticipants().size() == 2) {
            for(String key : conversationModel.getParticipants().keySet()) {
                if (!key.equals(Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString())) {
                    candidateId = key;
                    break;
                }
            }
        }

        return candidateId;
    }

    public void setMessageIsRead(){

        if(getActivity() != null && getActivity() instanceof MessageActivity) {

            if (conversationModel.getParticipants().size() == 2) {
                if (conversationModel.getLastMessage().getSenderId().equals(Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString())) {
                    if (conversationModel.getIsRead().get(getCandidateId())) {
                        ((TextView) messageFooter.findViewById(R.id.messageViewed)).setText(R.string.viewed);
                    } else {
                        ((TextView) messageFooter.findViewById(R.id.messageViewed)).setText("");
                    }
                } else {
                    ((TextView) messageFooter.findViewById(R.id.messageViewed)).setText("");
                }
            } else if(conversationModel.getParticipants().size() > 2){
                if (conversationModel.getParticipants().size() > 2 && conversationModel.getLastMessage().getSenderId().equals(Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString())) {

                    String viewed_by = getString(R.string.viewed_by);

                    for(String participant : conversationModel.getParticipants().keySet()) {
                        if(!participant.equals(Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString()) && conversationModel.getIsRead().get(participant)) {
                            viewed_by += " " + conversationModel.getParticipants().get(participant) + ",";
                        }
                    }

                    if(viewed_by.equals(getString(R.string.viewed_by))) {
                        ((TextView) messageFooter.findViewById(R.id.messageViewed)).setText("");
                    } else {
                        ((TextView) messageFooter.findViewById(R.id.messageViewed)).setText(viewed_by.substring(0, viewed_by.length() - 1));
                    }
                } else {
                    ((TextView) messageFooter.findViewById(R.id.messageViewed)).setText("");
                }
            }

            if(!conversationModel.getIsRead().get(Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString())){
                Map<String, Object> updates = new HashMap<>();
                updates.put("/isRead/" + Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString(), true);
                conversationReference.updateChildren(updates);
            }

            ((MessageActivity) getActivity()).setConversationModel(conversationModel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        conversationReference.removeEventListener(childEventListener);
        conversationReference.removeEventListener(valueEventListener);
        messageEdit.removeTextChangedListener(textWatcher);
    }

    private class DataChangedTask extends AsyncTask<DataSnapshot, Void, ConversationModel> {

        @Override
        protected ConversationModel doInBackground(DataSnapshot... dataSnapshots) {
            System.out.println("onDataChange");

            if(dataSnapshots[0].getValue(true) != null) {
                HashMap<String, ?> json = (HashMap<String, ?>) dataSnapshots[0].getValue(true);
                Gson gson = new Gson();
                conversationModel = gson.fromJson(gson.toJson(gson.toJsonTree(json)), ConversationModel.class);
                conversationModel.setId(conversationId);

                return conversationModel;
            } else {
                return null;
            }
        }

        protected void onPostExecute(ConversationModel conversationModel) {
            if (conversationModel != null) {
                if (conversationModel.getStatus() != null && conversationModel.getStatus() != 0 && conversationModel.getType() == 1) {
                    sendMessage.setEnabled(false);
                    sendMessage.setImageResource(R.drawable.send_message_gray);
                    messageEdit.setText("");
                    messageEdit.setEnabled(false);
                    messageLayout.setBackgroundColor(Color.argb(255, 232, 232, 232));
                    messageEdit.setHint(R.string.conversation_disabled);
                }

                if (Appartoo.LOGGED_USER_PROFILE != null && conversationModel.getIsTyping() != null) {
                    setOtherIsTyping();
                }

                if (conversationModel.getMessages() != null && conversationModel.getMessages().size() != 0) {
                    setMessageIsRead();
                }
            }
        }
    }

}