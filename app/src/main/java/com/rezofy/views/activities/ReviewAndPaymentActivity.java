package com.rezofy.views.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rezofy.R;
import com.rezofy.adapters.CountrySpinnerAdapter;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.controllers.DatabaseController;
import com.rezofy.database.DbHelper;
import com.rezofy.models.request_models.BillingInfo;
import com.rezofy.models.request_models.GstBillingInfo;
import com.rezofy.models.request_models.Traveller;
import com.rezofy.models.response_models.AvailabilityResponse;
import com.rezofy.models.response_models.BookingValidateResponse;
import com.rezofy.models.response_models.CashCard;
import com.rezofy.models.response_models.Fare;
import com.rezofy.models.response_models.FlightData;
import com.rezofy.models.response_models.LoginResponse;
import com.rezofy.models.response_models.NameModel;
import com.rezofy.models.response_models.OtherCharge;
import com.rezofy.models.response_models.PaymentGateway;
import com.rezofy.models.response_models.PaymentGatewayList;
import com.rezofy.models.response_models.PriceAndConversionModel;
import com.rezofy.models.response_models.ViewTicketResponse;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.requests.Requests;
import com.rezofy.utils.DividerItemDecoration;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.custom_views.IconTextView;
import com.rezofy.views.custom_views.SelectorDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Linchpin66 on 20-01-2016.
 */
public class ReviewAndPaymentActivity extends AppCompatActivity implements View.OnClickListener, SelectorDialog.SelectorRecyclerViewHandler, NetworkTask.Result, NetworkTask.DoInBackground {

    private static final int APPLY_PROMOCODE = 102;
    private static final int ID_AVAILABILITY_CREDIT_LIMIT = 103;
    private static final int REMOVE_PROMOCODE = 104;
    private static final int ID_SSR = 105;
    private static final int ID_AVAILABILITY_FROM_PAY_NOW = 107;
    private int checkedPosition = -1;
    private final int ID_SHOW_TRAVELLERS = 1;
    private static final int CREDIT_BALANCE = 101;
    private final int ID_BOOKING_INITIALIZE = 4;
    private final int ID_VIEW_TICKET = 3;
    private final int ID_BOOKING_VALIDATE_CREDITCARD = 1;
    private final int ID_BOOKING_VALIDATE_CASHCARD = 8;
    private final int ID_AVAILABILITY = 2;
    private final int ID_AVAILABILITY_CREDIT_CARD = 5;
    private final int ID_AVAILABILITY_CASH_CARD = 7;
    private final int GUEST_LOGIN = 71;
    private final int ID_CHECK_BALANCE = 6;
    private final int request_code_webView = 1;
    private final String STATUS_SUCCESS = "SUCCESS";
    private ScrollView scrollView;
    private CheckBox iconTextViewCheckFirst, iconTextViewCheckSeconds, cbSendInfo, cbZeroCancellation, cbVisa, cbInsuration;
    protected IconTextView iTVMenu, iTVCreditCard, iTVCashCard, iTVCreditLimit, itvZeroCancellationInfo, itvVisaInfo, itvInsurance, iTVCompanyClick, iTVGstNoClick;
    protected FlightData flightDataFirstWay, flightDataSecondWay;
    protected int noOfAdults, noOfChildren, noOfInfants;
    private BookingValidateResponse bookingValidateResponse;
    private EditText etMobileNo, etIsdCode, etEmailId, card_number, card_pin;
    protected TextView tvNoOfPassengers, tvPlaceFirstWay, tvDurationFirstWay, tvDepartureDateFirstWay, tvFlightTypeFirstWay,
            tvPlaceSecondWay, tvDurationSecondWay, tvDepartureDateSecondWay, tvFlightTypeSecondWay;
    private TextView tvFare, tvTermsAndConditions, tvTitle, tvClickToSeeFlightDetails, tvClickToSeeDetails, tvCreditCard, tvCashCard,
            tvCreditLimitMode, submitBtn, checkBalance, tvCashLimit, tvCreditLimit, tvBookingAmount, tvRemainedCashLimit,
            tvRemainedCreditLimit, tvMessage, cashCardBalance, bookAmountCashCard, cashCardBalanceAfterBooking, book_from_cashCard,
            tvZeroCancellationRemarks, tvVisaRemarks, tvInsuranceRemarks;
    private boolean ticketEnabled;
    private ArrayList<Traveller> listPassengersInfo;
    private float bookingAmount, originalBookingAmount;
    private Float convenienceFee;
    boolean isConvinienceFeeAdded = false;
    private RelativeLayout rlFlightDetailsSecondWay, relativeLayoutPremLimit, relativeLayoutCreditCard, relativeLayoutCashCard,
            rlPaymentDetails, rlBook, rl_cardgrid, rl_cash_card_detail, rlZeroCancellation, rlVisa, rlInsurance;
    private LinearLayout ll_cash_card_info, gatewayGridLayout, llPaymentLayout;
    private AppPreferences appPreferences;
    private BillingInfo billingInfo;
    private String cardName = "", cashCardNumber, cashCardPin;
    private PaymentGatewayList gatewayList;
    private String paymentType;
    private TextView convineieceFeeeTv, tvTaxes, gstSubmit, gstReset;
    private TextView tvHaveAPromocode, tvPromoMessage, tvRemoveButton, tvFareBreakUp;
    private EditText etEnterPromocode;
    private ImageView btnFloating;
    private LinearLayout llEnterPromocode, llPromocode, llAdditionalBenefits;
    private IconTextView itvArrow;
    private View line;
    private Fare fareBreakUp, inFareBreakUp;
    private String zeroInfo, visaInfo, insuranceInfo;
    private String zeroCharge, visaCharge, insuranceCharge, toastMessage;
    private CheckBox cbGetGstInfo;
    private LinearLayout rlGetGstDetails;
    private TextView applyButton;
    private Spinner countrySpinner, stateSpinner, citySpinner;
    private List<String> countryList, stateList;
    private EditText gstNo, companyName, billingPhone, emailId, city, address, pincode;
    private String strCompanyName, gstNumber, strBillingPhone, email, countryName, cityName, stateName, strAddress, strPincode;
    private LinearLayout stateLayout, paymentLayout;
    private TextView tvPayNow, tvPayNowRemarks, tvProceedToBooking;
    private AvailabilityResponse availabilityResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_payment);
        init();
        setViewsAsPerUserType();
        setEmail();
        setProperties();
        setListener();
        flightDetails();
    }

    private void setEmail() {
        LoginResponse loginResponse = new LoginResponse();
        try {
            loginResponse = new Gson().fromJson(appPreferences.getUserData(), LoginResponse.class);
        } finally {
            if (loginResponse != null)
                etEmailId.setText(loginResponse.getEmail());
        }
    }

    private void setBillingInfo(String paymentType) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setName(new NameModel());
        try {
            loginResponse = new Gson().fromJson(appPreferences.getUserData(), LoginResponse.class);
        } finally {
            billingInfo.setBillingName(loginResponse.getName().getFirst() + " " + loginResponse.getName().getLast());
            billingInfo.setEmail(etEmailId.getText().toString());
            billingInfo.setBillingPhone(etMobileNo.getText().toString());
            billingInfo.setContactPhone(etMobileNo.getText().toString());
            billingInfo.setSendInformationToTraveller(cbSendInfo.isChecked());
            billingInfo.setPaymentType(paymentType);
            billingInfo.setCity(loginResponse.getCity());
            billingInfo.setAddress1(loginResponse.getAddress());
            billingInfo.setZip(loginResponse.getPincode());
            billingInfo.setChargePayment(true);
            billingInfo.setCountry(loginResponse.getCountry());
            billingInfo.setState(loginResponse.getState());
            if (paymentType.equals(Utils.PAYMENT_TYPE_CASH_CARD)) {
                CashCard cashCard = new CashCard();
                cashCard.setNumber(cashCardNumber);
                cashCard.setPin(cashCardPin);
                billingInfo.setCashCard(cashCard);
            }
            if (cbGetGstInfo.getText().toString().equalsIgnoreCase(getResources().getString(R.string.review_gst))) {
                GstBillingInfo gstDetails = new GstBillingInfo();
                gstDetails.setAddress(strAddress);
                gstDetails.setBillingPhone(strBillingPhone);
                gstDetails.setCity(cityName);
                gstDetails.setCompanyName(strCompanyName);
                gstDetails.setCountry(countryName);
                gstDetails.setEmail(email);
                gstDetails.setGstNumber(gstNumber);
                gstDetails.setState(stateName);
                gstDetails.setZip(strPincode);
                billingInfo.setGstBillingInfo(gstDetails);
            } else {
                billingInfo.setGstBillingInfo(null);
            }
        }
    }

    protected void setProperties() {
        int themeColor = UIUtils.getThemeColor(this);
        findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(this));
        iTVMenu.setTextColor(UIUtils.getThemeContrastColor(this));
        tvTitle.setTextColor(UIUtils.getThemeContrastColor(this));
        tvClickToSeeFlightDetails.setTextColor(UIUtils.getThemeColor(this));
        tvClickToSeeDetails.setTextColor(UIUtils.getThemeColor(this));
        tvTermsAndConditions.setTextColor(UIUtils.getThemeColor(this));
        iTVCreditCard.setTextColor(themeColor);
        iTVCashCard.setTextColor(themeColor);
        iTVCreditLimit.setTextColor(themeColor);
        tvCreditCard.setTextColor(themeColor);
        tvCashCard.setTextColor(themeColor);
        tvCreditLimitMode.setTextColor(themeColor);
        UIUtils.setRoundedButtonProperties(findViewById(R.id.book));
        UIUtils.setRoundedButtonProperties(submitBtn);
        UIUtils.setRoundedButtonProperties(checkBalance);
        UIUtils.setRoundedButtonProperties(tvPayNow);
        UIUtils.setRoundedButtonProperties(tvProceedToBooking);
        if (getString(R.string.isTketApp).equalsIgnoreCase("true")) {
            tvCreditCard.setText(getString(R.string.online_pay_text));
        }
    }


    private void init() {
        appPreferences = AppPreferences.getInstance(this);
        listPassengersInfo = (ArrayList<Traveller>) getIntent().getSerializableExtra("passenger_info");
        billingInfo = new BillingInfo();
        ticketEnabled = getIntent().getBooleanExtra("ticket_enabled", false);

        iTVMenu = (IconTextView) findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setText(getString(R.string.icon_text_k));
        iTVMenu.setTextSize(20);

        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        rlFlightDetailsSecondWay = (RelativeLayout) findViewById(R.id.flight_details_second_way);
        tvPlaceFirstWay = (TextView) findViewById(R.id.place_first_way);
        tvDurationFirstWay = (TextView) findViewById(R.id.duration_first_way);
        tvDepartureDateFirstWay = (TextView) findViewById(R.id.departure_date_first_way);
        tvFlightTypeFirstWay = (TextView) findViewById(R.id.flight_type_first_way);
        tvPlaceSecondWay = (TextView) findViewById(R.id.place_second_way);
        tvDurationSecondWay = (TextView) findViewById(R.id.duration_second_way);
        tvDepartureDateSecondWay = (TextView) findViewById(R.id.departure_date_second_way);
        tvFlightTypeSecondWay = (TextView) findViewById(R.id.flight_type_second_way);
        convineieceFeeeTv = (TextView) findViewById(R.id.convineieceFeeeTv);
        tvTaxes = (TextView) findViewById(R.id.tvTaxes);
        convenienceFee = 0.0f;

        tvNoOfPassengers = (TextView) findViewById(R.id.tv_review_adults_text);
        tvNoOfPassengers.setSelected(true);

        tvTermsAndConditions = (TextView) findViewById(R.id.tv_term_condition_text);
        tvTermsAndConditions.setOnClickListener(this);
        SpannableString content = new SpannableString(getString(R.string.term_text));
        content.setSpan(new UnderlineSpan(), 0, getString(R.string.term_text).length(), 0);
        tvTermsAndConditions.setText(content);

        tvFare = (TextView) findViewById(R.id.fare);
        tvClickToSeeFlightDetails = (TextView) findViewById(R.id.tv_flight_details);
        content = new SpannableString(getString(R.string.text_details));
        content.setSpan(new UnderlineSpan(), 0, getString(R.string.text_details).length(), 0);
        tvClickToSeeFlightDetails.setText(content);

        tvClickToSeeDetails = (TextView) findViewById(R.id.tv_click_to_see_details);
        content = new SpannableString(getString(R.string.text_click_to_see_details));
        content.setSpan(new UnderlineSpan(), 0, getString(R.string.text_click_to_see_details).length(), 0);
        tvClickToSeeDetails.setText(content);

        iconTextViewCheckFirst = (CheckBox) findViewById(R.id.icon_checkfirst);
        iconTextViewCheckSeconds = (CheckBox) findViewById(R.id.icon_checksecond);
        cbSendInfo = (CheckBox) findViewById(R.id.send_sms_email_info);
        etEmailId = (EditText) findViewById(R.id.edit_email);
        etMobileNo = (EditText) findViewById(R.id.edit_mobilenumber);
        etIsdCode = (EditText) findViewById(R.id.isd_code);

        tvTitle = (TextView) findViewById(R.id.header).findViewById(R.id.title);
        tvTitle.setText(getString(R.string.header_review_text));
        relativeLayoutCreditCard = (RelativeLayout) findViewById(R.id.rl_credit_card);
        relativeLayoutCashCard = (RelativeLayout) findViewById(R.id.rl_cash_card);
        relativeLayoutPremLimit = (RelativeLayout) findViewById(R.id.rl_creditlimit);
        iTVCreditCard = (IconTextView) findViewById(R.id.icon_credit_card);
        iTVCashCard = (IconTextView) findViewById(R.id.icon_cash_card);
        iTVCreditLimit = (IconTextView) findViewById(R.id.icon_credit_limit);
        tvCreditCard = (TextView) findViewById(R.id.credit_card_text);
        tvCashCard = (TextView) findViewById(R.id.cash_card_text);
        tvCreditLimitMode = (TextView) findViewById(R.id.credit_limit_text);
        rlPaymentDetails = (RelativeLayout) findViewById(R.id.rl_second);
        rlBook = (RelativeLayout) findViewById(R.id.rl_book);
        tvCashLimit = (TextView) findViewById(R.id.tv_cash_limit);
        tvCreditLimit = (TextView) findViewById(R.id.tv_amount);
        tvBookingAmount = (TextView) findViewById(R.id.tv_booking_amount);
        tvRemainedCashLimit = (TextView) findViewById(R.id.tv_rem_perm_cash);
        tvRemainedCreditLimit = (TextView) findViewById(R.id.tv_perm_amount);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        rl_cardgrid = (RelativeLayout) findViewById(R.id.rl_cardgrid);
        submitBtn = (TextView) findViewById(R.id.submitBtnGateway);
        rl_cash_card_detail = (RelativeLayout) findViewById(R.id.rl_cash_card_detail);
        card_number = (EditText) findViewById(R.id.card_number);
        card_pin = (EditText) findViewById(R.id.card_pin);
        checkBalance = (TextView) findViewById(R.id.check_balance_btn);
        ll_cash_card_info = (LinearLayout) findViewById(R.id.ll_cash_card_info);
        //ll_cash_card_info.setVisibility(View.VISIBLE);
        cashCardBalance = (TextView) findViewById(R.id.tv_cash_balance);
        bookAmountCashCard = (TextView) findViewById(R.id.tv_booking_amount_cashcard);
        cashCardBalanceAfterBooking = (TextView) findViewById(R.id.tv_cash_card_balance_after_deduction);
        book_from_cashCard = (TextView) findViewById(R.id.book_from_cashCard);
        llPaymentLayout = (LinearLayout) findViewById(R.id.ll_paymenticon);
        gatewayGridLayout = (LinearLayout) findViewById(R.id.gateway_girdLayout);
        rlPaymentDetails.setVisibility(View.GONE);
        Log.d("Trip", "token is :" + AppPreferences.getInstance(this).getToken() + ":");
        if (AppPreferences.getInstance(this).getToken().equals("")) {
            AppPreferences.getInstance(this).isB2B(false);
        }

        tvPayNow = (TextView) findViewById(R.id.tvPayNow);
        tvPayNowRemarks = (TextView) findViewById(R.id.tvPayNowRemarks);
        paymentLayout = (LinearLayout) findViewById(R.id.ll_payment);
        tvProceedToBooking = (TextView) findViewById(R.id.tvProceedToBooking);

        tvHaveAPromocode = (TextView) findViewById(R.id.have_a_promocode);
        content = new SpannableString(getString(R.string.have_promocode));
        content.setSpan(new UnderlineSpan(), 0, getString(R.string.have_promocode).length(), 0);
        tvHaveAPromocode.setText(content);
        tvHaveAPromocode.setOnClickListener(this);
        tvRemoveButton = (TextView) findViewById(R.id.remove_button);
        tvRemoveButton.setOnClickListener(this);

        llEnterPromocode = (LinearLayout) findViewById(R.id.ll_enter_promo);
        llPromocode = (LinearLayout) findViewById(R.id.promocode);

        etEnterPromocode = (EditText) findViewById(R.id.enter_promocode);
        /*content = new SpannableString(getString(R.string.enter_promocode));
        content.setSpan(new UnderlineSpan(), 0, getString(R.string.enter_promocode).length(), 0);
        etEnterPromocode.setOnClickListener(this);*/

        line = (View) findViewById(R.id.line);
        tvPromoMessage = (TextView) findViewById(R.id.tv_promo_message);
        tvFareBreakUp = (TextView) findViewById(R.id.tv_break_up);
        content = new SpannableString(getString(R.string.click_here));
        content.setSpan(new UnderlineSpan(), 30, 40, 0);
        content.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.upper_rl_home_color)), 30, 40, 0);
        /*content.setSpan(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewAndPaymentActivity.this, UpdatedFareBreakUpActivity.class);
                Bundle bundle = getIntent().getExtras();
                bundle.putSerializable("fare", fareBreakUp);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, 32, 42, 0);*/

        llAdditionalBenefits = (LinearLayout) findViewById(R.id.ll_additional_benefits);
        rlZeroCancellation = (RelativeLayout) findViewById(R.id.rl_zero_cancellation);
        rlVisa = (RelativeLayout) findViewById(R.id.rl_visa);
        rlInsurance = (RelativeLayout) findViewById(R.id.rl_insurance);
        cbZeroCancellation = (CheckBox) findViewById(R.id.cb_zero_cancellation);
        cbVisa = (CheckBox) findViewById(R.id.cb_visa);
        cbInsuration = (CheckBox) findViewById(R.id.cb_insure_your_trip);
        cbZeroCancellation.setOnClickListener(this);
        cbVisa.setOnClickListener(this);
        cbInsuration.setOnClickListener(this);
        itvZeroCancellationInfo = (IconTextView) findViewById(R.id.zero_cancel_info);
        itvZeroCancellationInfo.setOnClickListener(this);
        itvVisaInfo = (IconTextView) findViewById(R.id.visa_info);
        itvVisaInfo.setOnClickListener(this);
        itvInsurance = (IconTextView) findViewById(R.id.insure_info);
        itvInsurance.setOnClickListener(this);
        tvZeroCancellationRemarks = (TextView) findViewById(R.id.tv_zro_cancel_remarks);
        tvVisaRemarks = (TextView) findViewById(R.id.tv_visa_remarks);
        tvInsuranceRemarks = (TextView) findViewById(R.id.insure_remarks);

        tvFareBreakUp.setText(content);
        content = new SpannableString(getString(R.string.click_here_promo));
        content.setSpan(new UnderlineSpan(), 31, 41, 0);
        content.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.upper_rl_home_color)), 32, 42, 0);
        tvPromoMessage.setText(content);

        /*itvArrow = (IconTextView) findViewById(R.id.arrow);
        itvArrow.setOnClickListener(this);*/

        applyButton = (TextView) findViewById(R.id.apply_button);
        applyButton.setOnClickListener(this);

        cbGetGstInfo = (CheckBox) findViewById(R.id.addGstDetails);
        rlGetGstDetails = (LinearLayout) findViewById(R.id.gstDetailsLayout);
        gstSubmit = (TextView) findViewById(R.id.gstSubmit);
        gstReset = (TextView) findViewById(R.id.gstReset);

        companyName = (EditText) findViewById(R.id.company_name);
        billingPhone = (EditText) findViewById(R.id.billing_phone);
        emailId = (EditText) findViewById(R.id.email_id);
        city = (EditText) findViewById(R.id.city);
        address = (EditText) findViewById(R.id.address);
        pincode = (EditText) findViewById(R.id.pincode);
        iTVCompanyClick = (IconTextView) findViewById(R.id.CompanyNameClick);
        iTVGstNoClick = (IconTextView) findViewById(R.id.GstNoClick);
        gstNo = (EditText) findViewById(R.id.gst_no);

        stateSpinner = (Spinner) findViewById(R.id.state_spinner);
        countrySpinner = (Spinner) findViewById(R.id.country_spinner);
        spinnerTask();
        stateLayout = (LinearLayout) findViewById(R.id.state_layout);

        btnFloating = (ImageView) findViewById(R.id.btn_flaoting);
        if(Boolean.parseBoolean(getString(R.string.hideChatIcon))) {
            btnFloating.setVisibility(View.GONE);
        }
    }

    private void setViewsAsPerUserType() {
        if (AppPreferences.getInstance(this).getToken().isEmpty() || (!AppPreferences.getInstance(this).getToken().equals("") &&
                (!AppPreferences.getInstance(this).getB2B() || (AppPreferences.getInstance(this).getB2B() && !ticketEnabled))))
            iconTextViewCheckSeconds.setVisibility(View.GONE);
        if (!AppPreferences.getInstance(this).getB2B()) {
            relativeLayoutPremLimit.setVisibility(View.GONE);
        }
        /*if (!AppPreferences.getInstance(this).getToken().equals("") && !AppPreferences.getInstance(this).getB2B())
            relativeLayoutPremLimit.setVisibility(View.GONE);*/
        if (getIntent().getSerializableExtra("flightDataSecondWay") != null) {
            flightDataSecondWay = (FlightData) getIntent().getSerializableExtra("flightDataSecondWay");
        }
    }

    protected void setListener() {
        TextView.OnEditorActionListener actionNextListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (v.getId() == R.id.enter_promocode) {
                        etEnterPromocode.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        };

        iTVMenu.setOnClickListener(this);
        tvClickToSeeFlightDetails.setOnClickListener(this);
        tvClickToSeeDetails.setOnClickListener(this);
        relativeLayoutCreditCard.setOnClickListener(this);
        relativeLayoutCashCard.setOnClickListener(this);
        relativeLayoutPremLimit.setOnClickListener(this);
        rlBook.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        checkBalance.setOnClickListener(this);
        book_from_cashCard.setOnClickListener(this);
        etEnterPromocode.setOnEditorActionListener(actionNextListener);
        tvFareBreakUp.setOnClickListener(this);
        tvPromoMessage.setOnClickListener(this);

        cbGetGstInfo.setOnClickListener(this);
        gstSubmit.setOnClickListener(this);
        gstReset.setOnClickListener(this);
        iTVCompanyClick.setOnClickListener(this);
        iTVGstNoClick.setOnClickListener(this);

        tvPayNow.setOnClickListener(this);
        tvProceedToBooking.setOnClickListener(this);

        gstNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (gstNo.getTextColors().equals(R.color.red_first)) {
                        gstNo.getText().clear();
                    }
                    gstNo.setTextColor((getResources().getColor(R.color.black)));
                } else {
                    /*Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();*/
                    String gst = gstNo.getText().toString();
                    String pattern = "\\d{2}[A-Za-z]{5}\\d{4}[A-Za-z]{1}\\d{1}[A-Za-z\\d]{2}";
                    boolean chk = gst.matches(pattern);
                    if (!chk) {
                        Toast.makeText(getApplicationContext(), "Invalid GST No", Toast.LENGTH_LONG).show();
                        gstNo.setTextColor((getResources().getColor(R.color.red_first)));
                    }
                }
            }
        });

        emailId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Utils.hideSoftKey(ReviewAndPaymentActivity.this);
                }
            }
        });

        etMobileNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Utils.hideSoftKey(ReviewAndPaymentActivity.this);
                }
            }
        });

        btnFloating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatingButtonMove floatingButtonMove = new FloatingButtonMove(getApplicationContext());
                floatingButtonMove.dragView(v, event);
                return true;
            }
        });
    }

    protected void flightDetails() {
        flightDataFirstWay = (FlightData) getIntent().getSerializableExtra("flightDataFirstWay");
        String originCityName = getIntent().getStringExtra("originCityName");
        String destCityName = getIntent().getStringExtra("destCityName");

        noOfAdults = getIntent().getIntExtra("noOfAdults", 0);
        noOfChildren = getIntent().getIntExtra("noOfChildren", 0);
        noOfInfants = getIntent().getIntExtra("noOfInfants", 0);

        tvNoOfPassengers.setText(UIUtils.getTravellersText(this, noOfAdults, noOfChildren, noOfInfants));

        tvPlaceFirstWay.setText(originCityName + " - " + destCityName);
        tvDepartureDateFirstWay.setText(Utils.changeDateFormat(getIntent().getStringExtra("departDate"), Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_EEE_space_comma_d_space_LLL));
        tvDurationFirstWay.setText(Utils.getDurationInHrsAndMins(flightDataFirstWay.getSegments().get(0).getDuration()));
        tvFlightTypeFirstWay.setText(UIUtils.getFlightType(flightDataFirstWay.getSegments().get(0).getNoOfStops()));

        if (getIntent().getSerializableExtra("flightDataSecondWay") != null) {
            flightDataSecondWay = (FlightData) getIntent().getSerializableExtra("flightDataSecondWay");
            String retDateValue = getIntent().getStringExtra("retDate");
            rlFlightDetailsSecondWay.setVisibility(View.VISIBLE);
            tvPlaceSecondWay.setText(destCityName + " - " + originCityName);
            tvDepartureDateSecondWay.setText(Utils.changeDateFormat(retDateValue, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_EEE_space_comma_d_space_LLL));
            tvFlightTypeSecondWay.setText(UIUtils.getFlightType(flightDataSecondWay.getSegments().get(0).getNoOfStops()));
            if ((getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS))
                    || (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_REGULAR) && getIntent().getStringExtra("selected_tab") == null)) {
                bookingAmount = Float.valueOf(flightDataFirstWay.getFare().getTotal().getPrice().getAmount());
                tvFare.setText(UIUtils.getFareToDisplay(this, flightDataFirstWay.getFare().getTotal().getPrice().getAmount()));
            } else {
                bookingAmount = (float) (Math.ceil(Double.parseDouble(flightDataFirstWay.getFare().getTotal().getPrice().getAmount())) + Math.ceil(Double.parseDouble(flightDataSecondWay.getFare().getTotal().getPrice().getAmount())));
                tvFare.setText(UIUtils.getFareToDisplay(this, (float) (Math.ceil(Double.parseDouble(flightDataFirstWay.getFare().getTotal().getPrice().getAmount())) + Math.ceil(Double.parseDouble(flightDataSecondWay.getFare().getTotal().getPrice().getAmount())))));
            }
        } else {
            bookingAmount = Float.valueOf(flightDataFirstWay.getFare().getTotal().getPrice().getAmount());
            tvFare.setText(UIUtils.getFareToDisplay(this, flightDataFirstWay.getFare().getTotal().getPrice().getAmount()));
        }
        originalBookingAmount = bookingAmount;
        Log.d("Trip", "booking amount is " + bookingAmount);
    }

    private void getCashAndCreditBalance() {
        String url;
        NetworkTask networkTask = new NetworkTask(this, CREDIT_BALANCE);
        networkTask.setProgressDialog(true);
        networkTask.setDialogMessage(getString(R.string.please_wait));
        networkTask.exposePostExecute(this);
        String paramsArray[] = null;

        url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlCreditBalance;
        paramsArray = new String[]{url, null};
        networkTask.execute(paramsArray);
    }

    private void setAmountData() {
        tvCashLimit.setText(UIUtils.getOriginalFareToDisplay(this, appPreferences.getCashLimit()));
        tvCreditLimit.setText(UIUtils.getOriginalFareToDisplay(this, appPreferences.getCreditLimit()));
        float sellingPrice;
        if (flightDataSecondWay != null) {
            if ((getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS))
                    || (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_REGULAR) && getIntent().getStringExtra("selected_tab") == null)) {
                sellingPrice = Float.valueOf(flightDataFirstWay.getFare().getTotal().getPrice().getAmount());
            } else {
                sellingPrice = (float) (Math.ceil(Double.parseDouble(flightDataFirstWay.getFare().getTotal().getPrice().getAmount())) + Math.ceil(Double.parseDouble(flightDataSecondWay.getFare().getTotal().getPrice().getAmount())));
            }
        } else {
            sellingPrice = Float.valueOf(flightDataFirstWay.getFare().getTotal().getPrice().getAmount());
        }
        tvBookingAmount.setText(UIUtils.getOriginalFareToDisplay(this, sellingPrice));
        setRemainedLimits(sellingPrice);
        rlPaymentDetails.setVisibility(View.VISIBLE);
        scrollScreenToAdditionalBenefits();
    }


    private void doGuestLogin() {
        try {
            {
                if (validateForm()) {
                    String url;
                    NetworkTask networkTask = new NetworkTask(this, GUEST_LOGIN);
                    networkTask.setDialogMessage(getString(R.string.please_wait));
                    networkTask.exposePostExecute(this);
                    String paramsArray[];
                    url = UIUtils.getBaseUrl(this) + WebServiceConstants.registerApi;
                    paramsArray = new String[]{url, Requests.guestLoginRequest(etEmailId.getText().toString(), etMobileNo.getText().toString(), "Mr.")};
                    networkTask.execute(paramsArray);
                }
            }
        } catch (Exception e) {
            Log.d("Trip", "Eror in doGuestLogin " + e);
        }
    }

    private void setRemainedLimits(float sellingPrice) {
        float cashLimit = Float.parseFloat(appPreferences.getCashLimit());
        float creditLimit = Float.parseFloat(appPreferences.getCreditLimit());

        tvBookingAmount.setText(UIUtils.getOriginalFareToDisplay(this, sellingPrice));

        if (cashLimit >= sellingPrice) {
            tvRemainedCashLimit.setText(UIUtils.getOriginalFareToDisplay(this, cashLimit - sellingPrice));
            tvRemainedCreditLimit.setText(UIUtils.getOriginalFareToDisplay(this, creditLimit));
            tvMessage.setText(getString(R.string.deduction_from_cash));
            rlBook.setVisibility(View.VISIBLE);

        } else if (appPreferences.getIsExpired()) {
            tvRemainedCashLimit.setText(UIUtils.getOriginalFareToDisplay(this, cashLimit));
            tvRemainedCreditLimit.setText(UIUtils.getOriginalFareToDisplay(this, creditLimit));
            tvMessage.setText(getString(R.string.credit_expired));

        } else if (cashLimit > 0 && cashLimit + creditLimit >= sellingPrice) {
            tvRemainedCashLimit.setText(UIUtils.getOriginalFareToDisplay(this, cashLimit - sellingPrice));
            tvRemainedCreditLimit.setText(UIUtils.getOriginalFareToDisplay(this, creditLimit - (sellingPrice - cashLimit)));
            tvMessage.setText(getString(R.string.deduction_from_cash_and_credit));
            rlBook.setVisibility(View.VISIBLE);

        } else if (creditLimit >= sellingPrice) {
            tvRemainedCashLimit.setText(UIUtils.getOriginalFareToDisplay(this, cashLimit - sellingPrice));
            tvRemainedCreditLimit.setText(UIUtils.getOriginalFareToDisplay(this, creditLimit - sellingPrice));
            tvMessage.setText(getString(R.string.deduction_from_credit));
            rlBook.setVisibility(View.VISIBLE);

        } else {
            tvRemainedCashLimit.setText(UIUtils.getOriginalFareToDisplay(this, cashLimit));
            tvRemainedCreditLimit.setText(UIUtils.getOriginalFareToDisplay(this, creditLimit));
            tvMessage.setText(getString(R.string.insufficient_combined_limit));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_flight_details:
                Bundle bundle = getIntent().getExtras();
                if (isConvinienceFeeAdded) {
                    bundle.putFloat("convenienceFees", convenienceFee);
                } else {
                    bundle.putFloat("convenienceFees", 0.0f);
                }
                if (getIntent().getBooleanExtra("promocode_applied", false)) {
                    bundle.putSerializable("fare", fareBreakUp);
                    bundle.putSerializable("inboundFare", inFareBreakUp);
                }
                Intent i = new Intent(this, FlightDetailsActivity.class);
                i.putExtras(bundle);
                startActivity(i);
                break;

            case R.id.tv_click_to_see_details:
                SelectorDialog selectorDialog = SelectorDialog.getInstance(this, R.layout.dialog_selection, Window.FEATURE_NO_TITLE, true);
                ((TextView) selectorDialog.findViewById(R.id.title)).setText(R.string.text_travellers_title);
                selectorDialog.findViewById(R.id.separator).setBackgroundColor(UIUtils.getThemeColor(this));
                RecyclerView rcSelector = (RecyclerView) selectorDialog.findViewById(R.id.selector);
                rcSelector.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                rcSelector.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
                rcSelector.setAdapter(new SelectorDialog.SelectorAdapter(ID_SHOW_TRAVELLERS, R.id.tv_click_to_see_details, this));
                selectorDialog.show();
                break;

            case R.id.tv_term_condition_text:
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.terms_and_conditions_url)));
                startActivity(intent);
                break;

            case R.id.left_icon:
                finish();
                break;

            case R.id.rl_credit_card:
                relativeLayoutCreditCard.setSelected(true);
                relativeLayoutCashCard.setSelected(false);
                relativeLayoutPremLimit.setSelected(false);
                rlPaymentDetails.setVisibility(View.GONE);
                rl_cash_card_detail.setVisibility(View.GONE);
                card_number.setVisibility(View.GONE);
                card_pin.setVisibility(View.GONE);
                checkBalance.setVisibility(View.GONE);
                rlBook.setVisibility(View.GONE);
                ll_cash_card_info.setVisibility(View.GONE);
                paymentType = Utils.PAYMENT_TYPE_CREDIT_CARD;
                //goforCreditCard();
                if (AppPreferences.getInstance(ReviewAndPaymentActivity.this).getToken().equals(""))
                    doGuestLogin();
                else
                    goforCreditCard();
                break;
            case R.id.rl_cash_card:
                rlBook.setVisibility(View.GONE);
                relativeLayoutCreditCard.setSelected(false);
                relativeLayoutCashCard.setSelected(true);
                relativeLayoutPremLimit.setSelected(false);
                rlPaymentDetails.setVisibility(View.GONE);
                rl_cardgrid.setVisibility(View.GONE);
                ll_cash_card_info.setVisibility(View.GONE);
                paymentType = Utils.PAYMENT_TYPE_CASH_CARD;
                if (AppPreferences.getInstance(ReviewAndPaymentActivity.this).getToken().equals(""))
                    doGuestLogin();
                else {
//                    hitAvailabilitryForCashCard();
                    if (isConvinienceFeeAdded) {
                        convineieceFeeeTv.setVisibility(View.GONE);
//                            bookingAmount = bookingAmount - convenienceFee;
                        isConvinienceFeeAdded = false;
                    }
                    setUiforCashCard();
                }

                break;

            case R.id.rl_creditlimit:
                rl_cash_card_detail.setVisibility(View.GONE);
                card_number.setVisibility(View.GONE);
                card_pin.setVisibility(View.GONE);
                checkBalance.setVisibility(View.GONE);
                rl_cardgrid.setVisibility(View.GONE);
                ll_cash_card_info.setVisibility(View.GONE);
                paymentType = Utils.PAYMENT_TYPE_CREDIT_LIMIT;
                if (AppPreferences.getInstance(ReviewAndPaymentActivity.this).getToken().equals(""))
                    doGuestLogin();
                else {
                    if (!AppPreferences.getInstance(this).getToken().equals("") && AppPreferences.getInstance(this).getB2B())
                        hitAvailabilityForCreditLimit();
                    //getCashAndCreditBalance();
                }
                break;

            case R.id.have_a_promocode:
                tvHaveAPromocode.setVisibility(View.GONE);
                llEnterPromocode.setVisibility(View.VISIBLE);
                //    line.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_break_up:
                intent = new Intent(this, UpdatedFareBreakUpActivity.class);
                bundle = getIntent().getExtras();
                bundle.putSerializable("fare", fareBreakUp);
                bundle.putSerializable("inboundFare", inFareBreakUp);
                if (isConvinienceFeeAdded) {
                    bundle.putFloat("convenienceFees", convenienceFee);
                } else {
                    bundle.putFloat("convenienceFees", 0.0f);
                }
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.tv_promo_message:
                if (tvPromoMessage.getText().toString().contains("click here")) {
                    intent = new Intent(this, UpdatedFareBreakUpActivity.class);
                    bundle = getIntent().getExtras();
                    bundle.putSerializable("fare", fareBreakUp);
                    bundle.putSerializable("inboundFare", inFareBreakUp);
                    if (isConvinienceFeeAdded) {
                        bundle.putFloat("convenienceFees", convenienceFee);
                    } else {
                        bundle.putFloat("convenienceFees", 0.0f);
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;

            case R.id.apply_button:
                applyPromocode();
                break;

            case R.id.remove_button:
                removePromocode();
                break;

            case R.id.rl_book:
                if (!Utils.isNetworkAvailable(this)) {
                    Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

                } else if (validateForm()) {
                    String url;
                    NetworkTask networkTask = new NetworkTask(this, ID_AVAILABILITY);
                    networkTask.setDialogMessage(getString(R.string.please_wait));
                    networkTask.exposePostExecute(this);
                    networkTask.exposeDoInBackground(this);
                    String paramsArray[];
                    url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlAvailabilty;
                    Utils.appendLog("AvailabilityURL: " + url);
                    if (flightDataSecondWay != null) {
                        if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS)) {
                            paramsArray = new String[]{url, Requests.roundTripSpecialGDSAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                                    getIntent().getStringExtra("selected_tab"), flightDataFirstWay.getUniqueEntityId())};

                        } else if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && !getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS)) {
                            paramsArray = new String[]{url, Requests.roundTripSpecialLCCAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                                    getIntent().getStringExtra("selected_tab"), flightDataFirstWay.getUniqueEntityId(), flightDataSecondWay.getUniqueEntityId())};

                        } else if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_REGULAR) && getIntent().getStringExtra("selected_tab") == null) {
                            paramsArray = new String[]{url, Requests.oneWayRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), flightDataFirstWay.getUniqueEntityId())};

                        } else {
                            paramsArray = new String[]{url, Requests.roundTripRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                                    flightDataFirstWay.getUniqueEntityId(), flightDataSecondWay.getUniqueEntityId())};
                        }
                    } else
                        paramsArray = new String[]{url, Requests.oneWayRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), flightDataFirstWay.getUniqueEntityId())};

                    networkTask.execute(paramsArray);

                    /*try {
                        String url;
                        List<NameValuePair> list = new ArrayList<>();
                        list.add(new BasicNameValuePair("searchId", getIntent().getStringExtra("search_id")));
                        list.add(new BasicNameValuePair("token", AppPreferences.getInstance(this).getToken()));
                        NetworkTask networkTask = new NetworkTask(this, ID_BOOKING_INITIALIZE, list);
                        networkTask.setDialogMessage(getString(R.string.please_wait));
                        networkTask.exposePostExecute(this);
                        String paramsArray[];
                        url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlBookingInitialize;
                        paramsArray = new String[]{url, Requests.bookingInitializeRequest(getIntent().getStringExtra("search_id"), AppPreferences.getInstance(this).getToken())};
                        networkTask.execute(paramsArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }
                break;


            case R.id.submitBtnGateway:
                //hit validate request
                if (!Utils.isNetworkAvailable(this)) {
                    Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

                } else if (checkedPosition != -1) {
                    if (validateForm())
                        hitValidateForCreditCard(cardName);
                } else {
                    Toast.makeText(this, "Please select a card", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.check_balance_btn:
                String msg = validateCashCard();
                if (msg.equals("")) {
                    //hit api
                    cashCardNumber = card_number.getText().toString();
                    cashCardPin = card_pin.getText().toString();
                    hitCheckBalanceCard(cashCardNumber, cashCardPin);
                } else {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.book_from_cashCard:
                if (!Utils.isNetworkAvailable(this)) {
                    Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

                } else if (validateForm())
                    hitValidateForCashCard();
                break;

            case R.id.tvProceedToBooking:
                break;

            case R.id.zero_cancel_info:
                Utils.showInfo(this, zeroInfo);
                break;

            case R.id.visa_info:
                Utils.showInfo(this, visaInfo);
                break;

            case R.id.insure_info:
                Utils.showInfo(this, insuranceInfo);
                break;

            case R.id.cb_zero_cancellation:
                if (zeroCharge != null) {
                    if (cbZeroCancellation.isChecked()) {
                        hitForOtherCharges("ZEROCANCELLATIONCHARGE", true);
                        toastMessage = "Rs " + zeroCharge + " added";
                    } else {
                        hitForOtherCharges("ZEROCANCELLATIONCHARGE", false);
                        toastMessage = "Rs " + zeroCharge + " deducted";
                    }
                }
                break;

            case R.id.cb_visa:
                if (null != visaCharge) {
                    if (cbVisa.isChecked()) {
                        hitForOtherCharges("VISA", true);
                        toastMessage = "Rs " + visaCharge + " added";
                    } else {
                        hitForOtherCharges("VISA", false);
                        toastMessage = "Rs " + visaCharge + " deducted";
                    }
                }
                break;

            case R.id.cb_insure_your_trip:
                if (null != insuranceCharge) {
                    if (cbInsuration.isChecked()) {
                        hitForOtherCharges("INSURANCE", true);
                        toastMessage = "Rs " + insuranceCharge + " added";
                    } else {
                        hitForOtherCharges("INSURANCE", false);
                        toastMessage = "Rs " + insuranceCharge + " deducted";
                    }
                }
                break;

            case R.id.addGstDetails:
                if (((CheckBox) v).isChecked()) {
                    if (cbGetGstInfo.getText().equals(getResources().getString((R.string.send_gst_details)))) {
                        /*AffiliateData affiliateData = new Gson().fromJson(AppPreferences.getInstance(this).getAffiliateData(),
                                AffiliateData.class);
                        Company company = affiliateData.getCompany();
                        GstDetails gstDetails = company.getGstDetails();
                        if (gstDetails != null) {
                            companyName.setText(gstDetails.getCompanyName());
                            gstNo.setText(gstDetails.getGstNo());
                            billingPhone.setText(gstDetails.getBillingPhone());
                            emailId.setText(gstDetails.getEmail());
                            city.setText(gstDetails.getCityName());
                            address.setText(gstDetails.getAddress());
                            pincode.setText(gstDetails.getZipCode());
                            countryList = new ArrayList<>();
                            countryList.addAll(DatabaseController.getInstance(this).getCountryList());
                            CountrySpinnerAdapter countryAdapter = new CountrySpinnerAdapter(this, countryList);
                            countrySpinner.setAdapter(countryAdapter);
                            countrySpinner.setSelection(countryList.indexOf(gstDetails.getCountryName()));
                        } else {
                            spinnerTask();
                        }*/
                        //   scrollScreenToGstLayout();
                    }
                    rlGetGstDetails.setVisibility(View.VISIBLE);
                } else {
                    rlGetGstDetails.setVisibility(View.GONE);
                }
                break;

            case R.id.gstSubmit:
                String gstPattern = "\\d{2}[A-Za-z]{5}\\d{4}[A-Za-z]{1}\\d{1}[A-Za-z\\d]{2}";
                String phPattern = "^([0-9+-]*)$";
                strCompanyName = companyName.getText().toString();
                if (strCompanyName == null || strCompanyName.equals("") || strCompanyName.equalsIgnoreCase("Company Name")) {
                    Toast.makeText(getApplicationContext(), "Enter Company Name", Toast.LENGTH_LONG).show();
                } else {
                    gstNumber = gstNo.getText().toString();
                    if (gstNumber == null || gstNumber.equalsIgnoreCase("") || gstNumber.equalsIgnoreCase("G.S.T. Number")) {
                        Toast.makeText(getApplicationContext(), "Enter G.S.T. Number", Toast.LENGTH_LONG).show();
                    } else if (!gstNumber.matches(gstPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid G.S.T. Number", Toast.LENGTH_LONG).show();
                    } else {
                        strBillingPhone = billingPhone.getText().toString();
                        if (strBillingPhone == null || strBillingPhone.equalsIgnoreCase("") || strBillingPhone.equalsIgnoreCase("Billing Phone")) {
                            Toast.makeText(getApplicationContext(), "Enter Billing Phone Number", Toast.LENGTH_LONG).show();
                        } else if (strBillingPhone.length() != 10 || !strBillingPhone.matches(phPattern)) {
                            Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
                        } else {
                            email = emailId.getText().toString();
                            if (email == null || email.equalsIgnoreCase("") || email.equalsIgnoreCase("Email ID")) {
                                Toast.makeText(getApplicationContext(), "Enter Email ID", Toast.LENGTH_LONG).show();
                            } else if (!Utils.isValidEmail(email)) {
                                Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                            } else {
                                if (countrySpinner.getSelectedItemPosition() == 0) {
                                    Toast.makeText(getApplicationContext(), "Select Country", Toast.LENGTH_LONG).show();
                                } else {
                                    countryName = countryList.get(countrySpinner.getSelectedItemPosition());
                                    if (countryName.equalsIgnoreCase("India")) {
                                        stateName = stateList.get(stateSpinner.getSelectedItemPosition());
                                    }
                                    cityName = city.getText().toString();
                                    if (cityName == null || cityName.equalsIgnoreCase("") || cityName.equalsIgnoreCase("City")) {
                                        Toast.makeText(getApplicationContext(), "Enter City", Toast.LENGTH_LONG).show();
                                    } else {
                                        strAddress = address.getText().toString();
                                        if (strAddress == null || strAddress.equalsIgnoreCase("") || strAddress.equalsIgnoreCase("Address")) {
                                            Toast.makeText(getApplicationContext(), "Enter Address", Toast.LENGTH_LONG).show();
                                        } else {
                                            strPincode = pincode.getText().toString();
                                            if (strPincode == null || strPincode.equalsIgnoreCase("") || strPincode.equalsIgnoreCase("Pincode")) {
                                                Toast.makeText(getApplicationContext(), "Enter Pincode", Toast.LENGTH_LONG).show();
                                            } else {
                                                insertDataToDb();
                                                rlGetGstDetails.setVisibility(View.GONE);
                                                cbGetGstInfo.setText(R.string.review_gst);
                                                cbGetGstInfo.setChecked(false);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Utils.hideSoftKey(this);
                break;

            case R.id.gstReset:
                companyName.getText().clear();
                companyName.setHint("Company Name");
                gstNo.getText().clear();
                gstNo.setHint("G.S.T. Number");
                billingPhone.getText().clear();
                billingPhone.setHint("Billing Phone");
                emailId.getText().clear();
                emailId.setHint("Email ID");
                city.getText().clear();
                city.setHint("City");
                address.getText().clear();
                address.setHint("Address");
                pincode.getText().clear();
                pincode.setHint("Pincode");
//                spinnerTask();
                break;

            case R.id.CompanyNameClick:
                openGstSearchScreen("companyName");
                break;

            case R.id.GstNoClick:
                openGstSearchScreen("gstNo");
                break;

            case R.id.tvPayNow:
                String userData = AppPreferences.getInstance(this).getUserData();
                if (!userData.isEmpty()) {
                    //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    LoginResponse loginResponse = new Gson().fromJson(userData, LoginResponse.class);
                }
                hitAvailability();

                break;
        }
    }

    private void hitAvailability() {
        try {
            String url;
            NetworkTask networkTask = new NetworkTask(this, ID_AVAILABILITY_FROM_PAY_NOW);
            networkTask.setDialogMessage(getString(R.string.please_wait));
            networkTask.exposePostExecute(this);
            String paramsArray[];
            url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlAvailabilty;
            if (flightDataSecondWay != null) {
                if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS)) {
                    paramsArray = new String[]{url, Requests.roundTripSpecialGDSAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                            getIntent().getStringExtra("selected_tab"), flightDataFirstWay.getUniqueEntityId())};

                } else if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && !getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS)) {
                    paramsArray = new String[]{url, Requests.roundTripSpecialLCCAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                            getIntent().getStringExtra("selected_tab"), flightDataFirstWay.getUniqueEntityId(), flightDataSecondWay.getUniqueEntityId())};

                } else if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_REGULAR) && getIntent().getStringExtra("selected_tab") == null) {
                    paramsArray = new String[]{url, Requests.oneWayRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), flightDataFirstWay.getUniqueEntityId())};

                } else {
                    paramsArray = new String[]{url, Requests.roundTripRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                            flightDataFirstWay.getUniqueEntityId(), flightDataSecondWay.getUniqueEntityId())};
                }
            } else
                paramsArray = new String[]{url, Requests.oneWayRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), flightDataFirstWay.getUniqueEntityId())};

            networkTask.execute(paramsArray);
        } catch (Exception e) {
            Log.d("Trip", "Eror in goForCreditCar " + e);
        }
    }

    private void openGstSearchScreen(String basis) {
        try {
            Intent i = new Intent(this, GstDetailsSearchActivity.class);
            i.putExtra("BasisForSearch", basis);
            startActivityForResult(i, Utils.request_code_GstSearch);

        } catch (Exception e) {
            Log.d("Trip", "Eror in openSearchScreen " + e);
        }
    }

    public void spinnerTask() {
        countryList = new ArrayList<>();
        countryList.add("Country");
        countryList.addAll(DatabaseController.getInstance(this).getCountryList());
        CountrySpinnerAdapter countryAdapter = new CountrySpinnerAdapter(this, countryList);
        countrySpinner.setAdapter(countryAdapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String countryCode = DatabaseController.getInstance(ReviewAndPaymentActivity.this).getContryCode(countryList.get(countrySpinner.getSelectedItemPosition()));
                    //   Toast.makeText(parent.getContext(), countryCode, Toast.LENGTH_LONG).show();
                    Log.d("countryCode: ", countryCode);
                    stateList = DatabaseController.getInstance(ReviewAndPaymentActivity.this).getStateList(countryCode);
                    Log.d("stateList", String.valueOf(stateList.size()));
                    if (stateList.size() == 0) {
                        stateLayout.setVisibility(View.GONE);
                    } else {
                        stateLayout.setVisibility(View.VISIBLE);
                        CountrySpinnerAdapter stateAdapter = new CountrySpinnerAdapter(ReviewAndPaymentActivity.this, stateList);
                        stateSpinner.setAdapter(stateAdapter);
                        stateSpinner.setSelection(0);
                    }
                } else {
                    stateLayout.setVisibility(View.VISIBLE);
                    stateList = new ArrayList<>();
                    stateList.add("State");
                    CountrySpinnerAdapter stateAdapter = new CountrySpinnerAdapter(ReviewAndPaymentActivity.this, stateList);
                    stateSpinner.setAdapter(stateAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void insertDataToDb() {
        try {
            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.insertDataToGstTable(strCompanyName, gstNumber, strBillingPhone, email, countryName, stateName, cityName, strAddress, strPincode, Long.toString(System.currentTimeMillis()), db);
            db.close();
            dbHelper.close();
        } catch (Exception e) {
            Log.e("Error", "Error in inserting Gst Data due to: " + e.getMessage());
        }
    }

    private void removePromocode() {
        String url;
        NetworkTask networkTask = new NetworkTask(this, REMOVE_PROMOCODE);
        networkTask.setProgressDialog(true);
        networkTask.setDialogMessage(getString(R.string.please_wait));
        networkTask.exposePostExecute(this);
        String paramsArray[] = null;

        url = UIUtils.getBaseUrl(this) + WebServiceConstants.applyPromocode;
        String request = null;
        request = Requests.removePromocode(getIntent().getStringExtra("search_id"), flightDataFirstWay.getUniqueEntityId(), etEnterPromocode.getText());
        /*request = "{\n" +
                "   \"searchId\": \"LRSAADALKY\",\n" +
                "   \"selectedFlightId\": \"LRSAADALKY_11\",\n" +
                "   \"discount\": {\"discountCode\": \"TESTCODE01\"}\n" +
                "}";*/
        paramsArray = new String[]{url, request};
        networkTask.execute(paramsArray);
    }

    private void applyPromocode() {
        String url;
        NetworkTask networkTask = new NetworkTask(this, APPLY_PROMOCODE);
        networkTask.setProgressDialog(true);
        networkTask.setDialogMessage(getString(R.string.please_wait));
        networkTask.exposePostExecute(this);
        String paramsArray[] = null;

        url = UIUtils.getBaseUrl(this) + WebServiceConstants.applyPromocode;
        String request = null;
        request = Requests.applyPromocode(getIntent().getStringExtra("search_id"), flightDataFirstWay.getUniqueEntityId(), etEnterPromocode.getText());
        /*request = "{\n" +
                "   \"searchId\": \"LRSAADALKY\",\n" +
                "   \"selectedFlightId\": \"LRSAADALKY_11\",\n" +
                "   \"discount\": {\"discountCode\": \"TESTCODE01\"}\n" +
                "}";*/
        paramsArray = new String[]{url, request};
        networkTask.execute(paramsArray);
    }

    private boolean validateForm() {
        String email = etEmailId.getText().toString();
        String number = etMobileNo.getText().toString();
        String isdCode = etIsdCode.getText().toString();
        if (!Utils.isValidEmail(email)) {
            Toast.makeText(this, getString(R.string.wrong_email_id), Toast.LENGTH_SHORT).show();
            return false;

        } else if (!Utils.isValidMobile(number, isdCode)) {
            if (isdCode.equals("+61") || isdCode.equals("+64")) {
                Toast.makeText(this, getString(R.string.valid_phone_no_txt9), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.valid_phone_no_txt10), Toast.LENGTH_SHORT).show();
            }
            return false;

        } else if (!iconTextViewCheckFirst.isChecked()) {
            Toast.makeText(ReviewAndPaymentActivity.this, getString(R.string.agree_terms), Toast.LENGTH_SHORT).show();
            return false;

        } else
            return true;
    }

    private String validateCashCard() {
        String msg = "";
        if (card_number.getText().toString().equals("")) {
            msg = "Please enter the correct Card Number";
            return msg;
        } else if (card_pin.getText().toString().equals("")) {
            msg = "Please enter the correct Card Pin";
            return msg;
        } else {
            return msg;
        }
    }

    private void hitCheckBalanceCard(String cardNumber, String cardPin) {
        try {
            NetworkTask networkTask = new NetworkTask(this, ID_CHECK_BALANCE);
            networkTask.setDialogMessage(getString(R.string.please_wait));
            networkTask.exposePostExecute(this);
            String paramsArray[];
            String url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlCashCardBalance;
            paramsArray = new String[]{url, Requests.cashCardValidationRequest(cardNumber, cardPin)};
            networkTask.execute(paramsArray);
        } catch (Exception e) {

        }
    }

    private void hitValidateForCashCard() {
        try {
            NetworkTask networkTask = new NetworkTask(this, ID_BOOKING_VALIDATE_CASHCARD);
            networkTask.setDialogMessage(getString(R.string.please_wait));
            networkTask.exposePostExecute(this);
            String paramsArray[];
            String urlBookingValidate = UIUtils.getBaseUrl(this) + WebServiceConstants.urlBookingValidate;
            Utils.appendLog("BookingValidateURL: " + urlBookingValidate);
            for (Traveller traveller : listPassengersInfo)
                traveller.setMealPref(Utils.OPTION_NP);
            setBillingInfo(Utils.PAYMENT_TYPE_CASH_CARD);
            String request = Requests.bookingValidateRequest(getIntent().getStringExtra("search_id"), null, billingInfo, listPassengersInfo, iconTextViewCheckSeconds.isChecked());
            Utils.appendLog("BookingValidateRequest: " + request);
            paramsArray = new String[]{urlBookingValidate, request};
            networkTask.execute(paramsArray);
        } catch (Exception e) {

        }
    }

    private void hitValidateForCreditCard(String gateWay) {
        NetworkTask networkTask = new NetworkTask(this, ID_BOOKING_VALIDATE_CREDITCARD);
        networkTask.setDialogMessage(getString(R.string.please_wait));
        networkTask.exposePostExecute(this);
        String paramsArray[];
        String urlBookingValidate = UIUtils.getBaseUrl(this) + WebServiceConstants.urlBookingValidate;
        Utils.appendLog("BookingValidateURL: " + urlBookingValidate);
        for (Traveller traveller : listPassengersInfo)
            traveller.setMealPref(Utils.OPTION_NP);
        setBillingInfo(Utils.PAYMENT_TYPE_CREDIT_CARD);
        String request = Requests.bookingValidateRequest(getIntent().getStringExtra("search_id"), gateWay, billingInfo, listPassengersInfo, iconTextViewCheckSeconds.isChecked());
        Utils.appendLog("BookingValidateRequest: " + request);
        paramsArray = new String[]{urlBookingValidate, request};
        networkTask.execute(paramsArray);
    }

    private void setUiforCashCard() {
        try {
            //first hit avaailablility
            rl_cash_card_detail.setVisibility(View.VISIBLE);
            card_number.setVisibility(View.VISIBLE);
            card_number.setHint("Card number");
            card_pin.setVisibility(View.VISIBLE);
            card_pin.setHint("Card pin");
            checkBalance.setVisibility(View.VISIBLE);
            tvPromoMessage.setVisibility(View.GONE);
            if (!AppPreferences.getInstance(this).getB2B()) {
                llPromocode.setVisibility(View.VISIBLE);
                tvHaveAPromocode.setVisibility(View.VISIBLE);
                llEnterPromocode.setVisibility(View.GONE);
//                itvArrow.setVisibility(View.VISIBLE);
                applyButton.setVisibility(View.VISIBLE);
                tvRemoveButton.setVisibility(View.GONE);
            }
            scrollScreenToBottom();
        } catch (Exception e) {
            Log.d("Trip", "Eror in goForCashCard " + e);
        }
    }

    private void hitAvailabilitryForCashCard() {
        try {
            {
                String url;
                NetworkTask networkTask = new NetworkTask(this, ID_AVAILABILITY_CASH_CARD);
                networkTask.setDialogMessage(getString(R.string.please_wait));
                networkTask.exposePostExecute(this);
                String paramsArray[];
                url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlAvailabilty;
                if (flightDataSecondWay != null) {
                    if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS)) {
                        paramsArray = new String[]{url, Requests.roundTripSpecialGDSAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                                getIntent().getStringExtra("selected_tab"), flightDataFirstWay.getUniqueEntityId())};

                    } else if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && !getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS)) {
                        paramsArray = new String[]{url, Requests.roundTripSpecialLCCAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                                getIntent().getStringExtra("selected_tab"), flightDataFirstWay.getUniqueEntityId(), flightDataSecondWay.getUniqueEntityId())};

                    } else if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_REGULAR) && getIntent().getStringExtra("selected_tab") == null) {
                        paramsArray = new String[]{url, Requests.oneWayRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), flightDataFirstWay.getUniqueEntityId())};

                    } else {
                        paramsArray = new String[]{url, Requests.roundTripRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                                flightDataFirstWay.getUniqueEntityId(), flightDataSecondWay.getUniqueEntityId())};
                    }
                } else
                    paramsArray = new String[]{url, Requests.oneWayRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), flightDataFirstWay.getUniqueEntityId())};

                networkTask.execute(paramsArray);
            }
        } catch (Exception e) {
            Log.d("Trip", "Eror in goForCreditCar " + e);
        }
    }

    private void hitAvailabilityForCreditLimit() {
        try {
            {
                String url;
                NetworkTask networkTask = new NetworkTask(this, ID_AVAILABILITY_CREDIT_LIMIT);
                networkTask.setDialogMessage(getString(R.string.please_wait));
                networkTask.exposePostExecute(this);
                String paramsArray[];
                url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlAvailabilty;
                if (flightDataSecondWay != null) {
                    if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS)) {
                        paramsArray = new String[]{url, Requests.roundTripSpecialGDSAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                                getIntent().getStringExtra("selected_tab"), flightDataFirstWay.getUniqueEntityId())};

                    } else if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && !getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS)) {
                        paramsArray = new String[]{url, Requests.roundTripSpecialLCCAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                                getIntent().getStringExtra("selected_tab"), flightDataFirstWay.getUniqueEntityId(), flightDataSecondWay.getUniqueEntityId())};

                    } else if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_REGULAR) && getIntent().getStringExtra("selected_tab") == null) {
                        paramsArray = new String[]{url, Requests.oneWayRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), flightDataFirstWay.getUniqueEntityId())};

                    } else {
                        paramsArray = new String[]{url, Requests.roundTripRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                                flightDataFirstWay.getUniqueEntityId(), flightDataSecondWay.getUniqueEntityId())};
                    }
                } else
                    paramsArray = new String[]{url, Requests.oneWayRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), flightDataFirstWay.getUniqueEntityId())};

                networkTask.execute(paramsArray);
            }
        } catch (Exception e) {
            Log.d("Trip", "Eror in goForCreditCar " + e);
        }

    }

    private void goforCreditCard() {
        /*try {
            {
                String url;
                NetworkTask networkTask = new NetworkTask(this, ID_AVAILABILITY_CREDIT_CARD);
                networkTask.setDialogMessage(getString(R.string.please_wait));
                networkTask.exposePostExecute(this);
                String paramsArray[];
                url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlAvailabilty;
                if (flightDataSecondWay != null) {
                    if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS)) {
                        paramsArray = new String[]{url, Requests.roundTripSpecialGDSAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                                getIntent().getStringExtra("selected_tab"), flightDataFirstWay.getUniqueEntityId())};

                    } else if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && !getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS)) {
                        paramsArray = new String[]{url, Requests.roundTripSpecialLCCAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                                getIntent().getStringExtra("selected_tab"), flightDataFirstWay.getUniqueEntityId(), flightDataSecondWay.getUniqueEntityId())};

                    } else if (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_REGULAR) && getIntent().getStringExtra("selected_tab") == null) {
                        paramsArray = new String[]{url, Requests.oneWayRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), flightDataFirstWay.getUniqueEntityId())};

                    } else {
                        paramsArray = new String[]{url, Requests.roundTripRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), getIntent().getStringExtra("fare_results"),
                                flightDataFirstWay.getUniqueEntityId(), flightDataSecondWay.getUniqueEntityId())};
                    }
                } else
                    paramsArray = new String[]{url, Requests.oneWayRegularAvailabilityRequest(getIntent().getStringExtra("search_id"), flightDataFirstWay.getUniqueEntityId())};

                networkTask.execute(paramsArray);
            }
        } catch (Exception e) {
            Log.d("Trip", "Eror in goForCreditCar " + e);
        }*/

        setUiforPaymentGateways(gatewayList.getGateWayList());
        if (!isConvinienceFeeAdded) {
            convineieceFeeeTv.setVisibility(View.VISIBLE);
            convineieceFeeeTv.setText(getString(R.string.convenience_fees_is) + " " + UIUtils.getFareToDisplay(this, convenienceFee));
            bookingAmount = bookingAmount + convenienceFee;
            tvFare.setText(UIUtils.getFareToDisplay(this, bookingAmount));
            isConvinienceFeeAdded = true;
        } else {
            convineieceFeeeTv.setVisibility(View.VISIBLE);
            convineieceFeeeTv.setText(getString(R.string.convenience_fees_is) + " " + UIUtils.getFareToDisplay(this, convenienceFee));
            tvFare.setText(UIUtils.getFareToDisplay(this, bookingAmount));
            isConvinienceFeeAdded = true;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, int selectionId) {
        return new FieldViewHolder(LayoutInflater.from(this).inflate(R.layout.selector_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, int selectionId, int srcResId) {
        ((FieldViewHolder) holder).tvField.setText(new StringBuilder().append(position + 1).append(". ").append(listPassengersInfo.get(position).getTitle()).append(". ").append(listPassengersInfo.get(position).getFirstName()).append(" ").append(listPassengersInfo.get(position).getLastName()).toString());
    }

    @Override
    public int getItemCount(int selectionId) {
        return listPassengersInfo.size();
    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {
        if (object == null || object.equals("")) {
            Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else if (id == GUEST_LOGIN) {
            try {

                Log.d("Trip", "res is " + object);
                Gson gson = new Gson();
                JSONObject obj = new JSONObject(object);
                LoginResponse loginResponse;
                loginResponse = gson.fromJson(obj.getString("user"), LoginResponse.class);
                AppPreferences.getInstance(ReviewAndPaymentActivity.this).setUserData(obj.getString("user"));
                AppPreferences.getInstance(ReviewAndPaymentActivity.this).setToken(loginResponse.getToken());
                AppPreferences.getInstance(ReviewAndPaymentActivity.this).isLoggedIn(true);
                AppPreferences.getInstance(ReviewAndPaymentActivity.this).isB2B(loginResponse.isB2BUser());
                AppPreferences.getInstance(ReviewAndPaymentActivity.this).setGuestLogin(true);
                if (paymentType.equals(Utils.PAYMENT_TYPE_CREDIT_LIMIT)) {

                    if (!AppPreferences.getInstance(this).getToken().equals("") && AppPreferences.getInstance(this).getB2B())
                        getCashAndCreditBalance();
                } else if (paymentType.equals(Utils.PAYMENT_TYPE_CREDIT_CARD)) {
                    goforCreditCard();
                } else if (paymentType.equals(Utils.PAYMENT_TYPE_CASH_CARD)) {
                    hitAvailabilitryForCashCard();
                }
            } catch (Exception e) {

            }
        } else {
            if (id == ID_AVAILABILITY_FROM_PAY_NOW) {
                Log.d("AvailabilityResponse: ", object);
                Utils.appendLog("AvailabilityResponse: " + object);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(object);
                    if (obj.has("status") && obj.getString("status").equals("0")) {
                        flightRefineDialog(obj);

                    } else {
                        availabilityResponse = new Gson().fromJson(object, AvailabilityResponse.class);
                        String userData = AppPreferences.getInstance(this).getUserData();
                        if (!userData.isEmpty() && !AppPreferences.getInstance(this).getB2B()) {
                            //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                            LoginResponse loginResponse = new Gson().fromJson(userData, LoginResponse.class);
                        }
                        paymentLayout.setVisibility(View.VISIBLE);
                        tvPayNow.setVisibility(View.GONE);
                        tvPayNowRemarks.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(object);
                            if (jsonObject.has("status") && jsonObject.getString("status").equals("0")) {
                                flightRefineDialog(jsonObject);
                            } else {
                                setUiForPromoAndTaxBreakUp(availabilityResponse);
                            }
                        } catch (Exception e) {

                        }
                    }
                } catch (JSONException e) {
                    Utils.appendLog("Exception in Availability " + e.getMessage());
                    e.printStackTrace();
                }

            } else if (id == ID_AVAILABILITY) {
                try {
                    JSONObject obj = new JSONObject(object);
                    Utils.appendLog("AvailabilityResponse " + object);
                    if (obj.has("status") && obj.getString("status").equals("0")) {
                        flightRefineDialog(obj);
                    } else {
                        Log.d("Trip", "res is " + object);
                        bookingValidateResponse = new Gson().fromJson(object, BookingValidateResponse.class);
                        if (bookingValidateResponse.getStatus().equals(STATUS_SUCCESS)) {
                            String url;
                            List<NameValuePair> list = new ArrayList<>();
                            list.add(new BasicNameValuePair("searchId", getIntent().getStringExtra("search_id")));
                            list.add(new BasicNameValuePair("token", AppPreferences.getInstance(this).getToken()));
                            NetworkTask networkTask = new NetworkTask(this, ID_BOOKING_INITIALIZE, list);
                            networkTask.setDialogMessage(getString(R.string.please_wait));
                            networkTask.exposePostExecute(this);
                            String paramsArray[];
                            url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlBookingInitialize;
                            Utils.appendLog("BookingValidateURL: " + url);
                            String request = Requests.bookingInitializeRequest(getIntent().getStringExtra("search_id"), AppPreferences.getInstance(this).getToken());
                            Utils.appendLog("BookingValidateRequest: " + request);
                            paramsArray = new String[]{url, request};
                            networkTask.execute(paramsArray);
                        } else
                            Utils.showAlertDialog(this, getString(R.string.app_name), bookingValidateResponse.getMessage(), getString(R.string.ok_text), null, null, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.appendLog("Exception in Availability " + e.getMessage());
                }
            } else if (id == ID_AVAILABILITY_CREDIT_CARD) {
                Log.d("Trip", "res for cc is " + object);
                Utils.appendLog("AvailabilityResponse for Credit Card " + object);
                String res = "";
                try {
                    JSONObject jsonObject = new JSONObject(object);
                    if (jsonObject.has("status") && jsonObject.getString("status").equals("0")) {
                        flightRefineDialog(jsonObject);
                    } else {
                        try {
                            JSONArray jsonAr = jsonObject.getJSONArray("allPaymentGateways");
                            res = jsonAr.toString();
                            JSONObject data, selectedResult, fare = null, taxes = null, total, totalPrice, selectedInboundResult,
                                    selectedOutboundResult, inboundFare = null, inboundTaxes = null, inboundTotal, inboundTotalPrice;
                            data = jsonObject.getJSONObject("data");
                            if (object.contains("selectedOutBoundFlight")) {
                                Bundle bundle = getIntent().getExtras();
                                bundle.putSerializable("flightDataFirstWay", new Gson().fromJson(data.getJSONObject
                                        ("selectedOutBoundFlight").toString(), FlightData.class));
                                bundle.putSerializable("flightDataSecondWay", new Gson().fromJson(data.getJSONObject
                                        ("selectedInBoundFlight").toString(), FlightData.class));
                                getIntent().putExtras(bundle);
                            } else if (object.contains("selectedResult")) {
                                Bundle bundle = getIntent().getExtras();
                                bundle.putSerializable("flightDataFirstWay", new Gson().fromJson(data.getJSONObject
                                        ("selectedResult").toString(), FlightData.class));
                                bundle.putSerializable("flightDataSecondWay", null);
                                getIntent().putExtras(bundle);
                            }
                            if (object.contains("selectedResult")) {
                                selectedResult = data.getJSONObject("selectedResult");
//                                setUiForAdditionalBenefits(selectedResult);
                                fare = selectedResult.getJSONObject("fare");
                                taxes = fare.getJSONObject("taxes");
                                total = fare.getJSONObject("total");
                                totalPrice = total.getJSONObject("price");
                                bookingAmount = Float.parseFloat(totalPrice.getString("amount"));
                            } else {
                                selectedOutboundResult = data.getJSONObject("selectedOutBoundFlight");
//                                setUiForAdditionalBenefits(selectedOutboundResult);
                                selectedInboundResult = data.getJSONObject("selectedInBoundFlight");
                                fare = selectedOutboundResult.getJSONObject("fare");
                                taxes = fare.getJSONObject("taxes");
                                total = fare.getJSONObject("total");
                                totalPrice = total.getJSONObject("price");
                                inboundFare = selectedInboundResult.getJSONObject("fare");
                                inboundTaxes = inboundFare.getJSONObject("taxes");
                                inboundTotal = inboundFare.getJSONObject("total");
                                inboundTotalPrice = inboundTotal.getJSONObject("price");
                                bookingAmount = Float.parseFloat(totalPrice.getString("amount"))
                                        + Float.parseFloat(inboundTotalPrice.getString("amount"));
                                this.inFareBreakUp = new Gson().fromJson(inboundFare.toString(), Fare.class);
                            }
                            try {
                                float conFee = convenienceFee;
                                convenienceFee = Float.parseFloat(jsonObject.getString("convenienceFee"));
                                if (!isConvinienceFeeAdded) {
                                    convineieceFeeeTv.setVisibility(View.VISIBLE);
                                    convineieceFeeeTv.setText(getString(R.string.convenience_fees_is) + " " + UIUtils.getFareToDisplay(this, convenienceFee));
                                    bookingAmount = bookingAmount + convenienceFee;
                                    tvFare.setText(UIUtils.getFareToDisplay(this, bookingAmount));
                                    isConvinienceFeeAdded = true;
                                } else {
                                    convineieceFeeeTv.setVisibility(View.VISIBLE);
                                    convineieceFeeeTv.setText(getString(R.string.convenience_fees_is) + " " + UIUtils.getFareToDisplay(this, convenienceFee));
                                    bookingAmount = bookingAmount + convenienceFee - conFee;
                                    tvFare.setText(UIUtils.getFareToDisplay(this, bookingAmount));
                                    isConvinienceFeeAdded = true;
                                }
                            } catch (Exception e) {

                            }
                            Log.d("Trip", "fee is " + convenienceFee);
                            try {
                                String serviceCharge = taxes.getJSONObject("fare.code.servicecharge").getJSONObject("price").getString("amount");
                                String gstCharge = taxes.getJSONObject("fare.code.servicecharge.indian.gst").getJSONObject("price").getString("amount");
                                if (inboundTaxes != null) {
                                    serviceCharge = String.valueOf(Float.parseFloat(serviceCharge) + Float.parseFloat(
                                            taxes.getJSONObject("fare.code.servicecharge").getJSONObject("price").getString("amount")));
                                    gstCharge = String.valueOf(Float.parseFloat(gstCharge) + Float.parseFloat(
                                            taxes.getJSONObject("fare.code.servicecharge.indian.gst").getJSONObject("price").getString("amount")));
                                }
                                tvTaxes.setText("Service Charge: " + serviceCharge + "\nGST: " + gstCharge);
                                tvTaxes.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                tvTaxes.setVisibility(View.GONE);
                            }
                            tvFareBreakUp.setVisibility(View.VISIBLE);
                            this.fareBreakUp = new Gson().fromJson(fare.toString(), Fare.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Type listType = new TypeToken<ArrayList<PaymentGateway>>() {
                        }.getType();
                        gatewayList = new PaymentGatewayList();
                        ArrayList<PaymentGateway> gateways = (ArrayList<PaymentGateway>) new Gson().fromJson(res, listType);
                        gatewayList.setGatewayList(gateways);
                        setUiforPaymentGateways(gatewayList.getGateWayList());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (id == ID_AVAILABILITY_CREDIT_LIMIT) {
                try {
                    JSONObject obj = new JSONObject(object);
                    Utils.appendLog("AvailabilityResponse In Credit Limit " + object);
                    if (obj.has("status") && obj.getString("status").equals("0")) {
                        flightRefineDialog(obj);
                    } else {
                        try {
                            JSONObject data, selectedResult, fare = null, taxes = null, total, totalPrice, selectedInboundResult,
                                    selectedOutboundResult, inboundFare = null, inboundTaxes = null, inboundTotal, inboundTotalPrice;
                            data = obj.getJSONObject("data");
                            if (object.contains("selectedOutBoundFlight")) {
                                Bundle bundle = getIntent().getExtras();
                                bundle.putSerializable("flightDataFirstWay", new Gson().fromJson(data.getJSONObject
                                        ("selectedOutBoundFlight").toString(), FlightData.class));
                                bundle.putSerializable("flightDataSecondWay", new Gson().fromJson(data.getJSONObject
                                        ("selectedInBoundFlight").toString(), FlightData.class));
                                getIntent().putExtras(bundle);
                            } else if (object.contains("selectedResult")) {
                                Bundle bundle = getIntent().getExtras();
                                bundle.putSerializable("flightDataFirstWay", new Gson().fromJson(data.getJSONObject
                                        ("selectedResult").toString(), FlightData.class));
                                bundle.putSerializable("flightDataSecondWay", null);
                                getIntent().putExtras(bundle);
                            }
                            if (object.contains("selectedResult")) {
                                selectedResult = data.getJSONObject("selectedResult");
//                                setUiForAdditionalBenefits(selectedResult);
                                fare = selectedResult.getJSONObject("fare");
                                taxes = fare.getJSONObject("taxes");
                                total = fare.getJSONObject("total");
                                totalPrice = total.getJSONObject("price");
                                bookingAmount = Float.parseFloat(totalPrice.getString("amount"));
                            } else {
                                selectedOutboundResult = data.getJSONObject("selectedOutBoundFlight");
//                                setUiForAdditionalBenefits(selectedOutboundResult);
                                selectedInboundResult = data.getJSONObject("selectedInBoundFlight");
                                fare = selectedOutboundResult.getJSONObject("fare");
                                taxes = fare.getJSONObject("taxes");
                                total = fare.getJSONObject("total");
                                totalPrice = total.getJSONObject("price");
                                inboundFare = selectedInboundResult.getJSONObject("fare");
                                inboundTaxes = inboundFare.getJSONObject("taxes");
                                inboundTotal = inboundFare.getJSONObject("total");
                                inboundTotalPrice = inboundTotal.getJSONObject("price");
                                bookingAmount = Float.parseFloat(totalPrice.getString("amount"))
                                        + Float.parseFloat(inboundTotalPrice.getString("amount"));
                                this.inFareBreakUp = new Gson().fromJson(inboundFare.toString(), Fare.class);
                            }
                            tvFare.setText(UIUtils.getFareToDisplay(this, bookingAmount));
                            try {
                                String serviceCharge = taxes.getJSONObject("fare.code.servicecharge").getJSONObject("price").getString("amount");
                                String gstCharge = taxes.getJSONObject("fare.code.servicecharge.indian.gst").getJSONObject("price").getString("amount");
                                if (inboundTaxes != null) {
                                    serviceCharge = String.valueOf(Float.parseFloat(serviceCharge) + Float.parseFloat(
                                            taxes.getJSONObject("fare.code.servicecharge").getJSONObject("price").getString("amount")));
                                    gstCharge = String.valueOf(Float.parseFloat(gstCharge) + Float.parseFloat(
                                            taxes.getJSONObject("fare.code.servicecharge.indian.gst").getJSONObject("price").getString("amount")));
                                }
                                tvTaxes.setText("Service Charge: " + serviceCharge + "\nGST: " + gstCharge);
                                tvTaxes.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                tvTaxes.setVisibility(View.GONE);
                            }
                            tvFareBreakUp.setVisibility(View.VISIBLE);
                            fareBreakUp = new Gson().fromJson(fare.toString(), Fare.class);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        getCashAndCreditBalance();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (id == ID_AVAILABILITY_CASH_CARD) {
                try {
                    JSONObject obj = new JSONObject(object);
                    Utils.appendLog("AvailabilityResponse for Cash Card " + object);
                    if (obj.has("status") && obj.getString("status").equals("0")) {
                        flightRefineDialog(obj);
                    } else {
                        JSONObject data, selectedResult, fare = null, taxes = null, total, totalPrice, selectedInboundResult,
                                selectedOutboundResult, inboundFare = null, inboundTaxes = null, inboundTotal, inboundTotalPrice;
                        data = obj.getJSONObject("data");
                        if (object.contains("selectedOutBoundFlight")) {
                            Bundle bundle = getIntent().getExtras();
                            bundle.putSerializable("flightDataFirstWay", new Gson().fromJson(data.getJSONObject
                                    ("selectedOutBoundFlight").toString(), FlightData.class));
                            bundle.putSerializable("flightDataSecondWay", new Gson().fromJson(data.getJSONObject
                                    ("selectedInBoundFlight").toString(), FlightData.class));
                            getIntent().putExtras(bundle);
                        } else if (object.contains("selectedResult")) {
                            Bundle bundle = getIntent().getExtras();
                            bundle.putSerializable("flightDataFirstWay", new Gson().fromJson(data.getJSONObject
                                    ("selectedResult").toString(), FlightData.class));
                            bundle.putSerializable("flightDataSecondWay", null);
                            getIntent().putExtras(bundle);
                        }
                        if (object.contains("selectedResult")) {
                            selectedResult = data.getJSONObject("selectedResult");
//                            setUiForAdditionalBenefits(selectedResult);
                            fare = selectedResult.getJSONObject("fare");
                            taxes = fare.getJSONObject("taxes");
                            total = fare.getJSONObject("total");
                            totalPrice = total.getJSONObject("price");
                            bookingAmount = Float.parseFloat(totalPrice.getString("amount"));
                        } else {
                            selectedOutboundResult = data.getJSONObject("selectedOutBoundFlight");
//                            setUiForAdditionalBenefits(selectedOutboundResult);
                            selectedInboundResult = data.getJSONObject("selectedInBoundFlight");
                            fare = selectedOutboundResult.getJSONObject("fare");
                            taxes = fare.getJSONObject("taxes");
                            total = fare.getJSONObject("total");
                            totalPrice = total.getJSONObject("price");
                            inboundFare = selectedInboundResult.getJSONObject("fare");
                            inboundTaxes = inboundFare.getJSONObject("taxes");
                            inboundTotal = inboundFare.getJSONObject("total");
                            inboundTotalPrice = inboundTotal.getJSONObject("price");
                            bookingAmount = Float.parseFloat(totalPrice.getString("amount"))
                                    + Float.parseFloat(inboundTotalPrice.getString("amount"));
                            this.inFareBreakUp = new Gson().fromJson(inboundFare.toString(), Fare.class);
                        }
                        if (isConvinienceFeeAdded) {
                            convineieceFeeeTv.setVisibility(View.GONE);
//                            bookingAmount = bookingAmount - convenienceFee;
                            isConvinienceFeeAdded = false;
                        }
                        tvFare.setText(UIUtils.getFareToDisplay(this, bookingAmount));
                        try {
                            String serviceCharge = taxes.getJSONObject("fare.code.servicecharge").getJSONObject("price").getString("amount");
                            String gstCharge = taxes.getJSONObject("fare.code.servicecharge.indian.gst").getJSONObject("price").getString("amount");
                            if (inboundTaxes != null) {
                                serviceCharge = String.valueOf(Float.parseFloat(serviceCharge) + Float.parseFloat(
                                        taxes.getJSONObject("fare.code.servicecharge").getJSONObject("price").getString("amount")));
                                gstCharge = String.valueOf(Float.parseFloat(gstCharge) + Float.parseFloat(
                                        taxes.getJSONObject("fare.code.servicecharge.indian.gst").getJSONObject("price").getString("amount")));
                            }
                            tvTaxes.setText("Service Charge: " + serviceCharge + "\nGST: " + gstCharge);
                            tvTaxes.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            tvTaxes.setVisibility(View.GONE);
                        }
                        tvFareBreakUp.setVisibility(View.VISIBLE);
                        fareBreakUp = new Gson().fromJson(fare.toString(), Fare.class);
                        Log.d("Trip", "res is " + object);
                        setUiforCashCard();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (id == ID_VIEW_TICKET) {
                try {
                    Log.d("Trip", "view ticket es is " + object);
                    Utils.appendLog("ViewTicketResponse " + object);
                    JSONObject obj = new JSONObject(object);
                    if (obj.has("status") && obj.getString("status").equals("0")) {
                        Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.text_booking_failed), null, getString(R.string.ok), "Share Logs", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_NEUTRAL:
                                        finishAllAndTakeToHome();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        Utils.sendEmail(ReviewAndPaymentActivity.this);
                                }
                            }
                        });
                    } else {
                        ViewTicketResponse viewTicketResponse = new Gson().fromJson(object, ViewTicketResponse.class);
                        Intent intent = new Intent(this, ViewTicketActivity.class);
                        Bundle bundle = getIntent().getExtras();
                        bundle.putSerializable("view_ticket_response", viewTicketResponse);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (id == ID_BOOKING_INITIALIZE) {
                if (object != null) {
                    Utils.appendLog("BookingInitializeResponse " + object);
                    Log.d("Trip", " initialize res is " + object);
                    Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.ticket_booked).concat(bookingValidateResponse.getBookingRefNo()), null, getString(R.string.ok_text), null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    String url;
                                    NetworkTask networkTask = new NetworkTask(ReviewAndPaymentActivity.this, ID_VIEW_TICKET);
                                    networkTask.setDialogMessage(getString(R.string.please_wait));
                                    networkTask.exposePostExecute(ReviewAndPaymentActivity.this);
                                    String paramsArray[];
                                    url = UIUtils.getBaseUrl(ReviewAndPaymentActivity.this) + WebServiceConstants.urlViewTrip;
                                    Utils.appendLog("ViewTripURL: " + url);
                                    String request = Requests.viewTripRequest(bookingValidateResponse.getBookingRefNo());
                                    Utils.appendLog("ViewTripRequest: " + request);
                                    paramsArray = new String[]{url, request};
                                    networkTask.execute(paramsArray);
                                    break;
                            }
                        }
                    });
                }
            } else if (id == CREDIT_BALANCE) {
                try {
                    JSONObject obj = new JSONObject(object);
                    /*JSONObject data = obj.getJSONObject("data");
                    JSONObject selectedResult = data.getJSONObject("selectedResult");
                    JSONObject fare = selectedResult.getJSONObject("fare");
                    JSONObject total = fare.getJSONObject("total");
                    JSONObject totalPrice = total.getJSONObject("price");
                    bookingAmount = Float.parseFloat(totalPrice.getString("amount"));*/
                    if (isConvinienceFeeAdded) {
                        convineieceFeeeTv.setVisibility(View.GONE);
//                        bookingAmount = bookingAmount - convenienceFee;
                        tvFare.setText(UIUtils.getFareToDisplay(this, bookingAmount));
                        isConvinienceFeeAdded = false;
                    }
                    AppPreferences.getInstance(this).setCashLimit(obj.getString("cashBalance"));
                    AppPreferences.getInstance(this).setCreditLimit(obj.getString("creditBalance"));
                    AppPreferences.getInstance(this).setIsExpired(obj.getBoolean("isExprired"));
                    relativeLayoutCreditCard.setSelected(false);
                    relativeLayoutCashCard.setSelected(false);
                    relativeLayoutPremLimit.setSelected(true);
                    String userData = AppPreferences.getInstance(this).getUserData();
                    LoginResponse loginResponse = new Gson().fromJson(userData, LoginResponse.class);
                    if (loginResponse.isB2BUser()) {
                        setAmountData();
                    } else {
                        Toast.makeText(this, getString(R.string.btoc_not_allowed), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (id == ID_BOOKING_VALIDATE_CREDITCARD) { //ID_BOOKING_VALIDATE
                Utils.appendLog("BookingValidateResponse " + object);
                bookingValidateResponse = new Gson().fromJson(object, BookingValidateResponse.class);
                //open webview
                Intent intent = new Intent(this, CustomWebViewActivity.class);
                intent.putExtra("searchId", getIntent().getStringExtra("search_id"));
                startActivityForResult(intent, request_code_webView);
            } else if (id == ID_BOOKING_VALIDATE_CASHCARD) { //ID_BOOKING_VALIDATE
                Utils.appendLog("BookingValidateResponse " + object);
                Log.d("Trip", "for ID_BOOKING_VALIDATE_CASHCARD  response is " + object);
                bookingValidateResponse = new Gson().fromJson(object, BookingValidateResponse.class);
                if (bookingValidateResponse.getStatus().equals(STATUS_SUCCESS)) {
                    String url;
                    List<NameValuePair> list = new ArrayList<>();
                    list.add(new BasicNameValuePair("searchId", getIntent().getStringExtra("search_id")));
                    list.add(new BasicNameValuePair("token", AppPreferences.getInstance(this).getToken()));
                    NetworkTask networkTask = new NetworkTask(this, ID_BOOKING_INITIALIZE, list);
                    networkTask.setDialogMessage(getString(R.string.please_wait));
                    networkTask.exposePostExecute(this);
                    String paramsArray[];
                    url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlBookingInitialize;
                    paramsArray = new String[]{url, Requests.bookingInitializeRequest(getIntent().getStringExtra("search_id"), AppPreferences.getInstance(this).getToken())};
                    networkTask.execute(paramsArray);
                } else
                    Utils.showAlertDialog(this, getString(R.string.app_name), bookingValidateResponse.getMessage(), getString(R.string.ok_text), null, "Share Logs", null);
            } else if (id == ID_CHECK_BALANCE) {
                if (object.indexOf("cashCard") != -1)//success
                {
                    try {
                        JSONObject jsonOb = new JSONObject(object);
                        JSONObject cashCardOb = jsonOb.getJSONObject("cashCard");
                        String balance = cashCardOb.getString("balance");
                        Float balanceinFloat = Float.parseFloat(balance);
                        if (balanceinFloat > bookingAmount) {
                            ll_cash_card_info.setVisibility(View.VISIBLE);
                            rl_cash_card_detail.setVisibility(View.GONE);
                            bookAmountCashCard.setText(UIUtils.getFareToDisplay(this, bookingAmount));
                            cashCardBalance.setText(balance);
                            Float netBalance = balanceinFloat - bookingAmount;
                            cashCardBalanceAfterBooking.setText(Float.toString(netBalance));
                        } else {
                            Toast.makeText(this, "Balance in CashCard is not sufficient to make payment", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Invalid card ", Toast.LENGTH_SHORT).show();
                }
            } else if (id == APPLY_PROMOCODE) {
                Utils.appendLog("ApplyPromocodeResponse " + object);
                if (object.contains("already.applied")) {
                    tvPromoMessage.setText("Code Already Applied !");
                } else if (object.contains("No Promotion Code Found")) {
                    tvPromoMessage.setText("No Promotion Code Found !");
                } else if (object.contains("errorCode")) {
                    tvPromoMessage.setText("Error while applying PROMO code, Please try again !");
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(object);

                        if (jsonObject.has("data")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            /*if(object.contains("selectedOutBoundFlight")) {
                                Bundle bundle = getIntent().getExtras();
                                bundle.putSerializable("flightDataFirstWay", new Gson().fromJson(data.getJSONObject
                                        ("selectedOutBoundFlight").toString(), FlightData.class));
                                bundle.putSerializable("flightDataSecondWay", new Gson().fromJson(data.getJSONObject
                                        ("selectedInBoundFlight").toString(), FlightData.class));
                                getIntent().putExtras(bundle);
                            } else if (object.contains("selectedResult")) {
                                Bundle bundle = getIntent().getExtras();
                                bundle.putSerializable("flightDataFirstWay", new Gson().fromJson(data.getJSONObject
                                        ("selectedResult").toString(), FlightData.class));
                                bundle.putSerializable("flightDataSecondWay", null);
                                getIntent().putExtras(bundle);
                            }*/

                            Bundle bundle = getIntent().getExtras();
                            bundle.putBoolean("promocode_applied", true);
                            getIntent().putExtras(bundle);

                            JSONObject selectedResult = data.getJSONObject("selectedResult");
                            JSONObject fare = selectedResult.getJSONObject("fare");
                            JSONObject taxes = fare.getJSONObject("taxes");
                            JSONObject discount = taxes.getJSONObject("Discount");
                            JSONObject discountPrice = discount.getJSONObject("price");
                            int discountAmount = Integer.parseInt(discountPrice.getString("amount"));
//                            JSONObject total = fare.getJSONObject("total");
                            fareBreakUp = new Gson().fromJson(fare.toString(), Fare.class);
                            inFareBreakUp = null;
                            float bookingFare = Float.parseFloat(fareBreakUp.getTotal().getPrice().getAmount());

                            if (isConvinienceFeeAdded) {
                                convineieceFeeeTv.setVisibility(View.VISIBLE);
                                convineieceFeeeTv.setText(getString(R.string.convenience_fees_is) + " " + UIUtils.getFareToDisplay(this, jsonObject.getString("convenienceFee")));
                                bookingAmount = bookingAmount - convenienceFee;
                                try {
                                    convenienceFee = Float.parseFloat(jsonObject.getString("convenienceFee"));
                                } catch (Exception e) {

                                }
                                bookingAmount = bookingAmount + convenienceFee;
                                bookingFare = bookingFare + convenienceFee;
                                isConvinienceFeeAdded = true;
                            }

                            tvFare.setText(UIUtils.getFareToDisplay(this, bookingFare));

                            String promoMessage = "Congratulations! Rs. " + discountAmount + " has been discounted, now you have to pay only Rs. " +
                                    bookingFare + ". click here to view full break up.";

                            SpannableString content = new SpannableString(promoMessage);
                            int index = promoMessage.indexOf("click");
                            content.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.upper_rl_home_color)), index, index + 10, 0);
                            content.setSpan(new UnderlineSpan(), index, index + 10, 0);
                            /*content.setSpan(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(tvPromoMessage.getText().toString().contains("click here")) {
                                        Intent intent = new Intent(ReviewAndPaymentActivity.this, UpdatedFareBreakUpActivity.class);
                                        Bundle bundle = getIntent().getExtras();
                                        bundle.putSerializable("fare", fareAfterPromo);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            }, 32, 42, 0);*/

                            tvPromoMessage.setText(content);
                            /*String serviceCharge = taxes.getJSONObject("fareBreakUp").getJSONObject("code").getJSONObject("servicecharge")
                                    .getJSONObject("price").getString("amount");*/
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    itvArrow.setVisibility(View.GONE);
                    applyButton.setVisibility(View.GONE);
                    tvRemoveButton.setVisibility(View.VISIBLE);
                }
                tvPromoMessage.setVisibility(View.VISIBLE);
            } else if (id == REMOVE_PROMOCODE) {
                Utils.appendLog("RemovePromocode " + object);
                Log.d("RemovePromocode: ", object);
                if (object.contains("air.discount.code.removed")) {
                    SpannableString content = new SpannableString(getString(R.string.promocode_removed));
                    content.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.upper_rl_home_color)), 33, 43, 0);
                    content.setSpan(new UnderlineSpan(), 33, 43, 0);
                    tvPromoMessage.setText(content);
                    tvRemoveButton.setVisibility(View.GONE);
//                    itvArrow.setVisibility(View.VISIBLE);
                    applyButton.setVisibility(View.VISIBLE);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(object);
                        if (jsonObject.has("data")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            /*if(object.contains("selectedOutBoundFlight")) {
                                Bundle bundle = getIntent().getExtras();
                                bundle.putSerializable("flightDataFirstWay", new Gson().fromJson(data.getJSONObject
                                        ("selectedOutBoundFlight").toString(), FlightData.class));
                                bundle.putSerializable("flightDataSecondWay", new Gson().fromJson(data.getJSONObject
                                        ("selectedInBoundFlight").toString(), FlightData.class));
                                getIntent().putExtras(bundle);
                            } else if (object.contains("selectedResult")) {
                                Bundle bundle = getIntent().getExtras();
                                bundle.putSerializable("flightDataFirstWay", new Gson().fromJson(data.getJSONObject
                                        ("selectedResult").toString(), FlightData.class));
                                bundle.putSerializable("flightDataSecondWay", null);
                                getIntent().putExtras(bundle);
                            }*/
                            JSONObject selectedResult = data.getJSONObject("selectedResult");
                            JSONObject fare = selectedResult.getJSONObject("fare");
                            fareBreakUp = new Gson().fromJson(fare.toString(), Fare.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    float bookingFare = Float.parseFloat(fareBreakUp.getTotal().getPrice().getAmount());

                    if (isConvinienceFeeAdded) {
                        convineieceFeeeTv.setVisibility(View.VISIBLE);
                        try {
                            convineieceFeeeTv.setText(getString(R.string.convenience_fees_is) + " " + UIUtils.getFareToDisplay(this, jsonObject.getString("convenienceFee")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bookingAmount = bookingAmount - convenienceFee;
                        try {
                            convenienceFee = Float.parseFloat(jsonObject.getString("convenienceFee"));
                        } catch (Exception e) {

                        }
                        bookingAmount = bookingAmount + convenienceFee;
                        bookingFare = bookingFare + convenienceFee;
                        isConvinienceFeeAdded = true;
                    }

                    tvFare.setText(UIUtils.getFareToDisplay(this, bookingFare));

                } else if (object.contains("No Promotion Code Found")) {
                    tvPromoMessage.setText("No Promotion Code Found !");
                } else {
                    tvPromoMessage.setText("Error in removing promocode !");
                }
            } else if (id == ID_SSR) {
                Utils.appendLog("SSRResponse " + object);
                Log.d("SSRResponse", object);
                if (!object.contains("errorCode")) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(object);
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject selectedResult = data.getJSONObject("selectedResult");
                        JSONObject fare = selectedResult.getJSONObject("fare");
                        fareBreakUp = new Gson().fromJson(fare.toString(), Fare.class);
                    } catch (Exception e) {

                    }
                    try {
                        float bookingFare = Float.parseFloat(fareBreakUp.getTotal().getPrice().getAmount());
                        if (isConvinienceFeeAdded) {
                            convineieceFeeeTv.setVisibility(View.VISIBLE);
                            bookingAmount = bookingAmount - convenienceFee;
                            try {
                                convineieceFeeeTv.setText(getString(R.string.convenience_fees_is) + " " +
                                        UIUtils.getFareToDisplay(this, jsonObject.getString("convenienceFee")));
                                convenienceFee = Float.parseFloat(jsonObject.getString("convenienceFee"));
                            } catch (Exception e) {

                            }
                            bookingAmount = bookingAmount + convenienceFee;
                            bookingFare = bookingFare + convenienceFee;
                            isConvinienceFeeAdded = true;
                        }
                        tvFare.setText(UIUtils.getFareToDisplay(this, bookingFare));
                        if (relativeLayoutPremLimit.isSelected()) {
                            setRemainedLimits(bookingFare);
                        }
                    } catch (Exception e) {

                    }
                    Toast.makeText(ReviewAndPaymentActivity.this, toastMessage, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ReviewAndPaymentActivity.this, "Error while adding Other Charges", Toast.LENGTH_LONG).show();
                    cbZeroCancellation.setChecked(false);
                    cbInsuration.setChecked(false);
                    cbVisa.setChecked(false);
                }
            }
        }
    }

    private void setUiForPromoAndTaxBreakUp(AvailabilityResponse availabilityResponse) {
        try {
            try {
                FlightData selectedResult, selectedOutboundResult, selectedInboundResult;
                Fare fare, inboundFare;
                HashMap<String, PriceAndConversionModel> taxes, inboundTaxes = null;
                if (null != availabilityResponse.getData().getSelectedOutBoundFlight()) {
                    Bundle bundle = getIntent().getExtras();
                    bundle.putSerializable("flightDataFirstWay", availabilityResponse.getData().getSelectedOutBoundFlight());
                    bundle.putSerializable("flightDataSecondWay", availabilityResponse.getData().getSelectedInBoundFlight());
                    getIntent().putExtras(bundle);
                } else if (null != availabilityResponse.getData().getSelectedResult()) {
                    Bundle bundle = getIntent().getExtras();
                    bundle.putSerializable("flightDataFirstWay", availabilityResponse.getData().getSelectedResult());
                    bundle.putSerializable("flightDataSecondWay", null);
                    getIntent().putExtras(bundle);
                }
                if (null != availabilityResponse.getData().getSelectedResult()) {
                    selectedResult = availabilityResponse.getData().getSelectedResult();
                    setUiForAdditionalBenefits(selectedResult);
                    fare = selectedResult.getFare();
                    taxes = fare.getTaxes();
                    bookingAmount = Float.parseFloat(fare.getTotal().getPrice().getAmount());
                } else {
                    selectedOutboundResult = availabilityResponse.getData().getSelectedOutBoundFlight();
                    setUiForAdditionalBenefits(selectedOutboundResult);
                    selectedInboundResult = availabilityResponse.getData().getSelectedInBoundFlight();
                    fare = selectedOutboundResult.getFare();
                    taxes = fare.getTaxes();
                    inboundFare = selectedInboundResult.getFare();
                    inboundTaxes = inboundFare.getTaxes();
                    bookingAmount = Float.parseFloat(fare.getTotal().getPrice().getAmount())
                            + Float.parseFloat(inboundFare.getTotal().getPrice().getAmount());
                    this.inFareBreakUp = inboundFare;
                }
                /*try {
                    float conFee = convenienceFee;
                    convenienceFee = Float.parseFloat(availabilityResponse.getConvenienceFee());
                    if (!isConvinienceFeeAdded) {
                        convineieceFeeeTv.setVisibility(View.VISIBLE);
                        convineieceFeeeTv.setText(getString(R.string.convenience_fees_is) + " " + UIUtils.getFareToDisplay(this, convenienceFee));
                        bookingAmount = bookingAmount + convenienceFee;
                        tvFare.setText(UIUtils.getFareToDisplay(this, bookingAmount));
                        isConvinienceFeeAdded = true;
                    } else {
                        convineieceFeeeTv.setVisibility(View.VISIBLE);
                        convineieceFeeeTv.setText(getString(R.string.convenience_fees_is) + " " + UIUtils.getFareToDisplay(this, convenienceFee));
                        bookingAmount = bookingAmount + convenienceFee - conFee;
                        tvFare.setText(UIUtils.getFareToDisplay(this, bookingAmount));
                        isConvinienceFeeAdded = true;
                    }
                } catch (Exception e) {

                }*/
                convenienceFee = Float.parseFloat(availabilityResponse.getConvenienceFee());
                Log.d("Trip", "fee is " + convenienceFee);
                try {
                    String serviceCharge = taxes.get("fare.code.servicecharge").getPrice().getAmount();
                    String gstCharge = taxes.get("fare.code.servicecharge.indian.gst").getPrice().getAmount();
                    if (inboundTaxes != null) {
                        serviceCharge = String.valueOf(Float.parseFloat(serviceCharge) + Float.parseFloat(
                                taxes.get("fare.code.servicecharge").getPrice().getAmount()));
                        gstCharge = String.valueOf(Float.parseFloat(gstCharge) + Float.parseFloat(
                                taxes.get("fare.code.servicecharge.indian.gst").getPrice().getAmount()));
                    }
                    tvTaxes.setText("Service Charge: " + serviceCharge + "\nGST: " + gstCharge);
                    tvTaxes.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    tvTaxes.setVisibility(View.GONE);
                }
                tvFareBreakUp.setVisibility(View.VISIBLE);
                this.fareBreakUp = fare;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Type listType = new TypeToken<ArrayList<PaymentGateway>>() {
            }.getType();
            gatewayList = new PaymentGatewayList();
            List<PaymentGateway> gateways = availabilityResponse.getPaymentGateways();
            gatewayList.setGatewayList(new ArrayList<>(gateways));

            if (!AppPreferences.getInstance(this).getB2B()) {
                llPromocode.setVisibility(View.VISIBLE);
                tvPromoMessage.setVisibility(View.GONE);
                tvHaveAPromocode.setVisibility(View.VISIBLE);
                llEnterPromocode.setVisibility(View.GONE);
//                itvArrow.setVisibility(View.VISIBLE);
                applyButton.setVisibility(View.VISIBLE);
                tvRemoveButton.setVisibility(View.GONE);
            }
            scrollScreenToAdditionalBenefits();
//            setUiforPaymentGateways(gatewayList.getGateWayList());
        } catch (Exception e) {

        }
    }

    private void setUiForAdditionalBenefits(FlightData selectedResult) {
        int count = 0;
        llAdditionalBenefits.setVisibility(View.VISIBLE);
        Map<String, OtherCharge> applicableOtherCharges = selectedResult.getApplicableOtherCharges();
        OtherCharge zeroCancellationCharge = null;
        OtherCharge visa = null;
        OtherCharge insurance = null;
        try {
            zeroCancellationCharge = applicableOtherCharges.get("ZEROCANCELLATIONCHARGE");
            if (null == zeroCancellationCharge) {
                count++;
                rlZeroCancellation.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            count++;
            rlZeroCancellation.setVisibility(View.GONE);
        }
        try {
            String zeroCancellationRemarks = zeroCancellationCharge.getRemarks();
            tvZeroCancellationRemarks.setText(zeroCancellationRemarks);
            cbZeroCancellation.setChecked(false);
        } catch (Exception e) {
            tvZeroCancellationRemarks.setVisibility(View.GONE);
        }
        try {
            zeroCharge = zeroCancellationCharge.getAmount();
            zeroInfo = "Buy Zero Cancellation at Rs " + zeroCharge;
        } catch (Exception e) {

        }
        try {
            visa = applicableOtherCharges.get("VISA");
            if (null == visa) {
                count++;
                rlVisa.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            count++;
            rlVisa.setVisibility(View.GONE);
        }
        try {
            String visaRemarks = visa.getRemarks();
            tvVisaRemarks.setText(visaRemarks);
            cbVisa.setChecked(false);
        } catch (Exception e) {
            tvVisaRemarks.setVisibility(View.GONE);
        }
        try {
            visaCharge = visa.getAmount();
            visaInfo = "Buy Visa at Rs " + visaCharge;
        } catch (Exception e) {

        }
        try {
            insurance = applicableOtherCharges.get("INSURANCE");
            if (null == insurance) {
                count++;
                rlInsurance.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            count++;
            rlInsurance.setVisibility(View.GONE);
        }
        try {
            String insuranceRemarks = insurance.getRemarks();
            tvInsuranceRemarks.setText(insuranceRemarks);
            cbInsuration.setChecked(false);
        } catch (Exception e) {
            tvInsuranceRemarks.setVisibility(View.GONE);
        }
        try {
            insuranceCharge = insurance.getAmount();
            insuranceInfo = "Insure your Trip at Rs " + insuranceCharge;
        } catch (Exception e) {

        }
        if (count == 3) {
            llAdditionalBenefits.setVisibility(View.GONE);
        }
    }

    private void hitForOtherCharges(String type, boolean apply) {
        String url;
        NetworkTask networkTask = new NetworkTask(ReviewAndPaymentActivity.this, ID_SSR);
        networkTask.setDialogMessage(getString(R.string.please_wait));
        networkTask.exposePostExecute(ReviewAndPaymentActivity.this);
        String paramsArray[];
        String request = Requests.setSSRRequest(getIntent().getStringExtra("search_id"), flightDataFirstWay.getUniqueEntityId(), type, apply);
        url = UIUtils.getBaseUrl(ReviewAndPaymentActivity.this) + WebServiceConstants.urlSsr;
        paramsArray = new String[]{url, request};
        networkTask.execute(paramsArray);
    }

    private void setUiforPaymentGateways(final ArrayList<PaymentGateway> gateWayList) {
        int size = gateWayList.size();
        gatewayGridLayout.removeAllViews();
        LinearLayout rowLayout = null;
        for (int i = 0; i < size; i++) {
            if (i % 2 == 0) {
                rowLayout = new LinearLayout(this);
                LinearLayout.LayoutParams paramRow = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                paramRow.setMargins(0, 10, 0, 10);
                rowLayout.setLayoutParams(paramRow);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                gatewayGridLayout.addView(rowLayout);
            }
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.row_grid_gateways, null);
            layout.setId(i);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRadioButtonUnchecked(gatewayGridLayout, gateWayList);
                    com.rezofy.views.custom_views.IconTextView radioButton = (com.rezofy.views.custom_views.IconTextView) v.findViewById(R.id.radioBtn_gateway);
                    radioButton.setText(getString(R.string.icon_text_Q));
                    checkedPosition = v.getId();
                    cardName = gateWayList.get(checkedPosition).getName();
                }
            });
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            param.setMargins(10, 0, 10, 0);
            layout.setLayoutParams(param);
            com.rezofy.views.custom_views.IconTextView radioButton = (com.rezofy.views.custom_views.IconTextView) layout.findViewById(R.id.radioBtn_gateway);
            GifImageView ivCarrierImage = (GifImageView) layout.findViewById(R.id.gifDecoderView);
            try {
                ivCarrierImage.setImageDrawable(new GifDrawable(getAssets(), new StringBuilder().append(getString(R.string.image_path_pre_string)).append(gateWayList.get(i).getName()).append(getString(R.string.image_path_post_string)).toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            rowLayout.addView(layout);
        }
        if (size > 0) {
            rl_cardgrid.setVisibility(View.VISIBLE);
            scrollScreenToBottom();
//            llAdditionalBenefits.getParent().requestChildFocus(llAdditionalBenefits,llAdditionalBenefits);
        }


    }

    private void scrollScreenToBottom() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, scrollView.getBottom());
            }
        });
    }

    private void scrollScreenToAdditionalBenefits() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, cbGetGstInfo.getTop());
            }
        });
    }

    private void setRadioButtonUnchecked(LinearLayout mainLayout, ArrayList<PaymentGateway> gateWayList) {
        int size = gateWayList.size();
        for (int i = 0; i < size; i++) {
            RelativeLayout rLayout = (RelativeLayout) mainLayout.findViewById(i);
            com.rezofy.views.custom_views.IconTextView radioButton = (com.rezofy.views.custom_views.IconTextView) rLayout.findViewById(R.id.radioBtn_gateway);
            radioButton.setText(getString(R.string.icon_text_R));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_code_webView && resultCode == RESULT_OK) {
            String status = data.getStringExtra("STATUS");
            if (status.equals("true")) {
                //hit ticket request
                String tripJson = Utils.getTripJsonFromCreditCardSuccesJson();
                viewTicket(tripJson);
                // getTicket();
            } else {
                //show error dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage(Utils.CreditCardFailureMsg);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        }
    }

    @Override
    public String doInBackground(Integer id, String... params) {
        String responseValidate = null;
        /*String urlAvailability = params[0];
        String requestJSONStringAvailabilty = params[1];
        String responseAvailabilty = Utils.httpPostRaw(urlAvailability, requestJSONStringAvailabilty, this);
        if (responseAvailabilty != null) {*/
        String urlBookingValidate = UIUtils.getBaseUrl(this) + WebServiceConstants.urlBookingValidate;
        for (Traveller traveller : listPassengersInfo)
            traveller.setMealPref(Utils.OPTION_NP);
        setBillingInfo(Utils.PAYMENT_TYPE_CREDIT_LIMIT);
        String requestJSONStringBookingValidate = Requests.bookingValidateRequest(getIntent().getStringExtra("search_id"), null, billingInfo, listPassengersInfo, iconTextViewCheckSeconds.isChecked());
        responseValidate = Utils.httpPostRaw(urlBookingValidate, requestJSONStringBookingValidate, this);
//        }
        return responseValidate;
    }

    private void viewTicket(String object) {
        ViewTicketResponse viewTicketResponse = new Gson().fromJson(object, ViewTicketResponse.class);
        Intent intent = new Intent(this, ViewTicketActivity.class);
        Bundle bundle = getIntent().getExtras();
        bundle.putSerializable("view_ticket_response", viewTicketResponse);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void finishAllAndTakeToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public class FieldViewHolder extends RecyclerView.ViewHolder {
        private TextView tvField;

        public FieldViewHolder(View itemView) {
            super(itemView);
            tvField = (TextView) itemView.findViewById(R.id.field);
        }
    }

    private void flightRefineDialog(JSONObject jsonObject) {
        String message = getString(R.string.some_technical_error);
        try {
            if (jsonObject.has("errorMessages") && jsonObject.getString("errorMessages").length() > 1) {
                message = jsonObject.getString("errorMessages");
                message = message.substring(2, message.length() - 2);
            }
        } catch (JSONException e) {

        }

        Utils.showAlertDialog(this, getString(R.string.app_name), message, "Try Again", null, "Share Logs", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEUTRAL:
                        Intent i = new Intent(ReviewAndPaymentActivity.this, HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Utils.sendEmail(ReviewAndPaymentActivity.this);
                        break;
                }
            }
        });
    }

}