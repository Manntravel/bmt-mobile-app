package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by linchpin11192 on 26-Feb-2016.
 */
public class PlaceDetail implements Serializable {
    private String name;
    private String country;

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
