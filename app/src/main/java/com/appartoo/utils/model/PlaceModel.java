package com.appartoo.utils.model;

import com.google.android.gms.location.places.AutocompletePrediction;

/**
 * Created by alexandre on 16-07-21.
 */
public class PlaceModel {
    String primaryText;
    String secondaryText;
    String fullText;
    String placeId;

    public PlaceModel(AutocompletePrediction autocompletePrediction) {
        this.primaryText = autocompletePrediction.getPrimaryText(null).toString();
        this.secondaryText = autocompletePrediction.getSecondaryText(null).toString();
        this.fullText = autocompletePrediction.getFullText(null).toString();
        this.placeId = autocompletePrediction.getPlaceId();
    }

    public PlaceModel() {}

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    @Override
    public String toString() {
        return fullText;
    }

}
