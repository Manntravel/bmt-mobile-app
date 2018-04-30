package com.rezofy.models.response_models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by linchpin11192 on 13-Jan-2016.
 */
public class Special implements Serializable{
    private List<FlightData> GDS;
    public List<FlightData> getGDS() {
        return GDS;
    }

    public void setGDS(List<FlightData> GDS) {
        this.GDS = GDS;
    }
}
