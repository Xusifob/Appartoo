package com.appartoo.message;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.appartoo.R;
import com.appartoo.utils.view.NavigationDrawerView;

/**
 * Created by alexandre on 16-08-05.
 */
public class MessagesListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationDrawerView navigationDrawerView;
    private DrawerLayout drawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerView = (NavigationDrawerView) findViewById(R.id.navigationDrawer);
    }

    @Override
    public void onStart() {
        super.onStart();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.my_messages);

        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        navigationDrawerView.updateHeader();
    }

}
