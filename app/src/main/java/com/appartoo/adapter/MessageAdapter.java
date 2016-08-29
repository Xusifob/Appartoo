package com.appartoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.model.MessageModel;
import com.appartoo.model.UserModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.DateManager;
import com.appartoo.utils.ImageManager;
import com.appartoo.utils.RestService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexandre on 16-08-17.
 */
public class MessageAdapter extends BaseAdapter {

    private ArrayList<MessageModel> messageModels;
    private Context context;
    private LayoutInflater layoutInflater;
    private final String userId;
    private HashMap<String, String> profilePictures;
    private RestService restService;

    public MessageAdapter(Context context, ArrayList<MessageModel> messageModels, final String userId) {
        this.context = context;
        this.messageModels = messageModels;
        this.layoutInflater = LayoutInflater.from(context);
        this.userId = userId;
        this.profilePictures = new HashMap<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restService = retrofit.create(RestService.class);
    }

    @Override
    public int getCount() {
        return messageModels.size();
    }

    @Override
    public Object getItem(int i) {
        return messageModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_message, null);
            final ViewHolder holder = new ViewHolder();

            holder.myPicture = (ImageView) convertView.findViewById(R.id.userPicture);
            holder.myMessage = (TextView) convertView.findViewById(R.id.userMessage);
            holder.myMessageHour = (TextView) convertView.findViewById(R.id.userMessageHour);
            holder.myMessageContainer = (LinearLayout) convertView.findViewById(R.id.userMessageContainer);
            holder.othersPicture = (ImageView) convertView.findViewById(R.id.othersPicture);
            holder.othersMessage = (TextView) convertView.findViewById(R.id.othersMessage);
            holder.othersMessageHour = (TextView) convertView.findViewById(R.id.othersMessageHour);
            holder.othersMessageContainer = (LinearLayout) convertView.findViewById(R.id.othersMessageContainer);

            convertView.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final MessageModel messageModel = messageModels.get(i);
        final boolean isMe = messageModel.getSenderId() != null && messageModel.getSenderId().equals(userId);

        System.out.println(profilePictures.get(messageModel.getSenderId()));

        if(isMe) {
            holder.myMessage.setText(messageModel.getMessage());

            if(Appartoo.LOGGED_USER_PROFILE != null && Appartoo.LOGGED_USER_PROFILE.getImage() != null) {
                ImageManager.downloadPictureIntoView(context, holder.myPicture, Appartoo.LOGGED_USER_PROFILE.getImage().getContentUrl(), ImageManager.TRANFORM_SQUARE);
            } else {
                holder.myPicture.setBackgroundResource(R.drawable.circle_light_gray);
            }
            holder.myMessageHour.setText(DateManager.getFormattedDate(messageModel.getCreatedTime()));
            holder.myMessageContainer.setVisibility(View.VISIBLE);
            holder.othersPicture.setBackgroundResource(R.color.colorTransparent);
            holder.othersPicture.setImageDrawable(null);
            holder.othersMessageContainer.setVisibility(View.GONE);
        } else {
            holder.othersMessage.setText(messageModel.getMessage());

            if(profilePictures.get(messageModel.getSenderId()) == null) {
                holder.othersPicture.setBackgroundResource(R.drawable.circle_light_gray);
                retrieveUserProfilePic(messageModel.getSenderId());
            } else {
                ImageManager.downloadPictureIntoView(context, holder.othersPicture, profilePictures.get(messageModel.getSenderId()), ImageManager.TRANFORM_SQUARE);
            }

            holder.othersMessageHour.setText(DateManager.getFormattedDate(messageModel.getCreatedTime()));
            holder.othersMessageContainer.setVisibility(View.VISIBLE);
            holder.myPicture.setBackgroundResource(R.color.colorTransparent);
            holder.myPicture.setImageDrawable(null);
            holder.myMessageContainer.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void retrieveUserProfilePic(final String id) {
        System.out.println("/profiles/" + id);

        Call<UserModel> callback = restService.getUserProfileById(RestService.REST_URL + "/profiles/" + id);

        callback.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful()) {
                    profilePictures.put(id, response.body().getImage().getContentUrl());
                } else {
                    System.out.println(response.code());
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private static class ViewHolder {
        public ImageView myPicture;
        public ImageView othersPicture;
        public TextView myMessage;
        public TextView myMessageHour;
        public TextView othersMessage;
        public TextView othersMessageHour;
        public LinearLayout othersMessageContainer;
        public LinearLayout myMessageContainer;
    }
}
