package mobile.appartoo.utils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandre on 16-07-27.
 */
public class ServerResponse<T> {
    @SerializedName("hydra:member")
    private T data;

    public T getData(){
        return data;
    }
}
