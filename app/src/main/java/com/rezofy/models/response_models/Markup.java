package com.rezofy.models.response_models;

import java.io.Serializable;

/**
 * Created by linchpin11192 on 25-Jan-2016.
 */
public class Markup implements Serializable {
    private int id;
    private String basefrom;
    private String baseto;
    private String baseamount;
    private String servicefrom;
    private String serviceto;
    private String serviceamount;
    private String bookingfrom;
    private String bookingto;
    private String bookingamount;
    private boolean perBooking;
    private boolean sortedByRout;
    private boolean sortedBySupplier;
    private boolean sortedByAirline;
    private int index;
    private String addOnMarkup;
    private String total;
    private boolean domestic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBasefrom() {
        return basefrom;
    }

    public void setBasefrom(String basefrom) {
        this.basefrom = basefrom;
    }

    public String getBaseto() {
        return baseto;
    }

    public void setBaseto(String baseto) {
        this.baseto = baseto;
    }

    public String getBaseamount() {
        return baseamount;
    }

    public void setBaseamount(String baseamount) {
        this.baseamount = baseamount;
    }

    public String getServicefrom() {
        return servicefrom;
    }

    public void setServicefrom(String servicefrom) {
        this.servicefrom = servicefrom;
    }

    public String getServiceto() {
        return serviceto;
    }

    public void setServiceto(String serviceto) {
        this.serviceto = serviceto;
    }

    public String getServiceamount() {
        return serviceamount;
    }

    public void setServiceamount(String serviceamount) {
        this.serviceamount = serviceamount;
    }

    public String getBookingfrom() {
        return bookingfrom;
    }

    public void setBookingfrom(String bookingfrom) {
        this.bookingfrom = bookingfrom;
    }

    public String getBookingto() {
        return bookingto;
    }

    public void setBookingto(String bookingto) {
        this.bookingto = bookingto;
    }

    public String getBookingamount() {
        return bookingamount;
    }

    public void setBookingamount(String bookingamount) {
        this.bookingamount = bookingamount;
    }

    public boolean isPerBooking() {
        return perBooking;
    }

    public void setPerBooking(boolean perBooking) {
        this.perBooking = perBooking;
    }

    public boolean isSortedByRout() {
        return sortedByRout;
    }

    public void setSortedByRout(boolean sortedByRout) {
        this.sortedByRout = sortedByRout;
    }

    public boolean isSortedBySupplier() {
        return sortedBySupplier;
    }

    public void setSortedBySupplier(boolean sortedBySupplier) {
        this.sortedBySupplier = sortedBySupplier;
    }

    public boolean isSortedByAirline() {
        return sortedByAirline;
    }

    public void setSortedByAirline(boolean sortedByAirline) {
        this.sortedByAirline = sortedByAirline;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAddOnMarkup() {
        return addOnMarkup;
    }

    public void setAddOnMarkup(String addOnMarkup) {
        this.addOnMarkup = addOnMarkup;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public boolean isDomestic() {
        return domestic;
    }

    public void setDomestic(boolean domestic) {
        this.domestic = domestic;
    }
}
