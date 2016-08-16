package com.appartoo.utils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by alexandre on 16-07-12.
 */
public interface GoogleMapsService {
    @GET("geocode/json")
    Call<GeocoderResponse> getAddressFromPlaceId(@Query("place_id") String address, @Query("key") String googleMapsApiKey);
}