package com.appartoo.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.model.CompleteUserModel;
import com.appartoo.model.ConversationModel;
import com.appartoo.model.MessageModel;
import com.appartoo.model.UserModel;
import com.appartoo.model.UserProfileModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.ImageManager;
import com.appartoo.utils.RestService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexandre on 16-08-17.
 */
public class ConversationsAdapter extends BaseAdapter {

    private SimpleDateFormat simpleDateFormat;
    private ArrayList<ConversationModel> conversationModels;
    private Context context;
    private LayoutInflater layoutInflater;
    private RestService restService;

    public ConversationsAdapter (Context context, ArrayList<ConversationModel> conversationModels) {
        this.context = context;
        this.conversationModels = conversationModels;
        this.layoutInflater = LayoutInflater.from(context);
        this.simpleDateFormat = new SimpleDateFormat("EEE MMM d, HH:mm", Locale.getDefault());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restService = retrofit.create(RestService.class);
    }

    @Override
    public int getCount() {
        return conversationModels.size();
    }

    @Override
    public Object getItem(int i) {
        return conversationModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.list_item_conversation_snippet, null);
            holder.userPicture = (ImageView) convertView.findViewById(R.id.conversationUserProfilePic);
            holder.firstname = (TextView) convertView.findViewById(R.id.conversationUserName);
            holder.snippet = (TextView) convertView.findViewById(R.id.conversationSnippet);
            holder.lastMessageHour = (TextView) convertView.findViewById(R.id.conversationLastMessageHour);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ConversationModel conversation = conversationModels.get(i);
        MessageModel lastMessage = conversation.getLastMessage();
        String ownerName = conversation.getOwnerName();

        holder.lastMessageHour.setText(simpleDateFormat.format(conversation.getLastMessageDate()));
        holder.firstname.setText(conversation.getOwnerName());
        if(lastMessage != null) holder.snippet.setText(lastMessage.getMessage()); else holder.snippet.setText("");
        getUserPicture(holder, conversation.getOwner());

        return convertView;
    }

    private void getUserPicture(final ViewHolder holder, String userId){
        System.out.println("Retrieving /profiles/4");

        Call<UserProfileModel> callback = restService.getUserProfile("/profiles/" + userId);

        callback.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if(response.isSuccessful()) {
                    UserModel user = response.body();

                    if (user.getImage().getThumbnail() != null) {
                        ImageManager.downloadPictureIntoView(context, holder.userPicture, user.getImage().getThumbnail().getContentUrl(), true);
                    } else if (!user.getImage().getContentUrl().equals("images/profile.png")) {
                        ImageManager.downloadPictureIntoView(context, holder.userPicture, user.getImage().getContentUrl(), true);
                    } else {
                        holder.userPicture.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_profile_picture));
                    }
                } else {
                    holder.userPicture.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_profile_picture));
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                t.printStackTrace();
                holder.userPicture.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_profile_picture));
            }
        });
    }

    private static class ViewHolder{
        public ImageView userPicture;
        public TextView firstname;
        public TextView snippet;
        public TextView lastMessageHour;
    }
}
