package com.rezofy.models.response_models;

import android.os.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by anuj on 20/1/17.
 */
public class PackagesCountryList  implements Serializable {

    private ArrayList<PackagesCountry> countries;

    protected PackagesCountryList(Parcel in) {
    }

    public ArrayList<PackagesCountry> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<PackagesCountry> countries) {
        this.countries = countries;
    }


}
