package com.rezofy.models.response_models;

import java.util.List;

/**
 * Created by linchpin on 20/1/17.
 */

public class MyTripResponse {

    List<MyTrip> tripList;
    List<String> messages;


    public List<MyTrip> getTripList() {
        return tripList;
    }

    public void setTripList(List<MyTrip> tripList) {
        this.tripList = tripList;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
