package mobile.appartoo.utils;

import android.app.Application;
import mobile.appartoo.model.UserWithProfileModel;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by alexandre on 16-07-06.
 */
public class Appartoo extends Application{

    public static final String SERVER_URL = "http://88abd5c6.ngrok.io";
    public static String TOKEN = "";
    public static UserWithProfileModel LOGGED_USER_PROFILE;

    @Override
    public void onCreate() {
        super.onCreate();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }
}