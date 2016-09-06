package com.appartoo.utils.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.login.LoginActivity;
import com.appartoo.general.MainActivity;
import com.appartoo.message.MessageActivity;
import com.appartoo.message.MessagesListActivity;
import com.appartoo.search.SearchActivity;
import com.appartoo.profile.UserProfileActivity;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.ImageManager;

/**
 * Created by alexandre on 16-07-13.
 */
public class NavigationDrawerView extends NavigationView {

    private DrawerLayout drawerLayout;
    private Context context;
    private static ImageView profilePicture;
    private static String userName;
    private static String userMail;
    private SharedPreferences sharedPreferences;

    public NavigationDrawerView(Context context) {
        super(context);
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(Appartoo.APP_NAME, Context.MODE_PRIVATE);
        setMenuActions();
        updateHeader();
    }

    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(Appartoo.APP_NAME, Context.MODE_PRIVATE);
        setMenuActions();
        updateHeader();
    }

    public NavigationDrawerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(Appartoo.APP_NAME, Context.MODE_PRIVATE);
        setMenuActions();
        updateHeader();
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) { this.drawerLayout = drawerLayout; }

    public static void setHeaderInformations(String fullName, String email){
        userName = fullName;
        userMail = email;
    }

    public void updateHeader() {
        View header = getHeaderView(0);
        if(userMail != null && userName != null) {
            ((TextView) header.findViewById(R.id.drawerUserName)).setText(userName);
            ((TextView) header.findViewById(R.id.drawerUserEmail)).setText(userMail);
        }

        profilePicture = (ImageView) header.findViewById(R.id.drawerUserProfilePic);

        if (Appartoo.LOGGED_USER_PROFILE != null && Appartoo.LOGGED_USER_PROFILE.getImage() != null) {
            ImageManager.downloadPictureIntoView(context, profilePicture, Appartoo.LOGGED_USER_PROFILE.getImage().getContentUrl(), ImageManager.TRANFORM_SQUARE);
        } else {
            String profilePicUrlThumb = sharedPreferences.getString("profilePicUrlThumbnail", null);
            String profilePicUrl = sharedPreferences.getString(Appartoo.KEY_PROFILE_PICTURE, null);

            if(profilePicUrlThumb != null) {
                ImageManager.downloadPictureIntoView(context, profilePicture, profilePicUrlThumb, ImageManager.TRANFORM_SQUARE);
            } else if(profilePicUrl != null) {
                ImageManager.downloadPictureIntoView(context, profilePicture, profilePicUrl, ImageManager.TRANFORM_SQUARE);
            } else {
                profilePicture.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_profile_picture));
            }
        }
    }

    private void setMenuActions() {

        if(context instanceof MainActivity) {
            setCheckableItem(getMenu().findItem(R.id.drawer_offers));
        } else if(context instanceof UserProfileActivity) {
            setCheckableItem(getMenu().findItem(R.id.drawer_profile));
        } else if(context instanceof SearchActivity) {
            setCheckableItem(getMenu().findItem(R.id.drawer_search));
        } else if(context instanceof MessagesListActivity) {
            setCheckableItem(getMenu().findItem(R.id.drawer_message));
        }

        setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                item.setCheckable(false);

                if (drawerLayout != null) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }

                switch (item.getItemId()){
                    case R.id.drawer_profile:
                        if(!(context instanceof UserProfileActivity)) {
                            Intent intent = new Intent(context, UserProfileActivity.class);
                            context.startActivity(intent);
                        }
                        return true;
                    case R.id.drawer_offers:
                        if(!(context instanceof MainActivity)){
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        }
                        return true;
                    case R.id.drawer_search:
                        if(!(context instanceof SearchActivity)) {
                            Intent intent = new Intent(context, SearchActivity.class);
                            context.startActivity(intent);
                        }
                        return true;
                    case R.id.drawer_message:
                        if(!(context instanceof MessageActivity)) {
                            Intent intent = new Intent(context, MessagesListActivity.class);
                            context.startActivity(intent);
                        }
                        return true;
                    case R.id.drawer_logout:

                        Appartoo.setUserIsOnline(false);

                        context.getSharedPreferences(Appartoo.APP_NAME, Context.MODE_PRIVATE).edit()
                                .remove("token")
                                .remove(Appartoo.KEY_FAMILY_NAME)
                                .remove(Appartoo.KEY_EMAIL)
                                .remove(Appartoo.KEY_AGE)
                                .remove(Appartoo.KEY_PROFILE_PICTURE).apply();

                        Appartoo.LOGGED_USER_PROFILE = null;
                        Appartoo.TOKEN = null;

                        ((Activity) context).finish();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    default:
                        return false;
                }

            }
        });
    }

    public void setCheckableItem(MenuItem item) {
        item.setChecked(true);
        item.setCheckable(false);
    }
}
