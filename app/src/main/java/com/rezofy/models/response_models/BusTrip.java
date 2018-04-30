package com.rezofy.models.response_models;

import java.io.Serializable;

import java.util.List;

/**
 * Created by linchpin on 30/1/17.
 */
public class BusTrip implements Serializable{
    String t_Id,origin, destination, startDate, toDate, pnr, bookingDate, email_id, user_id, status, remarks, totalPrice, basePrice, tax, transactionCharge, grandTotal, bookedByName, bookingRefNo, ticketNumbers;
    SelectedResult selectedResult;
    Boolean paymentDone;

    List<Traveller> travellers;


    public List<Traveller> getTravellers() {
        return travellers;
    }

    public void setTravellers(List<Traveller> travellers) {
        this.travellers = travellers;
    }

    public class SelectedResult implements Serializable{


        List<Segments> segments;

        public List<Segments> getSegments() {
            return segments;
        }

        public void setSegments(List<Segments> segments) {
            this.segments = segments;
        }
    }

    public class Segments  implements Serializable
    {
        Operator operator;

        List<SelectedSeats> selectedSeats;

        DepartDate departDate;

        DepartPoint departPoint;

        public Operator getOperator() {
            return operator;
        }

        public void setOperator(Operator operator) {
            this.operator = operator;
        }

        public List<SelectedSeats> getSelectedSeats() {
            return selectedSeats;
        }

        public void setSelectedSeats(List<SelectedSeats> selectedSeats) {
            this.selectedSeats = selectedSeats;
        }

        public DepartDate getDepartDate() {
            return departDate;
        }

        public void setDepartDate(DepartDate departDate) {
            this.departDate = departDate;
        }

        public DepartPoint getDepartPoint() {
            return departPoint;
        }

        public void setDepartPoint(DepartPoint departPoint) {
            this.departPoint = departPoint;
        }
    }

    public class DepartDate implements Serializable
    {
        String dateWithoutTime;
        String time;

        public String getDateWithoutTime() {
            return dateWithoutTime;
        }

        public void setDateWithoutTime(String dateWithoutTime) {
            this.dateWithoutTime = dateWithoutTime;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public class DepartPoint implements Serializable
    {
         String address, name;

        Time time;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Time getTime() {
            return time;
        }

        public void setTime(Time time) {
            this.time = time;
        }
    }

    public class Time implements Serializable
    {
        String dateWithoutTime;
        String time;

        public String getDateWithoutTime() {
            return dateWithoutTime;
        }

        public void setDateWithoutTime(String dateWithoutTime) {
            this.dateWithoutTime = dateWithoutTime;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public class Operator implements Serializable
    {
        String name, addressLine1;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddressLine1() {
            return addressLine1;
        }

        public void setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
        }
    }

    public class SelectedSeats implements Serializable
    {
        String number, type;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public class Traveller implements Serializable {
        String firstName, lastName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }

    public Boolean getPaymentDone() {
        return paymentDone;
    }

    public void setPaymentDone(Boolean paymentDone) {
        this.paymentDone = paymentDone;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getT_Id() {
        return t_Id;
    }

    public void setT_Id(String t_Id) {
        this.t_Id = t_Id;
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

    public String getTransactionCharge() {
        return transactionCharge;
    }

    public void setTransactionCharge(String transactionCharge) {
        this.transactionCharge = transactionCharge;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public SelectedResult getSelectedResult() {
        return selectedResult;
    }

    public void setSelectedResult(SelectedResult selectedResult) {
        this.selectedResult = selectedResult;
    }

    public String getBookedByName() {
        return bookedByName;
    }

    public void setBookedByName(String bookedByName) {
        this.bookedByName = bookedByName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }


    public String getTicketNumbers() {
        return ticketNumbers;
    }

    public void setTicketNumbers(String ticketNumbers) {
        this.ticketNumbers = ticketNumbers;
    }
}
