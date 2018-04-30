package com.rezofy.requests;

import android.text.Editable;

import com.google.gson.Gson;
import com.rezofy.models.request_models.BillingInfo;
import com.rezofy.models.request_models.Traveller;
import com.rezofy.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by linchpin11192 on 24-Dec-2015.
 */

public class Requests {


    //Login Request Parameters
    private static final String PR_LOGIN_ID = "id";
    private static final String PR_LOGIN_PASSWORD = "password";

    //One Way Search Request Parameters
    private static final String PR_REG_TITLE = "title";
    private static final String PR_REG_EMAIL = "email";
    private static final String PR_REG_PASSWORD = "password";
    private static final String PR_REG_MOBILE = "mobile";
    private static final String PR_REG_FIRST_NAME = "firstname";
    private static final String PR_REG_MIDDLE_NAME = "middlename";
    private static final String PR_REG_LAST_NAME = "lastname";
    private static final String PR_REG_BOOKING_AMT = "totalBookingAmount";
    private static final String PR_REG_CREATED_ON = "createdOn";
    private static final String PR_REG_GUEST_USER = "guestUser";
    private static final String PR_ONE_WAY_SEARCH_SEARCH_CRITERIA = "searchCriteria";
    private static final String PR_ONE_WAY_SEARCH_DEPART_DATE_TIME = "departDateTime";
    private static final String PR_ONE_WAY_SEARCH_ORIGIN_NAME = "originName";
    private static final String PR_ONE_WAY_SEARCH_DEST_NAME = "destinationName";
    private static final String PR_ONE_WAY_SEARCH_DATE_WO_TIME = "dateWithoutTime";
    private static final String PR_ONE_WAY_SEARCH_ADULTS = "noOfAdults";
    private static final String PR_ONE_WAY_SEARCH_MINORS = "noOfMinors";
    private static final String PR_ONE_WAY_SEARCH_INFANTS = "noOfInfants";
    private static final String PR_ONE_WAY_SEARCH_TRIP_TYPE = "tripType";
    private static final String PR_ONE_WAY_SEARCH_TYPE = "type";
    private static final String PR_ONE_WAY_SEARCH_CLASS_CODE = "serviceClassCode";
    private static final String PR_ONE_WAY_SEARCH_CARRIER_TYPE = "carrierType";

    //Round Trip Search Request Parameters
    private static final String PR_ROUND_TRIP_SEARCH_SEARCH_CRITERIA = "searchCriteria";
    private static final String PR_ROUND_TRIP_SEARCH_DEPART_DATE_TIME = "departDateTime";
    private static final String PR_ROUND_TRIP_SEARCH_RETURN_DATE_TIME = "returnDateTime";
    private static final String PR_ROUND_TRIP_SEARCH_ORIGIN_NAME = "originName";
    private static final String PR_ROUND_TRIP_SEARCH_DEST_NAME = "destinationName";
    private static final String PR_ROUND_TRIP_SEARCH_DATE_WO_TIME = "dateWithoutTime";
    private static final String PR_ROUND_TRIP_SEARCH_ADULTS = "noOfAdults";
    private static final String PR_ROUND_TRIP_SEARCH_MINORS = "noOfMinors";
    private static final String PR_ROUND_TRIP_SEARCH_INFANTS = "noOfInfants";
    private static final String PR_ROUND_TRIP_SEARCH_TRIP_TYPE = "tripType";
    private static final String PR_ROUND_TRIP_SEARCH_TYPE = "type";
    private static final String PR_ROUND_TRIP_SEARCH_CLASS_CODE = "serviceClassCode";
    private static final String PR_ROUND_TRIP_SEARCH_CARRIER_TYPE = "carrierType";


    //One Way Availability Parameters
    private static final String PR_ONE_WAY_AVL_SEARCH_ID = "searchId";
    private static final String PR_ONE_WAY_AVL_FLIGHT_ID = "selectedFlightId";

    //Booking Validate Parameters
    private static final String PR_BOOKING_VAL_SEARCH_ID = "searchId";
    private static final String PR_BOOKING_VAL_GATEWAY = "paymentGateway";
    private static final String PR_BOOKING_VAL_BILLING = "billingInfo";
    private static final String PR_BOOKING_VAL_TRAVELLERS = "travellers";
    private static final String PR_BOOKING_VAL_API = "apiBooking";
    //CashCard Validation Parameters
    private static final String PR_CARD_VAL_NUMBER = "number";
    private static final String PR_CARD_VAL_PIN = "pin";

    //Fare Rule Parameters
    private static final String PR_RULE_SEARCH_ID = "searchId";
    private static final String PR_RULE_FLIGHT_ID = "selectedFlightId";
    private static final String PR_BOOKING_REF_NO = "bookingRefNo";
    private static final String PR_BOOKING_INITIALIZE_SEARCH_ID = "searchId";
    private static final String PR_BOOKING_INITIALIZE_TOKEN = "token";
    private static final String PR_ROUND_TRIP_AVL_SEARCH_ID = "searchId";
    private static final String PR_ROUND_TRIP_AVL_FARE_RESULTS_TYPE = "fareResultsType";
    private static final String PR_ROUND_TRIP_SEL_IN_FLIGHT_ID = "selectedInboundFlightId";
    private static final String PR_ROUND_TRIP_SEL_OUT_FLIGHT_ID = "selectedOutboundFlightId";
    private static final String PR_ROUND_TRIP_AVL_FLIGHT_ID = "selectedFlightId";
    private static final String PR_ROUND_TRIP_SEL_TAB = "selectedTab";
    private static final String PR_SOCIAL_NETWORK_LOGIN = "socialNetworkLogin";
    private static final String PR_BOOKING_VAL_TICKET_REQUESTED = "ticketRequested";

    private static final String PR_PROMOCODE_SEARCH_ID = "searchId";
    private static final String PR_PROMOCODE_FLIGHT_ID = "selectedFlightId";
    private static final String PR_PROMOCODE_DISCOUNT = "discount";
    private static final String PR_PROMOCODE_DISCOUNT_CODE ="discountCode";
    private static final String PR_PROMOCODE_REMOVE ="remove" ;
    private static final String SSR_SEARCH_ID = "searchId";
    private static final String SSR_FLIGHT_ID = "selectedFlightId";
    private static final String SSR_TYPE = "ssrType";
    private static final String SSR_ADD_OTHER_CHARGES = "addOtherCharges";

    public static String loginRequest(String id, String password) {
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_LOGIN_ID, id, paramsJSON);
        Utils.putObjectToJSON(PR_LOGIN_PASSWORD, password, paramsJSON);
        return paramsJSON.toString();
    }

    public static String forgetPswdRequest(String emailid) {
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_LOGIN_ID, emailid, paramsJSON);
        return paramsJSON.toString();
    }

    public static String registerRequest(String title, String fName, String mName, String lName, String mobile, String mailId, String pswd, boolean
            guestUser, int totalBookingAmt, String createdOn, boolean socialNetworkLogin) {
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_REG_TITLE, title, paramsJSON);
        Utils.putObjectToJSON(PR_REG_FIRST_NAME, fName, paramsJSON);
        Utils.putObjectToJSON(PR_REG_MIDDLE_NAME, mName, paramsJSON);
        Utils.putObjectToJSON(PR_REG_LAST_NAME, lName, paramsJSON);
        Utils.putObjectToJSON(PR_REG_MOBILE, mobile, paramsJSON);
        Utils.putObjectToJSON(PR_REG_EMAIL, mailId, paramsJSON);
        Utils.putObjectToJSON(PR_REG_PASSWORD, pswd, paramsJSON);
        Utils.putObjectToJSON(PR_REG_GUEST_USER, guestUser, paramsJSON);
        Utils.putObjectToJSON(PR_REG_BOOKING_AMT, totalBookingAmt, paramsJSON);
        Utils.putObjectToJSON(PR_REG_CREATED_ON, createdOn, paramsJSON);
        Utils.putObjectToJSON(PR_SOCIAL_NETWORK_LOGIN, socialNetworkLogin, paramsJSON);
        String req = paramsJSON.toString();
        return req;
    }

    public String registrationRequest(String title, String email, String password, String mobile,
                                      String firstName, String middleName, String lastName,
                                      int bookingAmt, String createdOn, boolean guestUser) {
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_REG_TITLE, title, paramsJSON);
        Utils.putObjectToJSON(PR_REG_EMAIL, email, paramsJSON);
        Utils.putObjectToJSON(PR_REG_PASSWORD, password, paramsJSON);
        Utils.putObjectToJSON(PR_REG_MOBILE, mobile, paramsJSON);
        Utils.putObjectToJSON(PR_REG_FIRST_NAME, firstName, paramsJSON);
        Utils.putObjectToJSON(PR_REG_MIDDLE_NAME, middleName, paramsJSON);
        Utils.putObjectToJSON(PR_REG_LAST_NAME, lastName, paramsJSON);
        Utils.putIntToJSON(PR_REG_BOOKING_AMT, bookingAmt, paramsJSON);
        Utils.putObjectToJSON(PR_REG_CREATED_ON, createdOn, paramsJSON);
        Utils.putBooleanToJSON(PR_REG_GUEST_USER, guestUser, paramsJSON);
        return paramsJSON.toString();
    }


    public static String oneWaySearchRequest(String originName, String destinationName, String departDateWithoutTime,
                                             int noOfAdults, int noOfMinors, int noOfInfants,
                                             String tripType, String type, String serviceClassCode,
                                             String carrierType) {
        JSONObject departDateTimeJSON = new JSONObject();
        Utils.putObjectToJSON(PR_ONE_WAY_SEARCH_DATE_WO_TIME, departDateWithoutTime, departDateTimeJSON);
        JSONObject searchCriteriaJSON = new JSONObject();
        Utils.putObjectToJSON(PR_ONE_WAY_SEARCH_DEPART_DATE_TIME, departDateTimeJSON, searchCriteriaJSON);
        Utils.putObjectToJSON(PR_ONE_WAY_SEARCH_ORIGIN_NAME, originName, searchCriteriaJSON);
        Utils.putObjectToJSON(PR_ONE_WAY_SEARCH_DEST_NAME, destinationName, searchCriteriaJSON);
        Utils.putIntToJSON(PR_ONE_WAY_SEARCH_ADULTS, noOfAdults, searchCriteriaJSON);
        Utils.putIntToJSON(PR_ONE_WAY_SEARCH_MINORS, noOfMinors, searchCriteriaJSON);
        Utils.putIntToJSON(PR_ONE_WAY_SEARCH_INFANTS, noOfInfants, searchCriteriaJSON);
        Utils.putObjectToJSON(PR_ONE_WAY_SEARCH_TRIP_TYPE, tripType, searchCriteriaJSON);
        Utils.putObjectToJSON(PR_ONE_WAY_SEARCH_TYPE, type, searchCriteriaJSON);
        Utils.putObjectToJSON(PR_ONE_WAY_SEARCH_CLASS_CODE, serviceClassCode, searchCriteriaJSON);
        Utils.putObjectToJSON(PR_ONE_WAY_SEARCH_CARRIER_TYPE, carrierType, searchCriteriaJSON);
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_ONE_WAY_SEARCH_SEARCH_CRITERIA, searchCriteriaJSON, paramsJSON);
        return paramsJSON.toString();
    }

    public static String roundTripSearchRequest(String originName, String destinationName, String departDateWithoutTime,
                                                String returnDateWithoutTime, int noOfAdults, int noOfMinors, int noOfInfants,
                                                String tripType, String type, String serviceClassCode,
                                                String carrierType) {
        JSONObject departDateTimeJSON = new JSONObject();
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEARCH_DATE_WO_TIME, departDateWithoutTime, departDateTimeJSON);
        JSONObject returnDateTimeJSON = new JSONObject();
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEARCH_DATE_WO_TIME, returnDateWithoutTime, returnDateTimeJSON);
        JSONObject searchCriteriaJSON = new JSONObject();
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEARCH_DEPART_DATE_TIME, departDateTimeJSON, searchCriteriaJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEARCH_RETURN_DATE_TIME, returnDateTimeJSON, searchCriteriaJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEARCH_ORIGIN_NAME, originName, searchCriteriaJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEARCH_DEST_NAME, destinationName, searchCriteriaJSON);
        Utils.putIntToJSON(PR_ROUND_TRIP_SEARCH_ADULTS, noOfAdults, searchCriteriaJSON);
        Utils.putIntToJSON(PR_ROUND_TRIP_SEARCH_MINORS, noOfMinors, searchCriteriaJSON);
        Utils.putIntToJSON(PR_ROUND_TRIP_SEARCH_INFANTS, noOfInfants, searchCriteriaJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEARCH_TRIP_TYPE, tripType, searchCriteriaJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEARCH_TYPE, type, searchCriteriaJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEARCH_CLASS_CODE, serviceClassCode, searchCriteriaJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEARCH_CARRIER_TYPE, carrierType, searchCriteriaJSON);
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEARCH_SEARCH_CRITERIA, searchCriteriaJSON, paramsJSON);
        return paramsJSON.toString();
    }

    public static String oneWayRegularAvailabilityRequest(String searchId, String selectedFlightId) {
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_ONE_WAY_AVL_SEARCH_ID, searchId, paramsJSON);
        Utils.putObjectToJSON(PR_ONE_WAY_AVL_FLIGHT_ID, selectedFlightId, paramsJSON);
        return paramsJSON.toString();
    }

    public static String guestLoginRequest(String emailId,String PhoneNumber,String title)
    {

        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_REG_TITLE, title, paramsJSON);
        Utils.putObjectToJSON(PR_REG_EMAIL, emailId, paramsJSON);
        Utils.putObjectToJSON(PR_REG_MOBILE, PhoneNumber, paramsJSON);
        Utils.putObjectToJSON(PR_REG_PASSWORD, "password", paramsJSON);
        Utils.putObjectToJSON(PR_REG_FIRST_NAME, "Guest", paramsJSON);
        Utils.putObjectToJSON(PR_REG_LAST_NAME, "Guest", paramsJSON);
        Utils.putObjectToJSON(PR_REG_BOOKING_AMT, 0, paramsJSON);
        Utils.putObjectToJSON(PR_REG_CREATED_ON, "BEFORE_BOOKING", paramsJSON);
        Utils.putObjectToJSON(PR_REG_GUEST_USER, false, paramsJSON);
        Utils.putObjectToJSON(PR_SOCIAL_NETWORK_LOGIN, true, paramsJSON);
        return paramsJSON.toString();
    }

    public static String roundTripRegularAvailabilityRequest(String searchId, String fareResultsType, String selectedOutboundFlightId, String selectedInboundFlightId) {
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_ROUND_TRIP_AVL_SEARCH_ID, searchId, paramsJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_AVL_FARE_RESULTS_TYPE, fareResultsType, paramsJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEL_OUT_FLIGHT_ID, selectedOutboundFlightId, paramsJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEL_IN_FLIGHT_ID, selectedInboundFlightId, paramsJSON);
        return paramsJSON.toString();
    }

    public static String roundTripSpecialLCCAvailabilityRequest(String searchId, String fareResultsType, String selectedTab, String selectedOutboundFlightId, String selectedInboundFlightId) {
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_ROUND_TRIP_AVL_SEARCH_ID, searchId, paramsJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_AVL_FARE_RESULTS_TYPE, fareResultsType, paramsJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEL_TAB, selectedTab, paramsJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEL_OUT_FLIGHT_ID, selectedOutboundFlightId, paramsJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEL_IN_FLIGHT_ID, selectedInboundFlightId, paramsJSON);
        return paramsJSON.toString();
    }


    public static String roundTripSpecialGDSAvailabilityRequest(String searchId, String fareResultsType, String selectedTab, String selectedFlightId) {
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_ROUND_TRIP_AVL_SEARCH_ID, searchId, paramsJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_AVL_FARE_RESULTS_TYPE, fareResultsType, paramsJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_SEL_TAB, selectedTab, paramsJSON);
        Utils.putObjectToJSON(PR_ROUND_TRIP_AVL_FLIGHT_ID, selectedFlightId, paramsJSON);
        return paramsJSON.toString();
    }

    public static String bookingValidateRequest(String searchId, String paymentGateway, BillingInfo billingInfo,
                                                List<Traveller> travellers, boolean ticketRequested) {
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_BOOKING_VAL_SEARCH_ID, searchId, paramsJSON);
        Utils.putObjectToJSON(PR_BOOKING_VAL_GATEWAY, paymentGateway, paramsJSON);
        try {
            Utils.putObjectToJSON(PR_BOOKING_VAL_BILLING, new JSONObject(new Gson().toJson(billingInfo)), paramsJSON);
            Utils.putObjectToJSON(PR_BOOKING_VAL_TRAVELLERS, new JSONArray(new Gson().toJson(travellers)), paramsJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ticketRequested)
            Utils.putObjectToJSON(PR_BOOKING_VAL_TICKET_REQUESTED, ticketRequested, paramsJSON);

        return paramsJSON.toString();
    }

    public static String cashCardValidationRequest(String number, String pin) {
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_CARD_VAL_NUMBER, number, paramsJSON);
        Utils.putObjectToJSON(PR_CARD_VAL_PIN, pin, paramsJSON);
        return paramsJSON.toString();
    }

    public static String fareRuleRequest(String searchId, String selectedFlightId) {
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_RULE_SEARCH_ID, searchId, paramsJSON);
        Utils.putObjectToJSON(PR_RULE_FLIGHT_ID, selectedFlightId, paramsJSON);
        return paramsJSON.toString();
    }

    public static String viewTripRequest(String bookingRefNo) {
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_BOOKING_REF_NO, bookingRefNo, paramsJSON);
        return paramsJSON.toString();
    }

    public static String bookingInitializeRequest(String searchId, String token) {
        HashMap<String, String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(PR_BOOKING_INITIALIZE_SEARCH_ID, searchId);
        paramsHashMap.put(PR_BOOKING_INITIALIZE_TOKEN, token);
        try {
            return Utils.getPostDataString(paramsHashMap);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String cancelTripRequest(String bookingRefNo) {
        JSONObject paramsJSON = new JSONObject();
        Utils.putObjectToJSON(PR_BOOKING_REF_NO, bookingRefNo, paramsJSON);
        return paramsJSON.toString();
    }

    public static String applyPromocode(String search_id, String uniqueEntityId, Editable text) {
        JSONObject paramJson = new JSONObject();
        try {
            paramJson.put(PR_PROMOCODE_SEARCH_ID, search_id);
            paramJson.put(PR_PROMOCODE_FLIGHT_ID, uniqueEntityId);
            JSONObject discount = new JSONObject();
            discount.put(PR_PROMOCODE_DISCOUNT_CODE, text);
            paramJson.put(PR_PROMOCODE_DISCOUNT, discount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return paramJson.toString();
    }

    public static String removePromocode(String search_id, String uniqueEntityId, Editable text) {
        JSONObject paramJson = new JSONObject();
        try {
            paramJson.put(PR_PROMOCODE_SEARCH_ID, search_id);
            paramJson.put(PR_PROMOCODE_FLIGHT_ID, uniqueEntityId);
            JSONObject discount = new JSONObject();
            discount.put(PR_PROMOCODE_DISCOUNT_CODE, text);
            discount.put(PR_PROMOCODE_REMOVE, true);
            paramJson.put(PR_PROMOCODE_DISCOUNT, discount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return paramJson.toString();
    }

    public static String setSSRRequest(String searchId, String flightId, String type, boolean apply) {
        JSONObject ssrRequest = new JSONObject();
        try {
            ssrRequest.put(SSR_SEARCH_ID, searchId);
            ssrRequest.put(SSR_FLIGHT_ID, flightId);
            ssrRequest.put(SSR_TYPE, type);
            ssrRequest.put(SSR_ADD_OTHER_CHARGES, apply);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ssrRequest.toString();
    }
}
