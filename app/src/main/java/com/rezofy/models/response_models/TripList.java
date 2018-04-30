package com.rezofy.models.response_models;

import java.util.ArrayList;

/**
 * Created by prince on 1/2/16.
 */
public class TripList {

    public ArrayList<TripRecord> getTripList() {
        return tripList;
    }

    public void setTripList(ArrayList<TripRecord> tripList) {
        this.tripList = tripList;
    }

    private ArrayList<TripRecord> tripList;
}
