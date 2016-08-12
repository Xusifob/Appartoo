package mobile.appartoo.fragment;

import android.app.admin.SystemUpdatePolicy;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import mobile.appartoo.R;
import mobile.appartoo.activity.AddOfferActivity;
import mobile.appartoo.activity.OfferDetailsActivity;
import mobile.appartoo.adapter.OffersAdapter;
import mobile.appartoo.model.OfferModel;
import mobile.appartoo.model.OfferModelWithDate;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.RestService;
import mobile.appartoo.utils.ServerResponse;
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

    private ArrayList<OfferModel> offersList;
    private OffersAdapter offersAdapter;
    private View progressBar;
    private ProgressBar moreOfferProgress;
    private String nextPage;
    private int offerPage;
    private FloatingActionButton addOfferButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshOffers);
        offersListView = (ListView) view.findViewById(R.id.offersList);
        addOfferButton = (FloatingActionButton) view.findViewById(R.id.offerListAddOfferButton);

        progressBar = inflater.inflate(R.layout.progress_bar_list_view, container, false);
        moreOfferProgress = (ProgressBar) progressBar.findViewById(R.id.footerListBar);

        offersList = new ArrayList<>();
        offersAdapter = new OffersAdapter(getActivity(), offersList);

        if (container != null) {
            container.removeAllViews();
        }

        offerPage = 1;

        return view;
    }

    @Override
    public void onStart() {
        moreOfferProgress.setIndeterminate(true);
        offersListView.addFooterView(progressBar);

        addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), AddOfferActivity.class));
            }
        });
        offersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OfferDetailsActivity.class);
                intent.putExtra("offer", offersList.get(position));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        offersListView.setAdapter(offersAdapter);

        if(offersAdapter.getCount() == 0) {
            getOffers(offerPage);
        }

        super.onStart();
    }

    public void refreshOffers() {
        swipeRefreshLayout.setRefreshing(true);
        offersListView.post(new Runnable() {

            @Override
            public void run() {
                offersListView.setSelection(0);
            }
        });
        offerPage = 1;
        getOffers(offerPage);

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

        offersListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount >= totalItemCount-5 && nextPage != null) {
                    offerPage++;
                    getOffers(offerPage);
                } else if(nextPage == null) {
                    moreOfferProgress.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getOffers(int page){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestService restService = retrofit.create(RestService.class);
        Call<ServerResponse<ArrayList<OfferModelWithDate>>> callback = restService.getOffers(page);

        callback.enqueue(new Callback<ServerResponse<ArrayList<OfferModelWithDate>>>(){
            @Override
            public void onResponse(Call<ServerResponse<ArrayList<OfferModelWithDate>>> call, Response<ServerResponse<ArrayList<OfferModelWithDate>>> response) {

                if(response.isSuccessful()) {
                    if(swipeRefreshLayout.isRefreshing()) {
                        offersList.clear();
                    }
                    offersList.addAll(response.body().getData());
                    offersAdapter.notifyDataSetChanged();
                    nextPage = response.body().getNextPage();

                } else {
                    System.out.println(response.code());
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (Exception e){

                    }
                    Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                }

                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<ArrayList<OfferModelWithDate>>> call, Throwable t) {

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
