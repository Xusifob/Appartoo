package com.appartoo.general;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.appartoo.R;
import com.appartoo.utils.adapter.ImageViewPagerAdapter;
import com.appartoo.utils.model.ImageModel;

import java.util.ArrayList;

public class OfferImagesActivity extends Activity {

    private ArrayList<ImageModel> pictures;
    private ViewPager viewPager;
    private int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_images);



        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //Retrieve the resources
        pictures = getIntent().getParcelableArrayListExtra("pictures");
        currentItem = getIntent().getIntExtra("currentImage", 0);
        viewPager = (ViewPager) findViewById(R.id.offerFlatImagesPager);
    }

    @Override
    protected void onStart(){
        super.onStart();

        //Add the pictures to the view pager
        viewPager.setAdapter(new ImageViewPagerAdapter(this, pictures));
        viewPager.setCurrentItem(currentItem);
    }
}
