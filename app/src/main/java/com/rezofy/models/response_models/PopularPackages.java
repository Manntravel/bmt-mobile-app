package com.rezofy.models.response_models;

import java.util.ArrayList;

/**
 * Created by anuj on 12/1/17.
 */
public class PopularPackages {

    private ArrayList<TourInformation> tourinfo;

    public ArrayList<TourInformation> getTourinfo() {
        return tourinfo;
    }

    public void setTourinfo(ArrayList<TourInformation> tourinfo) {
        this.tourinfo = tourinfo;
    }
}
