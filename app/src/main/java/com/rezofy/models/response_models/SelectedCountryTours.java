package com.rezofy.models.response_models;

import java.util.ArrayList;

/**
 * Created by anuj on 13/1/17.
 */
public class SelectedCountryTours {

    private ArrayList<CountryTours> selectedcountrytour;

    public ArrayList<CountryTours> getCountryToursArrayList() {
        return selectedcountrytour;
    }

    public void setCountryToursArrayList(ArrayList<CountryTours> selectedcountrytour) {
        this.selectedcountrytour = selectedcountrytour;
    }
}
