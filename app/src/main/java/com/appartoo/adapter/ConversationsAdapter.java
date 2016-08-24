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
import com.appartoo.model.ConversationModel;
import com.appartoo.model.MessageModel;
import com.appartoo.model.UserModel;
import com.appartoo.model.UserProfileModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.ImageManager;
import com.appartoo.utils.RestService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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

    private ArrayList<ConversationModel> conversationModels;
    private Context context;
    private LayoutInflater layoutInflater;

    public ConversationsAdapter (Context context, ArrayList<ConversationModel> conversationModels) {
        this.context = context;
        this.conversationModels = conversationModels;
        this.layoutInflater = LayoutInflater.from(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
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
            holder.name = (TextView) convertView.findViewById(R.id.conversationName);
            holder.snippet = (TextView) convertView.findViewById(R.id.conversationSnippet);
            holder.lastMessageHour = (TextView) convertView.findViewById(R.id.conversationLastMessageHour);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ConversationModel conversation = conversationModels.get(i);

        MessageModel lastMessage = conversation.getLastMessage();
        holder.lastMessageHour.setText(conversation.getLastMessageFormattedDate());
        holder.name.setText(conversation.getConversationName());

        if(lastMessage != null) holder.snippet.setText(lastMessage.getMessage()); else holder.snippet.setText("");
        if(holder.userPicture.getDrawable() == null) holder.userPicture.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_profile_picture));

        return convertView;
    }

    private static class ViewHolder{
        public ImageView userPicture;
        public TextView name;
        public TextView snippet;
        public TextView lastMessageHour;
    }
}
