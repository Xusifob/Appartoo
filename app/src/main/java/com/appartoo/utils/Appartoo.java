package com.appartoo.utils;

import android.app.Application;
import com.appartoo.model.CompleteUserModel;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by alexandre on 16-07-06.
 */
public class Appartoo extends Application{

    public static final String SERVER_URL = "http://7a6d30af.ngrok.io";
    public static String TOKEN = "";
    public static CompleteUserModel LOGGED_USER_PROFILE;

    @Override
    public void onCreate() {
        super.onCreate();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        Picasso.setSingletonInstance(built);

    }
}