package com.rezofy.models.response_models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class Fare implements Serializable {
    private Base base;
    private Total total;
    private HashMap<String, PriceAndConversionModel> taxes;
    private float sellingPrice;

    //  this is for  Selectetd Result
    private TravellerFare travellerFares;

    public TravellerFare getTravellerFares() {
        return travellerFares;
    }

    public void setTravellerFares(TravellerFare travellerFares) {
        this.travellerFares = travellerFares;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    public HashMap<String, PriceAndConversionModel> getTaxes() {
        return taxes;
    }

    public void setTaxes(HashMap<String, PriceAndConversionModel> taxes) {
        this.taxes = taxes;
    }

    public float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
