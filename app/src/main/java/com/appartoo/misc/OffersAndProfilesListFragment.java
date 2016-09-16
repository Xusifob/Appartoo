package com.appartoo.misc;

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
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.addoffer.AddModifyOfferActivity;
import com.appartoo.utils.adapter.OffersAndProfilesAdapter;
import com.appartoo.utils.model.ObjectHolderModel;
import com.appartoo.utils.model.ObjectHolderModelReceiver;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexandre on 16-07-13.
 */
public class OffersAndProfilesListFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView offersAndProfiles;
    private ArrayList<ObjectHolderModel> offersAndProfilesList;
    private OffersAndProfilesAdapter offersAndProfilesAdapter;
    private FloatingActionButton addOfferButton;
    private HashMap<String, Object> query;
    private int nextPage;
    private int pageNumber;
    private Integer offset;
    private Integer position;
    private boolean isLoading;
    private LinearLayoutManager linearLayoutManager;

    public static int LIMIT = 30;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers_and_profiles_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshOffers);
        offersAndProfiles = (RecyclerView) view.findViewById(R.id.offersList);
        addOfferButton = (FloatingActionButton) view.findViewById(R.id.offerListaddOfferButton);

        offersAndProfilesList = new ArrayList<>();
        offersAndProfilesAdapter = new OffersAndProfilesAdapter(getActivity(), offersAndProfilesList);

        offersAndProfiles.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        offersAndProfiles.setLayoutManager(linearLayoutManager);
        isLoading = false;

        query = (HashMap<String, Object>) getActivity().getIntent().getSerializableExtra("query");
        if(query == null) {
            query = new HashMap<>();
            query.put("limit", LIMIT);
        }

        if (container != null) {
            container.removeAllViews();
        }

        offersAndProfiles.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Integer currentPosition = linearLayoutManager.findLastVisibleItemPosition();

                if(offersAndProfilesAdapter.getItemCount() - currentPosition < 2 && nextPage > 0 && !isLoading) {
                    isLoading = true;
                    getOffersAndProfiles(nextPage);
                }
            }
        });

        pageNumber = 0;

        return view;
    }

    @Override
    public void onStart() {

        if(Appartoo.TOKEN == null || Appartoo.TOKEN.equals("")) addOfferButton.setVisibility(View.GONE);
        addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), AddModifyOfferActivity.class));
            }
        });

        offersAndProfiles.setAdapter(offersAndProfilesAdapter);
        if(offersAndProfilesAdapter.getItemCount() == 1) {
            getOffersAndProfiles(pageNumber);
        }

        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        position = linearLayoutManager.findLastVisibleItemPosition();
        if(offersAndProfiles.findViewHolderForAdapterPosition(position) != null) {
            offset = offersAndProfiles.findViewHolderForAdapterPosition(position).itemView.getTop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(offset != null && position != null)
            linearLayoutManager.scrollToPositionWithOffset(position, offset);
    }

    public void refreshOffers() {
        swipeRefreshLayout.setRefreshing(true);
        position = 0;
        offset = 0;
        pageNumber = 0;
        getOffersAndProfiles(pageNumber);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        offersAndProfiles.clearOnScrollListeners();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offersAndProfilesAdapter.setFooterVisibility(View.VISIBLE);
                refreshOffers();
            }
        });
    }

    private void getOffersAndProfiles(final int page){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestService restService = retrofit.create(RestService.class);

        query.put("start", page*LIMIT);

        String token;
        if(Appartoo.TOKEN != null) token = "Bearer " + Appartoo.TOKEN;
        else token = "";

        System.out.println(query);
        Call<ObjectHolderModelReceiver> callback = restService.getOffersOrProfiles(token, query);

        callback.enqueue(new Callback<ObjectHolderModelReceiver>(){
            @Override
            public void onResponse(Call<ObjectHolderModelReceiver> call, Response<ObjectHolderModelReceiver> response) {

                isLoading = false;

                if(response.isSuccessful()) {

                    if(swipeRefreshLayout.isRefreshing()) {
                        offersAndProfilesList.clear();
                    }

                    offersAndProfilesList.addAll(response.body().getHits());
                    offersAndProfilesAdapter.notifyDataSetChanged();

                    nextPage = page + 1;

                    if(nextPage * LIMIT >= response.body().getTotal()) {
                        nextPage = -1;
                        offersAndProfilesAdapter.setFooterVisibility(View.GONE);
                    }
                } else {

                    nextPage = -1;

                    try {
                        Log.v("OffersAndProfilesListFr", "getOffersAndProfiles: " + String.valueOf(response.code()));
                        Log.v("OffersAndProfilesListFr", "getOffersAndProfiles: " + response.errorBody().string());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                }

                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ObjectHolderModelReceiver> call, Throwable t) {

                nextPage = -1;

                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }

                Log.v("OffersAndProfilesListFr", "getOffersAndProfiles: " + t.getMessage());
                Toast.makeText(getActivity(),R.string.connection_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
