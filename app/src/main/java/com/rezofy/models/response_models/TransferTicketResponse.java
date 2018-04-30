package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by linchpin on 16/2/17.
 */

public class TransferTicketResponse implements Serializable {

    private TransferTrip trip;

    public TransferTrip getTrip() {
        return trip;
    }

    public void setTrip(TransferTrip trip) {
        this.trip = trip;
    }
}
