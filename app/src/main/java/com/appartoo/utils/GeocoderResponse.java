package com.appartoo.utils;

import com.appartoo.model.AddressInformationsModel;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-07-27.
 */
public class GeocoderResponse {
    private ArrayList<AddressInformationsModel> results;

    public ArrayList<AddressInformationsModel> getResults() {
        return results;
    }
}
