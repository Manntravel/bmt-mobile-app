package com.rezofy.models.request_models;

import com.rezofy.models.response_models.CashCard;

import java.io.Serializable;

/**
 * Created by linchpin11192 on 24-Dec-2015.
 */
public class BillingInfo implements Serializable {
    private String  billingPhone;
    private String email;
    private String address1;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String contactPhone;
    private boolean chargePayment;
    private String paymentType;
    private String billingName;
    private GstBillingInfo gstBillingInfo;

    public CashCard getCashCard() {
        return cashCard;
    }

    public void setCashCard(CashCard cashCard) {
        this.cashCard = cashCard;
    }

    private CashCard cashCard;

    public String getBillingName() {
        return billingName;
    }

    public void setBillingName(String billingName) {
        this.billingName = billingName;
    }
    //private String address2;
    //private String countryIsdCode;
    private boolean sendInformationToTraveller;

    public String getBillingPhone() {
        return billingPhone;
    }

    public void setBillingPhone(String billingPhone) {
        this.billingPhone = billingPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public boolean isChargePayment() {
        return chargePayment;
    }

    public void setChargePayment(boolean chargePayment) {
        this.chargePayment = chargePayment;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

/*    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCountryIsdCode() {
        return countryIsdCode;
    }

    public void setCountryIsdCode(String countryIsdCode) {
        this.countryIsdCode = countryIsdCode;
    }*/

    public boolean isSendInformationToTraveller() {
        return sendInformationToTraveller;
    }

    public void setSendInformationToTraveller(boolean sendInformationToTraveller) {
        this.sendInformationToTraveller = sendInformationToTraveller;
    }

    public GstBillingInfo getGstBillingInfo() {
        return gstBillingInfo;
    }

    public void setGstBillingInfo(GstBillingInfo gstBillingInfo) {
        this.gstBillingInfo = gstBillingInfo;
    }
}
