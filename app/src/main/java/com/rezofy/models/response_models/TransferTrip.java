package com.rezofy.models.response_models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by linchpin on 16/2/17.
 */

public class TransferTrip implements Serializable {


   private String origin, destination, pnr, status, invoiceNo, contactphone, bookingRefNo;

    private SelectedResult selectedResult;


    private List<Travellers> travellers;

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

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }

    public SelectedResult getSelectedResult() {
        return selectedResult;
    }

    public void setSelectedResult(SelectedResult selectedResult) {
        this.selectedResult = selectedResult;
    }

    public List<Travellers> getTravellers() {
        return travellers;
    }

    public void setTravellers(List<Travellers> travellers) {
        this.travellers = travellers;
    }

    public class SelectedResult implements Serializable{
        private String outboundSource, outboundDateTime, noOfTravellers, transferProvider;
        private Fare fare;

        public String getOutboundSource() {
            return outboundSource;
        }

        public void setOutboundSource(String outboundSource) {
            this.outboundSource = outboundSource;
        }

        public String getOutboundDateTime() {
            return outboundDateTime;
        }

        public void setOutboundDateTime(String outboundDateTime) {
            this.outboundDateTime = outboundDateTime;
        }

        public String getNoOfTravellers() {
            return noOfTravellers;
        }

        public void setNoOfTravellers(String noOfTravellers) {
            this.noOfTravellers = noOfTravellers;
        }

        public String getTransferProvider() {
            return transferProvider;
        }

        public void setTransferProvider(String transferProvider) {
            this.transferProvider = transferProvider;
        }

        public Fare getFare() {
            return fare;
        }

        public void setFare(Fare fare) {
            this.fare = fare;
        }
    }

    public  class Fare implements Serializable
    {
        private FareDetail total;

        private FareDetail base;

        public FareDetail getTotal() {
            return total;
        }

        public void setTotal(FareDetail total) {
            this.total = total;
        }

        public FareDetail getBase() {
            return base;
        }

        public void setBase(FareDetail base) {
            this.base = base;
        }
    }

    public  class FareDetail implements Serializable
    {
        private Price price;

        public Price getPrice() {
            return price;
        }

        public void setPrice(Price price) {
            this.price = price;
        }
    }
    public  class Price implements Serializable
    {
       private String amount, roundOff;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getRoundOff() {
            return roundOff;
        }

        public void setRoundOff(String roundOff) {
            this.roundOff = roundOff;
        }
    }

    public class Travellers implements Serializable
    {
        private String firstName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    }
}
