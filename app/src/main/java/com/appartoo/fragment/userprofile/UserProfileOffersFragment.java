package com.appartoo.fragment.userprofile;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.activity.AddOfferActivity;
import com.appartoo.activity.OfferDetailsActivity;
import com.appartoo.adapter.OffersAdapter;
import com.appartoo.adapter.OffersAndProfilesAdapter;
import com.appartoo.model.OfferModel;
import com.appartoo.model.OfferModelWithDetailledDate;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    private ProgressBar progressBar;
    private FloatingActionButton addOfferButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshOffers);
        offersListView = (RecyclerView) view.findViewById(R.id.offersList);
        addOfferButton = (FloatingActionButton) view.findViewById(R.id.offerListAddOfferButton);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

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
        progressBar.setIndeterminate(true);
        offersListView.setAdapter(offersAdapter);

        getOffers();

        super.onStart();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), AddOfferActivity.class));
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
        Call<ArrayList<OfferModelWithDetailledDate>> callback = restService.getUserOffers(RestService.REST_URL + Appartoo.LOGGED_USER_PROFILE.getId() + "/offers");

        callback.enqueue(new Callback<ArrayList<OfferModelWithDetailledDate>>(){
            @Override
            public void onResponse(Call<ArrayList<OfferModelWithDetailledDate>> call, Response<ArrayList<OfferModelWithDetailledDate>> response) {
                progressBar.setVisibility(View.GONE);

                if(response.isSuccessful()) {
                    System.out.println(response.body().size());

                    offersList.clear();
                    offersList.addAll(response.body());
                    offersAdapter.notifyDataSetChanged();
                } else {
                    System.out.println(response.code());
                    Toast.makeText(getActivity(),R.string.connection_error, Toast.LENGTH_SHORT).show();
                }

                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OfferModelWithDetailledDate>> call, Throwable t) {

                progressBar.setVisibility(View.GONE);

                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }

                t.printStackTrace();
                if(t instanceof IllegalStateException) {
                    Toast.makeText(getActivity(), R.string.no_offer_found, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}