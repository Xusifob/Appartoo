package com.appartoo.utils.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appartoo.misc.OfferDetailsActivity;
import com.appartoo.misc.OfferImagesActivity;
import com.appartoo.utils.model.ImageModel;
import com.appartoo.utils.ImageManager;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by alexandre on 16-07-12.
 */
public class ImageModelViewPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<ImageModel> pictures;

    public ImageModelViewPagerAdapter(Context context, ArrayList<ImageModel> pictures) {
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


        if(context instanceof OfferDetailsActivity) {
            ImageView imageView = new ImageView(context);
            ImageManager.downloadPictureIntoView(context, imageView, pictures.get(position).getContentUrl(), ImageManager.TRANFORM_SQUARE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OfferImagesActivity.class);
                    intent.putExtra("currentImage", position);
                    intent.putParcelableArrayListExtra("pictures", pictures);
                    context.startActivity(intent);
                }
            });

            container.addView(imageView);
            return imageView;
        } else {
            PhotoView imageView = new PhotoView(context);
            ImageManager.downloadPictureIntoView(context, imageView, pictures.get(position).getContentUrl(), null);
            container.addView(imageView);
            return imageView;
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}