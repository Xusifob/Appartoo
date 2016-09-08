package com.appartoo.utils.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.general.OfferDetailsActivity;
import com.appartoo.utils.model.OfferModelWithDetailledDate;
import com.appartoo.utils.ImageManager;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-07-05.
 */
public class OffersAdapter extends RecyclerView.Adapter {

    private ArrayList<OfferModelWithDetailledDate> offersModels;
    private Context context;

    public OffersAdapter(Context context, ArrayList<OfferModelWithDetailledDate> om) {
        this.context = context;
        this.offersModels = om;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return offersModels.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View offerView = LayoutInflater.from(context).inflate(R.layout.list_item_offers, null);
        return new ViewHolder(offerView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindData(offersModels.get(position), context);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView owner;
        TextView city;
        TextView keyword;
        TextView rooms;
        TextView price;
        ImageView ownerImageThumbnail;
        ImageView flatImage;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.owner = (TextView) itemView.findViewById(R.id.offerOwner);
            this.city = (TextView) itemView.findViewById(R.id.offerCity);
            this.rooms = (TextView) itemView.findViewById(R.id.offerRooms);
            this.keyword = (TextView) itemView.findViewById(R.id.offerKeyword);
            this.price = (TextView) itemView.findViewById(R.id.offerPrice);
            this.flatImage = (ImageView) itemView.findViewById(R.id.offerFlatImage);
            this.ownerImageThumbnail = (ImageView) itemView.findViewById(R.id.offerOwnerImage);
        }

        public void bindData(final OfferModelWithDetailledDate offerModel, final Context context){
            owner.setText(offerModel.getOwner().getGivenName());
            keyword.setText(offerModel.getKeyword());
            rooms.setText(Integer.toString(offerModel.getRooms()));
            price.setText(Integer.toString(offerModel.getPrice()) + " " + context.getString(R.string.euro));

            if (offerModel.getImages().size() > 0)
                ImageManager.downloadPictureIntoView(context, flatImage, offerModel.getImages().get(0).getContentUrl(), ImageManager.TRANFORM_SQUARE);
            else
                flatImage.setImageDrawable(null);

            if (offerModel.getOwner().getImage() != null)
                ImageManager.downloadPictureIntoView(context, ownerImageThumbnail, offerModel.getOwner().getImage().getContentUrl(), ImageManager.TRANFORM_SQUARE);
            else
                ownerImageThumbnail.setImageDrawable(ResourcesCompat.getDrawable(this.itemView.getResources(), R.drawable.default_profile_picture, null));

            try {
                city.setText(String.valueOf(offerModel.getAddress().getCity()));
            } catch (Exception e) {
                city.setText(R.string.unknown_city);
            }

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OfferDetailsActivity.class);
                    intent.putExtra("offer", offerModel);
                    intent.putExtra("isOwner", true);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.left_out);;
                }
            });
        }
    }
}