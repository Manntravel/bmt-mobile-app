package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by linchpin11192 on 12-Jan-2016.
 */
public class PriceAndConversionModel implements Serializable {
    private Price    price;
    private String   conversionRate;

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(String conversionRate) {
        this.conversionRate = conversionRate;
    }
}
