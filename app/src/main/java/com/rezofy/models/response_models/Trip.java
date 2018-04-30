package com.rezofy.models.response_models;

import com.rezofy.models.request_models.BillingInfo;
import com.rezofy.models.request_models.Traveller;

import java.io.Serializable;
import java.util.List;

/**
 * Created by linchpin11192 on 25-Jan-2016.
 */
public class Trip implements Serializable {
    private int t_Id;
    private String origin;
    private String destination;
    private String startDate;
    private String endDate;
    private String pnr;
    private String type;
    private String bookingDate;
    private String email_id;
    private String user_id;
    private String status;
    private String remarks;
    private String gds;
    private FlightData selectedResult;
    private String totalPrice;
    private String basePrice;
    private String tax;
    private String vendorTotal;
    private String contactphone;
    private String currencyType;
    private List<Traveller> travellers;
    private String affiliateId;
    private String markUpPrice;
    private String ticketDate;
    private String country;
    private String originName;
    private String lastName;
    private String discountAmount;
    private BillingInfo billingInfo;
    private int tripCount;
    private int flightCount;
    private int hotelCount;
    private int carCount;
    private int creditCardChargeFee;
    private String refundAmount;
    private String cancelCharges;
    private String grandTotal;
    private int activeTravellerCount;
    private String commissionEarned;
    private boolean activeStatus;
    private String tds;
    private String serviceTax;
    private String transactionCharge;
    private String totalBookingAmount;
    private TripDetail tripDetail;
    private String bookingRefNo;
    private String agencyBookingAmount;
    private String ticketNumbers;
    private int invoiceNo;
    private String agencyTotalRefund;
    private boolean paymentDone;
    private String transactionId;
    private TripHistory tripHistory;
    private String cancellationStatus;
    private String cancelRequestDate;
    private int totalRow;
    private String transientTotalPriceSum;
    private boolean isBusNotCancelled;
    private String sessionId;
    private String serviceClass;
    private String bookingType;
    private String bookedSupplier;
    private String domIntl;
    private boolean editable;
    private String purchaseInvoiceNum;
    private boolean cancelAllowed;

    public int getT_Id() {
        return t_Id;
    }

    public void setT_Id(int t_Id) {
        this.t_Id = t_Id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getGds() {
        return gds;
    }

    public void setGds(String gds) {
        this.gds = gds;
    }

    public FlightData getSelectedResult() {
        return selectedResult;
    }

    public void setSelectedResult(FlightData selectedResult) {
        this.selectedResult = selectedResult;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getVendorTotal() {
        return vendorTotal;
    }

    public void setVendorTotal(String vendorTotal) {
        this.vendorTotal = vendorTotal;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public List<Traveller> getTravellers() {
        return travellers;
    }

    public void setTravellers(List<Traveller> travellers) {
        this.travellers = travellers;
    }

    public String getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(String affiliateId) {
        this.affiliateId = affiliateId;
    }

    public String getMarkUpPrice() {
        return markUpPrice;
    }

    public void setMarkUpPrice(String markUpPrice) {
        this.markUpPrice = markUpPrice;
    }

    public String getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(String ticketDate) {
        this.ticketDate = ticketDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BillingInfo getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(BillingInfo billingInfo) {
        this.billingInfo = billingInfo;
    }

    public int getTripCount() {
        return tripCount;
    }

    public void setTripCount(int tripCount) {
        this.tripCount = tripCount;
    }

    public int getFlightCount() {
        return flightCount;
    }

    public void setFlightCount(int flightCount) {
        this.flightCount = flightCount;
    }

    public int getHotelCount() {
        return hotelCount;
    }

    public void setHotelCount(int hotelCount) {
        this.hotelCount = hotelCount;
    }

    public int getCarCount() {
        return carCount;
    }

    public void setCarCount(int carCount) {
        this.carCount = carCount;
    }

    public int getCreditCardChargeFee() {
        return creditCardChargeFee;
    }

    public void setCreditCardChargeFee(int creditCardChargeFee) {
        this.creditCardChargeFee = creditCardChargeFee;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getCancelCharges() {
        return cancelCharges;
    }

    public void setCancelCharges(String cancelCharges) {
        this.cancelCharges = cancelCharges;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public int getActiveTravellerCount() {
        return activeTravellerCount;
    }

    public void setActiveTravellerCount(int activeTravellerCount) {
        this.activeTravellerCount = activeTravellerCount;
    }

    public String getCommissionEarned() {
        return commissionEarned;
    }

    public void setCommissionEarned(String commissionEarned) {
        this.commissionEarned = commissionEarned;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }

    public String getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(String serviceTax) {
        this.serviceTax = serviceTax;
    }

    public String getTransactionCharge() {
        return transactionCharge;
    }

    public void setTransactionCharge(String transactionCharge) {
        this.transactionCharge = transactionCharge;
    }

    public String getTotalBookingAmount() {
        return totalBookingAmount;
    }

    public void setTotalBookingAmount(String totalBookingAmount) {
        this.totalBookingAmount = totalBookingAmount;
    }

    public TripDetail getTripDetail() {
        return tripDetail;
    }

    public void setTripDetail(TripDetail tripDetail) {
        this.tripDetail = tripDetail;
    }

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }

    public String getAgencyBookingAmount() {
        return agencyBookingAmount;
    }

    public void setAgencyBookingAmount(String agencyBookingAmount) {
        this.agencyBookingAmount = agencyBookingAmount;
    }

    public String getTicketNumbers() {
        return ticketNumbers;
    }

    public void setTicketNumbers(String ticketNumbers) {
        this.ticketNumbers = ticketNumbers;
    }

    public int getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(int invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getAgencyTotalRefund() {
        return agencyTotalRefund;
    }

    public void setAgencyTotalRefund(String agencyTotalRefund) {
        this.agencyTotalRefund = agencyTotalRefund;
    }

    public boolean isPaymentDone() {
        return paymentDone;
    }

    public void setPaymentDone(boolean paymentDone) {
        this.paymentDone = paymentDone;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TripHistory getTripHistory() {
        return tripHistory;
    }

    public void setTripHistory(TripHistory tripHistory) {
        this.tripHistory = tripHistory;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public String getTransientTotalPriceSum() {
        return transientTotalPriceSum;
    }

    public void setTransientTotalPriceSum(String transientTotalPriceSum) {
        this.transientTotalPriceSum = transientTotalPriceSum;
    }

    public boolean isBusNotCancelled() {
        return isBusNotCancelled;
    }

    public void setBusNotCancelled(boolean busNotCancelled) {
        isBusNotCancelled = busNotCancelled;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getBookedSupplier() {
        return bookedSupplier;
    }

    public void setBookedSupplier(String bookedSupplier) {
        this.bookedSupplier = bookedSupplier;
    }

    public String getDomIntl() {
        return domIntl;
    }

    public void setDomIntl(String domIntl) {
        this.domIntl = domIntl;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getPurchaseInvoiceNum() {
        return purchaseInvoiceNum;
    }

    public void setPurchaseInvoiceNum(String purchaseInvoiceNum) {
        this.purchaseInvoiceNum = purchaseInvoiceNum;
    }

    public String getCancellationStatus() {
        return cancellationStatus;
    }

    public void setCancellationStatus(String cancellationStatus) {
        this.cancellationStatus = cancellationStatus;
    }

    public String getCancelRequestDate() {
        return cancelRequestDate;
    }

    public void setCancelRequestDate(String cancelRequestDate) {
        this.cancelRequestDate = cancelRequestDate;
    }

    public boolean isCancelAllowed() {
        return cancelAllowed;
    }

    public void setCancelAllowed(boolean cancelAllowed) {
        this.cancelAllowed = cancelAllowed;
    }
}
