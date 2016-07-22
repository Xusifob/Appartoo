package mobile.appartoo.utils;

import mobile.appartoo.model.SignUpModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by alexandre on 16-07-12.
 */
public interface RestService {
    @FormUrlEncoded
    @POST("/login")
    Call<ResponseBody> postLogIn(@Field("username") String username, @Field("password") String password);

    @POST("/register")
    Call<ResponseBody> postUser(@Body SignUpModel signUpModel);

    @GET("/offers")
    Call<ResponseBody> getOffers();

    @GET("/me")
    Call<ResponseBody> getLoggedUserProfile(@Header("Authorization") String bearerToken);
}