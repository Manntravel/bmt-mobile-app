package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by linchpin11192 on 25-Jan-2016.
 */
public class AppliedCommissionRule implements Serializable{
    private boolean international;
    private String baseFare;
    private String surcharge;
    private String tax;
    private String incentive;
    private String plb;
    private String total;
    private String tdsRate;

    public boolean isInternational() {
        return international;
    }

    public void setInternational(boolean international) {
        this.international = international;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(String surcharge) {
        this.surcharge = surcharge;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getIncentive() {
        return incentive;
    }

    public void setIncentive(String incentive) {
        this.incentive = incentive;
    }

    public String getPlb() {
        return plb;
    }

    public void setPlb(String plb) {
        this.plb = plb;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTdsRate() {
        return tdsRate;
    }

    public void setTdsRate(String tdsRate) {
        this.tdsRate = tdsRate;
    }
}
