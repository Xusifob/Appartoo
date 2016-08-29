package com.appartoo.utils;

import android.app.Application;

import com.appartoo.model.CompleteUserModel;
import com.appartoo.model.ConversationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexandre on 16-07-06.
 */
public class Appartoo extends Application{

    public static final String SERVER_URL = "http://7849b389.ngrok.io";
    public static String TOKEN = "";
    public static CompleteUserModel LOGGED_USER_PROFILE;
    public static DatabaseReference databaseReference;
    public static ArrayList<String> conversationsIds;

    @Override
    public void onCreate() {
        super.onCreate();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        Picasso.setSingletonInstance(built);

        if (conversationsIds == null) conversationsIds = new ArrayList<>();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        System.out.println("TERMINATING");
        setUserIsOnline(false);
    }

    public static void initiateFirebase(){

        if(databaseReference == null && Appartoo.LOGGED_USER_PROFILE != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();

            databaseReference.getRoot()
                    .child("profiles/")
                    .child(Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString())
                    .child("/conversations/")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (Appartoo.LOGGED_USER_PROFILE != null) {
                                GenericTypeIndicator<Map<String, String>> mapType = new GenericTypeIndicator<Map<String, String>>() {};
                                Map<String, String> conversations = dataSnapshot.getValue(mapType);

                                if (conversations != null) {
                                    conversationsIds.clear();
                                    conversationsIds.addAll(conversations.values());
                                    setUserIsOnline(true);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    public static void setUserIsOnline(final boolean userIsOnline) {
        System.out.println("READY");
        if(databaseReference != null) {
            System.out.println("STEADY");

            if (conversationsIds != null && Appartoo.LOGGED_USER_PROFILE != null) {
                System.out.println("GO");
                Map<String, Object> updates = new HashMap<>();

                for (String conversationId : conversationsIds) {
                    System.out.println(conversationId);
                    updates.put("conversations/" + conversationId + "/isOnline/" + Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString(), userIsOnline);
                }

                databaseReference.updateChildren(updates);
            }
        }
    }
}