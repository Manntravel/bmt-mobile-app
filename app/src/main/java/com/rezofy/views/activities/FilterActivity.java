package com.rezofy.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.adapters.Filter_filght_list_adapter;
import com.rezofy.models.response_models.Data;
import com.rezofy.models.response_models.FlightData;
import com.rezofy.models.response_models.SearchResponse;
import com.rezofy.models.util_models.FilterListModel;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by linchpin on 3/2/16.
 */
public class FilterActivity extends Activity implements View.OnClickListener {
    SearchResponse domesticOneWaySearchResponse;
    RecyclerView rView;
    Filter_filght_list_adapter adapter;
    HashSet<FilterListModel> hashSet;
    ArrayList<FilterListModel> models;
    private LinearLayout layout_0_stop, layout_1_stop, layout_2_stop, layout_3_stop, layout_arrival_timeslot1, layout_arrival_timeslot2, layout_arrival_timeslot3, layout_arrival_timeslot4;
    private LinearLayout layout_departure_timeslot1, layout_departure_timeslot2, layout_departure_timeslot3, layout_departure_timeslot4;
    private LinearLayout arrivalLayout;
    //  private int arrayIds[] = {R.id.layout_0_stop,R.id.layout_1_stop,R.id.layout_2_stop,R.id.layout_3_stop,R.id.layout_arrival_timeslot1,R.id.layout_arrival_timeslot2,R.id.layout_arrival_timeslot3,R.id.layout_arrival_timeslot4,R.id.layout_departure_timeslot1,R.id.layout_departure_timeslot2,R.id.layout_departure_timeslot3,R.id.layout_departure_timeslot4};
    private LinearLayout layoutAr[];// = {layout_0_stop,layout_1_stop,layout_2_stop,layout_3_stop,layout_arrival_timeslot1,layout_arrival_timeslot2,layout_arrival_timeslot3,layout_arrival_timeslot4,layout_departure_timeslot1,layout_departure_timeslot2,layout_departure_timeslot3,layout_departure_timeslot4};
    private boolean applyButtonEnabled = false;
    private Button btnBack, btnReset;
    private CheckBox checkBox_hide_multiairlines;
    private String type;
    List<FlightData> inbounds;
    ArrayList<String> stopList = new ArrayList<>();
    ArrayList<String> arrivalSlotList = new ArrayList<>();
    ArrayList<String> departureSlotList = new ArrayList<>();
    ArrayList<String> checkedAirlineNames;

    //data used for twoWayDomestic
    FlightsSearchDomesticTwowayActivity twoWayOb;
    FlightsSearchInternationalTwowayActivity twoWayIntOb;
    private List<FlightData> originalRegularInboundFlights, toShowRegularInboundFlights, originalRegularOutboundFlights, toShowRegularOutboundFlights;
    private LinkedHashMap<String, ArrayList<FlightData>> originalMapSpecialFlights;
    private boolean filtersResetted = false;
    private boolean isSearchInternational;
    ArrayList<FlightData> twoWayIntFlightList;
    // private int arrivalTimeAr[] = {0,700,1100,};
    private ImageView btnFloating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_domestic);
        type = getIntent().getStringExtra("type");
        Log.d("Trip", "type is " + type);
        if (getIntent().getStringExtra("destCountryCode").equals(getString(R.string.originCountryCode)) && getIntent().getStringExtra("originCountryCode").equals(getString(R.string.originCountryCode))) {
            isSearchInternational = false;
        } else {
            isSearchInternational = true;
        }

        init();
        setListner();
        setData();
        if (Utils.show_saved_filters) {
            restoreOldFilters();

        } else {
            checkedAirlineNames = new ArrayList<>();
        }
        rView.setLayoutManager(new LinearLayoutManager(this, 1, false));
//        adapter = new Filter_filght_list_adapter(this, models,checkedAirlineNames);
        adapter = new Filter_filght_list_adapter(this, models);
        rView.setAdapter(adapter);
        if (Utils.show_saved_filters) {
            adapter.setCheckedAirlineNames(checkedAirlineNames);
            adapter.notifyDataSetChanged();
            boolean flag = checkForApplyButtion();
            toggleBackButton(flag);
        }
        Log.d("Trip", "showSaved filters is " + Utils.show_saved_filters);


    }

    private void init() {

        rView = (RecyclerView) findViewById(R.id.rViewFilter);
        rView.setNestedScrollingEnabled(false);
        rView.setHasFixedSize(true);

        layout_0_stop = (LinearLayout) findViewById(R.id.layout_0_stop);
        layout_1_stop = (LinearLayout) findViewById(R.id.layout_1_stop);
        layout_2_stop = (LinearLayout) findViewById(R.id.layout_2_stop);
        layout_3_stop = (LinearLayout) findViewById(R.id.layout_3_stop);

        //arrival is enabled only in case of round trip
        if (type.equals(Utils.TYPE_ROUND_TRIP)) {
            arrivalLayout = (LinearLayout) findViewById(R.id.arrival_Layout);
            arrivalLayout.setVisibility(View.VISIBLE);
            layout_arrival_timeslot1 = (LinearLayout) findViewById(R.id.layout_arrival_timeslot1);
            layout_arrival_timeslot2 = (LinearLayout) findViewById(R.id.layout_arrival_timeslot2);
            layout_arrival_timeslot3 = (LinearLayout) findViewById(R.id.layout_arrival_timeslot3);
            layout_arrival_timeslot4 = (LinearLayout) findViewById(R.id.layout_arrival_timeslot4);
        }
        layout_departure_timeslot1 = (LinearLayout) findViewById(R.id.layout_departure_timeslot1);
        layout_departure_timeslot2 = (LinearLayout) findViewById(R.id.layout_departure_timeslot2);
        layout_departure_timeslot3 = (LinearLayout) findViewById(R.id.layout_departure_timeslot3);
        layout_departure_timeslot4 = (LinearLayout) findViewById(R.id.layout_departure_timeslot4);
        btnBack = (Button) findViewById(R.id.backBtn);
        btnReset = (Button) findViewById(R.id.resetBtn);
        checkBox_hide_multiairlines = (CheckBox) findViewById(R.id.checkbox_hide_multipleAirlines);
        btnFloating = (ImageView) findViewById(R.id.btn_flaoting);
        if(Boolean.parseBoolean(getString(R.string.hideChatIcon))) {
            btnFloating.setVisibility(View.GONE);
        }
    }

    private void restoreOldFilters() {
        try {
            checkBox_hide_multiairlines.setChecked(Utils.filter_hideMultiAirlines);
            stopList = Utils.filter_no_of_stop_list;
            departureSlotList = Utils.filter_dep_slot_list;
            arrivalSlotList = Utils.filter_arrival_slot_list;

            int len = stopList.size();
            Log.d("Trip", "len is 1* " + len);
            for (int i = 0; i < len; i++) {
                String slot = stopList.get(i);
                if (slot.equals("zero")) {
                    resetBackGroundColor(layout_0_stop, getResources().getColor(R.color.filter_screen_selection_color));
                } else if (slot.equals("one")) {
                    resetBackGroundColor(layout_1_stop, getResources().getColor(R.color.filter_screen_selection_color));
                } else if (slot.equals("two")) {
                    resetBackGroundColor(layout_2_stop, getResources().getColor(R.color.filter_screen_selection_color));
                } else if (slot.equals("three")) {
                    resetBackGroundColor(layout_3_stop, getResources().getColor(R.color.filter_screen_selection_color));
                }
            }

            len = departureSlotList.size();
            Log.d("Trip", "len is 2* " + len);
            for (int i = 0; i < len; i++) {
                String slot = departureSlotList.get(i);
                if (slot.equals("zero")) {
                    resetBackGroundColor(layout_departure_timeslot1, getResources().getColor(R.color.filter_screen_selection_color));
                } else if (slot.equals("one")) {
                    resetBackGroundColor(layout_departure_timeslot2, getResources().getColor(R.color.filter_screen_selection_color));
                } else if (slot.equals("two")) {
                    resetBackGroundColor(layout_departure_timeslot3, getResources().getColor(R.color.filter_screen_selection_color));
                } else if (slot.equals("three")) {
                    resetBackGroundColor(layout_departure_timeslot4, getResources().getColor(R.color.filter_screen_selection_color));
                }
            }

            len = arrivalSlotList.size();
            Log.d("Trip", "len is 3* " + len);
            for (int i = 0; i < len; i++) {
                String slot = arrivalSlotList.get(i);
                if (slot.equals("zero")) {
                    resetBackGroundColor(layout_arrival_timeslot1, getResources().getColor(R.color.filter_screen_selection_color));
                } else if (slot.equals("one")) {
                    resetBackGroundColor(layout_arrival_timeslot2, getResources().getColor(R.color.filter_screen_selection_color));
                } else if (slot.equals("two")) {
                    resetBackGroundColor(layout_arrival_timeslot3, getResources().getColor(R.color.filter_screen_selection_color));
                } else if (slot.equals("three")) {
                    resetBackGroundColor(layout_arrival_timeslot4, getResources().getColor(R.color.filter_screen_selection_color));
                }
            }
            Log.d("Trip", "size of util checked is " + Utils.filter_checked_airlineNames.size());
            if (Utils.filter_checked_airlineNames.size() > 0) {
                checkedAirlineNames = Utils.filter_checked_airlineNames;
            } else {
                checkedAirlineNames = new ArrayList<>();
            }


        } catch (Exception e) {
            Log.d("Trip", "Error in restoreOldFilters" + e);
        }
    }

    private void saveFilters() {
        try {
            // Log.d("Trip","inside saveFilters* ");
            Utils.filter_hideMultiAirlines = checkBox_hide_multiairlines.isChecked();
            Utils.filter_no_of_stop_list = stopList;
            Utils.filter_dep_slot_list = departureSlotList;
            Utils.filter_arrival_slot_list = arrivalSlotList;
            Utils.filter_checked_airlineNames = adapter.getCheckedAirlineNames();


        } catch (Exception e) {
            Log.d("Trip", "Error in saveFilters" + e);
        }
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        //super.onBackPressed();  // optional depending on your needs
        if (filtersResetted) {
            if (type.equals(Utils.TYPE_ONE_WAY)) {

                String json = new Gson().toJson(domesticOneWaySearchResponse);
                Utils.writeToLargeDataFile(this, json, Utils.Filtered_DATA_FILE_NAME);
                Intent intent = new Intent();
                intent.putExtra("isTwoWayInternational", false);
                setResult(RESULT_OK, intent);
                finish();

            } else if (type.equals(Utils.TYPE_ROUND_TRIP)) {
                if (isSearchInternational) {
                    twoWayIntOb.getRoundTripSpecialAdapter().setFlightDataList(twoWayIntOb.getSearchResponse().getData().getResults());
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    intent.putExtra("isTwoWayInternational", true);
                    finish();

                } else {
                    originalRegularInboundFlights.clear();
                    originalRegularOutboundFlights.clear();
                    originalMapSpecialFlights.clear();
                    originalRegularInboundFlights.addAll(twoWayOb.getHeaderAdapter().getOriginalRegularInboundFlights());// = ;
                    originalRegularOutboundFlights.addAll(twoWayOb.getHeaderAdapter().getOriginalRegularOutboundFlights());// =;
                    originalMapSpecialFlights.putAll(twoWayOb.getHeaderAdapter().getOriginalMapSpecialFlights());
                    twoWayOb.getHeaderAdapter().setToShowRegularOutboundFlights(originalRegularOutboundFlights);
                    twoWayOb.getHeaderAdapter().setToShowRegularInboundFlights(originalRegularInboundFlights);
                    twoWayOb.getHeaderAdapter().setToShowMapSpecialFlights(originalMapSpecialFlights);
                    twoWayOb.getHeaderAdapter().resetData();

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    intent.putExtra("isTwoWayInternational", false);
                    finish();
                }

            }
        } else {
            super.onBackPressed();
        }
    }

    private void setData() {
        try {

            if (type.equals(Utils.TYPE_ONE_WAY)) {
                String data = Utils.getFromLargeDataFile(this, Utils.LARGE_DATA_FILE_NAME);
                if (data != null)
                    domesticOneWaySearchResponse = new Gson().fromJson(data, SearchResponse.class);
                // Log.d("Trip","json data is "+data);
                inbounds = domesticOneWaySearchResponse.getData().getResults();
                // Log.d("Trip", "size of list is " + inbounds.size());
                setFilterListModelData(inbounds);

            } else if (type.equals(Utils.TYPE_ROUND_TRIP)) {
                if (!isSearchInternational) {
                    twoWayOb = FlightsSearchDomesticTwowayActivity.getInstance();
                    originalRegularInboundFlights = new ArrayList<>();
                    originalRegularOutboundFlights = new ArrayList<>();
                    originalMapSpecialFlights = new LinkedHashMap<>();
                    originalRegularInboundFlights.addAll(twoWayOb.getHeaderAdapter().getOriginalRegularInboundFlights());// = ;
                    originalRegularOutboundFlights.addAll(twoWayOb.getHeaderAdapter().getOriginalRegularOutboundFlights());// =;
                    originalMapSpecialFlights.putAll(twoWayOb.getHeaderAdapter().getOriginalMapSpecialFlights());// = twoWayOb.getHeaderAdapter().getOriginalMapSpecialFlights();
                    setFilterListModelData(originalMapSpecialFlights, originalRegularInboundFlights, originalRegularOutboundFlights);
                } else {
                    //for international flights
                    twoWayIntOb = FlightsSearchInternationalTwowayActivity.getInstance();
                    twoWayIntFlightList = new ArrayList<>();
                    twoWayIntFlightList.addAll(twoWayIntOb.getSearchResponse().getData().getResults());
                    setFilterListModelData(twoWayIntFlightList);
                    ;
                }


            }

        } catch (Exception e) {
            Log.d("Trip", "Eror in setData " + e);
        }
    }


    private void setFilterListModelData(LinkedHashMap<String, ArrayList<FlightData>> mapOfSpecialFlights, List<FlightData> inbound, List<FlightData> outbound) {
        try {

            List<String> keysList = new ArrayList<>(mapOfSpecialFlights.keySet());
            int size = keysList.size();
            List<FlightData> allFlights = new ArrayList<>();
            allFlights.addAll(inbound);
            allFlights.addAll(outbound);
            for (int j = 0; j < size; j++) {
                /// Log.d("Trip","at "+j+" key is "+keysList.get(j));
                allFlights.addAll(mapOfSpecialFlights.get(keysList.get(j)));

            }

            hashSet = new HashSet<FilterListModel>();
            int len = allFlights.size();
            // Log.d("Trip","size of allFlights is "+len);
            for (int i = 0; i < len; i++) {
                FilterListModel model = new FilterListModel();
                model.setCarrier(allFlights.get(i).getCarrier());
                model.setAirlLineName(allFlights.get(i).getSegments().get(0).getLegs().get(0).getAirline());

                hashSet.add(model);
            }

            models = new ArrayList<>();
            Iterator<FilterListModel> iterator = hashSet.iterator();

            // check values
            while (iterator.hasNext()) {
                models.add(iterator.next());
            }
            Log.d("Trip", "size of models is " + models.size());
            layoutAr = new LinearLayout[12];
            layoutAr[0] = layout_0_stop;
            layoutAr[1] = layout_1_stop;
            layoutAr[2] = layout_2_stop;
            layoutAr[3] = layout_3_stop;
            layoutAr[4] = layout_arrival_timeslot1;
            layoutAr[5] = layout_arrival_timeslot2;
            layoutAr[6] = layout_arrival_timeslot3;
            layoutAr[7] = layout_arrival_timeslot4;
            layoutAr[8] = layout_departure_timeslot1;
            layoutAr[9] = layout_departure_timeslot2;
            layoutAr[10] = layout_departure_timeslot3;
            layoutAr[11] = layout_departure_timeslot4;


        } catch (Exception e) {
            Log.d("Trip", "Error in setFilterListModelData" + e);
        }
    }

    private void setFilterListModelData(List<FlightData> flights) {
        try {
            hashSet = new HashSet<FilterListModel>();
            int len = flights.size();
            for (int i = 0; i < len; i++) {
                FilterListModel model = new FilterListModel();
                model.setCarrier(flights.get(i).getCarrier());
                model.setAirlLineName(flights.get(i).getSegments().get(0).getLegs().get(0).getAirline());

                hashSet.add(model);
            }

            models = new ArrayList<>();
            Iterator<FilterListModel> iterator = hashSet.iterator();

            // check values
            while (iterator.hasNext()) {
                models.add(iterator.next());
            }
            Log.d("Trip", "size of models is " + models.size());
            layoutAr = new LinearLayout[12];
            layoutAr[0] = layout_0_stop;
            layoutAr[1] = layout_1_stop;
            layoutAr[2] = layout_2_stop;
            layoutAr[3] = layout_3_stop;
            layoutAr[4] = layout_arrival_timeslot1;
            layoutAr[5] = layout_arrival_timeslot2;
            layoutAr[6] = layout_arrival_timeslot3;
            layoutAr[7] = layout_arrival_timeslot4;
            layoutAr[8] = layout_departure_timeslot1;
            layoutAr[9] = layout_departure_timeslot2;
            layoutAr[10] = layout_departure_timeslot3;
            layoutAr[11] = layout_departure_timeslot4;


        } catch (Exception e) {
            Log.d("Trip", "Error in setFilterListModelData" + e);
        }
    }


    private void setListner() {

        layout_0_stop.setOnClickListener(this);
        layout_1_stop.setOnClickListener(this);
        layout_2_stop.setOnClickListener(this);
        layout_3_stop.setOnClickListener(this);
        if (type.equals(Utils.TYPE_ROUND_TRIP)) {
            layout_arrival_timeslot1.setOnClickListener(this);
            layout_arrival_timeslot2.setOnClickListener(this);
            layout_arrival_timeslot3.setOnClickListener(this);
            layout_arrival_timeslot4.setOnClickListener(this);
        }
        layout_departure_timeslot1.setOnClickListener(this);
        layout_departure_timeslot2.setOnClickListener(this);
        layout_departure_timeslot3.setOnClickListener(this);
        layout_departure_timeslot4.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        checkBox_hide_multiairlines.setOnClickListener(this);

        btnFloating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatingButtonMove floatingButtonMove = new FloatingButtonMove(getApplicationContext());
                floatingButtonMove.dragView(v, event);
                return true;
            }
        });
    }

    private void addRemoveFromList(ArrayList<String> arList, String param) {
        try {
            if (arList.contains(param)) {
                arList.remove(param);
            } else {
                arList.add(param);
            }

        } catch (Exception e) {
            Log.d("Trip", "Eror in addRemoveList " + e);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_0_stop:
                changeBackgroundColor(layout_0_stop);
                addRemoveFromList(stopList, "zero");
                break;

            case R.id.layout_1_stop:
                changeBackgroundColor(layout_1_stop);
                addRemoveFromList(stopList, "one");
                break;

            case R.id.layout_2_stop:
                changeBackgroundColor(layout_2_stop);
                addRemoveFromList(stopList, "two");
                break;

            case R.id.layout_3_stop:
                changeBackgroundColor(layout_3_stop);
                addRemoveFromList(stopList, "three");
                break;

            case R.id.layout_arrival_timeslot1:
                changeBackgroundColor(layout_arrival_timeslot1);
                addRemoveFromList(arrivalSlotList, "zero");
                break;

            case R.id.layout_arrival_timeslot2:
                changeBackgroundColor(layout_arrival_timeslot2);
                addRemoveFromList(arrivalSlotList, "one");
                break;

            case R.id.layout_arrival_timeslot3:
                changeBackgroundColor(layout_arrival_timeslot3);
                addRemoveFromList(arrivalSlotList, "two");
                break;

            case R.id.layout_arrival_timeslot4:
                changeBackgroundColor(layout_arrival_timeslot4);
                addRemoveFromList(arrivalSlotList, "three");
                break;

            case R.id.layout_departure_timeslot1:
                changeBackgroundColor(layout_departure_timeslot1);
                addRemoveFromList(departureSlotList, "zero");
                break;

            case R.id.layout_departure_timeslot2:
                changeBackgroundColor(layout_departure_timeslot2);
                addRemoveFromList(departureSlotList, "one");
                break;

            case R.id.layout_departure_timeslot3:
                changeBackgroundColor(layout_departure_timeslot3);
                addRemoveFromList(departureSlotList, "two");
                break;

            case R.id.layout_departure_timeslot4:
                changeBackgroundColor(layout_departure_timeslot4);
                addRemoveFromList(departureSlotList, "three");
                break;

            case R.id.backBtn:
                // Log.d("Trip","inside backbtn"+applyButtonEnabled);
                if (applyButtonEnabled) {
                    saveFilters();
                    Utils.show_saved_filters = true;
                    openFilteredList();
                } else {
                    if (type.equals(Utils.TYPE_ONE_WAY)) {
//                        inbounds = domesticOneWaySearchResponse.getData().getResults();
//                        Data dt = new Data();
//                        dt.setResults(inbounds);
//                        domesticOneWaySearchResponse.setData(dt);
                        try {
                            String json = new Gson().toJson(domesticOneWaySearchResponse);
                            Utils.writeToLargeDataFile(this, json, Utils.Filtered_DATA_FILE_NAME);
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            intent.putExtra("isTwoWayInternational", false);
                            finish();
                        }
                        catch (Exception e)
                        {
                            Log.d("Trip","eror is "+e);
                        }

                    } else if (type.equals(Utils.TYPE_ROUND_TRIP)) {
                        if (isSearchInternational) {
                            twoWayIntOb.getRoundTripSpecialAdapter().setFlightDataList(twoWayIntOb.getSearchResponse().getData().getResults());
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            intent.putExtra("isTwoWayInternational", true);
                            finish();
                        } else {
                            originalRegularInboundFlights.clear();
                            originalRegularOutboundFlights.clear();
                            originalMapSpecialFlights.clear();
                            originalRegularInboundFlights.addAll(twoWayOb.getHeaderAdapter().getOriginalRegularInboundFlights());// = ;
                            originalRegularOutboundFlights.addAll(twoWayOb.getHeaderAdapter().getOriginalRegularOutboundFlights());// =;
                            originalMapSpecialFlights.putAll(twoWayOb.getHeaderAdapter().getOriginalMapSpecialFlights());
                            twoWayOb.getHeaderAdapter().setToShowRegularOutboundFlights(originalRegularOutboundFlights);
                            twoWayOb.getHeaderAdapter().setToShowRegularInboundFlights(originalRegularInboundFlights);
                            twoWayOb.getHeaderAdapter().setToShowMapSpecialFlights(originalMapSpecialFlights);
                            twoWayOb.getHeaderAdapter().resetData();

                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            intent.putExtra("isTwoWayInternational", false);
                            finish();
                        }

                    }


                }
                break;
            case R.id.resetBtn:
                if (applyButtonEnabled) {
                    resetFilters();
                    toggleBackButton(false);
                }
                break;


        }

        if (view.getId() != R.id.backBtn) {
            boolean flag = checkForApplyButtion();
            toggleBackButton(flag);
            // Log.d("Trip", "applyenabled is " + applyButtonEnabled);

        }
    }

    private void resetFilters() {
        try {
            filtersResetted = true;
            checkBox_hide_multiairlines.setChecked(false);
            resetBackGroundColor(layout_0_stop, getResources().getColor(R.color.white));
            resetBackGroundColor(layout_1_stop, getResources().getColor(R.color.white));
            resetBackGroundColor(layout_2_stop, getResources().getColor(R.color.white));
            resetBackGroundColor(layout_3_stop, getResources().getColor(R.color.white));
            resetBackGroundColor(layout_departure_timeslot1, getResources().getColor(R.color.white));
            resetBackGroundColor(layout_departure_timeslot2, getResources().getColor(R.color.white));
            resetBackGroundColor(layout_departure_timeslot3, getResources().getColor(R.color.white));
            resetBackGroundColor(layout_departure_timeslot4, getResources().getColor(R.color.white));
            resetBackGroundColor(layout_arrival_timeslot1, getResources().getColor(R.color.white));
            resetBackGroundColor(layout_arrival_timeslot2, getResources().getColor(R.color.white));
            resetBackGroundColor(layout_arrival_timeslot3, getResources().getColor(R.color.white));
            resetBackGroundColor(layout_arrival_timeslot4, getResources().getColor(R.color.white));
            stopList.clear();
            departureSlotList.clear();
            arrivalSlotList.clear();
            if (adapter.getCheckedAirlineNames().size() > 0) {
                //uncheck all airlinenames
                adapter.getCheckedAirlineNames().clear();
                adapter.notifyDataSetChanged();

            }
        } catch (Exception e) {
            Log.d("Trip", "Eror in reserFilters " + e);
        }
    }

    public void toggleBackButton(boolean flag) {
        try {
            if (flag) {
                applyButtonEnabled = flag;
                btnBack.setText("APPLY");
                btnBack.setTextColor(getResources().getColor(R.color.filter_button_textCollor2));
                btnReset.setTextColor(getResources().getColor(R.color.filter_button_textCollor2));
                UIUtils.setRoundedButtonProperties(btnBack);
                UIUtils.setRoundedButtonProperties(btnReset);
/*                btnBack.setBackgroundColor(getResources().getColor(R.color.filter_button_bg2));
                btnReset.setBackgroundColor(getResources().getColor(R.color.filter_button_bg2));*/
            } else {
                applyButtonEnabled = flag;
                btnBack.setText("BACK");
                btnBack.setTextColor(getResources().getColor(R.color.filter_button_textCollor1));
                btnReset.setTextColor(getResources().getColor(R.color.filter_button_textCollor1));
                btnBack.setBackgroundColor(getResources().getColor(R.color.filter_button_bg1));
                btnReset.setBackgroundColor(getResources().getColor(R.color.filter_button_bg1));
            }

        } catch (Exception e) {
            Log.d("Trip", "eror in toggleBtn " + e);
        }
    }

    private void openFilteredList() {
        if (type.equals(Utils.TYPE_ONE_WAY)) {
            List<FlightData> filteredFlights = new ArrayList<>();

            //multi airlines filtering
            int size = inbounds.size();
            //  Log.d("Trip", "inside openFilteredlist**1 " + size);
            if (checkBox_hide_multiairlines.isChecked()) {
                filteredFlights.clear();
                for (int j = 0; j < size; j++) {
                    if (!inbounds.get(j).isMultiCarrier()) {
                        filteredFlights.add(inbounds.get(j));
                    }
                }
                inbounds.clear();
                inbounds.addAll(filteredFlights);

            }
            //no of stops filtering

            filteredFlights.clear();
            size = inbounds.size();
            // Log.d("Trip", "inside openFilteredlist**2 " + size);
            int stopListSize = stopList.size();

            for (int i = 0; i < stopListSize; i++) {
                String noOfStops = stopList.get(i);

                for (int j = 0; j < size; j++) {
                    if (noOfStops.equals("zero") && inbounds.get(j).getSegments().get(0).getNoOfStops() == 0) {
                        filteredFlights.add(inbounds.get(j));
                    } else if (noOfStops.equals("one") && inbounds.get(j).getSegments().get(0).getNoOfStops() == 1) {
                        filteredFlights.add(inbounds.get(j));
                    } else if (noOfStops.equals("two") && inbounds.get(j).getSegments().get(0).getNoOfStops() == 2) {
                        filteredFlights.add(inbounds.get(j));
                    } else if (noOfStops.equals("three") && inbounds.get(j).getSegments().get(0).getNoOfStops() >= 3) {
                        filteredFlights.add(inbounds.get(j));
                    }


                }

            }

            if (stopListSize > 0) {
                inbounds.clear();
                inbounds.addAll(filteredFlights);
            }

            //departure filtering
            filteredFlights.clear();
            ;
            int depListSize = departureSlotList.size();
            size = inbounds.size();
            // Log.d("Trip", "inside openFilteredlist**3 " + size);

            for (int j = 0; j < depListSize; j++) {
                String slot = departureSlotList.get(j);

                for (int i = 0; i < size; i++) {
                    // Log.d("Trip", "Departure time is " + j + " :::: " + i + ":::: " + Integer.parseInt(inbounds.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) + " ::::: " + inbounds.get(i).getSegments().get(0).getLegs().get(0).getAirlineName());
                    if (slot.equals("zero") && Integer.parseInt(inbounds.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 0 && Integer.parseInt(inbounds.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 700) {
                        filteredFlights.add(inbounds.get(i));
                    } else if (slot.equals("one") && Integer.parseInt(inbounds.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 700 && Integer.parseInt(inbounds.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 1100) {
                        filteredFlights.add(inbounds.get(i));
                    } else if (slot.equals("two") && Integer.parseInt(inbounds.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 1100 && Integer.parseInt(inbounds.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 1800) {
                        filteredFlights.add(inbounds.get(i));
                    } else if (slot.equals("three") && Integer.parseInt(inbounds.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 1800 && Integer.parseInt(inbounds.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 2359) {
                        filteredFlights.add(inbounds.get(i));
                    }


                }
            }


            if (depListSize > 0) {
                inbounds.clear();
                inbounds.addAll(filteredFlights);
            }

            //filter for airline names
            ArrayList<String> checkedAirlineNames = adapter.getCheckedAirlineNames();
            int chekSize = checkedAirlineNames.size();
            filteredFlights.clear();
            size = inbounds.size();
            Log.d("Trip", "check size is " + chekSize);
            for (int i = 0; i < chekSize; i++) {
                String airlineName = checkedAirlineNames.get(i);

                //;
                for (int j = 0; j < size; j++) {
                    //Log.d("Trip", "airline name is " + i + " ::::: " + airlineName + ":::: " + j + " :::: " + inbounds.get(j).getSegments().get(0).getLegs().get(0).getAirlineName());
                    if (airlineName.equalsIgnoreCase(inbounds.get(j).getCarrier()))   //getSegments().get(0).getLegs().get(0).getAirline()))
                    {
                        // Log.d("Trip", "iinside for to add airline name is " + i + " ::::: " + airlineName + ":::: " + inbounds.get(j).getSegments().get(0).getLegs().get(0).getAirlineName());
                        filteredFlights.add(inbounds.get(j));
                    }
                }

            }

            if (chekSize > 0) {
                inbounds.clear();
                inbounds.addAll(filteredFlights);
            }


            Data dt = new Data();
            dt.setResults(inbounds);
            domesticOneWaySearchResponse.setData(dt);
            String json = new Gson().toJson(domesticOneWaySearchResponse);
            Utils.writeToLargeDataFile(this, json, Utils.Filtered_DATA_FILE_NAME);


            Intent intent = new Intent();
            intent.putExtra("isTwoWayInternational", false);
            setResult(RESULT_OK, intent);
            finish();
        } else if (type.equals(Utils.TYPE_ROUND_TRIP)) {

            if (isSearchInternational) {
                filterIntRoundTrip();
            } else {
                filterRoundTrip();
            }
        }


    }

    private void filterRoundTrip() {
        ArrayList<FlightData> tempFlights = new ArrayList<>();
        //multi airlines filtering
        int size = 0;
        if (checkBox_hide_multiairlines.isChecked()) {
            size = originalRegularInboundFlights.size();
            tempFlights.clear();
            for (int j = 0; j < size; j++) {
                if (!originalRegularInboundFlights.get(j).isMultiCarrier()) {
                    tempFlights.add(originalRegularInboundFlights.get(j));
                }
            }

            originalRegularInboundFlights.clear();
            originalRegularInboundFlights.addAll(tempFlights);


            size = originalRegularOutboundFlights.size();
            tempFlights.clear();
            for (int j = 0; j < size; j++) {
                if (!originalRegularOutboundFlights.get(j).isMultiCarrier()) {
                    tempFlights.add(originalRegularOutboundFlights.get(j));
                }
            }

            originalRegularOutboundFlights.clear();
            originalRegularOutboundFlights.addAll(tempFlights);


            List<String> keysList = new ArrayList<>(originalMapSpecialFlights.keySet());
            int len = keysList.size();
            for (int i = 0; i < len; i++) {
                ArrayList<FlightData> flightsAtIndex = originalMapSpecialFlights.get(keysList.get(i));
                size = flightsAtIndex.size();
                tempFlights.clear();
                for (int j = 0; j < size; j++) {
                    if (!flightsAtIndex.get(j).isMultiCarrier()) {
                        tempFlights.add(flightsAtIndex.get(j));
                    }
                }

                if (tempFlights.size() > 0) {
                    ArrayList<FlightData> tmp = new ArrayList<>();
                    tmp.addAll(tempFlights);
                    originalMapSpecialFlights.put(keysList.get(i), tmp);
                } else {
                    originalMapSpecialFlights.remove(keysList.get(i));
                }


            }

        }

        //no of stops filtering for originalRegularInboundFlights
        tempFlights.clear();
        size = originalRegularInboundFlights.size();
        // Log.d("Trip", "inside openFilteredlist**2 " + size);
        int stopListSize = stopList.size();

        for (int i = 0; i < stopListSize; i++) {
            String noOfStops = stopList.get(i);

            for (int j = 0; j < size; j++) {
                if (noOfStops.equals("zero") && originalRegularInboundFlights.get(j).getSegments().get(0).getNoOfStops() == 0) {
                    tempFlights.add(originalRegularInboundFlights.get(j));
                } else if (noOfStops.equals("one") && originalRegularInboundFlights.get(j).getSegments().get(0).getNoOfStops() == 1) {
                    tempFlights.add(originalRegularInboundFlights.get(j));
                } else if (noOfStops.equals("two") && originalRegularInboundFlights.get(j).getSegments().get(0).getNoOfStops() == 2) {
                    tempFlights.add(originalRegularInboundFlights.get(j));
                } else if (noOfStops.equals("three") && originalRegularInboundFlights.get(j).getSegments().get(0).getNoOfStops() >= 3) {
                    tempFlights.add(originalRegularInboundFlights.get(j));
                }


            }

        }
        if (stopListSize > 0) {
            originalRegularInboundFlights.clear();
            originalRegularInboundFlights.addAll(tempFlights);
        }

        //no of stops filtering for originalRegularOutboundFlights
        tempFlights.clear();
        size = originalRegularOutboundFlights.size();

        for (int i = 0; i < stopListSize; i++) {
            String noOfStops = stopList.get(i);

            for (int j = 0; j < size; j++) {
                if (noOfStops.equals("zero") && originalRegularOutboundFlights.get(j).getSegments().get(0).getNoOfStops() == 0) {
                    tempFlights.add(originalRegularOutboundFlights.get(j));
                } else if (noOfStops.equals("one") && originalRegularOutboundFlights.get(j).getSegments().get(0).getNoOfStops() == 1) {
                    tempFlights.add(originalRegularOutboundFlights.get(j));
                } else if (noOfStops.equals("two") && originalRegularOutboundFlights.get(j).getSegments().get(0).getNoOfStops() == 2) {
                    tempFlights.add(originalRegularOutboundFlights.get(j));
                } else if (noOfStops.equals("three") && originalRegularOutboundFlights.get(j).getSegments().get(0).getNoOfStops() >= 3) {
                    tempFlights.add(originalRegularOutboundFlights.get(j));
                }


            }

        }
        if (stopListSize > 0) {
            originalRegularOutboundFlights.clear();
            originalRegularOutboundFlights.addAll(tempFlights);
        }


        //no of stops filtering for originalMapLists

        List<String> keysList = new ArrayList<>(originalMapSpecialFlights.keySet());
        int len = keysList.size();
        for (int j = 0; j < len; j++) {
            ArrayList<FlightData> flightsAtIndex = originalMapSpecialFlights.get(keysList.get(j));
            size = flightsAtIndex.size();
            tempFlights.clear();
            if (keysList.get(j).equals("GDS"))//for GDS Flights
            {
                for (int i = 0; i < stopListSize; i++) {
                    String noOfStops = stopList.get(i);

                    for (int k = 0; k < size; k++) {
                        if (noOfStops.equals("zero") && flightsAtIndex.get(k).getSegments().get(0).getNoOfStops() == 0 && flightsAtIndex.get(k).getSegments().get(1).getNoOfStops() == 0) {
                            tempFlights.add(flightsAtIndex.get(k));
                        } else if (noOfStops.equals("one") && flightsAtIndex.get(k).getSegments().get(0).getNoOfStops() == 1 && flightsAtIndex.get(k).getSegments().get(1).getNoOfStops() == 1) {
                            tempFlights.add(flightsAtIndex.get(k));
                        } else if (noOfStops.equals("two") && flightsAtIndex.get(k).getSegments().get(0).getNoOfStops() == 2 && flightsAtIndex.get(k).getSegments().get(1).getNoOfStops() == 2) {
                            tempFlights.add(flightsAtIndex.get(k));
                        } else if (noOfStops.equals("three") && flightsAtIndex.get(k).getSegments().get(0).getNoOfStops() >= 3 && flightsAtIndex.get(k).getSegments().get(1).getNoOfStops() >= 3) {
                            tempFlights.add(flightsAtIndex.get(k));
                        }


                    }


                }

                if (tempFlights.size() > 0) {
                    ArrayList<FlightData> tmp = new ArrayList<>();
                    tmp.addAll(tempFlights);
                    originalMapSpecialFlights.put(keysList.get(j), tmp);
                } else {
                    if (stopListSize > 0)
                        originalMapSpecialFlights.remove(keysList.get(j));
                }

            } else//for nonGDS flights
            {
                for (int i = 0; i < stopListSize; i++) {
                    String noOfStops = stopList.get(i);

                    for (int k = 0; k < size; k++) {
                        if (noOfStops.equals("zero") && flightsAtIndex.get(k).getSegments().get(0).getNoOfStops() == 0) {
                            tempFlights.add(flightsAtIndex.get(k));
                        } else if (noOfStops.equals("one") && flightsAtIndex.get(k).getSegments().get(0).getNoOfStops() == 1) {
                            tempFlights.add(flightsAtIndex.get(k));
                        } else if (noOfStops.equals("two") && flightsAtIndex.get(k).getSegments().get(0).getNoOfStops() == 2) {
                            tempFlights.add(flightsAtIndex.get(k));
                        } else if (noOfStops.equals("three") && flightsAtIndex.get(k).getSegments().get(0).getNoOfStops() >= 3) {
                            tempFlights.add(flightsAtIndex.get(k));
                        }


                    }


                }

                if (tempFlights.size() > 0) {
                    ArrayList<FlightData> tmp = new ArrayList<>();
                    tmp.addAll(tempFlights);
                    originalMapSpecialFlights.put(keysList.get(j), tmp);
                } else {
                    if (stopListSize > 0)
                        originalMapSpecialFlights.remove(keysList.get(j));
                }

            }

        }

        //Filtering for Departure Time only for Outbound
        //departure filtering
        tempFlights.clear();
        int depListSize = departureSlotList.size();
        size = originalRegularOutboundFlights.size();
        //Log.d("Trip", "inside openFilteredlist**3 " + size);
        for (int j = 0; j < depListSize; j++) {
            String slot = departureSlotList.get(j);

            for (int i = 0; i < size; i++) {
                // Log.d("Trip", "Departure time for outbound is " + j + " :::: " + i + ":::: " + Integer.parseInt(originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) + " ::::: " + originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getAirlineName());
                if (slot.equals("zero") && Integer.parseInt(originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 0 && Integer.parseInt(originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 700) {
                    tempFlights.add(originalRegularOutboundFlights.get(i));
                } else if (slot.equals("one") && Integer.parseInt(originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 700 && Integer.parseInt(originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 1100) {
                    tempFlights.add(originalRegularOutboundFlights.get(i));
                } else if (slot.equals("two") && Integer.parseInt(originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 1100 && Integer.parseInt(originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 1800) {
                    tempFlights.add(originalRegularOutboundFlights.get(i));
                } else if (slot.equals("three") && Integer.parseInt(originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 1800 && Integer.parseInt(originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 2359) {
                    tempFlights.add(originalRegularOutboundFlights.get(i));
                }


            }

        }

        if (depListSize > 0) {
            originalRegularOutboundFlights.clear();
            originalRegularOutboundFlights.addAll(tempFlights);
        }


        //Filtering for Departure Time only for orginalMap OutBound
        keysList = new ArrayList<>(originalMapSpecialFlights.keySet());
        len = keysList.size();
        for (int j = 0; j < len; j++) {
            ArrayList<FlightData> flightsAtIndex = originalMapSpecialFlights.get(keysList.get(j));
            size = flightsAtIndex.size();
            if (keysList.get(j).contains("OUTBOUND") || keysList.get(j).equals("GDS")) {
                tempFlights.clear();
                for (int k = 0; k < depListSize; k++) {
                    String slot = departureSlotList.get(k);

                    for (int i = 0; i < size; i++) {
                        // Log.d("Trip", "Departure time for outbound is " + j + " :::: " + i + ":::: " + Integer.parseInt(originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) + " ::::: " + originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getAirlineName());
                        if (slot.equals("zero") && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 0 && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 700) {
                            tempFlights.add(flightsAtIndex.get(i));
                        } else if (slot.equals("one") && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 700 && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 1100) {
                            tempFlights.add(flightsAtIndex.get(i));
                        } else if (slot.equals("two") && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 1100 && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 1800) {
                            tempFlights.add(flightsAtIndex.get(i));
                        } else if (slot.equals("three") && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 1800 && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 2359) {
                            tempFlights.add(flightsAtIndex.get(i));
                        }


                    }

                }

                if (tempFlights.size() > 0) {
                    ArrayList<FlightData> tmp = new ArrayList<>();
                    tmp.addAll(tempFlights);
                    originalMapSpecialFlights.put(keysList.get(j), tmp);
                } else {
                    if (depListSize > 0)
                        originalMapSpecialFlights.remove(keysList.get(j));
                }
            }


        }

        //Filtering for Departure Time only for orginalMap INBOUND
        int depListSizeInbound = arrivalSlotList.size();
        keysList = new ArrayList<>(originalMapSpecialFlights.keySet());
        len = keysList.size();
        for (int j = 0; j < len; j++) {
            ArrayList<FlightData> flightsAtIndex = originalMapSpecialFlights.get(keysList.get(j));
            size = flightsAtIndex.size();
            if (keysList.get(j).contains("INBOUND")) {
                tempFlights.clear();
                for (int k = 0; k < depListSizeInbound; k++) {
                    String slot = arrivalSlotList.get(k);

                    for (int i = 0; i < size; i++) {
                        // Log.d("Trip", "Departure time for outbound is " + j + " :::: " + i + ":::: " + Integer.parseInt(originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) + " ::::: " + originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getAirlineName());
                        if (slot.equals("zero") && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 0 && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 700) {
                            tempFlights.add(flightsAtIndex.get(i));
                        } else if (slot.equals("one") && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 700 && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 1100) {
                            tempFlights.add(flightsAtIndex.get(i));
                        } else if (slot.equals("two") && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 1100 && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 1800) {
                            tempFlights.add(flightsAtIndex.get(i));
                        } else if (slot.equals("three") && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 1800 && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 2359) {
                            tempFlights.add(flightsAtIndex.get(i));
                        }


                    }

                }

                if (tempFlights.size() > 0) {
                    ArrayList<FlightData> tmp = new ArrayList<>();
                    tmp.addAll(tempFlights);
                    originalMapSpecialFlights.put(keysList.get(j), tmp);
                } else {
                    if (depListSizeInbound > 0)
                        originalMapSpecialFlights.remove(keysList.get(j));
                }
            } else if (keysList.get(j).equals("GDS"))//for GDS Flights
            {
                tempFlights.clear();
                for (int k = 0; k < depListSizeInbound; k++) {
                    String slot = arrivalSlotList.get(k);

                    for (int i = 0; i < size; i++) {
                        // Log.d("Trip", "Departure time for outbound is " + j + " :::: " + i + ":::: " + Integer.parseInt(originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) + " ::::: " + originalRegularOutboundFlights.get(i).getSegments().get(0).getLegs().get(0).getAirlineName());
                        if (slot.equals("zero") && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) >= 0 && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) <= 700) {
                            tempFlights.add(flightsAtIndex.get(i));
                        } else if (slot.equals("one") && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) >= 700 && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) <= 1100) {
                            tempFlights.add(flightsAtIndex.get(i));
                        } else if (slot.equals("two") && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) >= 1100 && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) <= 1800) {
                            tempFlights.add(flightsAtIndex.get(i));
                        } else if (slot.equals("three") && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) >= 1800 && Integer.parseInt(flightsAtIndex.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) <= 2359) {
                            tempFlights.add(flightsAtIndex.get(i));
                        }


                    }

                }

                if (tempFlights.size() > 0) {
                    ArrayList<FlightData> tmp = new ArrayList<>();
                    tmp.addAll(tempFlights);
                    originalMapSpecialFlights.put(keysList.get(j), tmp);
                } else {
                    if (depListSizeInbound > 0)
                        originalMapSpecialFlights.remove(keysList.get(j));
                }

            }


        }


        //Filtering for Departure Time only for Inbound
        //departure filtering
        tempFlights.clear();
        depListSizeInbound = arrivalSlotList.size();
        size = originalRegularInboundFlights.size();

        for (int j = 0; j < depListSizeInbound; j++) {
            String slot = arrivalSlotList.get(j);

            for (int i = 0; i < size; i++) {
                //  Log.d("Trip", "Departure time for inbound is " + j + " :::: " + i + ":::: " + Integer.parseInt(originalRegularInboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) + " ::::: " + originalRegularInboundFlights.get(i).getSegments().get(0).getLegs().get(0).getAirlineName());
                if (slot.equals("zero") && Integer.parseInt(originalRegularInboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 0 && Integer.parseInt(originalRegularInboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 700) {
                    tempFlights.add(originalRegularInboundFlights.get(i));
                } else if (slot.equals("one") && Integer.parseInt(originalRegularInboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 700 && Integer.parseInt(originalRegularInboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 1100) {
                    tempFlights.add(originalRegularInboundFlights.get(i));
                } else if (slot.equals("two") && Integer.parseInt(originalRegularInboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 1100 && Integer.parseInt(originalRegularInboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 1800) {
                    tempFlights.add(originalRegularInboundFlights.get(i));
                } else if (slot.equals("three") && Integer.parseInt(originalRegularInboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 1800 && Integer.parseInt(originalRegularInboundFlights.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 2359) {
                    tempFlights.add(originalRegularInboundFlights.get(i));
                }


            }
        }


        if (depListSizeInbound > 0) {
            originalRegularInboundFlights.clear();
            originalRegularInboundFlights.addAll(tempFlights);
        }


        //filter for airline names Outbound
        ArrayList<String> checkedAirlineNames = adapter.getCheckedAirlineNames();
        int chekSize = checkedAirlineNames.size();
        tempFlights.clear();
        size = originalRegularOutboundFlights.size();

        for (int i = 0; i < chekSize; i++) {
            String airlineName = checkedAirlineNames.get(i);

            //;
            for (int j = 0; j < size; j++) {
                // Log.d("Trip", "airline name is " + i + " ::::: " + airlineName + ":::: " + j + " :::: " + originalRegularOutboundFlights.get(j).getSegments().get(0).getLegs().get(0).getAirlineName());
                if (airlineName.equalsIgnoreCase(originalRegularOutboundFlights.get(j).getSegments().get(0).getLegs().get(0).getAirline())) {
                    //Log.d("Trip", "iinside for to add airline name is " + i + " ::::: " + airlineName + ":::: " + originalRegularOutboundFlights.get(j).getSegments().get(0).getLegs().get(0).getAirlineName());
                    tempFlights.add(originalRegularOutboundFlights.get(j));
                }
            }

        }

        if (chekSize > 0) {
            originalRegularOutboundFlights.clear();
            originalRegularOutboundFlights.addAll(tempFlights);
        }

        //filter for airLinenames originalMap

        for (int k = 0; k < chekSize; k++) {
            String airlineName = checkedAirlineNames.get(k);
            keysList = new ArrayList<>(originalMapSpecialFlights.keySet());
            len = keysList.size();
            for (int i = 0; i < len; i++) {
                ArrayList<FlightData> flightsAtIndex = originalMapSpecialFlights.get(keysList.get(i));
                if (flightsAtIndex != null) {
                    size = flightsAtIndex.size();
                    Log.d("Trip", "filghtlist is not null " + i + " :: " + keysList.get(i) + " ::: " + k);
                } else {
                    Log.d("Trip", "filghtlist is null " + i + " :: " + keysList.get(i));
                    size = 0;
                }


                tempFlights.clear();
                for (int j = 0; j < size; j++) {
                    if (airlineName.equalsIgnoreCase(flightsAtIndex.get(j).getSegments().get(0).getLegs().get(0).getAirline())) {
                        //Log.d("Trip", "iinside for to add airline name is " + i + " ::::: " + airlineName + ":::: " + originalRegularOutboundFlights.get(j).getSegments().get(0).getLegs().get(0).getAirlineName());
                        tempFlights.add(flightsAtIndex.get(j));
                    }
                }

                if (tempFlights.size() > 0) {
                    ArrayList<FlightData> tmp = new ArrayList<>();
                    tmp.addAll(tempFlights);
                    originalMapSpecialFlights.put(keysList.get(i), tmp);
                } else {
                    if (chekSize > 0)
                        originalMapSpecialFlights.remove(keysList.get(i));
                }


            }

        }

        ////////////////////////
        keysList = new ArrayList<>(originalMapSpecialFlights.keySet());
        len = keysList.size();
        Log.d("Trip", "size of keylist is " + len);
        for (int i = 0; i < len; i++) {
            String key = keysList.get(i);
            String sub;
            if (key.contains("INBOUND")) {
                sub = key.substring(0, 2);
                String newKey = sub + "OUTBOUND";
                if (originalMapSpecialFlights.get(newKey) == null) {
                    originalMapSpecialFlights.remove(key);
                }

            } else if (key.contains("OUTBOUND")) {
                sub = key.substring(0, 2);
                String newKey = sub + "INBOUND";
                if (originalMapSpecialFlights.get(newKey) == null) {
                    originalMapSpecialFlights.remove(key);
                }

            }
            // Log.d("Trip","for "+i+" ::: key is "+keysList.get(i)+" ::::: "+originalMapSpecialFlights.get(keysList.get(i)).size());
        }

        //
        //filter for airline names Inbound

        tempFlights.clear();
        size = originalRegularInboundFlights.size();

        for (int i = 0; i < chekSize; i++) {
            String airlineName = checkedAirlineNames.get(i);


            for (int j = 0; j < size; j++) {

                if (airlineName.equalsIgnoreCase(originalRegularInboundFlights.get(j).getSegments().get(0).getLegs().get(0).getAirline())) {

                    tempFlights.add(originalRegularInboundFlights.get(j));
                }
            }

        }
        if (chekSize > 0) {
            originalRegularInboundFlights.clear();
            originalRegularInboundFlights.addAll(tempFlights);
        }

        //do finish
        twoWayOb.getHeaderAdapter().setToShowRegularOutboundFlights(originalRegularOutboundFlights);
        twoWayOb.getHeaderAdapter().setToShowRegularInboundFlights(originalRegularInboundFlights);
        twoWayOb.getHeaderAdapter().setToShowMapSpecialFlights(originalMapSpecialFlights);
        twoWayOb.getHeaderAdapter().resetData();
        Intent intent = new Intent();
        intent.putExtra("isTwoWayInternational", false);
        setResult(RESULT_OK, intent);
        finish();
    }


    private void filterIntRoundTrip() {
        List<FlightData> filteredFlights = new ArrayList<>();

        //multi airlines filtering
        int size = twoWayIntFlightList.size();
        //  Log.d("Trip", "inside openFilteredlist**1 " + size);
        if (checkBox_hide_multiairlines.isChecked()) {
            filteredFlights.clear();
            for (int j = 0; j < size; j++) {
                if (!twoWayIntFlightList.get(j).isMultiCarrier()) {
                    filteredFlights.add(twoWayIntFlightList.get(j));
                }
            }
            twoWayIntFlightList.clear();
            twoWayIntFlightList.addAll(filteredFlights);

        }
        //no of stops filtering

        filteredFlights.clear();
        size = twoWayIntFlightList.size();
        // Log.d("Trip", "inside openFilteredlist**2 " + size);
        int stopListSize = stopList.size();

        for (int i = 0; i < stopListSize; i++) {
            String noOfStops = stopList.get(i);

            for (int j = 0; j < size; j++) {
                if (noOfStops.equals("zero") && twoWayIntFlightList.get(j).getSegments().get(0).getNoOfStops() == 0 && twoWayIntFlightList.get(j).getSegments().get(1).getNoOfStops() == 0) {
                    filteredFlights.add(twoWayIntFlightList.get(j));
                } else if (noOfStops.equals("one") && twoWayIntFlightList.get(j).getSegments().get(0).getNoOfStops() == 1 && twoWayIntFlightList.get(j).getSegments().get(1).getNoOfStops() == 1) {
                    filteredFlights.add(twoWayIntFlightList.get(j));
                } else if (noOfStops.equals("two") && twoWayIntFlightList.get(j).getSegments().get(0).getNoOfStops() == 2 && twoWayIntFlightList.get(j).getSegments().get(1).getNoOfStops() == 2) {
                    filteredFlights.add(twoWayIntFlightList.get(j));
                } else if (noOfStops.equals("three") && twoWayIntFlightList.get(j).getSegments().get(0).getNoOfStops() >= 3 && twoWayIntFlightList.get(j).getSegments().get(1).getNoOfStops() >= 3) {
                    filteredFlights.add(twoWayIntFlightList.get(j));
                }


            }

        }

        if (stopListSize > 0) {
            twoWayIntFlightList.clear();
            twoWayIntFlightList.addAll(filteredFlights);
        }

        //departure filtering outbound
        filteredFlights.clear();
        ;
        int depListSize = departureSlotList.size();
        size = twoWayIntFlightList.size();
        // Log.d("Trip", "inside openFilteredlist**3 " + size);


        for (int j = 0; j < depListSize; j++) {
            String slot = departureSlotList.get(j);

            for (int i = 0; i < size; i++) {
                // Log.d("Trip", "Departure time is " + j + " :::: " + i + ":::: " + Integer.parseInt(inbounds.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) + " ::::: " + inbounds.get(i).getSegments().get(0).getLegs().get(0).getAirlineName());
                if (slot.equals("zero") && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 0 && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 700) {
                    filteredFlights.add(twoWayIntFlightList.get(i));
                } else if (slot.equals("one") && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 700 && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 1100) {
                    filteredFlights.add(twoWayIntFlightList.get(i));
                } else if (slot.equals("two") && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 1100 && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 1800) {
                    filteredFlights.add(twoWayIntFlightList.get(i));
                } else if (slot.equals("three") && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) >= 1800 && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) <= 2359) {
                    filteredFlights.add(twoWayIntFlightList.get(i));
                }


            }
        }


        if (depListSize > 0) {
            twoWayIntFlightList.clear();
            twoWayIntFlightList.addAll(filteredFlights);
        }


        //departure filtering inbound
        filteredFlights.clear();
        ;
        int arrListSize = arrivalSlotList.size();
        size = twoWayIntFlightList.size();
        // Log.d("Trip", "inside openFilteredlist**3 " + size);


        for (int j = 0; j < arrListSize; j++) {
            String slot = arrivalSlotList.get(j);

            for (int i = 0; i < size; i++) {
                // Log.d("Trip", "Departure time is " + j + " :::: " + i + ":::: " + Integer.parseInt(inbounds.get(i).getSegments().get(0).getLegs().get(0).getDepartureTime()) + " ::::: " + inbounds.get(i).getSegments().get(0).getLegs().get(0).getAirlineName());
                if (slot.equals("zero") && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) >= 0 && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) <= 700) {
                    filteredFlights.add(twoWayIntFlightList.get(i));
                } else if (slot.equals("one") && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) >= 700 && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) <= 1100) {
                    filteredFlights.add(twoWayIntFlightList.get(i));
                } else if (slot.equals("two") && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) >= 1100 && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) <= 1800) {
                    filteredFlights.add(twoWayIntFlightList.get(i));
                } else if (slot.equals("three") && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) >= 1800 && Integer.parseInt(twoWayIntFlightList.get(i).getSegments().get(1).getLegs().get(0).getDepartureTime()) <= 2359) {
                    filteredFlights.add(twoWayIntFlightList.get(i));
                }


            }
        }


        if (arrListSize > 0) {
            twoWayIntFlightList.clear();
            twoWayIntFlightList.addAll(filteredFlights);
        }

        //filter for airline names
        ArrayList<String> checkedAirlineNames = adapter.getCheckedAirlineNames();
        int chekSize = checkedAirlineNames.size();
        filteredFlights.clear();
        size = twoWayIntFlightList.size();
        Log.d("Trip", "check size is " + chekSize);
        for (int i = 0; i < chekSize; i++) {
            String airlineName = checkedAirlineNames.get(i);

            //;
            for (int j = 0; j < size; j++) {
                //Log.d("Trip", "airline name is " + i + " ::::: " + airlineName + ":::: " + j + " :::: " + inbounds.get(j).getSegments().get(0).getLegs().get(0).getAirlineName());
                if (airlineName.equalsIgnoreCase(twoWayIntFlightList.get(j).getSegments().get(0).getLegs().get(0).getAirline())) {
                    // Log.d("Trip", "iinside for to add airline name is " + i + " ::::: " + airlineName + ":::: " + inbounds.get(j).getSegments().get(0).getLegs().get(0).getAirlineName());
                    filteredFlights.add(twoWayIntFlightList.get(j));
                }
            }

        }

        if (chekSize > 0) {
            twoWayIntFlightList.clear();
            twoWayIntFlightList.addAll(filteredFlights);
        }

        //do finish
        twoWayIntOb.getRoundTripSpecialAdapter().setFlightDataList(twoWayIntFlightList);
        Intent intent = new Intent();
        intent.putExtra("isTwoWayInternational", true);
        setResult(RESULT_OK, intent);
        finish();

    }

    private void resetBackGroundColor(LinearLayout layout, int color) {
        //getResources().getColor(R.color.white)
        if (layout != null)
            layout.setBackgroundColor(color);
    }

    private void changeBackgroundColor(LinearLayout layout) {
        try {
            ColorDrawable viewColor = (ColorDrawable) layout.getBackground();
            int colorId = viewColor.getColor();
            if (colorId == Color.parseColor("#ffffff")) {
                layout.setBackgroundColor(getResources().getColor(R.color.filter_screen_selection_color));
            } else {
                layout.setBackgroundColor(getResources().getColor(R.color.white));
            }
        } catch (Exception e) {
            Log.d("Trip", "Eror in changebackground " + e);
        }
    }

    public boolean checkForApplyButtion() {
        // Log.d("Trip","inside checkForApplyButtion");
        boolean apply = false;
        try {
            if (checkBox_hide_multiairlines.isChecked()) {
                apply = true;
                return apply;
            }

            if (adapter.getCheckedAirlineNames() != null && adapter.getCheckedAirlineNames().size() > 0) {
                apply = true;
                return apply;
            }

            int len = layoutAr.length;
            for (int i = 0; i < len; i++) {
                if (layoutAr[i] != null) {

                    ColorDrawable viewColor = (ColorDrawable) layoutAr[i].getBackground();
                    int colorId = viewColor.getColor();
                    if (colorId == Color.parseColor("#989898")) {
                        apply = true;
                        break;
                    }
                }
            }


        } catch (Exception e) {
            Log.d("Trip", "Eror in checkForApplyButton" + e);
        }
        return apply;
    }
}
