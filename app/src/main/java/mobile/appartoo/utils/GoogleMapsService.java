package mobile.appartoo.utils;

import java.util.ArrayList;
import java.util.Map;

import mobile.appartoo.model.OfferModelWithDate;
import mobile.appartoo.model.OfferModelWithDetailledDate;
import mobile.appartoo.model.UserWithProfileModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by alexandre on 16-07-12.
 */
public interface GoogleMapsService {
    @GET("geocode/json")
    Call<GeocoderResponse> getAddressFromPlaceId(@Query("place_id") String address, @Query("key") String googleMapsApiKey);
}