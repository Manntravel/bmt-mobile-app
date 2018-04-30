package com.rezofy.models.ui;



/**
 * Created by linchpin on 15/2/17.
 */

public class TrainVoucher  {

    String trainName, pnr, bookingRefNo, source, destination, reservationId, arrivalTime, departureTime, status, coach, seat, passenger, ticketFare, irctcTax, agentServiceCharge, pgCharge, total, journeyDate;

    public TrainVoucher(String trainName, String pnr, String bookingRefNo, String source, String destination, String reservationId, String arrivalTime, String departureTime, String status, String coach, String seat, String passenger, String ticketFare, String irctcTax, String agentServiceCharge, String pgCharge, String total, String journeyDate) {
        this.trainName = trainName;
        this.pnr = pnr;
        this.bookingRefNo = bookingRefNo;
        this.source = source;
        this.destination = destination;
        this.reservationId = reservationId;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.status = status;
        this.coach = coach;
        this.seat = seat;
        this.passenger = passenger;
        this.ticketFare = ticketFare;
        this.irctcTax = irctcTax;
        this.agentServiceCharge = agentServiceCharge;
        this.pgCharge = pgCharge;
        this.total = total;
        this.journeyDate = journeyDate;

    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getPnr() {
        return pnr;
    }

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getStatus() {
        return status;
    }

    public String getCoach() {
        return coach;
    }

    public String getSeat() {
        return seat;
    }

    public String getPassenger() {
        return passenger;
    }

    public String getTicketFare() {
        return ticketFare;
    }

    public String getIrctcTax() {
        return irctcTax;
    }

    public String getAgentServiceCharge() {
        return agentServiceCharge;
    }

    public String getPgCharge() {
        return pgCharge;
    }

    public String getTotal() {
        return total;
    }
}
