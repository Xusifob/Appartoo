package com.appartoo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.activity.AddOfferActivity;
import com.appartoo.adapter.OffersAndProfilesAdapter;
import com.appartoo.model.ObjectHolderModel;
import com.appartoo.model.ObjectHolderModelReceiver;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;

import java.util.ArrayList;

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
    private ProgressBar progressBar;
    private int nextPage;
    private int pageNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshOffers);
        offersAndProfiles = (RecyclerView) view.findViewById(R.id.offersList);
        addOfferButton = (FloatingActionButton) view.findViewById(R.id.offerListAddOfferButton);

        offersAndProfilesList = new ArrayList<>();
        offersAndProfilesAdapter = new OffersAndProfilesAdapter(getActivity(), offersAndProfilesList);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        offersAndProfiles.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        offersAndProfiles.setLayoutManager(linearLayoutManager);


        if (container != null) {
            container.removeAllViews();
        }

        pageNumber = 0;

        return view;
    }

    @Override
    public void onStart() {

        progressBar.setIndeterminate(true);
        addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), AddOfferActivity.class));
            }
        });

        offersAndProfiles.setAdapter(offersAndProfilesAdapter);

        if(offersAndProfilesAdapter.getItemCount() == 0) {
            getOffersAndProfiles(pageNumber);
        }

        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void refreshOffers() {
        swipeRefreshLayout.setRefreshing(true);
        pageNumber = 0;
        getOffersAndProfiles(pageNumber);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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

        String token;
        if(Appartoo.TOKEN != null) token = "Bearer " + Appartoo.TOKEN;
        else token = "";

        Call<ObjectHolderModelReceiver> callback = restService.getOffersOrProfiles(token, page*20, 20);

        callback.enqueue(new Callback<ObjectHolderModelReceiver>(){
            @Override
            public void onResponse(Call<ObjectHolderModelReceiver> call, Response<ObjectHolderModelReceiver> response) {

                if(response.isSuccessful()) {

                    if(swipeRefreshLayout.isRefreshing()) {
                        offersAndProfilesList.clear();
                    }

                    offersAndProfilesList.addAll(response.body().getHits());
                    offersAndProfilesAdapter.notifyDataSetChanged();

                    System.out.println(offersAndProfilesList.size());
                    System.out.println(offersAndProfilesAdapter.getItemCount());

                    nextPage = page + 1;

                    if(nextPage * 20 >= response.body().getTotal()) {
                        progressBar.setVisibility(View.GONE);
                        nextPage = -1;
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    System.out.println(response.code());
                    try {
                        System.out.println(response.errorBody().string());
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

                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }

                t.printStackTrace();
                Toast.makeText(getActivity(),R.string.connection_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
