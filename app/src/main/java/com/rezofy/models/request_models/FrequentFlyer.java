package com.rezofy.models.request_models;

/**
 * Created by linchpin11192 on 24-Dec-2015.
 */
public class FrequentFlyer {
    private int id;
    private int t_Id;
    private String name;
    private String number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getT_Id() {
        return t_Id;
    }

    public void setT_Id(int t_Id) {
        this.t_Id = t_Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
