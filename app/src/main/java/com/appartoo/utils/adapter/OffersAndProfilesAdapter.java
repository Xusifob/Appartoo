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
import com.appartoo.misc.UserDetailActivity;
import com.appartoo.utils.model.ObjectHolderModel;
import com.appartoo.utils.model.OfferModel;
import com.appartoo.utils.model.UserProfileModel;
import com.appartoo.utils.ImageManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-07-05.
 */
public class OffersAndProfilesAdapter extends RecyclerView.Adapter {

    private ArrayList<ObjectHolderModel> offersAndProfilesModels;
    private Context context;
    private Gson gson;
    private int footerVisibility;

    private static final int OFFER_TYPE = 0;
    private static final int PROFILE_TYPE = 1;
    private static final int FOOTER_TYPE = 2;

    public OffersAndProfilesAdapter(Context context, ArrayList<ObjectHolderModel> om) {
        this.context = context;
        this.offersAndProfilesModels = om;
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").create();
        this.footerVisibility = View.VISIBLE;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return offersAndProfilesModels.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case OFFER_TYPE:
                View offerView = LayoutInflater.from(context).inflate(R.layout.list_item_offers, null);
                return new OfferViewHolder(offerView);
            case PROFILE_TYPE:
                View profileView = LayoutInflater.from(context).inflate(R.layout.list_item_profiles, null);
                return new ProfileViewHolder(profileView);
            case FOOTER_TYPE:
                View progressBar = LayoutInflater.from(context).inflate(R.layout.progress_bar, parent, false);
                return new FooterProgressBarViewHolder(progressBar);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);

        if(position < offersAndProfilesModels.size()) {
            final ObjectHolderModel object = offersAndProfilesModels.get(position);
            final LinkedTreeMap<?, ?> treeMap = (LinkedTreeMap) object.getSource();

            if (itemType == OFFER_TYPE) {
                ((OfferViewHolder) holder).bindData(gson.fromJson(gson.toJson(treeMap), OfferModel.class), context, object.getId().toString());
            } else if (itemType == PROFILE_TYPE) {
                ((ProfileViewHolder) holder).bindData(gson.fromJson(gson.toJson(treeMap), UserProfileModel.class), context, object.getId().toString());
            }
        } else {
            ((FooterProgressBarViewHolder) holder).progressBar.setVisibility(footerVisibility);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == offersAndProfilesModels.size()) {
            return FOOTER_TYPE;
        } else if(offersAndProfilesModels.get(position).getType().equals("offer")) {
            return OFFER_TYPE;
        } else {
            return PROFILE_TYPE;
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

    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        ImageView inRelationship;
        ImageView smoker;
        ImageView acceptAnimals;
        View itemView;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.name = (TextView) itemView.findViewById(R.id.profileName);
            this.image = (ImageView) itemView.findViewById(R.id.profileImage);
            this.acceptAnimals = (ImageView) itemView.findViewById(R.id.profileAcceptsAnimals);
            this.inRelationship = (ImageView) itemView.findViewById(R.id.profileRelationship);
            this.smoker = (ImageView) itemView.findViewById(R.id.profileSmoker);
        }

        public void bindData(final UserProfileModel userModel, final Context context, final String id){
            name.setText(userModel.getGivenName() + " " + userModel.getFamilyName());

            if(userModel.getImages() != null && userModel.getImages().size() > 0)
                ImageManager.downloadPictureIntoView(context, image, userModel.getImages().get(0).getContentUrl(), ImageManager.TRANFORM_SQUARE);
            else
                image.setImageDrawable(null);

            if(userModel.getRelationshipStatus() != null) {
                if (userModel.getRelationshipStatus().equals("single"))
                    inRelationship.setImageDrawable(ResourcesCompat.getDrawable(this.itemView.getResources(), R.drawable.single, null));
                else
                    inRelationship.setImageDrawable(ResourcesCompat.getDrawable(this.itemView.getResources(), R.drawable.couple, null));
            } else {
                inRelationship.setImageDrawable(ResourcesCompat.getDrawable(this.itemView.getResources(), R.drawable.untold_relationship, null));
            }

            if(userModel.getSmoker() != null) {
                if (userModel.getSmoker())
                    smoker.setImageDrawable(ResourcesCompat.getDrawable(this.itemView.getResources(), R.drawable.is_smoker, null));
                else
                    smoker.setImageDrawable(ResourcesCompat.getDrawable(this.itemView.getResources(), R.drawable.is_not_smoker, null));
            } else {
                smoker.setImageDrawable(ResourcesCompat.getDrawable(this.itemView.getResources(), R.drawable.untold_smoker, null));
            }

            if(userModel.getAcceptAnimal() != null) {
                if (userModel.getAcceptAnimal())
                    acceptAnimals.setImageDrawable(ResourcesCompat.getDrawable(this.itemView.getResources(), R.drawable.accept_animals, null));
                else
                    acceptAnimals.setImageDrawable(ResourcesCompat.getDrawable(this.itemView.getResources(), R.drawable.dont_accept_animals, null));
            } else {
                acceptAnimals.setImageDrawable(ResourcesCompat.getDrawable(this.itemView.getResources(), R.drawable.untold_accept_animals, null));
            }

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UserDetailActivity.class);
                    intent.putExtra("profileId", id);
                    intent.putExtra("userName", userModel.getGivenName() + " " + userModel.getFamilyName());
                    context.startActivity(intent);
                }
            });
        }
    }

    static class OfferViewHolder extends RecyclerView.ViewHolder {
        TextView owner;
        TextView city;
        TextView keyword;
        TextView rooms;
        TextView price;
        ImageView ownerImageThumbnail;
        ImageView flatImage;
        View itemView;

        public OfferViewHolder(View itemView) {
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
        
        public void bindData(OfferModel offerModel, final Context context, final String id){
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
                    intent.putExtra("offerId", id);
                    context.startActivity(intent);
                }
            });
        }
    }
}