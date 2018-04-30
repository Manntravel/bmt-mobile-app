package com.rezofy.models.response_models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class AvailabilityResponse implements Serializable {
    private List<PaymentGateway> paymentGateways;
    private boolean paymentGatewayEnabled;
    private List<PaymentGateway> allPaymentGateways;
    private boolean cashCardEnabled;
    private Data data;
    private String convenienceFee;

    public List<PaymentGateway> getPaymentGateways() {
        return paymentGateways;
    }

    public void setPaymentGateways(List<PaymentGateway> paymentGateways) {
        this.paymentGateways = paymentGateways;
    }

    public boolean isPaymentGatewayEnabled() {
        return paymentGatewayEnabled;
    }

    public void setPaymentGatewayEnabled(boolean paymentGatewayEnabled) {
        this.paymentGatewayEnabled = paymentGatewayEnabled;
    }

    public List<PaymentGateway> getAllPaymentGateways() {
        return allPaymentGateways;
    }

    public void setAllPaymentGateways(List<PaymentGateway> allPaymentGateways) {
        this.allPaymentGateways = allPaymentGateways;
    }

    public boolean isCashCardEnabled() {
        return cashCardEnabled;
    }

    public void setCashCardEnabled(boolean cashCardEnabled) {
        this.cashCardEnabled = cashCardEnabled;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(String convenienceFee) {
        this.convenienceFee = convenienceFee;
    }
}
