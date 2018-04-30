package com.rezofy.models.ui;

/**
 * Created by linchpin on 16/2/17.
 */

public class TransferVoucher {
    String bookingRefNo, source, journeyDate,destination,pnr,name, transferProvider, providerContactNo, reportTime, receiptNo, travellerCount, baseFare, tax, total;

    public TransferVoucher(String bookingRefNo, String source, String journeyDate, String destination, String pnr, String name, String transferProvider, String providerContactNo, String reportTime, String receiptNo, String travellerCount, String baseFare, String tax, String total) {
        this.bookingRefNo = bookingRefNo;
        this.source = source;
        this.journeyDate = journeyDate;
        this.destination = destination;
        this.pnr = pnr;
        this.name = name;
        this.transferProvider = transferProvider;
        this.providerContactNo = providerContactNo;
        this.reportTime = reportTime;
        this.receiptNo = receiptNo;
        this.travellerCount = travellerCount;
        this.baseFare = baseFare;
        this.tax = tax;
        this.total = total;
    }

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransferProvider() {
        return transferProvider;
    }

    public void setTransferProvider(String transferProvider) {
        this.transferProvider = transferProvider;
    }

    public String getProviderContactNo() {
        return providerContactNo;
    }

    public void setProviderContactNo(String providerContactNo) {
        this.providerContactNo = providerContactNo;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getTravellerCount() {
        return travellerCount;
    }

    public void setTravellerCount(String travellerCount) {
        this.travellerCount = travellerCount;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
