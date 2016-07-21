package mobile.appartoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompletePrediction;

import java.util.ArrayList;
import java.util.List;

import mobile.appartoo.R;
import mobile.appartoo.model.PlaceModel;

/**
 * Created by alexandre on 16-07-21.
 */
public class PlacesAdapter extends ArrayAdapter<PlaceModel> {

    private Filter filter = new KNoFilter();
    private ArrayList<PlaceModel> places;
    private LayoutInflater layoutInflater;

    public PlacesAdapter(Context context, int textViewResourceId, List<PlaceModel> objects) {
        super(context, textViewResourceId, objects);
        this.places = (ArrayList<PlaceModel>) objects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public PlaceModel getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_places, null);

            holder = new ViewHolder();

            holder.placePrimary = (TextView) convertView.findViewById(R.id.autocompletePlacePrimary);
            holder.placeSecondary = (TextView) convertView.findViewById(R.id.autocompletePlaceSecondary);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.placePrimary.setText(places.get(position).getPrimaryText() + " ");
        holder.placeSecondary.setText(places.get(position).getSecondaryText());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    static class ViewHolder {
        TextView placePrimary;
        TextView placeSecondary;
    }

    private class KNoFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence arg0) {
            FilterResults result = new FilterResults();
            result.values = places;
            result.count = places.size();
            return result;
        }

        @Override
        protected void publishResults(CharSequence arg0, FilterResults arg1) {
            notifyDataSetChanged();
        }
    }
}
