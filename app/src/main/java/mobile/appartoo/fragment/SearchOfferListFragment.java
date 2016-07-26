package mobile.appartoo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import mobile.appartoo.R;
import mobile.appartoo.model.OfferModel;
import mobile.appartoo.utils.Appartoo;
import mobile.appartoo.utils.RestService;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by alexandre on 16-07-25.
 */
public class SearchOfferListFragment extends Fragment {

    private ListView offerList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_offer_list, container, false);

        if (container != null) {
            container.removeAllViews();
        }

        offerList = (ListView) view.findViewById(R.id.offersList);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    public void searchOffer(String[] keywords){
        final String[] values = keywords;

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl.Builder builder = request.url().newBuilder();
                for(String val : values) {
                    builder.addQueryParameter("q", val);
                }
                HttpUrl url = builder.build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Appartoo.SERVER_URL)
                .client(client)
                .build();

        RestService restService = retrofit.create(RestService.class);
        Call<ResponseBody> callback = restService.searchOffer();

        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        String responseBody = IOUtils.toString(response.body().charStream());
                        JSONObject jsonObject = new JSONObject(responseBody);
                        ArrayList<OfferModel> offers = new Gson().fromJson(jsonObject.getJSONArray("hydra:member").toString(), new TypeToken<ArrayList<OfferModel>>(){}.getType());
                        System.out.println(offers.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Erreur dans le chargement des offres.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.out.println(response.code());
                    System.out.println("Est-ce que le serveur est en ligne ?");
                    Toast.makeText(getActivity(), "Erreur de connection.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
