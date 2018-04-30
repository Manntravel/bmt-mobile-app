package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by Rakhi Mittal on 16-Feb-18.
 */

public class Wallet implements Serializable {
    private int id;
    private String userId;
    private String phoneNumber;
    private String ecashAmount;
    private String cashAmount;
    private boolean status;
    private String createdDate;
    private String updatedDate;
    private boolean utilizeCash;
    private boolean utilizeEcash;
    private String utilizeCashBalance;
    private String utilizeEcashBalance;
    private String remainingBalance;
    private String pendingAmount;
    private String walletEcashLimit;
    private String totalBalance;
    private String cashBackInCash;
    private String cashBackInECash;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEcashAmount() {
        return ecashAmount;
    }

    public void setEcashAmount(String ecashAmount) {
        this.ecashAmount = ecashAmount;
    }

    public String getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(String cashAmount) {
        this.cashAmount = cashAmount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isUtilizeCash() {
        return utilizeCash;
    }

    public void setUtilizeCash(boolean utilizeCash) {
        this.utilizeCash = utilizeCash;
    }

    public boolean isUtilizeEcash() {
        return utilizeEcash;
    }

    public void setUtilizeEcash(boolean utilizeEcash) {
        this.utilizeEcash = utilizeEcash;
    }

    public String getUtilizeCashBalance() {
        return utilizeCashBalance;
    }

    public void setUtilizeCashBalance(String utilizeCashBalance) {
        this.utilizeCashBalance = utilizeCashBalance;
    }

    public String getUtilizeEcashBalance() {
        return utilizeEcashBalance;
    }

    public void setUtilizeEcashBalance(String utilizeEcashBalance) {
        this.utilizeEcashBalance = utilizeEcashBalance;
    }

    public String getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(String remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(String pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getWalletEcashLimit() {
        return walletEcashLimit;
    }

    public void setWalletEcashLimit(String walletEcashLimit) {
        this.walletEcashLimit = walletEcashLimit;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getCashBackInCash() {
        return cashBackInCash;
    }

    public void setCashBackInCash(String cashBackInCash) {
        this.cashBackInCash = cashBackInCash;
    }

    public String getCashBackInECash() {
        return cashBackInECash;
    }

    public void setCashBackInECash(String cashBackInECash) {
        this.cashBackInECash = cashBackInECash;
    }
}
