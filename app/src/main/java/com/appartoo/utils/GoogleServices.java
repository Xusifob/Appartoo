package com.appartoo.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by alexandre on 16-07-12.
 */
public interface GoogleServices {
    @GET("geocode/json")
    Call<GeocoderResponse> getAddressFromPlaceId(@Query("place_id") String address, @Query("key") String googleMapsApiKey);

    @FormUrlEncoded
    @POST("formResponse")
    Call<ResponseBody> reportError(
            @Field("entry.741070548") String exception,
            @Field("entry.1053371281") String phoneInfos,
            @Field("entry.1287320624") String logcat,
            @Field("entry.325751971") String userComment,
            @Field("entry.1652759026") String other);
}