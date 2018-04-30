package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by linchpin11192 on 25-Jan-2016.
 */
public class TripDetail implements Serializable {
    private int id;
    private int tripId;
    private String refundAmount;
    private String lastModifiedDate;
    private String cancellationCharge;
    private String gdsCancellationCharge;
    private String serviceTaxOnCancellation;
    private String status;
    private String remarks;
    private String adminCancellationCharge;
    private String agencyBookingAmount;
    private int crnNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCancellationCharge() {
        return cancellationCharge;
    }

    public void setCancellationCharge(String cancellationCharge) {
        this.cancellationCharge = cancellationCharge;
    }

    public String getGdsCancellationCharge() {
        return gdsCancellationCharge;
    }

    public void setGdsCancellationCharge(String gdsCancellationCharge) {
        this.gdsCancellationCharge = gdsCancellationCharge;
    }

    public String getServiceTaxOnCancellation() {
        return serviceTaxOnCancellation;
    }

    public void setServiceTaxOnCancellation(String serviceTaxOnCancellation) {
        this.serviceTaxOnCancellation = serviceTaxOnCancellation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAdminCancellationCharge() {
        return adminCancellationCharge;
    }

    public void setAdminCancellationCharge(String adminCancellationCharge) {
        this.adminCancellationCharge = adminCancellationCharge;
    }

    public String getAgencyBookingAmount() {
        return agencyBookingAmount;
    }

    public void setAgencyBookingAmount(String agencyBookingAmount) {
        this.agencyBookingAmount = agencyBookingAmount;
    }

    public int getCrnNo() {
        return crnNo;
    }

    public void setCrnNo(int crnNo) {
        this.crnNo = crnNo;
    }
}
