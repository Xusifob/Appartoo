package com.appartoo.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.appartoo.R;
import com.appartoo.activity.LoginActivity;
import com.appartoo.model.CompleteUserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexandre on 16-07-06.
 */
@ReportsCrashes(
        reportSenderFactoryClasses = GoogleFormSenderFactory.class,
        mode = ReportingInteractionMode.DIALOG,
        reportDialogClass = com.appartoo.utils.CrashReportDialog.class,
        resDialogIcon = R.drawable.error_medium,
        resDialogTheme = R.style.ReportDialog,
        resDialogTitle = R.string.crash_dialog_title,
        resDialogText = R.string.crash_dialog_text,
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt,
        resDialogOkToast = R.string.crash_dialog_ok_toast
)
public class Appartoo extends Application{

    public static final String SERVER_URL = "http://626ff880.ngrok.io";
    public static final String APP_NAME = "Appartoo";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_AGE = "age";
    public static final String KEY_GIVEN_NAME = "givenName";
    public static final String KEY_FAMILY_NAME = "familyName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PROFILE_PICTURE = "profilePicUrl";


    private static final String KEY_APP_CRASHED = "KEY_APP_CRASHED";

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
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
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
        if(databaseReference != null && conversationsIds != null && Appartoo.LOGGED_USER_PROFILE != null) {
            Map<String, Object> updates = new HashMap<>();

            for (String conversationId : conversationsIds) {
                updates.put("conversations/" + conversationId + "/isOnline/" + Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString(), userIsOnline);
            }

            databaseReference.updateChildren(updates);
        }
    }
}