package mobile.appartoo.utils;

import java.util.ArrayList;

import mobile.appartoo.model.AddressInformationsModel;

/**
 * Created by alexandre on 16-07-27.
 */
public class GeocoderResponse {
    private ArrayList<AddressInformationsModel> results;

    public ArrayList<AddressInformationsModel> getResults() {
        return results;
    }
}
