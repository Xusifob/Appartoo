package com.appartoo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexandre on 16-08-29.
 */
public class ObjectHolderModel {
    @SerializedName("_index")
    private String index;
    @SerializedName("_type")
    private String type;
    @SerializedName("_id")
    private Integer id;
    @SerializedName("_score")
    private Float score;
    @SerializedName("_source")
    private Object source;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "ObjectHolderModel{" +
                "index='" + index + '\'' +
                ", type='" + type + '\'' +
                ", id=" + id +
                ", score=" + score +
                ", source=" + source +
                '}';
    }
}
