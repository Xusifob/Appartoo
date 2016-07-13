package mobile.appartoo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;

import mobile.appartoo.R;
import mobile.appartoo.model.UserModel;
import mobile.appartoo.view.DrawerListView;

/**
 * Created by alexandre on 16-07-06.
 */
public class UserDetailActivity extends Activity {


    SimpleDateFormat toString = new SimpleDateFormat("dd/MM/yyyy");
    private DrawerLayout drawerLayout;
    private UserModel user;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout facebookButton;
    private RelativeLayout phoneButton;
    private RelativeLayout messengerButton;
    private DrawerListView drawerListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerListView = (DrawerListView) findViewById(R.id.drawerList);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        facebookButton = (RelativeLayout) findViewById(R.id.userFacebookLayout);
        phoneButton = (RelativeLayout) findViewById(R.id.userPhoneLayout);
        messengerButton = (RelativeLayout) findViewById(R.id.userMessageLayout);

        user = (UserModel) getIntent().getSerializableExtra("user");
    }

    @Override
    public void onStart(){
        super.onStart();
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        drawerListView.setDrawerLayout(drawerLayout);
        drawerLayout.addDrawerListener(drawerToggle);

        facebookButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openUserFacebook(v, event);
                return true;
            }
        });

        messengerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openUserMessenger(v, event);
                return true;
            }
        });

        phoneButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openUserPhone(v, event);
                return true;
            }
        });

        populateView();
    }

    private void openUserFacebook(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            ((TransitionDrawable)((RelativeLayout)v).getChildAt(1).getBackground()).startTransition(0);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            ((TransitionDrawable)((RelativeLayout)v).getChildAt(1).getBackground()).reverseTransition(0);
        }
    }

    private void openUserMessenger(View v, MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            ((TransitionDrawable)((RelativeLayout)v).getChildAt(1).getBackground()).startTransition(0);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            ((TransitionDrawable)((RelativeLayout)v).getChildAt(1).getBackground()).reverseTransition(0);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:" + user.getTelephone()));
            startActivity(intent);
        }
    }

    private void openUserPhone(View v, MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            ((TransitionDrawable)((RelativeLayout)v).getChildAt(1).getBackground()).startTransition(0);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            ((TransitionDrawable)((RelativeLayout)v).getChildAt(1).getBackground()).reverseTransition(0);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + user.getTelephone()));
            startActivity(intent);
        }
    }

    private void populateView() {
        ((TextView) findViewById(R.id.userName)).setText(user.getGivenName());
        String job = user.getContract();
        if (!user.getSociety().equals("null")) {
            job += ", " + user.getSociety();
        }
        ((TextView) findViewById(R.id.userContractAndSociety)).setText(job);
        ((TextView) findViewById(R.id.userAge)).setText(Integer.toString(user.getAge()) + " " + getResources().getString(R.string.age));
        ((TextView) findViewById(R.id.userDescription)).setText(user.getDescription());
        if(user.getSmoker() != null) {
            if(user.getSmoker() == true) {
                ((ImageView) findViewById(R.id.residentIsSmoker)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.is_smoker, null));
            } else {
                ((ImageView) findViewById(R.id.residentIsSmoker)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.is_not_smoker, null));
            }
        }

        if(user.getInRelationship() == true) {
            ((ImageView) findViewById(R.id.residentIsSingle)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.couple, null));
        }

        if(user.getContract().equals("worker")) {
            ((ImageView) findViewById(R.id.residentContract)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.worker, null));
        }

        new RetrieveUserPicTask().execute(user.getImageModel().getContentUrl());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class RetrieveUserPicTask extends AsyncTask<String, Void, Drawable> {

        @Override
        protected void onPreExecute(){
            System.out.println("Retrieving picture...");
        }

        @Override
        protected Drawable doInBackground(String... params) {
            try {
                InputStream is = (InputStream) new URL(params[0]).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");
                return d;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable d){
            ((ImageView) findViewById(R.id.userProfilePic)).setImageDrawable(d);
        }
    }
}