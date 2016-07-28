package mobile.appartoo.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import mobile.appartoo.R;
import mobile.appartoo.activity.OfferDetailActivity;
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
    private ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshOffers);
        offersListView = (ListView) view.findViewById(R.id.offersList);

        offersList = new ArrayList<>();
        offersAdapter = new OffersAdapter(getActivity(), offersList);
        progress = new ProgressDialog(getActivity());

        if (container != null) {
            container.removeAllViews();
        }

        return view;
    }

    @Override
    public void onStart() {
        progress.setTitle(getResources().getString(R.string.progress_bar_title));
        progress.setMessage(getResources().getString(R.string.progress_bar_description));

        offersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
                intent.putExtra("offer", offersList.get(position));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        offersListView.setAdapter(offersAdapter);

        if(offersAdapter.getCount() == 0) {
            getOffers();
        }

        super.onStart();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {

        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getOffers();
            }
        });
    }

    private void getOffers(){
        if(!swipeRefreshLayout.isRefreshing()){
            progress.show();
        }



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestService restService = retrofit.create(RestService.class);
        Call<ServerResponse<ArrayList<OfferModelWithDate>>> callback = restService.getOffers();

        callback.enqueue(new Callback<ServerResponse<ArrayList<OfferModelWithDate>>>(){
            @Override
            public void onResponse(Call<ServerResponse<ArrayList<OfferModelWithDate>>> call, Response<ServerResponse<ArrayList<OfferModelWithDate>>> response) {

                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    progress.dismiss();
                }

                if(response.isSuccessful()) {
                    offersList.clear();
                    offersList.addAll(response.body().getData());
                    offersAdapter.notifyDataSetChanged();
                } else {
                    System.out.println(response.code());
                    Toast.makeText(getActivity(), "Erreur de connection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<ArrayList<OfferModelWithDate>>> call, Throwable t) {
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    progress.dismiss();
                }
                t.printStackTrace();
                Toast.makeText(getActivity(), "Erreur de connection avec le serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
    }


//    public String loadJSONFromAsset() {
//        String json = null;
//        try {
//
//            InputStream is = getActivity().getAssets().open("data.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//
//    }
}
