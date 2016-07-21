package mobile.appartoo.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import mobile.appartoo.R;
import mobile.appartoo.activity.LoginActivity;
import mobile.appartoo.activity.MainActivity;
import mobile.appartoo.activity.UserProfileActivity;
import mobile.appartoo.adapter.NavigationDrawerAdapter;

/**
 * Created by alexandre on 16-07-13.
 */
public class DrawerListView extends NonScrollableListView {

    private String[] drawerTitles;
    private TypedArray drawerImages;
    private Context context;
    private DrawerLayout drawerLayout;
    private SharedPreferences sharedPreferences;

    public DrawerListView(Context context) {
        super(context);
        if(!isInEditMode()) {
            defineDrawerMenu(context);
        }
    }

    public DrawerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()) {
            defineDrawerMenu(context);
        }
    }

    public DrawerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(!isInEditMode()) {
            defineDrawerMenu(context);
        }
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    public void defineDrawerMenu(Context context) {

        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("Appartoo", Context.MODE_PRIVATE);

        this.drawerTitles = getResources().getStringArray(R.array.navigation_drawer_titles);
        this.drawerImages = getResources().obtainTypedArray(R.array.navigation_drawer_images);

        setAdapter(new NavigationDrawerAdapter(context, drawerTitles, drawerImages));
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectMenuOption(position);
            }
        });
    }

    private void selectMenuOption(int position) {
        Intent intent;

        switch (position) {
            case 0:
                if(!(context instanceof UserProfileActivity)){
                    context.startActivity(new Intent(context, UserProfileActivity.class));
                }
                break;
            case 1:
                if(!(context instanceof MainActivity)){
                    context.startActivity(new Intent(context, MainActivity.class));
                }
                break;
            case 2:
                sharedPreferences.edit().remove("token").commit();
                ((Activity) context).finish();

                intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);

                break;
            default:
                break;
        }

        if(drawerLayout != null) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }
}
