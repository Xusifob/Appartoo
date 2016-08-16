package com.appartoo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import com.appartoo.R;
import com.appartoo.activity.AddOfferActivity;
import com.appartoo.activity.OfferDetailsActivity;
import com.appartoo.adapter.OffersAdapter;
import com.appartoo.model.OfferModel;
import com.appartoo.model.OfferModelWithDetailledDate;
import com.appartoo.utils.Appartoo;
import com.appartoo.utils.RestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfileOffersFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView offersListView;
    private ArrayList<OfferModel> offersList;
    private OffersAdapter offersAdapter;
    private View progressBar;
    private ProgressBar moreOfferProgress;
    private FloatingActionButton addOfferButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshOffers);
        offersListView = (ListView) view.findViewById(R.id.offersList);
        addOfferButton = (FloatingActionButton) view.findViewById(R.id.offerListAddOfferButton);

        offersList = new ArrayList<>();
        offersAdapter = new OffersAdapter(getActivity(), offersList);

        progressBar = inflater.inflate(R.layout.progress_bar_list_view, container, false);
        moreOfferProgress = (ProgressBar) progressBar.findViewById(R.id.footerListBar);

        if (container != null) {
            container.removeAllViews();
        }

        return view;
    }

    @Override
    public void onStart() {
        moreOfferProgress.setIndeterminate(true);

        offersListView.setAdapter(offersAdapter);
        offersListView.addFooterView(progressBar);
        offersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OfferDetailsActivity.class);
                intent.putExtra("offer", offersList.get(position));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        if(offersAdapter.getCount() == 0) {
            if(Appartoo.LOGGED_USER_PROFILE != null) {
                getOffers();
            }
        }

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
        Call<ArrayList<OfferModelWithDetailledDate>> callback = restService.getUserOffers(Appartoo.LOGGED_USER_PROFILE.getId() + "/offers");

        callback.enqueue(new Callback<ArrayList<OfferModelWithDetailledDate>>(){
            @Override
            public void onResponse(Call<ArrayList<OfferModelWithDetailledDate>> call, Response<ArrayList<OfferModelWithDetailledDate>> response) {

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
                } else {
                    moreOfferProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OfferModelWithDetailledDate>> call, Throwable t) {

                moreOfferProgress.setVisibility(View.GONE);

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