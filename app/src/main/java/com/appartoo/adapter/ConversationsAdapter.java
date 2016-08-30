package com.appartoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.model.ConversationModel;
import com.appartoo.model.MessageModel;
import com.appartoo.model.OfferModelWithDate;
import com.appartoo.model.UserModel;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.ImageManager;
import com.appartoo.utils.RestService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
    private RestService restService;
    private static HashMap<String, String> offerPictures;
    private static HashMap<String, String> ownerPictures;

    public ConversationsAdapter (Context context, ArrayList<ConversationModel> conversationModels) {
        this.context = context;
        this.conversationModels = conversationModels;
        this.layoutInflater = LayoutInflater.from(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restService = retrofit.create(RestService.class);

        if(offerPictures == null) offerPictures = new HashMap<>();
        if(ownerPictures == null) ownerPictures = new HashMap<>();
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

        if(holder.userPicture.getDrawable() == null && conversation.getType() != null) {
            if (conversation.getType() == 2) {
                if(offerPictures.get(conversation.getOffer().get("id")) == null) {
                    retrieveOfferPicture(holder.userPicture, conversation.getOffer().get("id"));
                }
                else {
                    ImageManager.downloadPictureIntoView(context, holder.userPicture, offerPictures.get(conversation.getOffer().get("id")), ImageManager.TRANFORM_SQUARE);
                }
            } else if (conversation.getType() == 1) {
                if(ownerPictures.get(conversation.getOwner()) == null) retrieveOwnerPicture(holder.userPicture, conversation.getOwner());
                else ImageManager.downloadPictureIntoView(context, holder.userPicture, ownerPictures.get(conversation.getOwner()), ImageManager.TRANFORM_SQUARE);
            }
        }

        if(lastMessage != null) holder.snippet.setText(lastMessage.getMessage()); else holder.snippet.setText("");

        return convertView;
    }

    private static class ViewHolder{
        public ImageView userPicture;
        public TextView name;
        public TextView snippet;
        public TextView lastMessageHour;
    }

    public void retrieveOfferPicture(final ImageView pic, String id) {
        if (id != null && !id.equals("")) {
            Call<OfferModelWithDate> callback = restService.getOfferById(RestService.REST_URL + "/offers/" + id);

            callback.enqueue(new Callback<OfferModelWithDate>() {
                @Override
                public void onResponse(Call<OfferModelWithDate> call, Response<OfferModelWithDate> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getImages().size() > 0) {
                            ImageManager.downloadPictureIntoView(context, pic, response.body().getImages().get(0).getContentUrl(), ImageManager.TRANFORM_SQUARE);
                        }
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
                public void onFailure(Call<OfferModelWithDate> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void retrieveOwnerPicture(final ImageView pic, String id) {
        if (id != null && !id.equals("")) {
            Call<UserModel> callback = restService.getUserInformationsById(RestService.REST_URL + "/profiles/" + id);

            callback.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        ImageManager.downloadPictureIntoView(context, pic, response.body().getImage().getContentUrl(), ImageManager.TRANFORM_SQUARE);
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
    }
}
