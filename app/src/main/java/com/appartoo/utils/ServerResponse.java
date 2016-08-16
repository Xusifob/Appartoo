package com.appartoo.utils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandre on 16-07-27.
 */
public class ServerResponse<T> {
    @SerializedName("hydra:nextPage")
    private String nextPage;

    @SerializedName("hydra:member")
    private T data;

    public T getData(){
        return data;
    }

    public String getNextPage() {
        return nextPage;
    }
}
