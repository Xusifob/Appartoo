package com.appartoo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.appartoo.R;
import com.appartoo.activity.AddOfferActivity;
import com.appartoo.activity.OfferDetailsActivity;
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
public class OffersListFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView offersListView;

    private ArrayList<ObjectHolderModel> offersAndProfilesList;
    private OffersAndProfilesAdapter offersAndProfilesAdapter;
    private View progressBar;
    private int nextPage;
    private int pageNumber;
    private boolean isLoading;
    private FloatingActionButton addOfferButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshOffers);
        offersListView = (ListView) view.findViewById(R.id.offersList);
        addOfferButton = (FloatingActionButton) view.findViewById(R.id.offerListAddOfferButton);

        progressBar = inflater.inflate(R.layout.progress_bar_list_view, container, false);

        offersAndProfilesList = new ArrayList<>();
        offersAndProfilesAdapter = new OffersAndProfilesAdapter(getActivity(), offersAndProfilesList);
        isLoading = true;

        if (container != null) {
            container.removeAllViews();
        }

        pageNumber = 0;

        return view;
    }

    @Override
    public void onStart() {

        addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), AddOfferActivity.class));
            }
        });

        offersListView.setAdapter(offersAndProfilesAdapter);
        if(offersListView.getFooterViewsCount() == 0 && nextPage >= 0) {
            offersListView.addFooterView(progressBar);
        }

        if(offersAndProfilesAdapter.getCount() == 0) {
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
        offersListView.post(new Runnable() {

            @Override
            public void run() {
                offersListView.setSelection(0);
            }
        });
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
        offersListView.addFooterView(progressBar);
        offersListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem + visibleItemCount >= totalItemCount -5 && totalItemCount != 0)
                {
                    if(!isLoading && nextPage > 0) {
                        isLoading = true;
                        getOffersAndProfiles(nextPage);
                    }
                }
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

                isLoading = false;

                if(response.isSuccessful()) {

                    if(swipeRefreshLayout.isRefreshing()) {
                        offersAndProfilesList.clear();
                    }

                    System.out.println(response.body().getHits());
                    offersAndProfilesList.addAll(response.body().getHits());
                    offersAndProfilesAdapter.notifyDataSetChanged();

                    System.out.println(offersAndProfilesAdapter.getCount());

                    nextPage = page + 1;

                    if(nextPage * 20 >= response.body().getTotal()) {
                        nextPage = -1;
                        offersListView.removeFooterView(progressBar);
                    }
                } else {
                    System.out.println(response.code());
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (Exception e){

                    }
                    if(pageNumber == 1) {
                        Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.no_offer_found, Toast.LENGTH_SHORT).show();
                    }
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
