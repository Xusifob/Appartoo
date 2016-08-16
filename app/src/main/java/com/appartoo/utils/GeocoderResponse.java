package com.appartoo.utils;

import java.util.ArrayList;

import com.appartoo.model.AddressInformationsModel;

/**
 * Created by alexandre on 16-07-27.
 */
public class GeocoderResponse {
    private ArrayList<AddressInformationsModel> results;

    public ArrayList<AddressInformationsModel> getResults() {
        return results;
    }
}
