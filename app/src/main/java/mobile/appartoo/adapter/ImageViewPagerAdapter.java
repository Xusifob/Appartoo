package mobile.appartoo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import mobile.appartoo.model.ImageModel;
import mobile.appartoo.utils.ImageReceiver;

/**
 * Created by alexandre on 16-07-12.
 */
public class ImageViewPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<ImageModel> pictures;
    private boolean crop;
    private boolean isZoomable;

    public ImageViewPagerAdapter(Context context, ArrayList<ImageModel> pictures) {
        this.pictures = pictures;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pictures.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImageView imageView = new ImageView(context);
        ImageReceiver.getSquaredPicture(context, imageView, pictures.get(position).getContentUrl());
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}