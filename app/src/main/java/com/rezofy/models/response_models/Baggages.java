package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class Baggages implements Serializable {
    private int id;
    private String countryCode;
    private String airlineCode;
    private String serviceClass;
    private String baggageQuantity;
    private String cabinBaggageQuantity;
    private String baggageUnits;
    private String cabinBaggageUnits;
    private String baggagePrice;
    private boolean isDomestic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getBaggageQuantity() {
        return baggageQuantity;
    }

    public void setBaggageQuantity(String baggageQuantity) {
        this.baggageQuantity = baggageQuantity;
    }

    public String getCabinBaggageQuantity() {
        return cabinBaggageQuantity;
    }

    public void setCabinBaggageQuantity(String cabinBaggageQuantity) {
        this.cabinBaggageQuantity = cabinBaggageQuantity;
    }

    public String getBaggageUnits() {
        return baggageUnits;
    }

    public void setBaggageUnits(String baggageUnits) {
        this.baggageUnits = baggageUnits;
    }

    public String getCabinBaggageUnits() {
        return cabinBaggageUnits;
    }

    public void setCabinBaggageUnits(String cabinBaggageUnits) {
        this.cabinBaggageUnits = cabinBaggageUnits;
    }

    public String getBaggagePrice() {
        return baggagePrice;
    }

    public void setBaggagePrice(String baggagePrice) {
        this.baggagePrice = baggagePrice;
    }

    public boolean isDomestic() {
        return isDomestic;
    }

    public void setIsDomestic(boolean isDomestic) {
        this.isDomestic = isDomestic;
    }
}
