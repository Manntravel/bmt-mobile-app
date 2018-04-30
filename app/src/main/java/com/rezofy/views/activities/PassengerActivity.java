package com.rezofy.views.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rezofy.R;
import com.rezofy.adapters.CountrySpinnerAdapter;
import com.rezofy.adapters.PassengerIconsAdapter;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.controllers.DatabaseController;
import com.rezofy.database.DbHelper;
import com.rezofy.models.request_models.Traveller;
import com.rezofy.models.response_models.FlightData;
import com.rezofy.models.response_models.Leg;
import com.rezofy.models.util_models.TravellerDBModel;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.requests.Requests;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.custom_views.IconTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Linchpin66 on 19-01-2016.
 */
public class PassengerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int APPLY_PROMOCODE = 1;
    private final String SPINNER_OPTION_SELECT = "Select";
    private final String SPINNER_OPTION_MALE = "Male";
    private final String SPINNER_OPTION_FEMALE = "Female";
    private final String SPINNER_OPTION_VEG = "Veg";
    private final String SPINNER_OPTION_NON_VEG = "Non-Veg";
    private RelativeLayout rlFlightDetailsSecondWay;
    private IconTextView iTVMenu;
    private Spinner genderSpinner, mealSpinner, spinnerResidence, spinnerIssuedBy;
    private IconTextView spinnerClick, mealSpinnerClick, residenceSpinnerClick, ppIssuedByClick;
    private RecyclerView rvPassengers;
    private TextView passengerDate, tvPPExpDate, tvPlaceFirstWay, tvDurationFirstWay, tvDepartureDateFirstWay, tvFlightTypeFirstWay,
            tvPlaceSecondWay, tvDurationSecondWay, tvDepartureDateSecondWay, tvFlightTypeSecondWay,
            tvPassengerNo, tvPassengerType, tvPreviousPassenger, tvNextPassenger, tvFare, tvGo, tvTitle;
    private EditText etFirstName, etLastName, etPassportNo;
    private String originCountryCode, destCountryCode;
    private final String TEXT_DOB = "Date of Birth";
    private final String TEXT_PED = "Passport Expiration Date";
    private LinearLayout llResidenceSpinner, llPPIssuedBySpinner;
    private int noOfAdults;
    private int noOfChildren;
    private int noOfInfants;
    private String originCityName, destCityName, departDateValue, retDateValue;
    private FlightData flightDataFirstWay, flightDataSecondWay;
    private PassengerIconsAdapter adapter;
    private ArrayList<Traveller> listPassengersInfo;
    private com.rezofy.views.custom_views.IconTextView searchBtn;
    List<String> countryList;
    private TextView tvClickToSeeDetails, tvMeal;
    private ImageView btnFloating;
    private boolean doLegContainAirAsia;
    private boolean isTketApp;
    private LinearLayout llMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        init();
        setProperties();
        setListeners();
        setData();
    }

    private void setProperties() {
        int themeColor = UIUtils.getThemeColor(this);
        findViewById(R.id.header).setBackgroundColor(themeColor);
        tvClickToSeeDetails.setTextColor(UIUtils.getThemeColor(this));
        iTVMenu.setTextColor(UIUtils.getThemeContrastColor(this));
        tvTitle.setTextColor(UIUtils.getThemeContrastColor(this));
        spinnerClick.setTextColor(themeColor);
        residenceSpinnerClick.setTextColor(themeColor);
        ppIssuedByClick.setTextColor(themeColor);
        mealSpinnerClick.setTextColor(themeColor);
        UIUtils.setNormalButtonProperties(tvGo);
        UIUtils.setFareProperties(tvFare);
        if(isTketApp) {
            llMeal.setVisibility(View.GONE);
            tvMeal.setVisibility(View.GONE);
        }
    }

    private void setData() {
        tvPlaceFirstWay.setText(originCityName + " - " + destCityName);
        tvDepartureDateFirstWay.setText(Utils.changeDateFormat(departDateValue, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_EEE_space_comma_d_space_LLL));
        tvDurationFirstWay.setText(Utils.getDurationInHrsAndMins(flightDataFirstWay.getSegments().get(0).getDuration()));
        tvFlightTypeFirstWay.setText(UIUtils.getFlightType(flightDataFirstWay.getSegments().get(0).getNoOfStops()));
        if (noOfAdults + noOfChildren + noOfInfants == 1)
            findViewById(R.id.passenger_navigator).setVisibility(View.GONE);
        if (getIntent().getSerializableExtra("flightDataSecondWay") != null) {
            flightDataSecondWay = (FlightData) getIntent().getSerializableExtra("flightDataSecondWay");
            retDateValue = getIntent().getStringExtra("retDate");
            rlFlightDetailsSecondWay.setVisibility(View.VISIBLE);
            tvPlaceSecondWay.setText(destCityName + " - " + originCityName);
            tvDurationSecondWay.setText(Utils.getDurationInHrsAndMins(flightDataSecondWay.getSegments().get(0).getDuration()));
            tvDepartureDateSecondWay.setText(Utils.changeDateFormat(retDateValue, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_EEE_space_comma_d_space_LLL));
            tvFlightTypeSecondWay.setText(UIUtils.getFlightType(flightDataSecondWay.getSegments().get(0).getNoOfStops()));
            if ((getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS))
                    || (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_REGULAR) && getIntent().getStringExtra("selected_tab") == null)) {
                tvFare.setText(UIUtils.getFareToDisplay(this, flightDataFirstWay.getFare().getTotal().getPrice().getAmount()));
            } else
                tvFare.setText(UIUtils.getFareToDisplay(this, (float) (Math.ceil(Double.parseDouble(flightDataFirstWay.getFare().getTotal().getPrice().getAmount())) + Math.ceil(Double.parseDouble(flightDataSecondWay.getFare().getTotal().getPrice().getAmount())))));
        } else {
            tvFare.setText(UIUtils.getFareToDisplay(this, flightDataFirstWay.getFare().getTotal().getPrice().getAmount()));
        }
    }

    private void setListeners() {
        TextView.OnEditorActionListener actionNextListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (v.getId() == R.id.last_name) {
                        if (etPassportNo.getVisibility() == View.VISIBLE)
                            etPassportNo.requestFocus();
                        else
                            Utils.hideSoftKey(PassengerActivity.this);
                        return true;
                    } else if (v.getId() == R.id.passport_number) {
                        Utils.hideSoftKey(PassengerActivity.this);
                        return true;
                    }
                }
                return false;
            }
        };
        etLastName.setOnEditorActionListener(actionNextListener);
        etPassportNo.setOnEditorActionListener(actionNextListener);
//        etEnterPromocode.setOnEditorActionListener(actionNextListener);
        iTVMenu.setOnClickListener(this);
        passengerDate.setOnClickListener(this);
        tvGo.setOnClickListener(this);
        tvPreviousPassenger.setOnClickListener(this);
        tvNextPassenger.setOnClickListener(this);
        btnFloating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatingButtonMove floatingButtonMove = new FloatingButtonMove(getApplicationContext());
                floatingButtonMove.dragView(v, event);
                return true;
            }
        });
        if(Boolean.parseBoolean(getString(R.string.hideChatIcon))) {
            btnFloating.setVisibility(View.GONE);
        }
    }

    private void init() {
        noOfAdults = getIntent().getIntExtra("noOfAdults", 0);
        noOfChildren = getIntent().getIntExtra("noOfChildren", 0);
        noOfInfants = getIntent().getIntExtra("noOfInfants", 0);
        originCityName = getIntent().getStringExtra("originCityName");
        destCityName = getIntent().getStringExtra("destCityName");
        departDateValue = getIntent().getStringExtra("departDate");
        originCountryCode = getIntent().getStringExtra("originCountryCode");
        destCountryCode = getIntent().getStringExtra("destCountryCode");
        flightDataFirstWay = (FlightData) getIntent().getSerializableExtra("flightDataFirstWay");
        isTketApp = Boolean.parseBoolean(getString(R.string.isTketApp));

        doLegContainAirAsia = false;
        for (int i = 0; i < flightDataFirstWay.getSegments().size(); i++) {
            for (int j = 0; j < flightDataFirstWay.getSegments().get(i).getLegCount(); j++) {
                Leg leg = flightDataFirstWay.getSegments().get(i).getLegs().get(j);
                if (leg.getAirline().equalsIgnoreCase("I5")) {
                    doLegContainAirAsia = true;
                }
            }
        }
        if (getIntent().getSerializableExtra("flightDataSecondWay") != null) {
            flightDataSecondWay = (FlightData) getIntent().getSerializableExtra("flightDataSecondWay");
            for (int i = 0; i < flightDataSecondWay.getSegments().size(); i++) {
                for (int j = 0; j < flightDataSecondWay.getSegments().get(i).getLegCount(); j++) {
                    Leg leg = flightDataSecondWay.getSegments().get(i).getLegs().get(j);
                    if (leg.getAirline().equalsIgnoreCase("I5")) {
                        doLegContainAirAsia = true;
                    }
                }
            }
        }

        createListPassengersInfo();
        iTVMenu = (IconTextView) findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setText("k");
        iTVMenu.setTextSize(20);

        tvTitle = (TextView) findViewById(R.id.header).findViewById(R.id.title);
        tvTitle.setText(getString(R.string.passenger_text));
        genderSpinner = (Spinner) findViewById(R.id.gender_spinner);

        tvClickToSeeDetails = (TextView) findViewById(R.id.tv_click_to_see_details);
        SpannableString content = new SpannableString(getString(R.string.text_details));
        content.setSpan(new UnderlineSpan(), 0, getString(R.string.text_details).length(), 0);
        tvClickToSeeDetails.setText(content);
        tvClickToSeeDetails.setOnClickListener(this);
        spinnerClick = (IconTextView) findViewById(R.id.spinnerClick);
        spinnerClick.setOnClickListener(this);

        llResidenceSpinner = (LinearLayout) findViewById(R.id.residence_spinner_layout);
        llPPIssuedBySpinner = (LinearLayout) findViewById(R.id.pp_issued_by_spinner_layout);

        spinnerResidence = (Spinner) findViewById(R.id.residence_spinner);
        residenceSpinnerClick = (IconTextView) findViewById(R.id.residenceSpinnerClick);
        residenceSpinnerClick.setOnClickListener(this);

        spinnerIssuedBy = (Spinner) findViewById(R.id.pp_issued_by_spinner);
        ppIssuedByClick = (IconTextView) findViewById(R.id.ppIssuedBySpinnerClick);
        ppIssuedByClick.setOnClickListener(this);

        llMeal = (LinearLayout) findViewById(R.id.ll_meal);
        mealSpinnerClick = (IconTextView) findViewById(R.id.mealSpinnerClick);
        mealSpinnerClick.setOnClickListener(this);
        tvMeal = (TextView) findViewById(R.id.meal);

        etPassportNo = (EditText) findViewById(R.id.passport_number);
        mealSpinner = (Spinner) findViewById(R.id.meal_spinner);
        passengerDate = (TextView) findViewById(R.id.passenger_date);
        tvPPExpDate = (TextView) findViewById(R.id.pp_exp_date);
        tvPPExpDate.setOnClickListener(this);
        searchBtn = (com.rezofy.views.custom_views.IconTextView) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);
        rlFlightDetailsSecondWay = (RelativeLayout) findViewById(R.id.flight_details_second_way);
        tvPlaceFirstWay = (TextView) findViewById(R.id.place_first_way);
        tvDurationFirstWay = (TextView) findViewById(R.id.duration_first_way);
        tvDepartureDateFirstWay = (TextView) findViewById(R.id.departure_date_first_way);
        tvFlightTypeFirstWay = (TextView) findViewById(R.id.flight_type_first_way);
        tvPlaceSecondWay = (TextView) findViewById(R.id.place_second_way);
        tvDurationSecondWay = (TextView) findViewById(R.id.duration_second_way);
        tvDepartureDateSecondWay = (TextView) findViewById(R.id.departure_date_second_way);
        tvFlightTypeSecondWay = (TextView) findViewById(R.id.flight_type_second_way);
        tvPassengerNo = (TextView) findViewById(R.id.passenger_no);
        etFirstName = (EditText) findViewById(R.id.first_name);
        etLastName = (EditText) findViewById(R.id.last_name);
        tvPassengerType = (TextView) findViewById(R.id.passenger_type);
        adapter = new PassengerIconsAdapter(this, noOfAdults, noOfChildren, noOfInfants, listPassengersInfo, doLegContainAirAsia);
        rvPassengers = (RecyclerView) findViewById(R.id.passenger_recyclerview);
        rvPassengers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPassengers.setAdapter(adapter);
        adapter.handleViews();
        tvPreviousPassenger = (TextView) findViewById(R.id.previous_passenger);
        tvNextPassenger = (TextView) findViewById(R.id.next_passenger);
        tvFare = (TextView) findViewById(R.id.fare);
        tvGo = (TextView) findViewById(R.id.go);
        tvGo.setOnClickListener(this);
        spinnersTask();
        genderSpinner.setSelection(0);
        mealSpinner.setSelection(0);
        btnFloating = (ImageView) findViewById(R.id.btn_flaoting);
     //   itvArrow = (IconTextView) findViewById(R.id.arrow);
//        itvArrow.setOnClickListener(this);
    }

    private void createListPassengersInfo() {
        int totalPassengers = noOfAdults + noOfChildren + noOfInfants;
        listPassengersInfo = new ArrayList<>();
        for (int i = 0; i < totalPassengers; i++) {
            Traveller traveller = new Traveller();
            if (i >= 0 && i < noOfAdults) {
                traveller.setType(Utils.PASSENGER_TYPE_ADULT);
                traveller.setTravellerType(Utils.PASSENGER_TYPE_ADULT);

            } else if (i >= noOfAdults && i < totalPassengers - noOfInfants) {
                traveller.setType(Utils.PASSENGER_TYPE_CHILD);
                traveller.setTravellerType(Utils.PASSENGER_TYPE_CHILD);

            } else {
                traveller.setType(Utils.PASSENGER_TYPE_INFANT);
                traveller.setTravellerType(Utils.PASSENGER_TYPE_INFANT);
            }
            listPassengersInfo.add(traveller);
        }
    }

    private void spinnersTask() {
        List<String> genderCategories = new ArrayList<>();
        genderCategories.add(SPINNER_OPTION_SELECT);
        genderCategories.add(SPINNER_OPTION_MALE);
        genderCategories.add(SPINNER_OPTION_FEMALE);
        SpinnerCustomAdapter genderAdapter = new SpinnerCustomAdapter(genderCategories);
        genderSpinner.setAdapter(genderAdapter);

        countryList = DatabaseController.getInstance(this).getCountryList();
        CountrySpinnerAdapter resCountryAdapter = new CountrySpinnerAdapter(this, countryList);
        spinnerResidence.setAdapter(resCountryAdapter);
        spinnerResidence.setSelection(countryList.indexOf("Australia"));

        CountrySpinnerAdapter ppIssuedByAdapter = new CountrySpinnerAdapter(this, countryList);
        spinnerIssuedBy.setAdapter(ppIssuedByAdapter);
        spinnerIssuedBy.setSelection(countryList.indexOf("India"));

        List<String> mealCategories = new ArrayList<>();
        mealCategories.add(SPINNER_OPTION_SELECT);
        mealCategories.add(SPINNER_OPTION_VEG);
        mealCategories.add(SPINNER_OPTION_NON_VEG);
        SpinnerCustomAdapter mealsAdapter = new SpinnerCustomAdapter(mealCategories);
        mealSpinner.setAdapter(mealsAdapter);
    }

    private class SpinnerCustomAdapter extends BaseAdapter {
        List<String> list;

        public SpinnerCustomAdapter(List<String> list) {
            this.list = list;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(PassengerActivity.this).inflate(R.layout.spinner_row, viewGroup, false);
            TextView genderName = (TextView) view.findViewById(R.id.name_gender);
            genderName.setText(list.get(i));
            return view;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.passenger_date:
                createDialog(100);
                break;

            case R.id.pp_exp_date:
                createDialog(200);
                break;

            case R.id.left_icon:
                finish();
                break;

            case R.id.back:
                finish();
                break;

            case R.id.tv_click_to_see_details:
                Intent i = new Intent(this, FlightDetailsActivity.class);
                i.putExtras(getIntent().getExtras());
                startActivity(i);
                break;

            /*case R.id.have_a_promocode:
                tvHaveAPromocode.setVisibility(View.GONE);
                llEnterPromocode.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                break;*/

            case R.id.previous_passenger:
                if (adapter.getSelectedPassengerPosition() > 0)
                    adapter.handlePositionChange(adapter.getSelectedPassengerPosition() - 1);
                else
                    Toast.makeText(this, getString(R.string.reached_first_passenger), Toast.LENGTH_SHORT).show();
                break;

            case R.id.next_passenger:
                if (adapter.getSelectedPassengerPosition() < noOfAdults + noOfChildren + noOfInfants - 1)
                    adapter.handlePositionChange(adapter.getSelectedPassengerPosition() + 1);
                else
                    Toast.makeText(this, getString(R.string.reached_last_passenger), Toast.LENGTH_SHORT).show();
                break;

            case R.id.go:
                adapter.saveSelectedPassengerInfo();
                if (validatePassengerInfo()) {
                    insertDatatoDb();
                    Intent intent = new Intent(this, ReviewAndPaymentActivity.class);
                    Bundle bundle = getIntent().getExtras();
                    bundle.putSerializable("passenger_info", listPassengersInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;

            case R.id.spinnerClick:
                spinnerDropDown();
                break;

            case R.id.mealSpinnerClick:
                mealSpinnerDropDown();
                break;

            case R.id.residenceSpinnerClick:
                residenceSpinnerDropDown();
                break;

            case R.id.ppIssuedBySpinnerClick:
                issuedBySpinnerDropDown();
                break;
            case R.id.searchBtn:
                openSearchScreen();
                break;

            /*case R.id.arrow:
                applyPromocode();
                break;*/

            /*case R.id.enter_promocode:
                break;*/
        }
    }

    /*private void applyPromocode() {
        String url;
        NetworkTask networkTask = new NetworkTask(this, APPLY_PROMOCODE);
        networkTask.setProgressDialog(true);
        networkTask.setDialogMessage(getString(R.string.please_wait));
        networkTask.exposePostExecute(this);
        String paramsArray[] = null;

        url = UIUtils.getBaseUrl(this) + WebServiceConstants.applyPromocode;
        String request = null;
        request = Requests.applyPromocode(getIntent().getStringExtra("search_id"), flightDataFirstWay.getUniqueEntityId(), etEnterPromocode.getText());
        *//*request = "{\n" +
                "   \"searchId\": \"LRSAADALKY\",\n" +
                "   \"selectedFlightId\": \"LRSAADALKY_11\",\n" +
                "   \"discount\": {\"discountCode\": \"TESTCODE01\"}\n" +
                "}";*//*
        paramsArray = new String[]{url, request};
        networkTask.execute(paramsArray);
    }*/

    private void openSearchScreen() {
        try {
            Intent i = new Intent(this, PassengerSearchActivity.class);
            startActivityForResult(i, Utils.request_code_passengerSearch);

        } catch (Exception e) {
            Log.d("Trip", "Eror in openSearchScreen " + e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Trip", "inside onActivityResut " + requestCode + " :: " + resultCode + "::::: " + RESULT_OK);
        if (requestCode == Utils.request_code_passengerSearch && resultCode == RESULT_OK) {
            TravellerDBModel travellerDBModel = (TravellerDBModel) data.getSerializableExtra("traveller_db_model");
            //populate fieds
            etFirstName.setText(travellerDBModel.getFirstName());
            etLastName.setText(travellerDBModel.getLastName());
            String gender = travellerDBModel.getGender();
            if (gender.equalsIgnoreCase("mr")) {
                genderSpinner.setSelection(1);
            } else if (gender.equalsIgnoreCase("mrs")) {
                genderSpinner.setSelection(2);
            }
            String meal = travellerDBModel.getMealPref();
            if (meal.equalsIgnoreCase("veg")) {
                mealSpinner.setSelection(1);
            } else if (meal.equalsIgnoreCase("non-veg")) {
                mealSpinner.setSelection(2);
            }
            if (passengerDate.getVisibility() == View.VISIBLE) {
                String dob = travellerDBModel.getDob();
                if (!dob.equals("") && dob.length() > 5)
                    passengerDate.setText(dob);
            }
            if (spinnerResidence.getVisibility() == View.VISIBLE) {
                spinnerResidence.setSelection(countryList.indexOf(travellerDBModel.getCountry()));
            }
            if (spinnerIssuedBy.getVisibility() == View.VISIBLE) {
                spinnerIssuedBy.setSelection(countryList.indexOf(travellerDBModel.getIssuedBy()));
            }
            if (etPassportNo.getVisibility() == View.VISIBLE) {
                etPassportNo.setText(travellerDBModel.getPassportNo());
            }
            if (tvPPExpDate.getVisibility() == View.VISIBLE) {
                String expdate = travellerDBModel.getExpDate();
                if (!expdate.equals("") && expdate.length() > 5)
                    tvPPExpDate.setText(expdate);
            }
        }
    }

    private void insertDatatoDb() {
        try {
            //note title contain mr or ms not male and female please keep it in mind while playing with gender dropdown
            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int size = listPassengersInfo.size();
            String dob = "";
            for (int i = 0; i < size; i++) {

                if (listPassengersInfo.get(i).getDateOfBirth() != null)//to check dob is null as in case of domestic usres
                {
                    dob = listPassengersInfo.get(i).getDateOfBirth().getDate();
                }


                if (listPassengersInfo.get(i).getPassport() == null) {
                    dbHelper.insertDatatoPassengerTable(listPassengersInfo.get(i).getFirstName(), listPassengersInfo.get(i).getLastName(), listPassengersInfo.get(i).getTitle(),
                            listPassengersInfo.get(i).getMealPref(), dob, "",
                            "", "", "",
                            "", Long.toString(System.currentTimeMillis()), db);
                } else {

                    dbHelper.insertDatatoPassengerTable(listPassengersInfo.get(i).getFirstName(), listPassengersInfo.get(i).getLastName(), listPassengersInfo.get(i).getTitle(),
                            listPassengersInfo.get(i).getMealPref(), dob, listPassengersInfo.get(i).getPassport().getNationality(),
                            listPassengersInfo.get(i).getPassport().getPassPortNo(), listPassengersInfo.get(i).getPassport().getIssuingCountry(), listPassengersInfo.get(i).getPassport().getPassExpDate().getDate(),
                            listPassengersInfo.get(i).getTravellerType(), Long.toString(System.currentTimeMillis()), db);
                }


            }
//            insertDatatoPassengerTable(String pas_first_name,String pas_last_name,String pas_gender,String pas_meal,String pas_dob,
//                    String pas_resident_country,String pas_passport_no,String pas_passport_issued_by,String pas_passport_expiration_date,
//                    String pas_passenger_type,String pas_timeStamp,SQLiteDatabase db )


            db.close();
            dbHelper.close();

        } catch (Exception e) {
            Log.d("Trip", "Eror in insertData " + e);
        }
    }

    private void issuedBySpinnerDropDown() {
        spinnerIssuedBy.performClick();
    }

    private void residenceSpinnerDropDown() {
        spinnerResidence.performClick();
    }

    private void mealSpinnerDropDown() {
        mealSpinner.performClick();
    }

    private void spinnerDropDown() {
        genderSpinner.performClick();
    }

    private boolean validatePassengerInfo() {
        String errorMessage = null;
        for (int i = 0; i < listPassengersInfo.size(); i++) {
            Traveller traveller = listPassengersInfo.get(i);

            if (traveller.getFirstName() == null) {
                errorMessage = getString(R.string.error_enter_first_name) + (i + 1);
                break;
            } else if (traveller.getFirstName().length()<2) {
                errorMessage = getString(R.string.error_length_first_name);
                break;
            } else if (traveller.getLastName() == null) {
                errorMessage = getString(R.string.error_enter_last_name) + (i + 1);
                break;
            } else if (traveller.getLastName().length()<2) {
                errorMessage = getString(R.string.error_length_last_name);
                break;
            } else if (traveller.getTitle() == null) {
                errorMessage = getString(R.string.error_select_gender) + (i + 1);
                break;

            }  else if ((originCountryCode.equals(getString(R.string.originCountryCode)) && destCountryCode.equals(getString(R.string.originCountryCode)) && !traveller.getType().equals(Utils.PASSENGER_TYPE_ADULT) && traveller.getDateOfBirth() == null)
                    || ((!originCountryCode.equals(getString(R.string.originCountryCode)) || !destCountryCode.equals(getString(R.string.originCountryCode))) && traveller.getDateOfBirth() == null) || (doLegContainAirAsia && traveller.getDateOfBirth() == null)) {
                errorMessage = getString(R.string.error_select_dob) + (i + 1);
                break;

            } else if ((!originCountryCode.equals(getString(R.string.originCountryCode)) || !destCountryCode.equals(getString(R.string.originCountryCode))) && traveller.getPassport() == null) {
                errorMessage = getString(R.string.error_passport) + (i + 1);
                break;

            } else if ((!originCountryCode.equals(getString(R.string.originCountryCode)) || !destCountryCode.equals(getString(R.string.originCountryCode))) && traveller.getPassport().getPassPortNo() == null) {
                errorMessage = getString(R.string.error_enter_passport_no) + (i + 1);
                break;

            } else if ((!originCountryCode.equals(getString(R.string.originCountryCode)) || !destCountryCode.equals(getString(R.string.originCountryCode))) && !Utils.isPassportNoValid(traveller.getPassport().getPassPortNo())) {
                errorMessage = getString(R.string.enter_valid_pp_no) + (i + 1);
                break;

            } else if ((!originCountryCode.equals(getString(R.string.originCountryCode)) || !destCountryCode.equals(getString(R.string.originCountryCode))) && traveller.getPassport().getPassExpDate() == null) {
                errorMessage = getString(R.string.error_select_passport_exp_date) + (i + 1);
                break;
            }
        }

        if (errorMessage != null) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            return false;

        } else
            return true;
    }

    private void createDialog(int id) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dpDialog;
        Calendar minCalendar = Calendar.getInstance();//get the current day
        Calendar maxCalendar = Calendar.getInstance();//get the current day

        if (id == 100) {
            if (!passengerDate.getText().toString().equals(TEXT_DOB))
                c.setTime(Utils.getDateFromFormat(passengerDate.getText().toString(), Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy));
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            dpDialog = new DatePickerDialog(this, dobListener, year, month, day);
            DatePicker datePicker = dpDialog.getDatePicker();

            if (listPassengersInfo.get(adapter.getSelectedPassengerPosition()).getType().equals(Utils.PASSENGER_TYPE_CHILD)) {
                minCalendar.add(Calendar.YEAR, -12);
                datePicker.setMinDate(minCalendar.getTimeInMillis() - 100);//s
                maxCalendar.add(Calendar.YEAR, -2);
                datePicker.setMaxDate(maxCalendar.getTimeInMillis() - 100);//s

            } else if (listPassengersInfo.get(adapter.getSelectedPassengerPosition()).getType().equals(Utils.PASSENGER_TYPE_INFANT)) {
                datePicker.setMaxDate(maxCalendar.getTimeInMillis() + 1000);//s
                minCalendar.add(Calendar.YEAR, -2);
                datePicker.setMinDate(minCalendar.getTimeInMillis() - 100);//s

            } else {
                maxCalendar.add(Calendar.YEAR, -12);
                datePicker.setMaxDate(maxCalendar.getTimeInMillis() - 100);//s
            }

        } else {
            if (!tvPPExpDate.getText().toString().equals(TEXT_PED))
                c.setTime(Utils.getDateFromFormat(tvPPExpDate.getText().toString(), Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy));
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            dpDialog = new DatePickerDialog(this, ppExpDateListener, year, month, day);
            DatePicker datePicker = dpDialog.getDatePicker();
            if (retDateValue != null) {
                minCalendar.setTime(Utils.getDateFromFormat(retDateValue, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy));
            } else {
                minCalendar.setTime(Utils.getDateFromFormat(departDateValue, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy));
            }
            datePicker.setMinDate(minCalendar.getTimeInMillis());
        }

        dpDialog.show();
    }

    private DatePickerDialog.OnDateSetListener dobListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int y, int m, int d) {
            passengerDate.setText(d + "-" + (m + 1) + "-" + y);
        }
    };

    private DatePickerDialog.OnDateSetListener ppExpDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int y, int m, int d) {
            tvPPExpDate.setText(d + "-" + (m + 1) + "-" + y);
        }
    };

    public String getOriginCountryCode() {
        return originCountryCode;
    }

    public String getDestCountryCode() {
        return destCountryCode;
    }

    public TextView getTvPassengerNo() {
        return tvPassengerNo;
    }

    public void setTvPassengerNo(TextView tvPassengerNo) {
        this.tvPassengerNo = tvPassengerNo;
    }

    public Spinner getGenderSpinner() {
        return genderSpinner;
    }

    public void setGenderSpinner(Spinner genderSpinner) {
        this.genderSpinner = genderSpinner;
    }

    public Spinner getMealSpinner() {
        return mealSpinner;
    }

    public void setMealSpinner(Spinner mealSpinner) {
        this.mealSpinner = mealSpinner;
    }

    public TextView getPassengerDate() {
        return passengerDate;
    }

    public void setPassengerDate(TextView passengerDate) {
        this.passengerDate = passengerDate;
    }

    public EditText getEtFirstName() {
        return etFirstName;
    }

    public void setEtFirstName(EditText etFirstName) {
        this.etFirstName = etFirstName;
    }

    public EditText getEtLastName() {
        return etLastName;
    }

    public void setEtLastName(EditText etLastName) {
        this.etLastName = etLastName;
    }

    public TextView getTvPassengerType() {
        return tvPassengerType;
    }

    public void setTvPassengerType(TextView tvPassengerType) {
        this.tvPassengerType = tvPassengerType;
    }

    public LinearLayout getLlResidenceSpinner() {
        return llResidenceSpinner;
    }

    public LinearLayout getLlPPIssuedBySpinner() {
        return llPPIssuedBySpinner;
    }

    public EditText getEtPassportNo() {
        return etPassportNo;
    }

    public TextView getTvPPExpDate() {
        return tvPPExpDate;
    }

    public Spinner getSpinnerResidence() {
        return spinnerResidence;
    }

    public Spinner getSpinnerIssuedBy() {
        return spinnerIssuedBy;
    }

    public String getTEXT_DOB() {
        return TEXT_DOB;
    }

    public String getTEXT_PED() {
        return TEXT_PED;
    }

    public RecyclerView getRvPassengers() {
        return rvPassengers;
    }
}
