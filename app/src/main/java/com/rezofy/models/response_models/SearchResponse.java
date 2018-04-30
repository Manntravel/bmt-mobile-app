package com.rezofy.models.response_models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class SearchResponse implements Serializable {

    private List<String> errorMessages;
    private Data data;
    private String searchId;
    private boolean ticketEnabled;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public boolean isTicketEnabled() {
        return ticketEnabled;
    }

    public void setTicketEnabled(boolean ticketEnabled) {
        this.ticketEnabled = ticketEnabled;
    }
}
