package mobile.appartoo.utils;

import java.util.ArrayList;

import mobile.appartoo.model.OfferModel;
import mobile.appartoo.model.UserWithProfileModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    @GET("/offers")
    Call<ServerResponse<ArrayList<OfferModel>>> getOffers();

    @GET("/me")
    Call<UserWithProfileModel> getLoggedUserProfile(@Header("Authorization") String bearerToken);

    @GET("/search/offer")
    Call<ResponseBody> searchOffer();

    @GET
    Call<ResponseBody> getUserOffers(@Url String url);
}