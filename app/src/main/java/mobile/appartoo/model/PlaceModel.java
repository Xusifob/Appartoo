package mobile.appartoo.model;

import com.google.android.gms.location.places.AutocompletePrediction;

/**
 * Created by alexandre on 16-07-21.
 */
public class PlaceModel {
    String primaryText;
    String secondaryText;
    String fullText;

    public PlaceModel(AutocompletePrediction autocompletePrediction) {
        this.primaryText = autocompletePrediction.getPrimaryText(null).toString();
        this.secondaryText = autocompletePrediction.getSecondaryText(null).toString();
        this.fullText = autocompletePrediction.getFullText(null).toString();
    }

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

    @Override
    public String toString() {
        return fullText;
    }
}
