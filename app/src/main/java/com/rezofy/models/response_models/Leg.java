package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class Leg implements Serializable, Cloneable {
    private String origin;
    private String destination;
    private String airline;
    private String flightNumber;
    private String departureTime;
    private String arrivalTime;
    private String departureDate;
    private String arrivalDate;
    private String cabinClass;
    private PlaceDetail originDetail, destinationDetail;
    private int layoverDuration;
    private int duration;
    private int arrivalAfterDays;
    private String departureTerminal;
    private boolean technicalStop;
    private int noOfTechnicalStop;
    private int journyTimeMin;
    private String startTerminal;
    private String endTerminal;
    private int arrivalNoOFDaysCount;
    private int sequence;
    private String availSource;
    private String originName;
    private String destinationName;
    private String airlineCode;
    private String airlineName="";

    //  this is  for  Selected Result
    private String originCode;
    private String destinationCode;
    private String marketingAirline;
    private String arrivalTerminal;

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public String getMarketingAirline() {
        return marketingAirline;
    }

    public void setMarketingAirline(String marketingAirline) {
        this.marketingAirline = marketingAirline;
    }

    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    public void setArrivalTerminal(String arrivalTerminal) {
        this.arrivalTerminal = arrivalTerminal;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public PlaceDetail getOriginDetail() {
        return originDetail;
    }

    public void setOriginDetail(PlaceDetail originDetail) {
        this.originDetail = originDetail;
    }

    public PlaceDetail getDestinationDetail() {
        return destinationDetail;
    }

    public void setDestinationDetail(PlaceDetail destinationDetail) {
        this.destinationDetail = destinationDetail;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

    public int getLayoverDuration() {
        return layoverDuration;
    }

    public void setLayoverDuration(int layoverDuration) {
        this.layoverDuration = layoverDuration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getArrivalAfterDays() {
        return arrivalAfterDays;
    }

    public void setArrivalAfterDays(int arrivalAfterDays) {
        this.arrivalAfterDays = arrivalAfterDays;
    }

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public void setDepartureTerminal(String departureTerminal) {
        this.departureTerminal = departureTerminal;
    }

    public boolean isTechnicalStop() {
        return technicalStop;
    }

    public void setTechnicalStop(boolean technicalStop) {
        this.technicalStop = technicalStop;
    }

    public int getNoOfTechnicalStop() {
        return noOfTechnicalStop;
    }

    public void setNoOfTechnicalStop(int noOfTechnicalStop) {
        this.noOfTechnicalStop = noOfTechnicalStop;
    }

    public int getJournyTimeMin() {
        return journyTimeMin;
    }

    public void setJournyTimeMin(int journyTimeMin) {
        this.journyTimeMin = journyTimeMin;
    }

    public String getStartTerminal() {
        return startTerminal;
    }

    public void setStartTerminal(String startTerminal) {
        this.startTerminal = startTerminal;
    }

    public String getEndTerminal() {
        return endTerminal;
    }

    public void setEndTerminal(String endTerminal) {
        this.endTerminal = endTerminal;
    }

    public int getArrivalNoOFDaysCount() {
        return arrivalNoOFDaysCount;
    }

    public void setArrivalNoOFDaysCount(int arrivalNoOFDaysCount) {
        this.arrivalNoOFDaysCount = arrivalNoOFDaysCount;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getAvailSource() {
        return availSource;
    }

    public void setAvailSource(String availSource) {
        this.availSource = availSource;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
