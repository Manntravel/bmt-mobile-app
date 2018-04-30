package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by linchpin on 15/2/17.
 */

public class TrainTicketResponse  implements Serializable {

    TrainTrip trip;

    public TrainTrip getTrip() {
        return trip;
    }

    public void setTrip(TrainTrip trip) {
        this.trip = trip;
    }
}