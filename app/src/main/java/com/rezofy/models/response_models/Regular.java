package com.rezofy.models.response_models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class Regular implements Serializable {
    private List<FlightData> INBOUND;

    private List<FlightData> OUTBOUND;

    public List<FlightData> getINBOUND() {
        return INBOUND;
    }

    public void setINBOUND(List<FlightData> INBOUND) {
        this.INBOUND = INBOUND;
    }

    public List<FlightData> getOUTBOUND() {
        return OUTBOUND;
    }

    public void setOUTBOUND(List<FlightData> OUTBOUND) {
        this.OUTBOUND = OUTBOUND;
    }
}
