package com.appartoo.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.model.OfferModel;
import com.appartoo.utils.ImageManager;

import java.util.ArrayList;

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
            holder.flatImage = (ImageView) convertView.findViewById(R.id.offerFlatImage);
            holder.ownerImageThumbnail = (ImageView) convertView.findViewById(R.id.offerOwnerImage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OfferModel offerModel = offerModels.get(position);

        holder.owner.setText(offerModel.getOwner().getGivenName());

        if(offerModel.getImages().size() > 0) {
            ImageManager.downloadPictureIntoView(context, holder.flatImage, offerModel.getImages().get(0).getContentUrl(), ImageManager.TRANFORM_SQUARE);
            convertView.findViewById(R.id.noPictureIndicator).setVisibility(View.GONE);
        } else {
            holder.flatImage.setImageDrawable(null);
            convertView.findViewById(R.id.noPictureIndicator).setVisibility(View.VISIBLE);
        }

        if (offerModel.getOwner().getImage() != null){
            ImageManager.downloadPictureIntoView(context, holder.ownerImageThumbnail, offerModel.getOwner().getImage().getContentUrl(), ImageManager.TRANFORM_SQUARE);
        } else {
            holder.ownerImageThumbnail.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.default_profile_picture, null));
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
        ImageView ownerImageThumbnail;
        ImageView flatImage;
    }
}