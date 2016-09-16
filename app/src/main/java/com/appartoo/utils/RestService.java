package com.appartoo.utils;

import com.appartoo.utils.model.CommentModel;
import com.appartoo.utils.model.CommentsReceiver;
import com.appartoo.utils.model.CompleteUserModel;
import com.appartoo.utils.model.HasLikedReceiver;
import com.appartoo.utils.model.ImageModel;
import com.appartoo.utils.model.ObjectHolderModelReceiver;
import com.appartoo.utils.model.OfferModel;
import com.appartoo.utils.model.OfferModelWithDate;
import com.appartoo.utils.model.OfferModelWithDetailledDate;
import com.appartoo.utils.model.OfferToCreateModel;
import com.appartoo.utils.model.UserModel;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by alexandre on 16-07-12.
 */
public interface RestService {

    String REST_URL = "/a";

    @FormUrlEncoded
    @POST(REST_URL + "/login")
    Call<TokenReceiver> postLogIn(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST(REST_URL + "/register")
    Call<ResponseBody> postUser(@Field(Appartoo.KEY_EMAIL) String mail, @Field("password") String password, @Field(Appartoo.KEY_GIVEN_NAME) String givenName, @Field(Appartoo.KEY_FAMILY_NAME) String familyName);

    @FormUrlEncoded
    @POST(REST_URL + "/offer/create")
    Call<OfferModel> addOffer(@Header("Authorization") String bearerToken, @FieldMap Map<String, String> updateModel);

    @FormUrlEncoded
    @POST(REST_URL + "/organization/create")
    Call<ConversationIdReceiver> sendMessageToUser(@Header("Authorization") String bearerToken, @Field("profileId") String ownerProfileId);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> acceptCandidateToOffer(@Url String url, @Header("Authorization") String bearerToken, @Field("profile_id") String ownerProfileId, @Field("conversationId") String conversationId);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> addAddressToServer(@Url String url, @Header("Authorization") String bearerToken, @FieldMap Map<String, String> addressModel);

    @Multipart
    @POST
    Call<ImageModel> addImageToServer(@Url String url, @Header("Authorization") String bearerToken, @Part MultipartBody.Part file);

    @POST
    Call<ResponseBody> notifyNewMessage(@Url String url, @Header("Authorization") String bearerToken);

    @POST
    Call<ResponseBody> likeProfile(@Url String url, @Header("Authorization") String bearerToken);

    @GET
    Call<HasLikedReceiver> hasLiked(@Url String url, @Header("Authorization") String bearerToken);

    @GET
    Call<CommentsReceiver> getUserComments(@Url String url);

    @GET(REST_URL + "/searchGeneral")
    Call<ObjectHolderModelReceiver> getOffersOrProfiles(@Header("Authorization") String bearerToken, @QueryMap Map<String, Object> queryParam);

    @GET(REST_URL + "/me")
    Call<CompleteUserModel> getLoggedUserProfile(@Header("Authorization") String bearerToken);

    @GET(REST_URL + "/profiles/offers/my")
    Call<ArrayList<OfferModelWithDetailledDate>> getLoggedUserOffers(@Header("Authorization") String bearerToken);

    @GET
    Call<UserModel> getUserInformationsById(@Url String profileId);

    @GET
    Call<OfferModelWithDate> getOfferById(@Url String offerId);

    @PUT
    Call<CompleteUserModel> updateUserProfile(@Url String url, @Header("Authorization") String bearerToken, @Body CompleteUserModel updateModel);

    @PUT
    Call<OfferModelWithDate> updateOffer(@Url String url, @Header("Authorization") String bearerToken, @Body OfferModelWithDate offerToUpdate);

    @DELETE
    Call<ResponseBody> deleteImage(@Url String url, @Header("Authorization") String bearerToken);

    @FormUrlEncoded
    @HTTP(method = "DELETE", hasBody = true)
    Call<ResponseBody> refuseCandidateToOffer(@Url String url, @Header("Authorization") String bearerToken, @Field("profile_id") String ownerProfileId, @Field("conversationId") String conversationId);
}