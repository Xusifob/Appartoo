package mobile.appartoo.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import mobile.appartoo.R;

/**
 * Created by alexandre on 16-07-05.
 */
public class NavigationDrawerAdapter extends BaseAdapter {

    private String[] titles;
    private TypedArray images;
    private LayoutInflater layoutInflater;

    public NavigationDrawerAdapter(Context context, String[] titles, TypedArray images) {
        this.titles = titles;
        this.images = images;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_navigation_drawer, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.navigation_drawer_title);
            holder.icon = (ImageView) convertView.findViewById(R.id.navigation_drawer_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(titles[position]);
        holder.icon.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), images.getResourceId(position, 0), null));

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        ImageView icon;
    }
}