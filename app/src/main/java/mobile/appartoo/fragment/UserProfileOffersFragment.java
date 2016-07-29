package mobile.appartoo.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.util.ArrayList;

import mobile.appartoo.R;
import mobile.appartoo.activity.OfferDetailActivity;
import mobile.appartoo.adapter.OffersAdapter;
import mobile.appartoo.model.OfferModel;
import mobile.appartoo.model.OfferModelWithDetailledDate;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.RestService;
import okhttp3.ResponseBody;
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
            if(Appartoo.LOGGED_USER_PROFILE != null) {
                getOffers();
            }
        }

        super.onStart();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        if(!swipeRefreshLayout.isRefreshing()){
            progress.show();
        }

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

                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    progress.dismiss();
                }

                if(response.isSuccessful()) {
                    System.out.println(response.body().size());

                    offersList.clear();
                    offersList.addAll(response.body());
                    offersAdapter.notifyDataSetChanged();
                } else {
                    System.out.println(response.code());
                    Toast.makeText(getActivity(), "Erreur de connection.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OfferModelWithDetailledDate>> call, Throwable t) {
                t.printStackTrace();
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    progress.dismiss();
                }
                Toast.makeText(getActivity(), "Erreur de connection avec le serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }
}