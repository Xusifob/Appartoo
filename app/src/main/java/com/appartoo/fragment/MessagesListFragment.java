package com.appartoo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.appartoo.R;
import com.appartoo.adapter.ConversationsAdapter;
import com.appartoo.model.ConversationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

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

        databaseReference.getRoot().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, String>> hashMap = new GenericTypeIndicator<HashMap<String, String>>() {};
                GenericTypeIndicator<ConversationModel> conversationType = new GenericTypeIndicator<ConversationModel>() {};

                System.out.println(dataSnapshot.getValue().toString());

                //TODO replace "4" with logged user id
                HashMap<String, String> conversations = dataSnapshot
                        .child("profiles")
                        .child("4")
                        .child("conversations")
                        .getValue(hashMap);

                userConversations.clear();

                for(String conversation : conversations.values()) {
                    ConversationModel conversationModel = dataSnapshot
                            .child("conversations")
                            .child(conversation)
                            .getValue(conversationType);

                    userConversations.add(conversationModel);
                }

                System.out.println(userConversations.toString());
                System.out.println(conversationsAdapter.getItem(0).toString());

                conversationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void iterateDataSnapshot(DataSnapshot dataSnapshot, int level) {
        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
        while(iterator.hasNext()) {
            DataSnapshot next = iterator.next();
            for(int i = 0 ; i <= level ;  i++) {
                System.out.print("\t");
            }
            System.out.println(next.getKey() + ": " + next.getValue().toString());
            iterateDataSnapshot(next, level + 1);
        }
    }


}
