package com.rezofy.models.response_models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by LINCHPIN_02 on 24-12-2015.
 */
public class FlightData implements Serializable, Cloneable {
    private List<Segment> segments;
    private String carrier="";
    private String validatingCarrier;
    private String serviceClassCode;
    private Fare fare;
    private boolean specialReturnFare;
    private boolean multiCarrier;
    private boolean privateFare;
    private boolean couponFare;
    private List<Baggages> baggages;
    private boolean staticFareRule;
    private boolean isBaggageAvailable;
    private int duration;
    private int index;
    private String uniqueEntityId;
    private boolean rejectable;
    private boolean noRefund;
    private boolean nonCommissionable;
    private String repriceDiff;
    private boolean priceChanged;
    private String airlinePnr;
    private boolean isReprice;
    private String tripType;
    private boolean isSSRSelected;
    private boolean fpMissing;
    private boolean isCorporate;
    private boolean isSplitModification;
    private boolean isModified;
    private String flightSearchType;
    private String dataSource;
    private Markup appliedMarkup;
    private AppliedCommissionRule appliedCommissionRule;
    private String transactionID;
    private Markup affiliateMarkup;
    //private Taxes taxes;
    private Price actualTotal;
    private Price total;
    private Price basePrice;
    private Price totalTax;
    private String additionalRequest;
    private String originCountryCode;
    private String destinationCountryCode;
    private String pricingSource;
    private Map<String, OtherCharge> applicableOtherCharges;
    private Wallet wallet;

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getValidatingCarrier() {
        return validatingCarrier;
    }

    public void setValidatingCarrier(String validatingCarrier) {
        this.validatingCarrier = validatingCarrier;
    }

    public String getServiceClassCode() {
        return serviceClassCode;
    }

    public void setServiceClassCode(String serviceClassCode) {
        this.serviceClassCode = serviceClassCode;
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    public boolean isSpecialReturnFare() {
        return specialReturnFare;
    }

    public void setSpecialReturnFare(boolean specialReturnFare) {
        this.specialReturnFare = specialReturnFare;
    }

    public boolean isMultiCarrier() {
        return multiCarrier;
    }

    public void setMultiCarrier(boolean multiCarrier) {
        this.multiCarrier = multiCarrier;
    }

    public boolean isPrivateFare() {
        return privateFare;
    }

    public void setPrivateFare(boolean privateFare) {
        this.privateFare = privateFare;
    }

    public boolean isCouponFare() {
        return couponFare;
    }

    public void setCouponFare(boolean couponFare) {
        this.couponFare = couponFare;
    }

    public List<Baggages> getBaggages() {
        return baggages;
    }

    public void setBaggages(List<Baggages> baggages) {
        this.baggages = baggages;
    }

    public boolean isStaticFareRule() {
        return staticFareRule;
    }

    public void setStaticFareRule(boolean staticFareRule) {
        this.staticFareRule = staticFareRule;
    }

    public boolean isBaggageAvailable() {
        return isBaggageAvailable;
    }

    public void setIsBaggageAvailable(boolean isBaggageAvailable) {
        this.isBaggageAvailable = isBaggageAvailable;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUniqueEntityId() {
        return uniqueEntityId;
    }

    public void setUniqueEntityId(String uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    public boolean isRejectable() {
        return rejectable;
    }

    public void setRejectable(boolean rejectable) {
        this.rejectable = rejectable;
    }

    public boolean isNoRefund() {
        return noRefund;
    }

    public void setNoRefund(boolean noRefund) {
        this.noRefund = noRefund;
    }

    public boolean isNonCommissionable() {
        return nonCommissionable;
    }

    public void setNonCommissionable(boolean nonCommissionable) {
        this.nonCommissionable = nonCommissionable;
    }

    public String getRepriceDiff() {
        return repriceDiff;
    }

    public void setRepriceDiff(String repriceDiff) {
        this.repriceDiff = repriceDiff;
    }

    public void setBaggageAvailable(boolean baggageAvailable) {
        isBaggageAvailable = baggageAvailable;
    }

    public boolean isPriceChanged() {
        return priceChanged;
    }

    public void setPriceChanged(boolean priceChanged) {
        this.priceChanged = priceChanged;
    }

    public String getAirlinePnr() {
        return airlinePnr;
    }

    public void setAirlinePnr(String airlinePnr) {
        this.airlinePnr = airlinePnr;
    }

    public boolean isReprice() {
        return isReprice;
    }

    public void setReprice(boolean reprice) {
        isReprice = reprice;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public boolean isSSRSelected() {
        return isSSRSelected;
    }

    public void setSSRSelected(boolean SSRSelected) {
        isSSRSelected = SSRSelected;
    }

    public boolean isFpMissing() {
        return fpMissing;
    }

    public void setFpMissing(boolean fpMissing) {
        this.fpMissing = fpMissing;
    }

    public boolean isCorporate() {
        return isCorporate;
    }

    public void setCorporate(boolean corporate) {
        isCorporate = corporate;
    }

    public boolean isSplitModification() {
        return isSplitModification;
    }

    public void setSplitModification(boolean splitModification) {
        isSplitModification = splitModification;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public String getFlightSearchType() {
        return flightSearchType;
    }

    public void setFlightSearchType(String flightSearchType) {
        this.flightSearchType = flightSearchType;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Markup getAppliedMarkup() {
        return appliedMarkup;
    }

    public void setAppliedMarkup(Markup appliedMarkup) {
        this.appliedMarkup = appliedMarkup;
    }

    public AppliedCommissionRule getAppliedCommissionRule() {
        return appliedCommissionRule;
    }

    public void setAppliedCommissionRule(AppliedCommissionRule appliedCommissionRule) {
        this.appliedCommissionRule = appliedCommissionRule;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public Markup getAffiliateMarkup() {
        return affiliateMarkup;
    }

    public void setAffiliateMarkup(Markup affiliateMarkup) {
        this.affiliateMarkup = affiliateMarkup;
    }

    public Price getActualTotal() {
        return actualTotal;
    }

    public void setActualTotal(Price actualTotal) {
        this.actualTotal = actualTotal;
    }

    public Price getTotal() {
        return total;
    }

    public void setTotal(Price total) {
        this.total = total;
    }

    public Price getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Price basePrice) {
        this.basePrice = basePrice;
    }

    public Price getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Price totalTax) {
        this.totalTax = totalTax;
    }

    public String getAdditionalRequest() {
        return additionalRequest;
    }

    public void setAdditionalRequest(String additionalRequest) {
        this.additionalRequest = additionalRequest;
    }

    public String getOriginCountryCode() {
        return originCountryCode;
    }

    public void setOriginCountryCode(String originCountryCode) {
        this.originCountryCode = originCountryCode;
    }

    public String getDestinationCountryCode() {
        return destinationCountryCode;
    }

    public void setDestinationCountryCode(String destinationCountryCode) {
        this.destinationCountryCode = destinationCountryCode;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        FlightData clonedFlightData = (FlightData) super.clone();
        for (int i = 0; i < clonedFlightData.getSegments().size(); i++)
            clonedFlightData.getSegments().set(i, (Segment) clonedFlightData.getSegments().get(i).clone());
        return clonedFlightData;
    }

    public String getPricingSource() {
        return pricingSource;
    }

    public void setPricingSource(String pricingSource) {
        this.pricingSource = pricingSource;
    }

    public Map<String, OtherCharge> getApplicableOtherCharges() {
        return applicableOtherCharges;
    }

    public void setApplicableOtherCharges(Map<String, OtherCharge> applicableOtherCharges) {
        this.applicableOtherCharges = applicableOtherCharges;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
