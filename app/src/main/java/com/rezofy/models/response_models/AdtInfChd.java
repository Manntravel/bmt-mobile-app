package com.rezofy.models.response_models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class AdtInfChd implements Serializable{

    private String type;
    private Base base;
    private Total total;
    private HashMap<String,PriceAndConversionModel> taxes;

    public HashMap<String, PriceAndConversionModel> getTaxes() {
        return taxes;
    }

    public void setTaxes(HashMap<String, PriceAndConversionModel> taxes) {
        this.taxes = taxes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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


}
