package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class Taxes implements Serializable {
    private PriceAndConversionModel JN;
    private PriceAndConversionModel YQ;
    private PriceAndConversionModel WO;
    private PriceAndConversionModel IN;
    private TotalTax totalTax;
    private PriceAndConversionModel YM;
    private PriceAndConversionModel F2;
    private PriceAndConversionModel TDS;
    private PriceAndConversionModel markup;

    public PriceAndConversionModel getAdminTransactionCharges() {
        return adminTransactionCharges;
    }

    public void setAdminTransactionCharges(PriceAndConversionModel adminTransactionCharges) {
        this.adminTransactionCharges = adminTransactionCharges;
    }

    private PriceAndConversionModel adminTransactionCharges;

    public PriceAndConversionModel getCommission() {
        return commission;
    }

    public void setCommission(PriceAndConversionModel commission) {
        this.commission = commission;
    }

    private PriceAndConversionModel commission;

    public PriceAndConversionModel getPublishedPriceDiff() {
        return publishedPriceDiff;
    }

    public void setPublishedPriceDiff(PriceAndConversionModel publishedPriceDiff) {
        this.publishedPriceDiff = publishedPriceDiff;
    }

    private PriceAndConversionModel publishedPriceDiff;

    public PriceAndConversionModel getTotalServicetax() {
        return totalServicetax;
    }

    public void setTotalServicetax(PriceAndConversionModel totalServicetax) {
        this.totalServicetax = totalServicetax;
    }

    public PriceAndConversionModel getTDS() {
        return TDS;
    }

    public void setTDS(PriceAndConversionModel TDS) {
        this.TDS = TDS;
    }

    public PriceAndConversionModel getMarkup() {
        return markup;
    }

    public void setMarkup(PriceAndConversionModel markup) {
        this.markup = markup;
    }

    private PriceAndConversionModel totalServicetax;

    public PriceAndConversionModel getSupplierCommission() {
        return supplierCommission;
    }

    public void setSupplierCommission(PriceAndConversionModel supplierCommission) {
        this.supplierCommission = supplierCommission;
    }

    public PriceAndConversionModel getJN() {
        return JN;
    }

    public void setJN(PriceAndConversionModel JN) {
        this.JN = JN;
    }

    public PriceAndConversionModel getYQ() {
        return YQ;
    }

    public void setYQ(PriceAndConversionModel YQ) {
        this.YQ = YQ;
    }

    public PriceAndConversionModel getWO() {
        return WO;
    }

    public void setWO(PriceAndConversionModel WO) {
        this.WO = WO;
    }

    public PriceAndConversionModel getIN() {
        return IN;
    }

    public void setIN(PriceAndConversionModel IN) {
        this.IN = IN;
    }

    public TotalTax getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(TotalTax totalTax) {
        this.totalTax = totalTax;
    }

    public PriceAndConversionModel getYM() {
        return YM;
    }

    public void setYM(PriceAndConversionModel YM) {
        this.YM = YM;
    }

    public PriceAndConversionModel getF2() {
        return F2;
    }

    public void setF2(PriceAndConversionModel f2) {
        F2 = f2;
    }

    private PriceAndConversionModel supplierCommission  ;



}
