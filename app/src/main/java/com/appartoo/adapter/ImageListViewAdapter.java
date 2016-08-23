package com.appartoo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import com.appartoo.view.GridImageView;

/**
 * Created by alexandre on 16-08-10.
 */
public class ImageListViewAdapter extends BaseAdapter {

    // references to our images
    private ArrayList<ImageView> images;
    private Context context;

    public ImageListViewAdapter(Context context, ArrayList<ImageView> images){
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        return images.get(position);
    }
}
