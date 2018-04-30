package com.rezofy.models.response_models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class Segment implements Serializable, Cloneable {
    private List<Leg> legs;
    private String origin;
    private String destination;
    private String duration;
    private int noOfStops;

    private int legCount;
    private int arrivalAfterDays;
    // this  is for round one way
    private String distanceInMiles;
    // this is for selected result
    private boolean isSupplierCancelled;
    private String dataSource;
    private String segmentStatus;
    private String confirmIndicator;
    private String pnr;
    private String airlinePnr;
    private String tktTimeLimit;

    public boolean isSupplierCancelled() {
        return isSupplierCancelled;
    }

    public void setIsSupplierCancelled(boolean isSupplierCancelled) {
        this.isSupplierCancelled = isSupplierCancelled;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getLegCount() {
        return legCount;
    }

    public void setLegCount(int legCount) {
        this.legCount = legCount;
    }

    public int getNoOfStops() {
        return noOfStops;
    }

    public void setNoOfStops(int noOfStops) {
        this.noOfStops = noOfStops;
    }

    public int getArrivalAfterDays() {
        return arrivalAfterDays;
    }

    public void setArrivalAfterDays(int arrivalAfterDays) {
        this.arrivalAfterDays = arrivalAfterDays;
    }

    public String getDistanceInMiles() {
        return distanceInMiles;
    }

    public void setDistanceInMiles(String distanceInMiles) {
        this.distanceInMiles = distanceInMiles;
    }

    public void setSupplierCancelled(boolean supplierCancelled) {
        isSupplierCancelled = supplierCancelled;
    }

    public String getSegmentStatus() {
        return segmentStatus;
    }

    public void setSegmentStatus(String segmentStatus) {
        this.segmentStatus = segmentStatus;
    }

    public String getConfirmIndicator() {
        return confirmIndicator;
    }

    public void setConfirmIndicator(String confirmIndicator) {
        this.confirmIndicator = confirmIndicator;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getAirlinePnr() {
        return airlinePnr;
    }

    public void setAirlinePnr(String airlinePnr) {
        this.airlinePnr = airlinePnr;
    }

    public String getTktTimeLimit() {
        return tktTimeLimit;
    }

    public void setTktTimeLimit(String tktTimeLimit) {
        this.tktTimeLimit = tktTimeLimit;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Segment clonedSegment = (Segment) super.clone();
        for (int i = 0; i < clonedSegment.getLegs().size(); i++)
            clonedSegment.getLegs().set(i, (Leg) clonedSegment.getLegs().get(i).clone());
        return clonedSegment;
    }
}
