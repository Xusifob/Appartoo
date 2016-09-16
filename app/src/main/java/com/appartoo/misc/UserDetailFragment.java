package com.appartoo.misc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;
import com.appartoo.utils.adapter.CommentsAdapter;
import com.appartoo.utils.model.CommentModel;
import com.appartoo.utils.model.CommentsReceiver;
import com.appartoo.utils.model.HasLikedReceiver;
import com.appartoo.utils.model.UserModel;
import com.appartoo.utils.TextValidator;
import com.appartoo.utils.view.NonScrollableListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexandre on 16-07-18.
 */
public class UserDetailFragment extends Fragment {

    private TextView userContractAndSociety;
    private TextView userAge;
    private TextView userDescription;
    private TextView userName;
    private ImageView userIsSmoker;
    private ImageView userIsInRelationship;
    private ImageView userIsWorker;
    private ImageView likeButton;
    private RestService restService;
    private NonScrollableListView listView;
    private Integer userId;
    private TextView userRecommandation;
    private Integer plus;
    private CommentsAdapter adapter;
    private View commentSeparator;
    private ArrayList<CommentModel> comments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_detail, container, false);

        userContractAndSociety = (TextView) rootView.findViewById(R.id.userDetailContractAndSociety);
        userAge = (TextView) rootView.findViewById(R.id.userDetailAge);
        userName = (TextView) rootView.findViewById(R.id.userDetailName);
        userDescription = (TextView) rootView.findViewById(R.id.userDetailDescription);
        userIsSmoker = (ImageView) rootView.findViewById(R.id.userDetailSmoker);
        userIsInRelationship = (ImageView) rootView.findViewById(R.id.userDetailRelationship);
        userIsWorker = (ImageView) rootView.findViewById(R.id.userDetailIsWorker);
        likeButton = (ImageView) rootView.findViewById(R.id.likeButton);
        userRecommandation = (TextView) rootView.findViewById(R.id.userDetailRecommendation);
        listView = (NonScrollableListView) rootView.findViewById(R.id.userDetailComments);
        commentSeparator = rootView.findViewById(R.id.commentSeparator);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restService = retrofit.create(RestService.class);

        return rootView;
    }

    public void bindData(UserModel user){
        userName.setText(user.getGivenName());
        userId = user.getIdNumber();
        if(user.getPlus() == null) userRecommandation.setText("0");
        else userRecommandation.setText(String.valueOf(user.getPlus()));

        comments = new ArrayList<>();
        adapter = new CommentsAdapter(getActivity(), comments);
        listView.setAdapter(adapter);

        String job = "";
        if(user.getSociety() != null) job += user.getSociety() + ", ";
        if(user.getSociety() != null) job += user.getFunction() + ", ";
        if (user.getContract() != null) job += user.getContract();
        else if(job.length() > 2) job = job.substring(0, job.length()-2);

        if(!TextValidator.haveText(job)) userContractAndSociety.setText(R.string.no_job_refered);
        else userContractAndSociety.setText(job);

        if(user.getAge() > 0) {
            userAge.setText(Integer.toString(user.getAge()) + " " + getResources().getString(R.string.year_age));
        }

        if (user.getDescription() == null || !TextValidator.haveText(user.getDescription())) userDescription.setText(R.string.no_description);
        else userDescription.setText(user.getDescription());

        if(user.getSmoker() != null && user.getSmoker()) userIsSmoker.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.is_smoker_big, null));
        else if(user.getSmoker() != null) userIsSmoker.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.is_not_smoker_big, null));

        if(user.getInRelationship() != null && user.getInRelationship()) userIsInRelationship.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.couple_big, null));
        else if (user.getInRelationship() != null) userIsInRelationship.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.single_big, null));

        if(user.getContract() != null && user.getContract().equals("worker")) userIsWorker.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.worker_big, null));

        if(Appartoo.TOKEN != null && !Appartoo.TOKEN.equals("") && Appartoo.LOGGED_USER_PROFILE != null) defineLikeButton();

        getComments();
    }

    private void defineLikeButton() {

        String url = RestService.REST_URL + "/profiles/" + userId + "/recommendations/" + Appartoo.LOGGED_USER_PROFILE.getIdNumber();
        Call<HasLikedReceiver> call = restService.hasLiked(url, "Bearer " + Appartoo.TOKEN);
        call.enqueue(new Callback<HasLikedReceiver>() {
                @Override
                public void onResponse(Call<HasLikedReceiver> call, Response<HasLikedReceiver> response) {
                    if(response.isSuccessful()) {
                        if(response.body().getResult()) {
                            likeButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.thumb_up_big, null));
                        } else {
                            likeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    likeProfile();
                                    likeButton.setEnabled(false);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<HasLikedReceiver> call, Throwable t) {

                }
            });
    }


    private void getComments() {
        Call<CommentsReceiver> commentCallback = restService.getUserComments(RestService.REST_URL + "/opinion/" + userId);
        commentCallback.enqueue(new Callback<CommentsReceiver>() {
            @Override
            public void onResponse(Call<CommentsReceiver> call, Response<CommentsReceiver> response) {
                if(response.isSuccessful()) {
                    if(response.body().getHits().size() > 0) {
                        commentSeparator.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        comments.clear();
                        comments.addAll(response.body().getHits());
                        adapter.notifyDataSetChanged();
                    }

                    System.out.println(adapter.getCount());
                } else {
                    Toast.makeText(getActivity(), response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CommentsReceiver> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        plus = Integer.valueOf(userRecommandation.getText().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(plus != null) userRecommandation.setText(String.valueOf(plus));
    }

    private void likeProfile() {

        String url = RestService.REST_URL + "/profiles/" + userId + "/recommendations/" + Appartoo.LOGGED_USER_PROFILE.getIdNumber();

        Call<ResponseBody> callback = restService.likeProfile(url, "Bearer " + Appartoo.TOKEN);
        likeButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.thumb_up_big, null));
        userRecommandation.setText(String.valueOf(Integer.valueOf(userRecommandation.getText().toString()) + 1));

        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()) {
                    likeButton.setEnabled(true);
                    Toast.makeText(getActivity().getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                    likeButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.thumb_up_gray_big, null));
                    userRecommandation.setText(String.valueOf(Integer.valueOf(userRecommandation.getText().toString()) - 1));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                likeButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.thumb_up_gray_big, null));
                userRecommandation.setText(String.valueOf(Integer.valueOf(userRecommandation.getText().toString()) - 1));
            }
        });
    }
}
