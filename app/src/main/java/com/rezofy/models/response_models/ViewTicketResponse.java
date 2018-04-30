package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by linchpin11192 on 26-Jan-2016.
 */
public class ViewTicketResponse implements Serializable {
    private Trip trip;

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
