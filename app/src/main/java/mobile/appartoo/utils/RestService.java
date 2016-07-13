package mobile.appartoo.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by alexandre on 16-07-12.
 */
public interface RestService {
    @FormUrlEncoded
    @POST("/login")
    Call<ResponseBody> postLogIn(@Field("username") String username, @Field("password") String password);

    @GET("/offers")
    Call<ResponseBody> getOffers();
}