package com.rezofy.controllers;

import android.content.Context;

import com.rezofy.models.request_models.ModelFlightSearch;

import java.util.List;

/**
 * Created by linchpin11192 on 07-Jan-2016.
 */
/*public class SearchAirportController {

    private static List<ModelFlightSearch> defaultPlaceList;

    public static List<ModelFlightSearch> getSearchedFlightList(Context context, String input) {
        DatabaseController databaseController = DatabaseController.getInstance(context);
        int chk = 0;
        int chk1 = 0;
        String airCode = "";
        input = input.toUpperCase();
        Pattern sPattern = Pattern.compile("[^A-Za-z0-9]");
        boolean mMatches = sPattern.matcher(input).matches();
        if (!mMatches) {
            chk = databaseController.getAirportCount("'" + input + "'");
            chk1 = databaseController.getCountryAndCityNameCount("'" + input + "'");
            if (chk >= 1)
                return databaseController.getSearchListWithChk("'" + input + "'");

            else if (chk1 >= 1)
                return databaseController.getSearchListWithChk1("'" + input + "'");

            else if (databaseController.getCountryAndCityCount("'" + input + "'") >= 1)
                return databaseController.getSearchListOnCountryCityCount("'" + input + "'");

            else if (databaseController.getCountOnAliasName("'" + input + "'") >= 1)
                return databaseController.getSearchListIfAliasName("'" + input + "'");

            else if (databaseController.getCountOnRegularExpression("'" + input + "'") >= 1)
                return databaseController.getSearchListOnRegularExpression("'" + input + "'");
        }
        return null;
    }

    public static List<ModelFlightSearch> getDefaultPlaceList(Context context) {
        if (defaultPlaceList == null)
            defaultPlaceList = getSearchedFlightList(context, "");
        return defaultPlaceList;
    }
}*/

public class SearchAirportController {

    private static List<ModelFlightSearch> defaultPlaceList;

    public static List<ModelFlightSearch> getSearchedFlightList(Context context, String input) {
        String inputString = input.toUpperCase();
        DatabaseController databaseController = DatabaseController.getInstance(context);
        return databaseController.getAirportNames(inputString);

    }

    public static List<ModelFlightSearch> getCountrySearchedFlightList(Context context, String input, String countryName) {
        String inputString = input.toUpperCase();
        DatabaseController databaseController = DatabaseController.getInstance(context);
        return databaseController.getCountryAirportNames(inputString, countryName);

    }

    public static List<ModelFlightSearch> getDefaultPlaceList(Context context) {
        DatabaseController databaseController = DatabaseController.getInstance(context);
        defaultPlaceList = databaseController.getAirportNamesDefaultList();
        return defaultPlaceList;
    }

    public static List<ModelFlightSearch> getCountryPlaceList(Context context, String countryName) {
        DatabaseController databaseController = DatabaseController.getInstance(context);
        defaultPlaceList = databaseController.getAirportNamesCountryList(countryName);
        return defaultPlaceList;
    }
}
