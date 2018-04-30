package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by linchpin11192 on 25-Jan-2016.
 */
public class PaxFare implements Serializable {
    private Price baseFare;
    private Price tax;
    private Price totalFare;
    private Price agencyBookingAmount;

    public Price getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Price baseFare) {
        this.baseFare = baseFare;
    }

    public Price getTax() {
        return tax;
    }

    public void setTax(Price tax) {
        this.tax = tax;
    }

    public Price getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(Price totalFare) {
        this.totalFare = totalFare;
    }

    public Price getAgencyBookingAmount() {
        return agencyBookingAmount;
    }

    public void setAgencyBookingAmount(Price agencyBookingAmount) {
        this.agencyBookingAmount = agencyBookingAmount;
    }
}
