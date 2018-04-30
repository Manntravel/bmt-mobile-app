package com.rezofy.models.request_models;

import java.io.Serializable;

/**
 * Created by linchpin11192 on 24-Jan-2016.
 */
public class DateInfo implements Serializable {
    private String month="";
    private String year="";
    private String day="";
    private String date="";

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    @Override
//    public String toString() {
//        return this.day+"-"+this.month+"-"+this.year;
//    }
}
