package com.rezofy.models.response_models;

import com.rezofy.models.request_models.Traveller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class Data implements Serializable {
    private Regular REGULAR;
    private Special SPECIAL;
    //   this is  for avaibality responce
    private String fareResults;
    private List<Traveller> travellers;
    private FlightData selectedResult;
    private FlightData selectedOutBoundFlight;
    private FlightData selectedInBoundFlight;
    private String pnr;
    private String searchId;
    private boolean bookingDisabled;

    // this  is  for  OneWay
    private List<FlightData>  results;

    public FlightData getSelectedOutBoundFlight() {
        return selectedOutBoundFlight;
    }

    public void setSelectedOutBoundFlight(FlightData selectedOutBoundFlight) {
        this.selectedOutBoundFlight = selectedOutBoundFlight;
    }

    public FlightData getSelectedInBoundFlight() {
        return selectedInBoundFlight;
    }

    public void setSelectedInBoundFlight(FlightData selectedInBoundFlight) {
        this.selectedInBoundFlight = selectedInBoundFlight;
    }

    public Regular getREGULAR() {
        return REGULAR;
    }

    public void setREGULAR(Regular REGULAR) {
        this.REGULAR = REGULAR;
    }

    public String getFareResults() {
        return fareResults;
    }

    public void setFareResults(String fareResults) {
        this.fareResults = fareResults;
    }

    public List<Traveller> getTravellers() {
        return travellers;
    }

    public void setTravellers(List<Traveller> travellers) {
        this.travellers = travellers;
    }

    public FlightData getSelectedResult() {
        return selectedResult;
    }

    public void setSelectedResult(FlightData selectedResult) {
        this.selectedResult = selectedResult;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }


    public List<FlightData> getResults() {
        return results;
    }

    public void setResults(List<FlightData> results) {
        this.results = results;
    }

    public Special getSPECIAL() {
        return SPECIAL;
    }

    public void setSPECIAL(Special SPECIAL) {
        this.SPECIAL = SPECIAL;
    }

    public boolean isBookingDisabled() {
        return bookingDisabled;
    }

    public void setBookingDisabled(boolean bookingDisabled) {
        this.bookingDisabled = bookingDisabled;
    }
}
