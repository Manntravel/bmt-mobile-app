package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by LinchPin on 1/23/2016.
 */
public class NameModel implements Serializable {
    private String first = "";
    private String last = "";

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }
}
