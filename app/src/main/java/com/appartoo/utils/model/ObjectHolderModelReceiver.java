package com.appartoo.utils.model;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-08-29.
 */
public class ObjectHolderModelReceiver {
    private Integer total;
    private Float max_score;
    private ArrayList<ObjectHolderModel> hits;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Float getMax_score() {
        return max_score;
    }

    public void setMax_score(Float max_score) {
        this.max_score = max_score;
    }

    public ArrayList<ObjectHolderModel> getHits() {
        return hits;
    }

    public void setHits(ArrayList<ObjectHolderModel> hits) {
        this.hits = hits;
    }

    @Override
    public String toString() {
        return "ObjectHolderModelReceiver{" +
                "total=" + total +
                ", max_score=" + max_score +
                ", hits=" + hits +
                '}';
    }
}
