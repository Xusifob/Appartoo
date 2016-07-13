package mobile.appartoo.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mobile.appartoo.R;
import mobile.appartoo.model.UserModel;

/**
 * Created by alexandre on 16-07-05.
 */
public class ResidentsAdapter extends BaseAdapter {

    private ArrayList<UserModel> userModels;
    private LayoutInflater layoutInflater;
    private Boolean accetAnimals;

    public ResidentsAdapter(Context context, ArrayList<UserModel> om) {
        userModels = om;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setAcceptAnimals(Boolean acceptAnimals){
        this.accetAnimals = acceptAnimals;
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
            holder.isSmoker = (ImageView) convertView.findViewById(R.id.residentIsSmoker);
            holder.acceptAnimals = (ImageView) convertView.findViewById(R.id.residentAcceptsAnimals);
            holder.isSingle = (ImageView) convertView.findViewById(R.id.residentIsSingle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserModel userModel = userModels.get(position);
        holder.name.setText(userModel.getGivenName());
        holder.age.setText(Integer.toString(userModel.getAge()) +  " " + convertView.getResources().getString(R.string.age));
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

        if(accetAnimals != null){
            if(accetAnimals == true){
                holder.acceptAnimals.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.accept_animals, null));
            } else {
                holder.acceptAnimals.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.dont_accept_animals, null));
            }
        }

        if(userModel.getInRelationship() == true){
            holder.isSingle.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), R.drawable.couple, null));
        }
    }

    static class ViewHolder {
        TextView name;
        TextView age;
        ImageView isSmoker;
        ImageView acceptAnimals;
        ImageView isSingle;
    }
}