package com.rezofy.models.response_models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linchpin on 24/1/17.
 */

public class HotelTrip implements Serializable {

    String t_Id, destination, startDate, endDate, pnr, bookingDate, email_id, user_id, status, remarks, totalPrice, basePrice, tax, transactionCharge, grandTotal, bookedByName, bookingRefNo;
    SelectedResult selectedResult;

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
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

    public class SelectedResult implements Serializable{

        String name;
        String image;
        String address;
        String city;
        String country;

        ArrayList<SelectedRooms> selectedRooms;


        public ArrayList<SelectedRooms> getSelectedRooms() {
            return selectedRooms;
        }

        public void setSelectedRooms(ArrayList<SelectedRooms> selectedRooms) {
            this.selectedRooms = selectedRooms;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public class SelectedRooms  implements Serializable
    {

        private SelectedRoomInfo selectedRoomInfo;

        public SelectedRoomInfo getSelectedRoomInfo() {
            return selectedRoomInfo;
        }

        public void setSelectedRoomInfo(SelectedRoomInfo selectedRoomInfo) {
            this.selectedRoomInfo = selectedRoomInfo;
        }


    }

    public class Guest implements Serializable
    {
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

    public class SelectedRoomInfo implements Serializable{

        String category;
        int noOfAdults,  noOfChildren, noOfInfant, noOfSeniors;
        List<Guest> guests;

        public List<Guest> getGuests() {
            return guests;
        }

        public void setGuests(List<Guest> guests) {
            this.guests = guests;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getNoOfAdults() {
            return noOfAdults;
        }

        public void setNoOfAdults(int noOfAdults) {
            this.noOfAdults = noOfAdults;
        }

        public int getNoOfChildren() {
            return noOfChildren;
        }

        public void setNoOfChildren(int noOfChildren) {
            this.noOfChildren = noOfChildren;
        }

        public int getNoOfInfant() {
            return noOfInfant;
        }

        public void setNoOfInfant(int noOfInfant) {
            this.noOfInfant = noOfInfant;
        }

        public int getNoOfSeniors() {
            return noOfSeniors;
        }

        public void setNoOfSeniors(int noOfSeniors) {
            this.noOfSeniors = noOfSeniors;
        }
    }
}
