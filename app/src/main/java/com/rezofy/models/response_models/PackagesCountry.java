package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by anuj on 20/1/17.
 */
public class PackagesCountry implements Serializable{

    private String getalltours;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGetalltours() {
        return getalltours;
    }

    public void setGetalltours(String getalltours) {
        this.getalltours = getalltours;
    }
}
