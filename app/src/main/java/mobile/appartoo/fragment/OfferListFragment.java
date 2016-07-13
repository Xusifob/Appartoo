package mobile.appartoo.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import mobile.appartoo.R;
import mobile.appartoo.activity.OfferDetailActivity;
import mobile.appartoo.adapter.OfferAdapter;
import mobile.appartoo.model.OfferModel;
import mobile.appartoo.utils.Appartoo;

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

        if(offersList.size() == 0) {
            new RetrieveOffersTask().execute();
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
                new RetrieveOffersTask().execute();
            }
        });
    }

    private class RetrieveOffersTask extends AsyncTask<Void, Void, ArrayList<OfferModel>> {

        ArrayList<OfferModel> offerModels;

        @Override
        protected void onPreExecute(){
            if(!swipeRefreshLayout.isRefreshing()){
                progress.show();
            }
        }

        @Override
        protected ArrayList<OfferModel> doInBackground(Void... params) {
            offerModels = new ArrayList<>();

            try {
                URL url = new URL(Appartoo.SERVER_URL + "/offers");
                //String response = getHttpResponse(url);
                String response = loadJSONFromAsset();
                if (response != null) {
                    return retrieveOffers(response);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<OfferModel> offerModels){

            if(offerModels != null){
                offersList.clear();
                offersList.addAll(offerModels);
                OfferAdapter offerAdapter = new OfferAdapter(getActivity(), offersList);

                offersListView.setAdapter(offerAdapter);
                offersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
                        intent.putExtra("offer", offerModels.get(position));
                        startActivity(intent);
                    }
                });
                offerAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
            }

            if(swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(false);
            } else {
                progress.dismiss();
            }
        }

        public ArrayList<OfferModel> retrieveOffers(String response) {
            try {
                JSONObject json = new JSONObject(response);
                JSONArray array = json.getJSONArray("hydra:member");
                for(int i = 0 ; i < array.length() ; i++){
                    JSONObject object = array.getJSONObject(i);
                    OfferModel offerModel = new OfferModel();
                    offerModel.createFromJSON(object);
                    offerModels.add(offerModel);
                }

                return offerModels;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public String getHttpResponse(URL url) {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    return IOUtils.toString(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getActivity().getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
