package com.appartoo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.activity.MessageActivity;
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

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

/**
 * Created by alexandre on 16-07-05.
 */
public class ConversationsAdapter extends RecyclerView.Adapter {

    private ArrayList<ConversationModel> conversationModels;
    private Context context;
    private RestService restService;

    private static final HashMap<String, String> images = new HashMap<>();

    private static final int OFFER_TYPE = 0;
    private static final int CANDIDATE_TYPE = 2;
    private static final int PROFILE_TYPE = 1;

    public ConversationsAdapter(Context context, ArrayList<ConversationModel> conversationModels) {
        this.context = context;
        this.conversationModels = conversationModels;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restService = retrofit.create(RestService.class);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return conversationModels.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View offerView = LayoutInflater.from(context).inflate(R.layout.list_item_conversation_snippet, null);
        return new ConversationViewHolder(offerView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);
        final ConversationModel conversationModel = conversationModels.get(position);


        ((ConversationViewHolder) holder).userPicture.setImageDrawable(null);

        if (itemType == OFFER_TYPE) {
            ((ConversationViewHolder) holder).bindData(conversationModel, context);
            if(images.get("offer" + conversationModel.getOffer().get("id")) != null) {
                ((ConversationViewHolder) holder).bindPicture(images.get("offer" + conversationModel.getOffer().get("id")), context);
            } else {
                retrieveOfferPicture(((ConversationViewHolder) holder), conversationModel.getOffer().get("id"));
            }
        } else if (itemType == PROFILE_TYPE) {
            ((ConversationViewHolder) holder).bindData(conversationModel, context);
            for(String participant : conversationModel.getParticipants().keySet()) {
                if(!participant.equals(Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString())) {
                    if(images.get("profile" + participant) != null) {
                        ((ConversationViewHolder) holder).bindPicture(images.get("profile" + participant), context);
                    } else {
                        retrieveUserPicture(((ConversationViewHolder) holder), participant);
                    }
                    break;
                }
            }

        } else if (itemType == CANDIDATE_TYPE) {
            ((ConversationViewHolder) holder).bindData(conversationModel, context);
            for(String participant : conversationModel.getParticipants().keySet()) {
                if(!participant.equals(Appartoo.LOGGED_USER_PROFILE.getIdNumber().toString())) {
                    if(images.get("profile" + participant) != null) {
                        ((ConversationViewHolder) holder).bindPicture(images.get("profile" + participant), context);
                    } else {
                        retrieveUserPicture(((ConversationViewHolder) holder), participant);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        final ConversationModel conversationModel = conversationModels.get(position);

        if(conversationModel == null) {
            return PROFILE_TYPE;
        }

        if (conversationModel.getType() == 2) {
            return OFFER_TYPE;
        } else if (conversationModel.getType() == 1) {
            return CANDIDATE_TYPE;
        } else {
            return PROFILE_TYPE;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    static class ConversationViewHolder extends RecyclerView.ViewHolder {

        ImageView userPicture;
        TextView name;
        TextView snippet;
        TextView lastMessageHour;
        View itemView;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.userPicture = (ImageView) itemView.findViewById(R.id.conversationUserProfilePic);
            this.name = (TextView) itemView.findViewById(R.id.conversationName);
            this.snippet = (TextView) itemView.findViewById(R.id.conversationSnippet);
            this.lastMessageHour = (TextView) itemView.findViewById(R.id.conversationLastMessageHour);
        }

        public void bindData(final ConversationModel conversationModel, final Context context){

            MessageModel lastMessage = conversationModel.getLastMessage();
            lastMessageHour.setText(conversationModel.getLastMessageFormattedDate());
            name.setText(conversationModel.getConversationName());

            if (lastMessage != null) snippet.setText(lastMessage.getMessage());
            else snippet.setText("");

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, MessageActivity.class);

                    intent.putExtra("conversationName", conversationModel.getConversationName());
                    intent.putExtra("conversationId", conversationModel.getId());
                    intent.putExtra("conversationType", conversationModel.getType());
                    intent.putExtra("conversationOwner", conversationModel.getOwner());
                    intent.putExtra("conversationOffer", conversationModel.getOffer());
                    intent.putExtra("conversationStatus", conversationModel.getStatus());
                    context.startActivity(intent);
                }
            });
        }

        public void bindPicture(String url, Context context) {
            ImageManager.downloadPictureIntoView(context, userPicture, url, ImageManager.TRANFORM_SQUARE);
        }
    }

    public void retrieveOfferPicture(final ConversationViewHolder holder, final String id) {
        if (id != null && !id.equals("")) {
            Call<OfferModelWithDate> callback = restService.getOfferById(RestService.REST_URL + "/offers/" + id);

            System.out.println(RestService.REST_URL + "/offers/" + id);

            callback.enqueue(new Callback<OfferModelWithDate>() {
                @Override
                public void onResponse(Call<OfferModelWithDate> call, Response<OfferModelWithDate> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getImages().size() > 0) {
                            holder.bindPicture(response.body().getImages().get(0).getContentUrl(), context);
                            images.put("offer" + id, response.body().getImages().get(0).getContentUrl());
                        }
                    } else {
                        try {
                            Log.v("ConversationAdapter", "retrieveOfferPicture: " + String.valueOf(response.code()));
                            Log.v("ConversationAdapter", "retrieveOfferPicture: " + String.valueOf(response.errorBody().string()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<OfferModelWithDate> call, Throwable t) {
                    Log.v("ConversationAdapter", "retrieveOfferPicture: " + t.getMessage());
                }
            });
        }
    }

    public void retrieveUserPicture(final ConversationViewHolder holder, final String id) {
        if (id != null && !id.equals("")) {
            Call<UserModel> callback = restService.getUserInformationsById(RestService.REST_URL + "/profiles/" + id);

            callback.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        holder.bindPicture(response.body().getImage().getContentUrl(), context);
                        images.put("profile" + id, response.body().getImage().getContentUrl());
                    } else {
                        try {
                            Log.v("ConversationAdapter", "retrieveUserPicture: " + String.valueOf(response.code()));
                            Log.v("ConversationAdapter", "retrieveUserPicture: " + String.valueOf(response.errorBody().string()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Log.v("ConversationAdapter", "retrieveUserPicture: " + t.getMessage());
                }
            });
        }
    }
}
