package mobile.appartoo.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;

import mobile.appartoo.R;
import mobile.appartoo.activity.LoginActivity;
import mobile.appartoo.activity.MainActivity;
import mobile.appartoo.activity.UserProfileActivity;

/**
 * Created by alexandre on 16-07-13.
 */
public class NavigationDrawerView extends NavigationView {

    private Context context;
    private DrawerLayout drawerLayout;

    public NavigationDrawerView(Context context) {
        super(context);
        this.context = context;
        setMenuActions();
    }

    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setMenuActions();
    }

    public NavigationDrawerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        setMenuActions();
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) { this.drawerLayout = drawerLayout; }

    private void setMenuActions() {

        setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                item.setChecked(false);

                if (drawerLayout != null) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }

                switch (item.getItemId()){
                    case R.id.drawer_profile:
                        if(!(context instanceof UserProfileActivity)) {
                            context.startActivity(new Intent(context, UserProfileActivity.class));
                        }
                        return true;
                    case R.id.drawer_offers:
                        if(!(context instanceof MainActivity)){
                            context.startActivity(new Intent(context, MainActivity.class));
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
}
