package com.appartoo.utils;

import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.Map;

import com.appartoo.model.ImageModel;
import com.appartoo.model.OfferModel;
import com.appartoo.model.OfferModelWithDate;
import com.appartoo.model.OfferModelWithDetailledDate;
import com.appartoo.model.UserWithProfileModel;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by alexandre on 16-07-12.
 */
public interface RestService {
    @FormUrlEncoded
    @POST("/login")
    Call<TokenReceiver> postLogIn(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/register")
    Call<ResponseBody> postUser(@Field("email") String mail, @Field("password") String password, @Field("givenName") String givenName, @Field("familyName") String familyName, @Field("birthDate") String birthDate);

    @FormUrlEncoded
    @POST("/offer/create")
    Call<OfferModel> addOffer(@Header("Authorization") String bearerToken, @FieldMap Map<String, String> updateModel);

    @Multipart
    @POST
    Call<ImageModel> addImageToServer(@Url String url, @Header("Authorization") String bearerToken, @Part MultipartBody.Part file);

    @GET("/offers")
    Call<ServerResponse<ArrayList<OfferModelWithDate>>> getOffers(@IntRange(from=1) @Query("page") int page);

    @GET("/me")
    Call<UserWithProfileModel> getLoggedUserProfile(@Header("Authorization") String bearerToken);

    @GET("/search/offer")
    Call<ResponseBody> searchOffer();

    @GET
    Call<ArrayList<OfferModelWithDetailledDate>> getUserOffers(@Url String url);

    @PUT
    Call<UserWithProfileModel> updateUserProfile(@Url String url, @Header("Authorization") String bearerToken, @Body UserWithProfileModel updateModel);
}