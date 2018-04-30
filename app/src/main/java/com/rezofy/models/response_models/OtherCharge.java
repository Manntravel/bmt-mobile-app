package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by Rakhi Mittal on 15-Feb-18.
 */

public class OtherCharge implements Serializable {
    private String chargeType;
    private String amount;
    private boolean walletChargeRequested;
    private String fareComponentKey;
    private String creationDate;
    private String remarks;
    private boolean selected;

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isWalletChargeRequested() {
        return walletChargeRequested;
    }

    public void setWalletChargeRequested(boolean walletChargeRequested) {
        this.walletChargeRequested = walletChargeRequested;
    }

    public String getFareComponentKey() {
        return fareComponentKey;
    }

    public void setFareComponentKey(String fareComponentKey) {
        this.fareComponentKey = fareComponentKey;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
