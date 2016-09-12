package com.appartoo.utils.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appartoo.R;
import com.appartoo.misc.OfferDetailsActivity;
import com.appartoo.misc.OfferImagesActivity;
import com.appartoo.utils.ImageManager;
import com.appartoo.utils.model.ImageModel;

import java.io.File;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by alexandre on 16-07-12.
 */
public class ImageBitmapViewPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Bitmap> pictures;
    private ArrayList<File> files;

    public ImageBitmapViewPagerAdapter(Context context, ArrayList<Bitmap> pictures, ArrayList<File> files) {
        this.pictures = pictures;
        this.context = context;
        this.files = files;
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
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(context);

        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(pictures.get(position));
        container.addView(imageView);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppThemeDialog))
                        .setTitle("Retirer l'image ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pictures.remove(position);
                                files.remove(files.size() - 1 - position);
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("Non", null)
                        .show();
                return true;
            }
        });

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}