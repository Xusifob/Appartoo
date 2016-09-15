package com.appartoo.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.addoffer.AddModifyOfferActivity;
import com.appartoo.utils.adapter.OffersAdapter;
import com.appartoo.utils.model.OfferModelWithDetailledDate;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfileOffersFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView offersListView;
    private ArrayList<OfferModelWithDetailledDate> offersList;
    private OffersAdapter offersAdapter;
    private FloatingActionButton addOfferButton;
    private Integer position;
    private Integer offset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers_and_profiles_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshOffers);
        offersListView = (RecyclerView) view.findViewById(R.id.offersList);
        addOfferButton = (FloatingActionButton) view.findViewById(R.id.offerListaddOfferButton);

        offersList = new ArrayList<>();
        offersAdapter = new OffersAdapter(getActivity(), offersList);

        offersListView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        offersListView.setLayoutManager(linearLayoutManager);

        if (container != null) {
            container.removeAllViews();
        }

        return view;
    }

    @Override
    public void onStart() {
        offersListView.setAdapter(offersAdapter);

        getOffers();

        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        position = ((LinearLayoutManager) offersListView.getLayoutManager()).findLastVisibleItemPosition();
        if(offersListView.findViewHolderForAdapterPosition(position) != null) offset = offersListView.findViewHolderForAdapterPosition(position).itemView.getTop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(offset != null && position != null)
            ((LinearLayoutManager) offersListView.getLayoutManager()).scrollToPositionWithOffset(position, offset);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), AddModifyOfferActivity.class));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if(Appartoo.LOGGED_USER_PROFILE != null) {
                    getOffers();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "Impossible de récupérer vos offres.", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void getOffers(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyy-MM-dd HH:mm:ss.SSSSSS")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestService restService = retrofit.create(RestService.class);
        Call<ArrayList<OfferModelWithDetailledDate>> callback = restService.getLoggedUserOffers("Bearer " + Appartoo.TOKEN);

        callback.enqueue(new Callback<ArrayList<OfferModelWithDetailledDate>>(){
            @Override
            public void onResponse(Call<ArrayList<OfferModelWithDetailledDate>> call, Response<ArrayList<OfferModelWithDetailledDate>> response) {
                offersAdapter.setFooterVisibility(View.GONE);

                if(response.isSuccessful()) {
                    offersList.clear();
                    offersList.addAll(response.body());
                    offersAdapter.notifyDataSetChanged();
                } else {
                    try  {
                        Log.v("UserProfileOffersFragme", "getOffers: " + String.valueOf(response.code()));
                        Log.v("UserProfileOffersFragme", "getOffers: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(),R.string.connection_error, Toast.LENGTH_SHORT).show();
                }

                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OfferModelWithDetailledDate>> call, Throwable t) {

                offersAdapter.setFooterVisibility(View.GONE);

                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.v("UserProfileOffersFragme", "getOffers: " + t.getMessage());
            }
        });
    }
}