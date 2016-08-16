package com.appartoo.model;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-08-02.
 */
public class AddressInformationsModel {

    private ArrayList<AddressComponent> address_components;
    private String formatted_address;
    private Geometry geometry;
    private String place_id;

    public ArrayList<AddressComponent> getAddress_components() {
        return address_components;
    }

    public void setAddress_components(ArrayList<AddressComponent> address_components) {
        this.address_components = address_components;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public Double getLatitude() {
        return this.geometry.getLocation().getLat();
    }

    public Double getLongitude() {
        return this.geometry.getLocation().getLng();
    }

    private class Geometry {
        private LatitutideLongitude location;

        public LatitutideLongitude getLocation() {
            return location;
        }

        public void setLocation(LatitutideLongitude location) {
            this.location = location;
        }

        private class LatitutideLongitude {
            private Double lat;
            private Double lng;

            public Double getLat() {
                return lat;
            }

            public void setLat(Double lat) {
                this.lat = lat;
            }

            public Double getLng() {
                return lng;
            }

            public void setLng(Double lng) {
                this.lng = lng;
            }
        }
    }
}
