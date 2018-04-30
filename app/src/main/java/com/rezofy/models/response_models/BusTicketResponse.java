package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by linchpin on 30/1/17.
 */

public class BusTicketResponse implements Serializable {

    BusTrip trip;

    public BusTrip getTrip() {
        return trip;
    }

    public void setTrip(BusTrip trip) {
        this.trip = trip;
    }
}
