package mobile.appartoo.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import mobile.appartoo.R;
import mobile.appartoo.activity.OfferDetailActivity;
import mobile.appartoo.adapter.OfferAdapter;
import mobile.appartoo.model.OfferModel;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.RestService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexandre on 16-07-13.
 */
public class OfferListFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView offersListView;
    private ArrayList<OfferModel> offersList;
    private ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offer_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshOffers);
        offersListView = (ListView) view.findViewById(R.id.offersList);
        offersList = new ArrayList<>();
        progress = new ProgressDialog(getActivity());
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
            }
        });

        if(offersList.size() == 0) {
            getOffers();
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
        Call<ResponseBody> callback = restService.getOffers();

        callback.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    progress.dismiss();
                }

                if(response.isSuccessful()) {
                    try {
                        String responseBody = IOUtils.toString(response.body().charStream());
                        JSONObject jsonObject = new JSONObject(responseBody);
                        OfferModel[] offers = new Gson().fromJson(jsonObject.getJSONArray("hydra:member").toString(), OfferModel[].class);
                        populateView(offers);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Erreur, identification impossible.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.out.println("Est-ce que le serveur est en ligne ?");
                    Toast.makeText(getActivity(), "Erreur de connection.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    progress.dismiss();
                }
                Toast.makeText(getActivity(), "Erreur de connection avec le serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateView(OfferModel[] offers) {
        for(OfferModel o : offers) {

        }
        offersList.clear();
        offersList.addAll(Arrays.asList(offers));
        OfferAdapter offerAdapter = new OfferAdapter(getActivity(), offersList);
        offersListView.setAdapter(offerAdapter);
        offerAdapter.notifyDataSetChanged();
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
