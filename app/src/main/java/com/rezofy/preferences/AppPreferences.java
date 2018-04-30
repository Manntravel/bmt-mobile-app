package com.rezofy.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.rezofy.database.DbHelper;
import com.rezofy.models.response_models.LoginResponse;

/**
 * Created by LinchPin on 6/10/2015.
 */

//Singleton class for preferences
public class AppPreferences {
    private static final String APP_SHARED_PREFS = "com.rezofy"; //  Name of the file -.xml
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private static volatile AppPreferences appPreferences;

    public static AppPreferences getInstance(Context context) {
        if (appPreferences == null) {
            synchronized (AppPreferences.class) {
                if (appPreferences == null)
                    appPreferences = new AppPreferences(context);
            }
        }
        return appPreferences;
    }

    private AppPreferences(Context context) {
        appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        prefsEditor = appSharedPrefs.edit();
    }

    public void setToken(String token) {
        prefsEditor.putString("token", token);
        prefsEditor.commit();
    }

    public String getToken() {
        return appSharedPrefs.getString("token", "");

    }

    public void setCreditLimit(String creditLimit) {
        prefsEditor.putString("creditLimit", creditLimit);
        prefsEditor.commit();
    }

    public String getCreditLimit() {
        return this.appSharedPrefs.getString("creditLimit", "");

    }

    public void setUserData(String userData) {
        prefsEditor.putString("userData", userData);
        prefsEditor.commit();
    }

    public String getUserData() {
        return this.appSharedPrefs.getString("userData", "");

    }

    public void setCashLimit(String cashLimit) {
        prefsEditor.putString("cashLimit", cashLimit);
        prefsEditor.commit();
    }

    public String getCashLimit() {
        return this.appSharedPrefs.getString("cashLimit", "");

    }

    public void setIsExpired(boolean isExpired){
        prefsEditor.putBoolean("isExpired", isExpired);
        prefsEditor.commit();
    }

    public boolean getIsExpired() {
        return this.appSharedPrefs.getBoolean("isExpired", true);
    }

    public void isLoggedIn(boolean isLoggedIn) {
        prefsEditor.putBoolean("isLoggedIn", isLoggedIn);
        prefsEditor.commit();
    }

    public boolean getLogIn(Context context) {
        boolean loggedIn = false;
        if (this.appSharedPrefs.getBoolean("isLoggedIn", false)) {
            String userData = getUserData();
            if (!userData.isEmpty()) {
                LoginResponse loginResponse = new Gson().fromJson(userData, LoginResponse.class);
                if (loginResponse.getName().getFirst().contains("guest") || loginResponse.getName().getFirst().contains("Guest")) {
                    clearPreferences();
                    new DbHelper(context).deleteDB(context);
                    isLoggedIn(false);
                    loggedIn = false;
                } else {
                    loggedIn = true;
                }
            }
            else
            {
                isLoggedIn(false);
                loggedIn = false;
            }


        }

        return  loggedIn;
    }

    public void isB2B(boolean isB2B) {
        prefsEditor.putBoolean("isB2B", isB2B);
        prefsEditor.commit();
    }

    public boolean getB2B() {
        return this.appSharedPrefs.getBoolean("isB2B", false);

    }

    public void clearPreferences() {
        prefsEditor.clear().commit();
    }

    public void setBillingInfo(String json) {
        prefsEditor.putString("billingInfo", json);
        prefsEditor.commit();
    }

    public String getBillingInfo() {
        return this.appSharedPrefs.getString("billingInfo", "");

    }


    public void setGuestLogin(boolean value) {
        prefsEditor.putBoolean("guestLogin", value).apply();
    }

    public boolean isGuestLogin(){
        return this.appSharedPrefs.getBoolean("guestLogin", false);
    }
}