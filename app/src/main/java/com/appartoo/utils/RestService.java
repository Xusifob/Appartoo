package com.appartoo.utils;

import android.support.annotation.IntRange;

import com.appartoo.model.CompleteUserModel;
import com.appartoo.model.ImageModel;
import com.appartoo.model.ObjectHolderModel;
import com.appartoo.model.OfferModel;
import com.appartoo.model.OfferModelWithDate;
import com.appartoo.model.OfferModelWithDetailledDate;
import com.appartoo.model.UserModel;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
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

    String REST_URL = "";

    @FormUrlEncoded
    @POST(REST_URL + "/login")
    Call<TokenReceiver> postLogIn(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST(REST_URL + "/register")
    Call<ResponseBody> postUser(@Field("email") String mail, @Field("password") String password, @Field("givenName") String givenName, @Field("familyName") String familyName);

    @FormUrlEncoded
    @POST(REST_URL + "/offer/create")
    Call<OfferModel> addOffer(@Header("Authorization") String bearerToken, @FieldMap Map<String, String> updateModel);

    @FormUrlEncoded
    @POST(REST_URL + "/organization/create")
    Call<ConversationIdReceiver> sendMessageToUser(@Header("Authorization") String bearerToken, @Field("profileId") String ownerProfileId);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> acceptCandidateToOffer(@Url String url, @Header("Authorization") String bearerToken, @Field("profile_id") String ownerProfileId, @Field("conversationId") String conversationId);

    @Multipart
    @POST
    Call<ImageModel> addImageToServer(@Url String url, @Header("Authorization") String bearerToken, @Part MultipartBody.Part file);

    @GET(REST_URL + "/searchGeneral")
    Call<ObjectHolderModel> getOffersOrProfiles(@Header("Authorization") String bearerToken);

    @GET(REST_URL + "/offers")
    Call<ServerResponse<ArrayList<OfferModelWithDate>>> getOffers(@IntRange(from=1) @Query("page") int page);

    @GET(REST_URL + "/me")
    Call<CompleteUserModel> getLoggedUserProfile(@Header("Authorization") String bearerToken);

    @GET(REST_URL + "/search/offer")
    Call<ResponseBody> searchOffer();

    @GET
    Call<ArrayList<OfferModelWithDetailledDate>> getUserOffers(@Url String url);

    @GET
    Call<UserModel> getUserProfileById(@Url String profileId);

    @GET
    Call<OfferModel> getOfferById(@Url String profileId);

    @PUT
    Call<CompleteUserModel> updateUserProfile(@Url String url, @Header("Authorization") String bearerToken, @Body CompleteUserModel updateModel);

    @FormUrlEncoded
    @HTTP(method = "DELETE", hasBody = true)
    Call<ResponseBody> refuseCandidateToOffer(@Url String url, @Header("Authorization") String bearerToken, @Field("profile_id") String ownerProfileId, @Field("conversationId") String conversationId);
}