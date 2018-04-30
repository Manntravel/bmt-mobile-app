package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by linchpin11192 on 25-Jan-2016.
 */
public class SegmentTicket implements Serializable {
    private String number;
    private String status;
    private boolean cancelRequested;
    private String pnr;
    private PaxFare fare;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCancelRequested() {
        return cancelRequested;
    }

    public void setCancelRequested(boolean cancelRequested) {
        this.cancelRequested = cancelRequested;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public PaxFare getFare() {
        return fare;
    }

    public void setFare(PaxFare fare) {
        this.fare = fare;
    }
}
