package com.rezofy.models.request_models;

import java.io.Serializable;

/**
 * Created by linchpin11192 on 16-Feb-2016.
 */
public class Passport implements Serializable {
    private String nationality="";
    private String issuingCountry="";
    private String passPortNo="";
    private DateInfo passExpDate;

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIssuingCountry() {
        return issuingCountry;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public String getPassPortNo() {
        return passPortNo;
    }

    public void setPassPortNo(String passPortNo) {
        this.passPortNo = passPortNo;
    }

    public DateInfo getPassExpDate() {
        return passExpDate;
    }

    public void setPassExpDate(DateInfo passExpDate) {
        this.passExpDate = passExpDate;
    }
}
