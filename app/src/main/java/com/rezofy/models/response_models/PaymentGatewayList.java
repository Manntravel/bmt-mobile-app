package com.rezofy.models.response_models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by linchpin on 24/2/16.
 */
public class PaymentGatewayList implements Serializable {
    private  ArrayList<PaymentGateway> gateways ;


    public void setGatewayList(ArrayList<PaymentGateway> gateways)
    {
        this.gateways = gateways;
    }

    public ArrayList<PaymentGateway> getGateWayList()
    {
        return gateways;
    }
}
