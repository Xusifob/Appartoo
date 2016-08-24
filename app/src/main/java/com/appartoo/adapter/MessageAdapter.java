package com.appartoo.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.model.ConversationModel;
import com.appartoo.model.MessageModel;
import com.appartoo.model.UserModel;
import com.appartoo.model.UserProfileModel;
import com.appartoo.utils.DateManager;
import com.appartoo.utils.ImageManager;
import com.appartoo.utils.RestService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexandre on 16-08-17.
 */
public class MessageAdapter extends BaseAdapter {

    private ArrayList<MessageModel> messageModels;
    private Context context;
    private LayoutInflater layoutInflater;
    final private String userId;

    public MessageAdapter(Context context, ArrayList<MessageModel> messageModels, final String userId) {
        this.context = context;
        this.messageModels = messageModels;
        this.layoutInflater = LayoutInflater.from(context);
        this.userId = userId;
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

        if(isMe) {
            holder.myMessage.setText(messageModel.getMessage());
            holder.myPicture.setBackgroundResource(R.drawable.circle_light_gray);
            holder.myMessageHour.setText(DateManager.getFormattedDate(messageModel.getCreatedTime()));
            holder.myMessageContainer.setVisibility(View.VISIBLE);
            holder.othersPicture.setBackgroundResource(R.color.colorTransparent);
            holder.othersMessageContainer.setVisibility(View.GONE);
        } else {
            holder.othersMessage.setText(messageModel.getMessage());
            holder.othersPicture.setBackgroundResource(R.drawable.circle_light_gray);
            holder.othersMessageHour.setText(DateManager.getFormattedDate(messageModel.getCreatedTime()));
            holder.othersMessageContainer.setVisibility(View.VISIBLE);
            holder.myPicture.setBackgroundResource(R.color.colorTransparent);
            holder.myMessageContainer.setVisibility(View.GONE);
        }

        return convertView;
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
