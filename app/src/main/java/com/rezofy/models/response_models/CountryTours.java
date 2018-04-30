package com.rezofy.models.response_models;

/**
 * Created by anuj on 13/1/17.
 */
public class CountryTours {
    private String ID;
    private String city_name;
    private String country_name;
    private String discount_rate;
    private String duration;
    private String email;
    private String getselectedtoursdetails;
    private String name;
    private String phonenumber;
    private String post_modified;
    private String security_deposit_amount;
    private String starting_price;
    private String thumbnail;
    private String trav_tour_neighborhood;
    private boolean isFav;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(String discount_rate) {
        this.discount_rate = discount_rate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGetselectedtoursdetails() {
        return getselectedtoursdetails;
    }

    public void setGetselectedtoursdetails(String getselectedtoursdetails) {
        this.getselectedtoursdetails = getselectedtoursdetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPost_modified() {
        return post_modified;
    }

    public void setPost_modified(String post_modified) {
        this.post_modified = post_modified;
    }

    public String getSecurity_deposit_amount() {
        return security_deposit_amount;
    }

    public void setSecurity_deposit_amount(String security_deposit_amount) {
        this.security_deposit_amount = security_deposit_amount;
    }

    public String getStarting_price() {
        return starting_price;
    }

    public void setStarting_price(String starting_price) {
        this.starting_price = starting_price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTrav_tour_neighborhood() {
        return trav_tour_neighborhood;
    }

    public void setTrav_tour_neighborhood(String trav_tour_neighborhood) {
        this.trav_tour_neighborhood = trav_tour_neighborhood;
    }
    public Boolean getIsFav() {
        return isFav;
    }

    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }
}
