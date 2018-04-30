package com.rezofy.views.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.adapters.RoundTripSpecialAdapter;
import com.rezofy.adapters.TwoWayDomesticFlightsSearchAdapter;
import com.rezofy.adapters.TwoWayDomesticFlightsSearchHeaderAdapter;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.DividerItemDecoration;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;

public class FlightsSearchDomesticTwowayActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rcViewOutbound;
    private RecyclerView rcViewInbound;
    protected RecyclerView rcViewHeader;
    protected RecyclerView rcViewGDS;
    private TwoWayDomesticFlightsSearchAdapter outboundAdapter, inboundAdapter;
    protected RoundTripSpecialAdapter roundTripSpecialAdapter;

    private TwoWayDomesticFlightsSearchHeaderAdapter headerAdapter;

    public TwoWayDomesticFlightsSearchHeaderAdapter getHeaderAdapter() {
        return headerAdapter;
    }

    private TextView tvBack;
    private TextView tvFirstLoc;
    private TextView tvSecondLoc;
    private TextView tvDatesAndTravellers;
    private TextView tvOriginFirstWay;
    private TextView tvDestFirstWay;
    private TextView tvOriginSecondWay;
    private TextView tvDestSecondWay;
    protected TextView tvCustomerFare;
    private View viewFareDivider;
    protected TextView tvBusinessFare;
    private TextView tvBook;
    private TextView tvTimeFirstWay;
    private TextView tvPriceFirstWay;
    private TextView tvTimeSecondWay;
    private TextView tvPriceSecondWay;
    protected LinearLayout llGDS, llNonGDS;
    private RelativeLayout rlTimeFirstWay, rlPriceFirstWay, rlTimeSecondWay, rlPriceSecondWay, relativeLayoutDepartureGDS, relativeLayoutDurationGDS, relativeLayoutPriceGDS, rlRoot;
    private IconTextView iTVTripIcon, iTVSortIconFirstWayTime, iTVSortIconFirstWayPrice, iTVSortIconSecondWayTime, iTVSortIconSecondWayPrice;
    private TextView tvDepartureGDS, tvDurationGDS, tvPriceGDS;
    private IconTextView itvDepartureGDS, itvDurationGDS, itvPriceGDS;
    private String originCityCode;
    private String destCityCode;
    private int sortTypeOutbound, sortMannerOutbound, sortTypeInbound, sortMannerInbound, sortTypeGDS, sortMannerGDS;
    private String type = Utils.TYPE_ROUND_TRIP;
    private final int requestCodeForTwoWayFilter = 2;
    private static FlightsSearchDomesticTwowayActivity twoWayOb;
    private FloatingActionButton floatingActionButton;
    private ImageView btnFloating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights_domestic_twoway_search);
        twoWayOb = this;
        init();
        setProperties();
        setOutboundSortViews();
        setInboundSortViews();
        setSortViewsGDS();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (roundTripSpecialAdapter != null && roundTripSpecialAdapter.getFlightDataList() != null && !roundTripSpecialAdapter.getFlightDataList().isEmpty()) {
            roundTripSpecialAdapter.notifyDataSetChanged();
            manageTvFares();
        }

        if (inboundAdapter != null && inboundAdapter.getFlightDataList() != null && !inboundAdapter.getFlightDataList().isEmpty() && outboundAdapter != null && outboundAdapter.getFlightDataList() != null && !outboundAdapter.getFlightDataList().isEmpty()) {
            inboundAdapter.notifyDataSetChanged();
            outboundAdapter.notifyDataSetChanged();
            manageTvFares();
        }
    }

    protected void setAdapters(String data) {
        roundTripSpecialAdapter = new RoundTripSpecialAdapter(this);
        rcViewGDS.setAdapter(roundTripSpecialAdapter);

        inboundAdapter = new TwoWayDomesticFlightsSearchAdapter(this, Utils.FLIGHT_DIRECTION_INBOUND);
        rcViewInbound.setAdapter(inboundAdapter);

        outboundAdapter = new TwoWayDomesticFlightsSearchAdapter(this, Utils.FLIGHT_DIRECTION_OUTBOUND);
        rcViewOutbound.setAdapter(outboundAdapter);

        if (data != null) {
            headerAdapter = new TwoWayDomesticFlightsSearchHeaderAdapter(this, data, outboundAdapter, inboundAdapter, roundTripSpecialAdapter);
            rcViewHeader.setAdapter(headerAdapter);
        }
    }

    public static FlightsSearchDomesticTwowayActivity getInstance() {
        return twoWayOb;
    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(this);
        findViewById(R.id.content_main).findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(this));
        floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(UIUtils.getThemeColor(this)));
        floatingActionButton.setColorFilter(themeContrastColor);
        tvBack.setTextColor(themeContrastColor);
        tvFirstLoc.setTextColor(themeContrastColor);
        iTVTripIcon.setTextColor(themeContrastColor);
        tvSecondLoc.setTextColor(themeContrastColor);
        tvDatesAndTravellers.setTextColor(themeContrastColor);
        tvCustomerFare.setTextColor(themeContrastColor);
        viewFareDivider.setBackgroundColor(themeContrastColor);
        tvBusinessFare.setTextColor(themeContrastColor);
        UIUtils.setRoundedButtonProperties(tvBook);
    }

    private void init() {
        String data = Utils.getFromLargeDataFile(this, Utils.LARGE_DATA_FILE_NAME);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        rlRoot = (RelativeLayout) findViewById(R.id.root);
        rcViewGDS = (RecyclerView) findViewById(R.id.rcv_gds);
        rcViewOutbound = (RecyclerView) findViewById(R.id.rcView1);
        rcViewInbound = (RecyclerView) findViewById(R.id.rcView2);
        rcViewHeader = (RecyclerView) findViewById(R.id.rcView_horizontal);
        rcViewHeader.setBackgroundColor(UIUtils.getThemeLightColor(this));
        tvBack = (TextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.back);
        tvBack.setOnClickListener(this);
        tvBook = (TextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.book);
        tvBook.setOnClickListener(this);
        tvCustomerFare = (TextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.customer_fare);
        viewFareDivider = findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.fare_divider);
        tvBusinessFare = (TextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.business_fare);
        tvFirstLoc = (TextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.first_loc);
        tvFirstLoc.setText(getIntent().getStringExtra("originCityName"));
        iTVTripIcon = (IconTextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.trip_icon);
        iTVTripIcon.setText(getString(R.string.icon_text_G));
        originCityCode = getIntent().getStringExtra("originCityCode");
        destCityCode = getIntent().getStringExtra("destCityCode");
        tvOriginFirstWay = (TextView) findViewById(R.id.content_main).findViewById(R.id.origin_first_way);
        tvDestFirstWay = (TextView) findViewById(R.id.content_main).findViewById(R.id.dest_first_way);
        tvOriginSecondWay = (TextView) findViewById(R.id.content_main).findViewById(R.id.origin_second_way);
        tvDestSecondWay = (TextView) findViewById(R.id.content_main).findViewById(R.id.dest_second_way);
        tvOriginFirstWay.setText(originCityCode);
        tvDestFirstWay.setText(destCityCode);
        tvOriginSecondWay.setText(destCityCode);
        tvDestSecondWay.setText(originCityCode);
        tvSecondLoc = (TextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.second_loc);
        tvSecondLoc.setText(getIntent().getStringExtra("destCityName"));

        tvDatesAndTravellers = (TextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.dates_and_travellers);
        tvDatesAndTravellers.setText(Utils.changeDateFormat(getIntent().getStringExtra("departDate"), Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_d_space_LLL)
                + " - " + Utils.changeDateFormat(getIntent().getStringExtra("retDate"), Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_d_space_LLL)
                + " | " + UIUtils.getTravellersText(this, getIntent().getIntExtra("noOfAdults", 0), getIntent().getIntExtra("noOfChildren", 0), getIntent().getIntExtra("noOfInfants", 0)));
        tvDatesAndTravellers.setSelected(true);

        rlPriceFirstWay = (RelativeLayout) findViewById(R.id.content_main).findViewById(R.id.price_first_way_layout);
        rlTimeFirstWay = (RelativeLayout) findViewById(R.id.content_main).findViewById(R.id.time_first_way_layout);
        rlPriceSecondWay = (RelativeLayout) findViewById(R.id.content_main).findViewById(R.id.price_second_way_layout);
        rlTimeSecondWay = (RelativeLayout) findViewById(R.id.content_main).findViewById(R.id.time_second_way_layout);
        rlPriceFirstWay.setOnClickListener(this);
        rlTimeFirstWay.setOnClickListener(this);
        rlPriceSecondWay.setOnClickListener(this);
        rlTimeSecondWay.setOnClickListener(this);

        tvTimeFirstWay = (TextView) findViewById(R.id.content_main).findViewById(R.id.time_first_way);
        tvPriceFirstWay = (TextView) findViewById(R.id.content_main).findViewById(R.id.price_first_way);
        tvTimeSecondWay = (TextView) findViewById(R.id.content_main).findViewById(R.id.time_second_way);
        tvPriceSecondWay = (TextView) findViewById(R.id.content_main).findViewById(R.id.price_second_way);

        iTVSortIconFirstWayTime = (IconTextView) findViewById(R.id.content_main).findViewById(R.id.sort_icon_first_way_time);
        iTVSortIconFirstWayPrice = (IconTextView) findViewById(R.id.content_main).findViewById(R.id.sort_icon_first_way_price);
        iTVSortIconSecondWayTime = (IconTextView) findViewById(R.id.content_main).findViewById(R.id.sort_icon_second_way_time);
        iTVSortIconSecondWayPrice = (IconTextView) findViewById(R.id.content_main).findViewById(R.id.sort_icon_second_way_price);

        relativeLayoutDepartureGDS = (RelativeLayout) findViewById(R.id.content_main).findViewById(R.id.rl_departure);
        relativeLayoutDurationGDS = (RelativeLayout) findViewById(R.id.content_main).findViewById(R.id.rl_duration);
        relativeLayoutPriceGDS = (RelativeLayout) findViewById(R.id.content_main).findViewById(R.id.rl_price);

        relativeLayoutDepartureGDS.setOnClickListener(this);
        relativeLayoutDurationGDS.setOnClickListener(this);
        relativeLayoutPriceGDS.setOnClickListener(this);
        tvDepartureGDS = (TextView) findViewById(R.id.content_main).findViewById(R.id.departure_way_text);
        tvDurationGDS = (TextView) findViewById(R.id.content_main).findViewById(R.id.tv_duration);
        tvPriceGDS = (TextView) findViewById(R.id.content_main).findViewById(R.id.price_way);

        itvDepartureGDS = (IconTextView) findViewById(R.id.content_main).findViewById(R.id.icon_departure_way);
        itvDurationGDS = (IconTextView) findViewById(R.id.content_main).findViewById(R.id.icon_duration_way);
        itvPriceGDS = (IconTextView) findViewById(R.id.content_main).findViewById(R.id.icon_price_way);

        sortTypeOutbound = Utils.SORT_PRICE;
        sortMannerOutbound = Utils.SORT_INCREMENTAL;
        sortTypeInbound = Utils.SORT_PRICE;
        sortMannerInbound = Utils.SORT_INCREMENTAL;
        sortTypeGDS = Utils.SORT_PRICE;
        sortMannerGDS = Utils.SORT_INCREMENTAL;

        llGDS = (LinearLayout) findViewById(R.id.gds_layout);
        llNonGDS = (LinearLayout) findViewById(R.id.non_gds_layout);


        rcViewGDS.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcViewGDS.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));


        rcViewInbound.setLayoutManager(new LinearLayoutManager(this));
        rcViewInbound.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rcViewInbound.setHasFixedSize(true);

        rcViewOutbound.setLayoutManager(new LinearLayoutManager(this));
        rcViewOutbound.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rcViewOutbound.setHasFixedSize(true);

        rcViewHeader.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcViewHeader.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST));

        setAdapters(data);

        btnFloating = (ImageView) findViewById(R.id.btn_flaoting);
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

    protected void manageTvBusinessFare() {
        if (AppPreferences.getInstance(this).getB2B())
            ((LinearLayout) tvBusinessFare.getParent()).setVisibility(View.VISIBLE);
        else
            ((LinearLayout) tvBusinessFare.getParent()).setVisibility(View.GONE);
    }

    public void manageTvFares() {
        String customerFareToDisplay, businessFareToDisplay;
        manageTvBusinessFare();
        if (headerAdapter.getHeaderDataList().get(headerAdapter.getSelectedCategoryPosition()).getFareResultsType().equals(Utils.FARE_RESULTS_TYPE_REGULAR)
                || (headerAdapter.getHeaderDataList().get(headerAdapter.getSelectedCategoryPosition()).getFareResultsType().equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && !headerAdapter.getHeaderDataList().get(headerAdapter.getSelectedCategoryPosition()).getTab().equals(Utils.TAB_GDS))) {
            businessFareToDisplay = UIUtils.getFareToDisplay(this, (float) Math.ceil(Float.parseFloat(outboundAdapter.getFlightDataList().get(outboundAdapter.getSelectedFlightPosition()).getFare().getTotal().getPrice().getAmount()))
                    + (float) Math.ceil(Float.parseFloat(inboundAdapter.getFlightDataList().get(inboundAdapter.getSelectedFlightPosition()).getFare().getTotal().getPrice().getAmount())));
            customerFareToDisplay = UIUtils.getFareToDisplay(this, (float) Math.ceil(outboundAdapter.getFlightDataList().get(outboundAdapter.getSelectedFlightPosition()).getFare().getSellingPrice())
                    + (float) Math.ceil(inboundAdapter.getFlightDataList().get(inboundAdapter.getSelectedFlightPosition()).getFare().getSellingPrice()));

        } else {
            businessFareToDisplay = UIUtils.getFareToDisplay(this, roundTripSpecialAdapter.getFlightDataList().get(roundTripSpecialAdapter.getSelectedFlightPosition()).getFare().getTotal().getPrice().getAmount());
            customerFareToDisplay = UIUtils.getFareToDisplay(this, roundTripSpecialAdapter.getFlightDataList().get(roundTripSpecialAdapter.getSelectedFlightPosition()).getFare().getSellingPrice());
        }
        tvBusinessFare.setText(businessFareToDisplay);
        tvCustomerFare.setText(customerFareToDisplay);
    }

    private void setOutboundSortViews() {
        tvPriceFirstWay.setTextColor(UIUtils.getThemeColor(this));
        tvTimeFirstWay.setTextColor(UIUtils.getThemeColor(this));

        iTVSortIconFirstWayPrice.setVisibility(View.GONE);
        iTVSortIconFirstWayTime.setVisibility(View.GONE);


        if (sortTypeOutbound == Utils.SORT_PRICE && sortMannerOutbound == Utils.SORT_INCREMENTAL) {
            tvPriceFirstWay.setTextColor(getResources().getColor(R.color.black));
            iTVSortIconFirstWayPrice.setText(getString(R.string.icon_text_t));
            iTVSortIconFirstWayPrice.setVisibility(View.VISIBLE);

        } else if (sortTypeOutbound == Utils.SORT_PRICE && sortMannerOutbound == Utils.SORT_DECREMENTAL) {
            tvPriceFirstWay.setTextColor(getResources().getColor(R.color.black));
            iTVSortIconFirstWayPrice.setText(getString(R.string.icon_text_q));
            iTVSortIconFirstWayPrice.setVisibility(View.VISIBLE);

        } else if (sortTypeOutbound == Utils.SORT_TIME && sortMannerOutbound == Utils.SORT_INCREMENTAL) {
            tvTimeFirstWay.setTextColor(getResources().getColor(R.color.black));
            iTVSortIconFirstWayTime.setText(getString(R.string.icon_text_t));
            iTVSortIconFirstWayTime.setVisibility(View.VISIBLE);

        } else {
            tvTimeFirstWay.setTextColor(getResources().getColor(R.color.black));
            iTVSortIconFirstWayTime.setText(getString(R.string.icon_text_q));
            iTVSortIconFirstWayTime.setVisibility(View.VISIBLE);

        }
    }

    private void setInboundSortViews() {
        tvPriceSecondWay.setTextColor(UIUtils.getThemeColor(this));
        tvTimeSecondWay.setTextColor(UIUtils.getThemeColor(this));
        iTVSortIconSecondWayPrice.setVisibility(View.GONE);
        iTVSortIconSecondWayTime.setVisibility(View.GONE);

        if (sortTypeInbound == Utils.SORT_PRICE && sortMannerInbound == Utils.SORT_INCREMENTAL) {
            tvPriceSecondWay.setTextColor(getResources().getColor(R.color.black));
            iTVSortIconSecondWayPrice.setText(getString(R.string.icon_text_t));
            iTVSortIconSecondWayPrice.setVisibility(View.VISIBLE);

        } else if (sortTypeInbound == Utils.SORT_PRICE && sortMannerInbound == Utils.SORT_DECREMENTAL) {
            tvPriceSecondWay.setTextColor(getResources().getColor(R.color.black));
            iTVSortIconSecondWayPrice.setText(getString(R.string.icon_text_q));
            iTVSortIconSecondWayPrice.setVisibility(View.VISIBLE);

        } else if (sortTypeInbound == Utils.SORT_TIME && sortMannerInbound == Utils.SORT_INCREMENTAL) {
            tvTimeSecondWay.setTextColor(getResources().getColor(R.color.black));
            iTVSortIconSecondWayTime.setText(getString(R.string.icon_text_t));
            iTVSortIconSecondWayTime.setVisibility(View.VISIBLE);

        } else {
            tvTimeSecondWay.setTextColor(getResources().getColor(R.color.black));
            iTVSortIconSecondWayTime.setText(getString(R.string.icon_text_q));
            iTVSortIconSecondWayTime.setVisibility(View.VISIBLE);

        }
    }

    private void setSortViewsGDS() {
        tvDepartureGDS.setTextColor(UIUtils.getThemeColor(this));
        tvDurationGDS.setTextColor(UIUtils.getThemeColor(this));
        tvPriceGDS.setTextColor(UIUtils.getThemeColor(this));
        itvDepartureGDS.setVisibility(View.GONE);
        itvDurationGDS.setVisibility(View.GONE);
        itvPriceGDS.setVisibility(View.GONE);

        if (sortTypeGDS == Utils.SORT_PRICE && sortMannerGDS == Utils.SORT_INCREMENTAL) {
            tvPriceGDS.setTextColor(getResources().getColor(R.color.black));
            itvPriceGDS.setText(getString(R.string.icon_text_t));
            itvPriceGDS.setVisibility(View.VISIBLE);

        } else if (sortTypeGDS == Utils.SORT_PRICE && sortMannerGDS == Utils.SORT_DECREMENTAL) {
            tvPriceGDS.setTextColor(getResources().getColor(R.color.black));
            itvPriceGDS.setText(getString(R.string.icon_text_q));
            itvPriceGDS.setVisibility(View.VISIBLE);

        } else if (sortTypeGDS == Utils.SORT_TIME && sortMannerGDS == Utils.SORT_INCREMENTAL) {
            tvDepartureGDS.setTextColor(getResources().getColor(R.color.black));
            itvDepartureGDS.setText(getString(R.string.icon_text_t));
            itvDepartureGDS.setVisibility(View.VISIBLE);

        } else if (sortTypeGDS == Utils.SORT_TIME && sortMannerGDS == Utils.SORT_DECREMENTAL) {
            tvDepartureGDS.setTextColor(getResources().getColor(R.color.black));
            itvDepartureGDS.setText(getString(R.string.icon_text_q));
            itvDepartureGDS.setVisibility(View.VISIBLE);

        } else if (sortTypeGDS == Utils.SORT_DURATION && sortMannerGDS == Utils.SORT_INCREMENTAL) {
            tvDurationGDS.setTextColor(getResources().getColor(R.color.black));
            itvDurationGDS.setText(getString(R.string.icon_text_t));
            itvDurationGDS.setVisibility(View.VISIBLE);

        } else {
            tvDurationGDS.setTextColor(getResources().getColor(R.color.black));
            itvDurationGDS.setText(getString(R.string.icon_text_q));
            itvDurationGDS.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.book:
                onBookClick();
                break;

            case R.id.price_first_way_layout:
                if (sortTypeOutbound == Utils.SORT_PRICE) {
                    if (sortMannerOutbound == Utils.SORT_INCREMENTAL)
                        sortMannerOutbound = Utils.SORT_DECREMENTAL;
                    else
                        sortMannerOutbound = Utils.SORT_INCREMENTAL;
                } else {
                    sortTypeOutbound = Utils.SORT_PRICE;
                    sortMannerOutbound = Utils.SORT_INCREMENTAL;
                }
                setOutboundSortViews();
                outboundAdapter.setSelectedFlightPositionToDefault();
                outboundAdapter.createSortedFlightDataList();
                manageTvFares();
                outboundAdapter.notifyDataSetChanged();
                rcViewOutbound.smoothScrollToPosition(outboundAdapter.getSelectedFlightPosition());
                break;

            case R.id.time_first_way_layout:
                if (sortTypeOutbound == Utils.SORT_TIME) {
                    if (sortMannerOutbound == Utils.SORT_INCREMENTAL)
                        sortMannerOutbound = Utils.SORT_DECREMENTAL;
                    else
                        sortMannerOutbound = Utils.SORT_INCREMENTAL;
                } else {
                    sortTypeOutbound = Utils.SORT_TIME;
                    sortMannerOutbound = Utils.SORT_INCREMENTAL;
                }
                setOutboundSortViews();
                outboundAdapter.setSelectedFlightPositionToDefault();
                outboundAdapter.createSortedFlightDataList();
                manageTvFares();
                outboundAdapter.notifyDataSetChanged();
                rcViewOutbound.smoothScrollToPosition(outboundAdapter.getSelectedFlightPosition());
                break;

            case R.id.price_second_way_layout:
                if (sortTypeInbound == Utils.SORT_PRICE) {
                    if (sortMannerInbound == Utils.SORT_INCREMENTAL)
                        sortMannerInbound = Utils.SORT_DECREMENTAL;
                    else
                        sortMannerInbound = Utils.SORT_INCREMENTAL;
                } else {
                    sortTypeInbound = Utils.SORT_PRICE;
                    sortMannerInbound = Utils.SORT_INCREMENTAL;
                }
                setInboundSortViews();
                inboundAdapter.setSelectedFlightPositionToDefault();
                inboundAdapter.createSortedFlightDataList();
                manageTvFares();
                inboundAdapter.notifyDataSetChanged();
                rcViewInbound.smoothScrollToPosition(inboundAdapter.getSelectedFlightPosition());
                break;

            case R.id.time_second_way_layout:
                if (sortTypeInbound == Utils.SORT_TIME) {
                    if (sortMannerInbound == Utils.SORT_INCREMENTAL)
                        sortMannerInbound = Utils.SORT_DECREMENTAL;
                    else
                        sortMannerInbound = Utils.SORT_INCREMENTAL;
                } else {
                    sortTypeInbound = Utils.SORT_TIME;
                    sortMannerInbound = Utils.SORT_INCREMENTAL;
                }
                setInboundSortViews();
                inboundAdapter.setSelectedFlightPositionToDefault();
                inboundAdapter.createSortedFlightDataList();
                manageTvFares();
                inboundAdapter.notifyDataSetChanged();
                rcViewInbound.smoothScrollToPosition(inboundAdapter.getSelectedFlightPosition());
                break;

            case R.id.rl_departure:
                if (sortTypeGDS == Utils.SORT_TIME) {
                    if (sortMannerGDS == Utils.SORT_INCREMENTAL)
                        sortMannerGDS = Utils.SORT_DECREMENTAL;
                    else
                        sortMannerGDS = Utils.SORT_INCREMENTAL;
                } else {
                    sortTypeGDS = Utils.SORT_TIME;
                    sortMannerGDS = Utils.SORT_INCREMENTAL;
                }
                setSortViewsGDS();
                roundTripSpecialAdapter.setSelectedFlightPosition(0);
                roundTripSpecialAdapter.createSortedFlightDataList();
                manageTvFares();
                roundTripSpecialAdapter.notifyDataSetChanged();
                rcViewGDS.smoothScrollToPosition(roundTripSpecialAdapter.getSelectedFlightPosition());
                break;

            case R.id.rl_duration:
                if (sortTypeGDS == Utils.SORT_DURATION) {
                    if (sortMannerGDS == Utils.SORT_INCREMENTAL)
                        sortMannerGDS = Utils.SORT_DECREMENTAL;
                    else
                        sortMannerGDS = Utils.SORT_INCREMENTAL;
                } else {
                    sortTypeGDS = Utils.SORT_DURATION;
                    sortMannerGDS = Utils.SORT_INCREMENTAL;
                }
                setSortViewsGDS();
                roundTripSpecialAdapter.setSelectedFlightPosition(0);
                roundTripSpecialAdapter.createSortedFlightDataList();
                manageTvFares();
                roundTripSpecialAdapter.notifyDataSetChanged();
                rcViewGDS.smoothScrollToPosition(roundTripSpecialAdapter.getSelectedFlightPosition());
                break;

            case R.id.rl_price:
                if (sortTypeGDS == Utils.SORT_PRICE) {
                    if (sortMannerGDS == Utils.SORT_INCREMENTAL)
                        sortMannerGDS = Utils.SORT_DECREMENTAL;
                    else
                        sortMannerGDS = Utils.SORT_INCREMENTAL;
                } else {
                    sortTypeGDS = Utils.SORT_PRICE;
                    sortMannerGDS = Utils.SORT_INCREMENTAL;
                }
                setSortViewsGDS();
                roundTripSpecialAdapter.setSelectedFlightPosition(0);
                roundTripSpecialAdapter.createSortedFlightDataList();
                manageTvFares();
                roundTripSpecialAdapter.notifyDataSetChanged();
                rcViewGDS.smoothScrollToPosition(roundTripSpecialAdapter.getSelectedFlightPosition());
                break;
        }
    }

    protected void onBookClick() {
        AppPreferences.getInstance(this).setBillingInfo("");
        Bundle bundle = getIntent().getExtras();
        if (headerAdapter.getHeaderDataList().get(headerAdapter.getSelectedCategoryPosition()).getFareResultsType().equals(Utils.FARE_RESULTS_TYPE_REGULAR)
                || (headerAdapter.getHeaderDataList().get(headerAdapter.getSelectedCategoryPosition()).getFareResultsType().equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && !headerAdapter.getHeaderDataList().get(headerAdapter.getSelectedCategoryPosition()).getTab().equals(Utils.TAB_GDS))) {
            bundle.putSerializable("flightDataFirstWay", outboundAdapter.getFlightDataList().get(outboundAdapter.getSelectedFlightPosition()));
            bundle.putSerializable("flightDataSecondWay", inboundAdapter.getFlightDataList().get(inboundAdapter.getSelectedFlightPosition()));
        } else {
            bundle.putSerializable("flightDataFirstWay", roundTripSpecialAdapter.getFlightDataList().get(roundTripSpecialAdapter.getSelectedFlightPosition()));
            bundle.putSerializable("flightDataSecondWay", roundTripSpecialAdapter.getFlightDataList().get(roundTripSpecialAdapter.getSelectedFlightPosition()));
        }
        bundle.putString("search_id", headerAdapter.getSearchResponse().getSearchId());
        bundle.putBoolean("ticket_enabled", headerAdapter.getSearchResponse().isTicketEnabled());
        bundle.putString("fare_results", headerAdapter.getHeaderDataList().get(headerAdapter.getSelectedCategoryPosition()).getFareResultsType());
        bundle.putString("selected_tab", headerAdapter.getHeaderDataList().get(headerAdapter.getSelectedCategoryPosition()).getTab());
        Intent i = new Intent(this, PassengerActivity.class);
        i.putExtras(bundle);
        startActivity(i);

/*        if (!checkAndHandleGuestUser(bundle)) {
            Intent i = new Intent(this, FlightDetailsActivity.class);
            i.putExtras(bundle);
            startActivity(i);
        }*/
    }

/*    protected boolean checkAndHandleGuestUser(final Bundle bundle) {
        if (AppPreferences.getInstance(this).getUserData().isEmpty()) {
            Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.login_msg), null, getString(R.string.ok), getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent intent = new Intent(FlightsSearchDomesticTwowayActivity.this, LoginActivity.class);
                                    bundle.putBoolean("isFromBook", true);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    break;
                            }
                        }
                    });
            return true;

        } else
            return false;
    }*/

    public TextView getTvCustomerFare() {
        return tvCustomerFare;
    }

    public TextView getTvBusinessFare() {
        return tvBusinessFare;
    }

    public LinearLayout getLlGDS() {
        return llGDS;
    }

    public void setLlGDS(LinearLayout llGDS) {
        this.llGDS = llGDS;
    }

    public LinearLayout getLlNonGDS() {
        return llNonGDS;
    }

    public void setLlNonGDS(LinearLayout llNonGDS) {
        this.llNonGDS = llNonGDS;
    }

    public int getSortTypeOutbound() {
        return sortTypeOutbound;
    }

    public void setSortTypeOutbound(int sortTypeOutbound) {
        this.sortTypeOutbound = sortTypeOutbound;
    }

    public int getSortMannerOutbound() {
        return sortMannerOutbound;
    }

    public void setSortMannerOutbound(int sortMannerOutbound) {
        this.sortMannerOutbound = sortMannerOutbound;
    }

    public int getSortTypeInbound() {
        return sortTypeInbound;
    }

    public void setSortTypeInbound(int sortTypeInbound) {
        this.sortTypeInbound = sortTypeInbound;
    }

    public int getSortMannerInbound() {
        return sortMannerInbound;
    }

    public void setSortMannerInbound(int sortMannerInbound) {
        this.sortMannerInbound = sortMannerInbound;
    }

    public int getSortTypeGDS() {
        return sortTypeGDS;
    }

    public void setSortTypeGDS(int sortTypeGDS) {
        this.sortTypeGDS = sortTypeGDS;
    }

    public int getSortMannerGDS() {
        return sortMannerGDS;
    }

    public void setSortMannerGDS(int sortMannerGDS) {
        this.sortMannerGDS = sortMannerGDS;
    }

    public RelativeLayout getRlRoot() {
        return rlRoot;
    }

    public RecyclerView getRcViewHeader() {
        return rcViewHeader;
    }

    public TextView getTvBook() {
        return tvBook;
    }

    public RecyclerView getRcViewInbound() {
        return rcViewInbound;
    }

    public RecyclerView getRcViewOutbound() {
        return rcViewOutbound;
    }

    public void openFilterScreen(View v) {
        try {

            Intent intent = new Intent(this, FilterActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("destCountryCode", getIntent().getStringExtra("destCountryCode"));
            intent.putExtra("originCountryCode", getIntent().getStringExtra("originCountryCode"));
            startActivityForResult(intent, requestCodeForTwoWayFilter);
            // startActivity(intent);

        } catch (Exception e) {
            Log.d("Trip", "eror in openFilterScreen" + e);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Trip", "inside onActivityResut " + requestCode + " :: " + resultCode + "::::: " + RESULT_OK);
        // check if the request code is same as what is passed  here
        if (requestCode == requestCodeForTwoWayFilter && resultCode == RESULT_OK) {

            if (data.getBooleanExtra("isTwoWayInternational", false)) {
                if (roundTripSpecialAdapter.getFlightDataList().isEmpty()) {
                    findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.fares).setVisibility(View.GONE);
                    findViewById(R.id.filter_layout).setVisibility(View.GONE);
                    tvBook.setVisibility(View.GONE);

                } else {
                    findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.fares).setVisibility(View.VISIBLE);
                    findViewById(R.id.filter_layout).setVisibility(View.VISIBLE);
                    tvBook.setVisibility(View.VISIBLE);
                    roundTripSpecialAdapter.createSortedFlightDataList();
                    manageTvFares();
                }
                roundTripSpecialAdapter.notifyDataSetChanged();

            } else {
                headerAdapter.getInboundAdapter().notifyDataSetChanged();
                headerAdapter.getOutboundAdapter().notifyDataSetChanged();
                headerAdapter.notifyDataSetChanged();
                roundTripSpecialAdapter.notifyDataSetChanged();
                if (!headerAdapter.getHeaderDataList().isEmpty())
                    manageTvFares();
            }

        }
    }
}