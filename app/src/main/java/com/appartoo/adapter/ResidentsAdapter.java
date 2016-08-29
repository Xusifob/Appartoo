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
import com.appartoo.model.UserModel;
import com.appartoo.utils.ImageManager;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-07-05.
 */
public class ResidentsAdapter extends BaseAdapter {

    private ArrayList<UserModel> userModels;
    private LayoutInflater layoutInflater;
    private Boolean acceptAnimals;
    private Context context;

    public ResidentsAdapter(Context context, ArrayList<UserModel> om) {
        this.context = context;
        this.userModels = om;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setAcceptAnimals(Boolean acceptAnimals){
        this.acceptAnimals = acceptAnimals;
    }

    @Override
    public int getCount() {
        return userModels.size();
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
            convertView = layoutInflater.inflate(R.layout.list_item_users, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.residentName);
            holder.age = (TextView) convertView.findViewById(R.id.residentAge);
            holder.isSmoker = (ImageView) convertView.findViewById(R.id.userDetailSmoker);
            holder.acceptAnimals = (ImageView) convertView.findViewById(R.id.residentAcceptsAnimals);
            holder.isSingle = (ImageView) convertView.findViewById(R.id.userDetailRelationship);
            holder.residentImageThumbail = (ImageView) convertView.findViewById(R.id.residentThumbnail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserModel userModel = userModels.get(position);
        holder.name.setText(userModel.getGivenName());
        if(userModel.getAge() > 0) {
            holder.age.setText(Integer.toString(userModel.getAge()) + " " + convertView.getResources().getString(R.string.age));
        }
        setPictures(holder, userModel, convertView);

        return convertView;
    }

    public void setPictures(ViewHolder holder, UserModel userModel, View convertView) {
        if(userModel.getSmoker() != null){
            if(userModel.getSmoker() == true){
                holder.isSmoker.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.is_smoker, null));
            } else {
                holder.isSmoker.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.is_not_smoker, null));
            }
        }

        if(acceptAnimals != null){
            if(acceptAnimals == true){
                holder.acceptAnimals.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.accept_animals, null));
            } else {
                holder.acceptAnimals.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.dont_accept_animals, null));
            }
        }

        if(userModel.getInRelationship() != null && userModel.getInRelationship() == true){
            holder.isSingle.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.couple, null));
        }

        if (userModel.getImage() != null){
            ImageManager.downloadPictureIntoView(context, holder.residentImageThumbail, userModel.getImage().getContentUrl(), ImageManager.TRANFORM_SQUARE);
        } else {
            holder.residentImageThumbail.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.default_profile_picture, null));
        }
    }

    static class ViewHolder {
        TextView name;
        TextView age;
        ImageView isSmoker;
        ImageView acceptAnimals;
        ImageView isSingle;
        ImageView residentImageThumbail;
    }
}