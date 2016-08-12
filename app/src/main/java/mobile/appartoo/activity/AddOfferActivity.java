package mobile.appartoo.activity;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mobile.appartoo.R;
import mobile.appartoo.view.NavigationDrawerView;

public class AddOfferActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //Retrieve the drawer elements
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public void onStart(){
        super.onStart();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ajoutez une offre");


        toolbar.setNavigationIcon(R.drawable.left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void toggleView(View v) {
        ImageView animals = (ImageView) v.findViewById(R.id.addOfferImageAnimals);
        ImageView smoker = (ImageView) v.findViewById(R.id.addOfferImageSmoker);

        if(animals != null) {
            Boolean acceptAnimals = Boolean.valueOf(animals.getTag().toString());
            if(acceptAnimals) {
                animals.setTag("false");
                animals.setImageDrawable(ResourcesCompat.getDrawable(v.getResources(), R.drawable.dont_accept_animals, null));
                ((TextView) v.findViewById(R.id.addOfferTextAnimals)).setText(R.string.dont_accept_animals);
            } else {
                animals.setTag("true");
                animals.setImageDrawable(ResourcesCompat.getDrawable(v.getResources(), R.drawable.accept_animals, null));
                ((TextView) v.findViewById(R.id.addOfferTextAnimals)).setText(R.string.accept_animals);
            }
        } else if (smoker != null) {
            Boolean acceptSmoker = Boolean.valueOf(smoker.getTag().toString());
            if(acceptSmoker) {
                smoker.setTag("false");
                smoker.setImageDrawable(ResourcesCompat.getDrawable(v.getResources(), R.drawable.is_not_smoker, null));
                ((TextView) v.findViewById(R.id.addOfferTextSmoker)).setText(R.string.dont_accept_smokers);
            } else {
                smoker.setTag("true");
                smoker.setImageDrawable(ResourcesCompat.getDrawable(v.getResources(), R.drawable.is_smoker, null));
                ((TextView) v.findViewById(R.id.addOfferTextSmoker)).setText(R.string.accept_smokers);
            }
        }
    }
}
