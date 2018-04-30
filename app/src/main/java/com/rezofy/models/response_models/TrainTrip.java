package com.rezofy.models.response_models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by linchpin on 15/2/17.
 */
public class TrainTrip implements Serializable{

    String origin, destination, startDate, pnr, status,  totalPrice, bookingRefNo, ticketNumbers;
    SelectedResult selectedResult;
    Boolean paymentDone;

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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }

    public String getTicketNumbers() {
        return ticketNumbers;
    }

    public void setTicketNumbers(String ticketNumbers) {
        this.ticketNumbers = ticketNumbers;
    }

    public SelectedResult getSelectedResult() {
        return selectedResult;
    }

    public void setSelectedResult(SelectedResult selectedResult) {
        this.selectedResult = selectedResult;
    }

    public Boolean getPaymentDone() {
        return paymentDone;
    }

    public void setPaymentDone(Boolean paymentDone) {
        this.paymentDone = paymentDone;
    }

    public class SelectedResult implements Serializable
    {
        String trainNumber, trainName, originStationCode, originStationName, destinationStationCode, destinationStationName, arrivalTime, departureTime, journeyDate;

        TrainFare fare;

        public String getJourneyDate() {
            return journeyDate;
        }

        public void setJourneyDate(String journeyDate) {
            this.journeyDate = journeyDate;
        }

        public String getTrainNumber() {
            return trainNumber;
        }

        public void setTrainNumber(String trainNumber) {
            this.trainNumber = trainNumber;
        }

        public String getTrainName() {
            return trainName;
        }

        public void setTrainName(String trainName) {
            this.trainName = trainName;
        }

        public String getOriginStationCode() {
            return originStationCode;
        }

        public void setOriginStationCode(String originStationCode) {
            this.originStationCode = originStationCode;
        }

        public String getOriginStationName() {
            return originStationName;
        }

        public void setOriginStationName(String originStationName) {
            this.originStationName = originStationName;
        }

        public String getDestinationStationCode() {
            return destinationStationCode;
        }

        public void setDestinationStationCode(String destinationStationCode) {
            this.destinationStationCode = destinationStationCode;
        }

        public String getDestinationStationName() {
            return destinationStationName;
        }

        public void setDestinationStationName(String destinationStationName) {
            this.destinationStationName = destinationStationName;
        }

        public String getArrivalTime() {
            return arrivalTime;
        }

        public void setArrivalTime(String arrivalTime) {
            this.arrivalTime = arrivalTime;
        }

        public String getDepartureTime() {
            return departureTime;
        }

        public void setDepartureTime(String departureTime) {
            this.departureTime = departureTime;
        }

        public TrainFare getFare() {
            return fare;
        }

        public void setFare(TrainFare fare) {
            this.fare = fare;
        }
    }


    public  class TrainFare implements Serializable
    {
        TrainFareDetail total;

        TrainFareTaxes taxes;

        public TrainFareDetail getTotal() {
            return total;
        }

        public void setTotal(TrainFareDetail total) {
            this.total = total;
        }

        public TrainFareTaxes getTaxes() {
            return taxes;
        }

        public void setTaxes(TrainFareTaxes taxes) {
            this.taxes = taxes;
        }
    }

    public  class TrainFareDetail implements Serializable
    {
        TrainFarePrice price;

        public TrainFarePrice getPrice() {
            return price;
        }

        public void setPrice(TrainFarePrice price) {
            this.price = price;
        }
    }
    public  class TrainFarePrice implements Serializable
    {
        String amount, roundOff;

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
    public class TrainFareTaxes implements Serializable
    {
        @SerializedName("fare.code.ticket.fare")
        TrainFareDetail ticketFare;

        @SerializedName("fare.code.agent.service.charge")
        TrainFareDetail agentServiceCharge;

        @SerializedName("fare.code.irctc.total.servictaxcharge")
        TrainFareDetail irctcCharge;

        @SerializedName("fare.code.payment.gateway.charge")
        TrainFareDetail pgCharge;

        public TrainFareDetail getTicketFare() {
            return ticketFare;
        }

        public void setTicketFare(TrainFareDetail ticketFare) {
            this.ticketFare = ticketFare;
        }

        public TrainFareDetail getAgentServiceCharge() {
            return agentServiceCharge;
        }

        public void setAgentServiceCharge(TrainFareDetail agentServiceCharge) {
            this.agentServiceCharge = agentServiceCharge;
        }

        public TrainFareDetail getIrctcCharge() {
            return irctcCharge;
        }

        public void setIrctcCharge(TrainFareDetail irctcCharge) {
            this.irctcCharge = irctcCharge;
        }

        public TrainFareDetail getPgCharge() {
            return pgCharge;
        }

        public void setPgCharge(TrainFareDetail pgCharge) {
            this.pgCharge = pgCharge;
        }
    }
}

