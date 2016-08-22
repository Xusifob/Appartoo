package com.appartoo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.appartoo.R;

/**
 * Created by alexandre on 16-08-17.
 */
public class MessageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String conversationName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        conversationName = getIntent().getStringExtra("conversationName");
    }

    @Override
    public void onStart() {
        super.onStart();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(conversationName);

        toolbar.setNavigationIcon(R.drawable.left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
