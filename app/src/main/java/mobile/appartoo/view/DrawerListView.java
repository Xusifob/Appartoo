package mobile.appartoo.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import mobile.appartoo.R;
import mobile.appartoo.activity.LoginActivity;
import mobile.appartoo.adapter.NavigationDrawerAdapter;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.FeedReaderCredentials;

/**
 * Created by alexandre on 16-07-13.
 */
public class DrawerListView extends NonScrollableListView {

    private String[] drawerTitles;
    private TypedArray drawerImages;
    private Context context;
    private FeedReaderCredentials databaseHelper;
    private DrawerLayout drawerLayout;

    public DrawerListView(Context context) {
        super(context);
        defineDrawerMenu(context);
    }

    public DrawerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        defineDrawerMenu(context);
    }
    public DrawerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        defineDrawerMenu(context);
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    public void defineDrawerMenu(Context context) {

        this.databaseHelper = new FeedReaderCredentials(context);
        this.context = context;
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
        switch (position) {
            case 7:
                databaseHelper.updateUserLoggedState(Appartoo.LOGGED_USER_MAIL, false);
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
