package mobile.appartoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mobile.appartoo.R;
import mobile.appartoo.model.OfferModel;
import mobile.appartoo.model.OfferModelWithDate;

/**
 * Created by alexandre on 16-07-05.
 */
public class OffersAdapter extends BaseAdapter {

    private ArrayList<OfferModel> offerModels;
    private LayoutInflater layoutInflater;
    private Context context;

    public OffersAdapter(Context context, ArrayList<OfferModel> om) {
        this.context = context;
        this.offerModels = om;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return offerModels.size();
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
            convertView = layoutInflater.inflate(R.layout.list_item_offers, null);

            holder = new ViewHolder();

            holder.owner = (TextView) convertView.findViewById(R.id.offerOwner);
            holder.city = (TextView) convertView.findViewById(R.id.offerCity);
            holder.rooms = (TextView) convertView.findViewById(R.id.offerRooms);
            holder.keyword = (TextView) convertView.findViewById(R.id.offerKeyword);
            holder.price = (TextView) convertView.findViewById(R.id.offerPrice);
            holder.flatThumbnail = (ImageView) convertView.findViewById(R.id.offerFlatImage);
            holder.ownerThumbnail = (ImageView) convertView.findViewById(R.id.offerOwnerImage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OfferModel offerModel = offerModels.get(position);

        if(offerModel instanceof OfferModelWithDate) {
            holder.owner.setText(((OfferModelWithDate) offerModel).getOwner().getGivenName());
        }

        try {
            holder.city.setText(String.valueOf(offerModel.getAddress().getCity()));
        } catch (Exception e) {
            holder.city.setText(R.string.unknown_city);
        }
        holder.keyword.setText(offerModel.getKeyword());
        holder.rooms.setText(Integer.toString(offerModel.getRooms()));
        holder.price.setText(Integer.toString(offerModel.getPrice()) + " " + context.getString(R.string.euro));

        return convertView;
    }

    static class ViewHolder {
        TextView owner;
        TextView city;
        TextView keyword;
        TextView rooms;
        TextView price;
        ImageView ownerThumbnail;
        ImageView flatThumbnail;
    }
}