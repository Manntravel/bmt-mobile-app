package com.rezofy.models.response_models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class Result implements Serializable
{

    private  List<Segment> segments;
    private  String       carrier;
    private  String       validatingCarrier;
    private  Fare          fare;
    private  boolean  specialReturnFare;
    private  boolean  multiCarrier;
    private  boolean  privateFare;
    private  boolean  couponFare;
    private  boolean  staticFareRule;
    private  boolean  isBaggageAvailable;
    private  long     duration;
    private  int      index;
    private  String   uniqueEntityId;
    private  boolean  rejectable;
    private  boolean  noRefund;
    private  boolean  nonCommissionable;
    private  String   repriceDiff;

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getValidatingCarrier() {
        return validatingCarrier;
    }

    public void setValidatingCarrier(String validatingCarrier) {
        this.validatingCarrier = validatingCarrier;
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    public boolean isSpecialReturnFare() {
        return specialReturnFare;
    }

    public void setSpecialReturnFare(boolean specialReturnFare) {
        this.specialReturnFare = specialReturnFare;
    }

    public boolean isMultiCarrier() {
        return multiCarrier;
    }

    public void setMultiCarrier(boolean multiCarrier) {
        this.multiCarrier = multiCarrier;
    }

    public boolean isPrivateFare() {
        return privateFare;
    }

    public void setPrivateFare(boolean privateFare) {
        this.privateFare = privateFare;
    }

    public boolean isCouponFare() {
        return couponFare;
    }

    public void setCouponFare(boolean couponFare) {
        this.couponFare = couponFare;
    }

    public boolean isStaticFareRule() {
        return staticFareRule;
    }

    public void setStaticFareRule(boolean staticFareRule) {
        this.staticFareRule = staticFareRule;
    }

    public boolean isBaggageAvailable() {
        return isBaggageAvailable;
    }

    public void setIsBaggageAvailable(boolean isBaggageAvailable) {
        this.isBaggageAvailable = isBaggageAvailable;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUniqueEntityId() {
        return uniqueEntityId;
    }

    public void setUniqueEntityId(String uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    public boolean isRejectable() {
        return rejectable;
    }

    public void setRejectable(boolean rejectable) {
        this.rejectable = rejectable;
    }

    public boolean isNoRefund() {
        return noRefund;
    }

    public void setNoRefund(boolean noRefund) {
        this.noRefund = noRefund;
    }

    public boolean isNonCommissionable() {
        return nonCommissionable;
    }

    public void setNonCommissionable(boolean nonCommissionable) {
        this.nonCommissionable = nonCommissionable;
    }

    public String getRepriceDiff() {
        return repriceDiff;
    }

    public void setRepriceDiff(String repriceDiff) {
        this.repriceDiff = repriceDiff;
    }
}
