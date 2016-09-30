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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.misc.OfferDetailsActivity;
import com.appartoo.utils.model.ObjectHolderModel;
import com.appartoo.utils.model.OfferModel;
import com.appartoo.utils.model.OfferModelWithDetailledDate;
import com.appartoo.utils.ImageManager;
import com.appartoo.utils.model.UserProfileModel;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-07-05.
 */
public class OffersAdapter extends RecyclerView.Adapter {

    private ArrayList<OfferModelWithDetailledDate> offersModels;
    private Context context;
    private int footerVisibility;

    private static final int OFFER_TYPE = 0;
    private static final int FOOTER_TYPE = 1;

    public OffersAdapter(Context context, ArrayList<OfferModelWithDetailledDate> om) {
        this.context = context;
        this.offersModels = om;
        this.footerVisibility = View.VISIBLE;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return offersModels.size() + 1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case OFFER_TYPE:
                View offerView = LayoutInflater.from(context).inflate(R.layout.list_item_offers, null);
                return new ViewHolder(offerView);
            case FOOTER_TYPE:
                View progressBar = LayoutInflater.from(context).inflate(R.layout.progress_bar, parent, false);
                return new FooterProgressBarViewHolder(progressBar);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == offersModels.size()) {
            return FOOTER_TYPE;
        } else {
            return OFFER_TYPE;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position < offersModels.size()) {
            ((ViewHolder) holder).bindData(offersModels.get(position), context);
        } else {
            ((FooterProgressBarViewHolder) holder).progressBar.setVisibility(footerVisibility);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setFooterVisibility(int visibility) {
        if(visibility == View.VISIBLE || visibility == View.INVISIBLE || visibility == View.GONE) {
            footerVisibility = visibility;
            notifyDataSetChanged();
        }
    }

    static class FooterProgressBarViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public FooterProgressBarViewHolder(View itemView) {
            super(itemView);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ownerAndKeyword;
        TextView city;
        TextView rooms;
        TextView price;
        TextView offerActivated;
        TextView name;
        ImageView ownerImageThumbnail;
        ImageView flatImage;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.ownerAndKeyword = (TextView) itemView.findViewById(R.id.cardOfferOwnerAndKeyword);
            this.city = (TextView) itemView.findViewById(R.id.cardOfferCity);
            this.rooms = (TextView) itemView.findViewById(R.id.cardOfferRooms);
            this.price = (TextView) itemView.findViewById(R.id.cardOfferPrice);
            this.offerActivated = (TextView) itemView.findViewById(R.id.cardOfferActivated);
            this.name = (TextView) itemView.findViewById(R.id.cardOfferName);
            this.flatImage = (ImageView) itemView.findViewById(R.id.cardOfferFlatImage);
            this.ownerImageThumbnail = (ImageView) itemView.findViewById(R.id.cardOfferOwnerImage);
        }

        public void bindData(final OfferModelWithDetailledDate offerModel, final Context context){
            ownerAndKeyword.setText(offerModel.getKeyword());
            rooms.setText(Integer.toString(offerModel.getRooms()));
            price.setText(Integer.toString(offerModel.getPrice()) + " " + context.getString(R.string.euro));
            name.setText(offerModel.getName());

            if (offerModel.getImages().size() > 0)
                ImageManager.downloadPictureIntoView(context, flatImage, offerModel.getImages().get(0).getContentUrl(), ImageManager.TRANFORM_SQUARE);
            else
                flatImage.setImageDrawable(null);

            if (offerModel.getOwner().getImage() != null)
                ImageManager.downloadPictureIntoView(context, ownerImageThumbnail, offerModel.getOwner().getImage().getContentUrl(), ImageManager.TRANFORM_SQUARE);
            else
                ownerImageThumbnail.setImageDrawable(ResourcesCompat.getDrawable(this.itemView.getResources(), R.drawable.default_profile_picture, null));

            offerActivated.setVisibility(View.VISIBLE);
            if(offerModel.getActive()) {
                offerActivated.setText(R.string.activated);
                offerActivated.setBackgroundColor(ResourcesCompat.getColor(this.itemView.getResources(), R.color.colorAlphaGreen, null));
            }
            else {
                offerActivated.setText(R.string.desactivated);
                offerActivated.setBackgroundColor(ResourcesCompat.getColor(this.itemView.getResources(), R.color.colorAlphaSalmon, null));
            }

            try {
                city.setText(String.valueOf(offerModel.getAddress().getCity()));
            } catch (Exception e) {
                city.setText(R.string.unknown_city);
            }

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OfferDetailsActivity.class);
                    intent.putExtra("offerId", offerModel.getIdNumber().toString());
                    intent.putExtra("isOwner", true);
                    context.startActivity(intent);
                }
            });
        }
    }
}