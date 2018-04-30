package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by linchpin on 24/1/17.
 */

public class HotelTicketResponse implements Serializable {

  HotelTrip trip;

    public HotelTrip getTrip() {
        return trip;
    }

    public void setTrip(HotelTrip trip) {
        this.trip = trip;
    }
}
