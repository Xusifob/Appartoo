package mobile.appartoo.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mobile.appartoo.R;
import mobile.appartoo.activity.LoginActivity;
import mobile.appartoo.activity.MainActivity;
import mobile.appartoo.activity.SearchActivity;
import mobile.appartoo.activity.UserProfileActivity;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.ImageManager;

/**
 * Created by alexandre on 16-07-13.
 */
public class NavigationDrawerView extends NavigationView {

    private DrawerLayout drawerLayout;
    private Context context;
    private static ImageView profilePicture;
    private static String userName;
    private static String userMail;

    public NavigationDrawerView(Context context) {
        super(context);
        this.context = context;
        setMenuActions();
        updateHeader();
    }

    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setMenuActions();
        updateHeader();
    }

    public NavigationDrawerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
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

        if(Appartoo.LOGGED_USER_PROFILE != null && Appartoo.LOGGED_USER_PROFILE.getImage().getThumbnail() != null) {
            ImageManager.downloadPictureIntoView(context, profilePicture, Appartoo.LOGGED_USER_PROFILE.getImage().getThumbnail().getContentUrl(), true);
        } else if (Appartoo.LOGGED_USER_PROFILE != null && !Appartoo.LOGGED_USER_PROFILE.getImage().getContentUrl().equals("images/profile.png")) {
            ImageManager.downloadPictureIntoView(context, profilePicture, Appartoo.LOGGED_USER_PROFILE.getImage().getContentUrl(), true);
        } else {
            profilePicture.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.default_profile_picture, null));
        }
    }

    private void setMenuActions() {

        if(context instanceof MainActivity) {
            setCheckableItem(getMenu().findItem(R.id.drawer_offers));
        } else if(context instanceof UserProfileActivity) {
            setCheckableItem(getMenu().findItem(R.id.drawer_profile));
        } else if(context instanceof SearchActivity) {
            setCheckableItem(getMenu().findItem(R.id.drawer_search));
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
                    case R.id.drawer_logout:
                        context.getSharedPreferences("Appartoo", Context.MODE_PRIVATE).edit().remove("token").apply();

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
