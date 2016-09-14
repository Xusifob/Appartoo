package com.appartoo.utils.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appartoo.misc.OfferDetailsActivity;
import com.appartoo.misc.OfferImagesActivity;
import com.appartoo.utils.ImageManager;
import com.appartoo.utils.model.ImageModel;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by alexandre on 16-07-12.
 */
public class ImageViewViewPagerAdapter extends PagerAdapter {

    private ArrayList<ImageView> pictures;

    public ImageViewViewPagerAdapter(ArrayList<ImageView> pictures) {
        this.pictures = pictures;
    }

    @Override
    public int getCount() {
        return pictures.size();
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        container.addView(pictures.get(position));
        return pictures.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}