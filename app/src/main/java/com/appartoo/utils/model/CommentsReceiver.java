package com.appartoo.utils.model;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-09-16.
 */
public class CommentsReceiver {
    private Integer total;
    private ArrayList<CommentModel> hits;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public ArrayList<CommentModel> getHits() {
        return hits;
    }

    public void setHits(ArrayList<CommentModel> hits) {
        this.hits = hits;
    }
}
