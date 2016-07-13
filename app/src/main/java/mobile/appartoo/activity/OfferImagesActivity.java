package mobile.appartoo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import mobile.appartoo.R;
import mobile.appartoo.adapter.ImageViewPagerAdapter;

public class OfferImagesActivity extends Activity {

    private int[] resources;
    private ViewPager viewPager;
    private int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_images);
        resources = getIntent().getIntArrayExtra("resources");
        currentItem = getIntent().getIntExtra("currentImage", 0);
        viewPager = (ViewPager) findViewById(R.id.offerFlatImagesPager);
    }

    @Override
    protected void onStart(){
        super.onStart();

        viewPager.setAdapter(new ImageViewPagerAdapter(this, resources, false));
        viewPager.setCurrentItem(currentItem);
    }
}
