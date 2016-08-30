package com.appartoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.activity.OfferDetailsActivity;
import com.appartoo.activity.UserDetailActivity;
import com.appartoo.model.ObjectHolderModel;
import com.appartoo.model.OfferModelWithDetailledDate;
import com.appartoo.model.UserModel;
import com.appartoo.model.UserPresentationModel;
import com.appartoo.utils.ImageManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-07-05.
 */
public class OffersAndProfilesAdapter extends BaseAdapter {

    private ArrayList<ObjectHolderModel> offerModels;
    private LayoutInflater layoutInflater;
    private Context context;

    private static final int OFFER_TYPE = 0;
    private static final int PROFILE_TYPE = 1;

    public OffersAndProfilesAdapter(Context context, ArrayList<ObjectHolderModel> om) {
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        System.out.println(offerModels.get(position).toString());
        if(offerModels.get(position).getType().equals("offer")) {
            return OFFER_TYPE;
        } else {
            return PROFILE_TYPE;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ObjectHolderModel object = offerModels.get(position);
        LinkedTreeMap<?,?> treeMap = (LinkedTreeMap) object.getSource();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").create();;

        int viewType = this.getItemViewType(position);
        System.out.println(viewType);

        switch (viewType) {
            case OFFER_TYPE:
                OfferViewHolder offerHolder;

                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.list_item_offers, null);

                    offerHolder = new OfferViewHolder();

                    offerHolder.owner = (TextView) convertView.findViewById(R.id.offerOwner);
                    offerHolder.city = (TextView) convertView.findViewById(R.id.offerCity);
                    offerHolder.rooms = (TextView) convertView.findViewById(R.id.offerRooms);
                    offerHolder.keyword = (TextView) convertView.findViewById(R.id.offerKeyword);
                    offerHolder.price = (TextView) convertView.findViewById(R.id.offerPrice);
                    offerHolder.flatImage = (ImageView) convertView.findViewById(R.id.offerFlatImage);
                    offerHolder.ownerImageThumbnail = (ImageView) convertView.findViewById(R.id.offerOwnerImage);
                    convertView.setTag(offerHolder);
                } else {
                    System.out.println(convertView.getTag().getClass().getName());
                    offerHolder = (OfferViewHolder) convertView.getTag();
                }

                final OfferModelWithDetailledDate offerModel = gson.fromJson(gson.toJson(treeMap), OfferModelWithDetailledDate.class);

                offerHolder.owner.setText(offerModel.getOwner().getGivenName());
                offerHolder.keyword.setText(offerModel.getKeyword());
                offerHolder.rooms.setText(Integer.toString(offerModel.getRooms()));
                offerHolder.price.setText(Integer.toString(offerModel.getPrice()) + " " + context.getString(R.string.euro));

                if (offerModel.getImages().size() > 0)
                    ImageManager.downloadPictureIntoView(context, offerHolder.flatImage, offerModel.getImages().get(0).getContentUrl(), ImageManager.TRANFORM_SQUARE);
                else
                    offerHolder.flatImage.setImageDrawable(null);

                if (offerModel.getOwner().getImage() != null)
                    ImageManager.downloadPictureIntoView(context, offerHolder.ownerImageThumbnail, offerModel.getOwner().getImage().getContentUrl(), ImageManager.TRANFORM_SQUARE);
                else
                    offerHolder.ownerImageThumbnail.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.default_profile_picture, null));

                try {
                    offerHolder.city.setText(String.valueOf(offerModel.getAddress().getCity()));
                } catch (Exception e) {
                    offerHolder.city.setText(R.string.unknown_city);
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, OfferDetailsActivity.class);
                        intent.putExtra("offerId", object.getId().toString());
                        context.startActivity(intent);
                    }
                });

                return convertView;
            case PROFILE_TYPE:
                ProfileViewHolder profileHolder;

                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.list_item_profiles, null);

                    profileHolder = new ProfileViewHolder();

                    profileHolder.name = (TextView) convertView.findViewById(R.id.profileName);
                    profileHolder.image = (ImageView) convertView.findViewById(R.id.profileImage);
                    profileHolder.acceptAnimals = (ImageView) convertView.findViewById(R.id.profileAcceptsAnimals);
                    profileHolder.inRelationship = (ImageView) convertView.findViewById(R.id.profileRelationship);
                    profileHolder.smoker = (ImageView) convertView.findViewById(R.id.profileSmoker);

                    convertView.setTag(profileHolder);
                } else {
                    System.out.println(convertView.getTag().getClass().getName());
                    profileHolder = (ProfileViewHolder) convertView.getTag();
                }

                final UserPresentationModel userModel = gson.fromJson(gson.toJson(treeMap), UserPresentationModel.class);

                profileHolder.name.setText(userModel.getGivenName() + " " + userModel.getFamilyName());

                if(userModel.getImages() != null && userModel.getImages().size() > 0)
                    ImageManager.downloadPictureIntoView(context, profileHolder.image, userModel.getImages().get(0).getContentUrl(), ImageManager.TRANFORM_SQUARE);
                else
                    profileHolder.image.setImageDrawable(null);

                if(userModel.getRelationshipStatus() != null) {
                    if (userModel.getRelationshipStatus().equals("single"))
                        profileHolder.inRelationship.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.alone, null));
                    else
                        profileHolder.inRelationship.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.couple, null));
                }

                if(userModel.getSmoker() != null) {
                    if (userModel.getSmoker())
                        profileHolder.smoker.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.is_smoker, null));
                    else
                        profileHolder.smoker.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.is_not_smoker, null));
                }

                if(userModel.getAcceptAnimal() != null) {
                    if (userModel.getAcceptAnimal())
                        profileHolder.acceptAnimals.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.accept_animals, null));
                    else
                        profileHolder.acceptAnimals.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.dont_accept_animals, null));
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, UserDetailActivity.class);
                        intent.putExtra("profileId", object.getId().toString());
                        context.startActivity(intent);
                    }
                });

                return convertView;

            default:
                return convertView;
        }
    }


    static class ProfileViewHolder {
        TextView name;
        ImageView image;
        ImageView inRelationship;
        ImageView smoker;
        ImageView acceptAnimals;
    }

    static class OfferViewHolder {
        TextView owner;
        TextView city;
        TextView keyword;
        TextView rooms;
        TextView price;
        ImageView ownerImageThumbnail;
        ImageView flatImage;
    }
}