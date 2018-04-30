package com.rezofy.models.request_models;

import com.rezofy.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linchpin11192 on 24-Dec-2015.
 */
public class Traveller implements Serializable {
    //private int id;
    private String firstName;
    private String lastName;
    private String title;
    private String middleName = "";
    private String mealPref = Utils.OPTION_NP;
    private String specialPref = Utils.OPTION_NP;
    private String travellerType;
    private String type;
    //private String primaryContact;
    //private int tripProtectionValue;
    private DateInfo dateOfBirth;
    private Passport passport;
    //private PaxFare paxFare;
    //private List<SelectedSeat> selectedSeats;
    //private List<FrequentFlyer> frequentFlyerList;
    //private boolean extraSeatNeeded;
    //private int noOfBookings;
    //private List<SelectedBaggage> selectedBaggages;
    //private List<SegmentTicket> segmentTickets;
    private List<SelectedMeal> selectedmeal = new ArrayList<>();
    //private boolean isSSRSelected;

/*    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMealPref() {
        return mealPref;
    }

    public void setMealPref(String mealPref) {
        this.mealPref = mealPref;
    }

    public String getSpecialPref() {
        return specialPref;
    }

    public void setSpecialPref(String specialPref) {
        this.specialPref = specialPref;
    }

    public String getTravellerType() {
        return travellerType;
    }

    public void setTravellerType(String travellerType) {
        this.travellerType = travellerType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

/*    public String getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(String primaryContact) {
        this.primaryContact = primaryContact;
    }

    public int getTripProtectionValue() {
        return tripProtectionValue;
    }

    public void setTripProtectionValue(int tripProtectionValue) {
        this.tripProtectionValue = tripProtectionValue;
    }*/

    public DateInfo getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(DateInfo dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

/*    public List<SelectedSeat> getSelectedSeats() {
        return selectedSeats;
    }

    public void setSelectedSeats(List<SelectedSeat> selectedSeats) {
        this.selectedSeats = selectedSeats;
    }

    public List<FrequentFlyer> getFrequentFlyerList() {
        return frequentFlyerList;
    }

    public void setFrequentFlyerList(List<FrequentFlyer> frequentFlyerList) {
        this.frequentFlyerList = frequentFlyerList;
    }

    public boolean isExtraSeatNeeded() {
        return extraSeatNeeded;
    }

    public void setExtraSeatNeeded(boolean extraSeatNeeded) {
        this.extraSeatNeeded = extraSeatNeeded;
    }

    public int getNoOfBookings() {
        return noOfBookings;
    }

    public void setNoOfBookings(int noOfBookings) {
        this.noOfBookings = noOfBookings;
    }

    public List<SelectedBaggage> getSelectedBaggages() {
        return selectedBaggages;
    }

    public void setSelectedBaggages(List<SelectedBaggage> selectedBaggages) {
        this.selectedBaggages = selectedBaggages;
    }*/

    public List<SelectedMeal> getSelectedmeal() {
        return selectedmeal;
    }

    public void setSelectedmeal(List<SelectedMeal> selectedmeal) {
        this.selectedmeal = selectedmeal;
    }

    /*   public PaxFare getPaxFare() {
        return paxFare;
    }

    public void setPaxFare(PaxFare paxFare) {
        this.paxFare = paxFare;
    }

        public List<SegmentTicket> getSegmentTickets() {
        return segmentTickets;
    }

    public void setSegmentTickets(List<SegmentTicket> segmentTickets) {
        this.segmentTickets = segmentTickets;
    }

        public boolean isSSRSelected() {
        return isSSRSelected;
    }

    public void setSSRSelected(boolean SSRSelected) {
        isSSRSelected = SSRSelected;
    }*/

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }
}
