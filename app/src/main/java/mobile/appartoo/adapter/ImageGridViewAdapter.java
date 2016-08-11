package mobile.appartoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import mobile.appartoo.R;
import mobile.appartoo.view.GridImageView;

/**
 * Created by alexandre on 16-08-10.
 */
public class ImageGridViewAdapter extends BaseAdapter {
    // references to our images
    private ArrayList<GridImageView> images;

    public ImageGridViewAdapter(ArrayList<GridImageView> images){
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        return images.get(position);
    }
}
