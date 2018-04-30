package com.rezofy.views.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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

import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.adapters.OneWayDomesticFlightsSearchAdapter;
import com.rezofy.models.response_models.FlightData;
import com.rezofy.models.response_models.SearchResponse;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;

import java.util.List;

public class FlightsSearchOnewayActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rcViewFlights, rcViewHeader;
    private OneWayDomesticFlightsSearchAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private IconTextView tripIcon;
    private RelativeLayout relativeLayoutDeparture, relativeLayoutDuration, relativeLayoutPrice, rlFilter;
    private TextView tvDeparture, tvDuration, tvPrice;
    private IconTextView itvDeparture, itvDuration, itvPrice;
    private SearchResponse searchResponse;
    private String departDate;
    private View viewFareDivider;
    private String originCityCode, destCityCode, originCityName, destCityName;
    private int noOfAdults;
    private int noOfChildren;
    private int noOfInfants;
    private int sortType, sortManner;
    private TextView tvBack, tvBook, tvFirstLoc, tvSecondLoc, tvDatesAndTravellers, tvCustomerFare, tvBusinessFare;
    private List<FlightData> flightDataList;
    private final int requestCodeForOneWayFilter = 1;
    private String type = Utils.TYPE_ONE_WAY;
    private LinearLayout llFares;
    private FloatingActionButton floatingActionButton;
    private ImageView btnFloating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights_domestic_oneway_search);
        init();
        setProperties();
        setClickListener();
        setSortViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setTvBussinessFare();
        adapter.notifyDataSetChanged();
    }

    public void openFilterScreen(View v) {
        try {

            Intent intent = new Intent(this, FilterActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("destCountryCode", getIntent().getStringExtra("destCountryCode"));
            intent.putExtra("originCountryCode", getIntent().getStringExtra("originCountryCode"));

            startActivityForResult(intent, requestCodeForOneWayFilter);
            // startActivity(intent);

        } catch (Exception e) {
            Log.d("Trip", "eror in openFilterScreen" + e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d("Trip", "inside onActivityResut " + requestCode + " :: " + resultCode + "::::: " + RESULT_OK);
            // check if the request code is same as what is passed  here
            if (requestCode == requestCodeForOneWayFilter && resultCode == RESULT_OK) {
                String filteredData = Utils.getFromLargeDataFile(this, Utils.Filtered_DATA_FILE_NAME);
                Log.d("Trip", "inside  onactivity result is " + filteredData + ":::::");
                if (filteredData != null)
                    searchResponse = new Gson().fromJson(filteredData, SearchResponse.class);
                adapter.flightDataList = searchResponse.getData().getResults();
                 Log.d("Trip","size of flight list is "+adapter.flightDataList.size());
                if (adapter.flightDataList.isEmpty()) {
                    llFares.setVisibility(View.GONE);
                    rlFilter.setVisibility(View.GONE);
                    tvBook.setVisibility(View.GONE);

                } else {
                    llFares.setVisibility(View.VISIBLE);
                    rlFilter.setVisibility(View.VISIBLE);
                    tvBook.setVisibility(View.VISIBLE);
                    adapter.createSortedFlightDataList();
                    adapter.setTvCustomerFare();
                    adapter.setTvBussinessFare();
                    adapter.notifyDataSetChanged();
                }


            }
        }
        catch (Exception e)
        {
            Log.d("Trip","error is "+e);
        }
    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(this);
        findViewById(R.id.content_main).findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(this));
        UIUtils.setRoundedButtonProperties(tvBook);
        floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(UIUtils.getThemeColor(this)));
        floatingActionButton.setColorFilter(themeContrastColor);
        tvBack.setTextColor(themeContrastColor);
        tvFirstLoc.setTextColor(themeContrastColor);
        tripIcon.setTextColor(themeContrastColor);
        tvSecondLoc.setTextColor(themeContrastColor);
        tvDatesAndTravellers.setTextColor(themeContrastColor);
        tvCustomerFare.setTextColor(themeContrastColor);
        viewFareDivider.setBackgroundColor(themeContrastColor);
        tvBusinessFare.setTextColor(themeContrastColor);
    }

    private void init() {
        try {
            String data = Utils.getFromLargeDataFile(this, Utils.LARGE_DATA_FILE_NAME);
            Log.d("Trip", "search data is ****" + data);
            if (data != null) {
                searchResponse = new Gson().fromJson(data, SearchResponse.class);
            }
            try {
                flightDataList = searchResponse.getData().getResults();
                Utils.setSellingPriceInFlightDataList(flightDataList);
                Utils.writeToLargeDataFile(this, new Gson().toJson(searchResponse), Utils.LARGE_DATA_FILE_NAME);
            } catch (Exception e) {

            }
            originCityCode = getIntent().getStringExtra("originCityCode");
            destCityCode = getIntent().getStringExtra("destCityCode");
            originCityName = getIntent().getStringExtra("originCityName");
            destCityName = getIntent().getStringExtra("destCityName");
            departDate = getIntent().getStringExtra("departDate");
            noOfAdults = getIntent().getIntExtra("noOfAdults", 0);
            noOfChildren = getIntent().getIntExtra("noOfChildren", 0);
            noOfInfants = getIntent().getIntExtra("noOfInfants", 0);
            sortType = Utils.SORT_PRICE;
            sortManner = Utils.SORT_INCREMENTAL;

            departDate = getIntent().getStringExtra("departDate");
            originCityCode = getIntent().getStringExtra("originCityCode");
            destCityCode = getIntent().getStringExtra("destCityCode");


            floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
            llFares = (LinearLayout) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.fares);
            rlFilter = (RelativeLayout) findViewById(R.id.content_main).findViewById(R.id.filter_layout);
            tvBack = (TextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.back);
            tvBack.setOnClickListener(this);
            tvBook = (TextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.book);
            tvBook.setOnClickListener(this);
            tvCustomerFare = (TextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.customer_fare);
            viewFareDivider = findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.fare_divider);
            tvBusinessFare = (TextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.business_fare);
            rcViewHeader = (RecyclerView) findViewById(R.id.rcView_horizontal);
            rcViewHeader.setVisibility(View.GONE);
            mLayoutManager = new LinearLayoutManager(this);
            rcViewFlights = (RecyclerView) findViewById(R.id.rcView1);
            rcViewFlights.setHasFixedSize(true);
            rcViewFlights.setLayoutManager(mLayoutManager);
            adapter = new OneWayDomesticFlightsSearchAdapter(flightDataList, this);
            rcViewFlights.setAdapter(adapter);


            //rcViewFlights.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            rcViewFlights.addItemDecoration(new SimpleDividerItemDecoration(this));
            tripIcon = (IconTextView) findViewById(R.id.trip_icon);
            tripIcon.setText("s");

            relativeLayoutDeparture = (RelativeLayout) findViewById(R.id.rl_departure);
            relativeLayoutDuration = (RelativeLayout) findViewById(R.id.rl_duration);
            relativeLayoutPrice = (RelativeLayout) findViewById(R.id.rl_price);

            tvDeparture = (TextView) findViewById(R.id.departure_way_text);
            tvDuration = (TextView) findViewById(R.id.tv_duration);
            tvPrice = (TextView) findViewById(R.id.price_way);

            itvDeparture = (IconTextView) findViewById(R.id.icon_departure_way);
            itvDuration = (IconTextView) findViewById(R.id.icon_duration_way);
            itvPrice = (IconTextView) findViewById(R.id.icon_price_way);
            tvFirstLoc = (TextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.first_loc);
            tvFirstLoc.setText(originCityName);

            tvSecondLoc = (TextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.second_loc);
            tvSecondLoc.setText(destCityName);


            tvDatesAndTravellers = (TextView) findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.dates_and_travellers);
            tvDatesAndTravellers.setText(Utils.changeDateFormat(departDate, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_d_space_LLL)
                    + " | "
                    + UIUtils.getTravellersText(this, noOfAdults, noOfChildren, noOfInfants));
            tvDatesAndTravellers.setSelected(true);
        }
        catch (Exception e)
        {
            Log.d("Trip","eror in init"+e);
        }

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

    private void setSortViews() {
        tvDeparture.setTextColor(UIUtils.getThemeColor(this));
        tvDuration.setTextColor(UIUtils.getThemeColor(this));
        tvPrice.setTextColor(UIUtils.getThemeColor(this));
        itvDeparture.setVisibility(View.GONE);
        itvDuration.setVisibility(View.GONE);
        itvPrice.setVisibility(View.GONE);

        if (sortType == Utils.SORT_PRICE && sortManner == Utils.SORT_INCREMENTAL) {
            tvPrice.setTextColor(getResources().getColor(R.color.black));
            itvPrice.setText(getString(R.string.icon_text_t));
            itvPrice.setVisibility(View.VISIBLE);

        } else if (sortType == Utils.SORT_PRICE && sortManner == Utils.SORT_DECREMENTAL) {
            tvPrice.setTextColor(getResources().getColor(R.color.black));
            itvPrice.setText(getString(R.string.icon_text_q));
            itvPrice.setVisibility(View.VISIBLE);

        } else if (sortType == Utils.SORT_TIME && sortManner == Utils.SORT_INCREMENTAL) {
            tvDeparture.setTextColor(getResources().getColor(R.color.black));
            itvDeparture.setText(getString(R.string.icon_text_t));
            itvDeparture.setVisibility(View.VISIBLE);

        } else if (sortType == Utils.SORT_TIME && sortManner == Utils.SORT_DECREMENTAL) {
            tvDeparture.setTextColor(getResources().getColor(R.color.black));
            itvDeparture.setText(getString(R.string.icon_text_q));
            itvDeparture.setVisibility(View.VISIBLE);

        } else if (sortType == Utils.SORT_DURATION && sortManner == Utils.SORT_INCREMENTAL) {
            tvDuration.setTextColor(getResources().getColor(R.color.black));
            itvDuration.setText(getString(R.string.icon_text_t));
            itvDuration.setVisibility(View.VISIBLE);

        } else {
            tvDuration.setTextColor(getResources().getColor(R.color.black));
            itvDuration.setText(getString(R.string.icon_text_q));
            itvDuration.setVisibility(View.VISIBLE);
        }
    }

    private void setClickListener() {
        relativeLayoutDeparture.setOnClickListener(this);
        relativeLayoutDuration.setOnClickListener(this);
        relativeLayoutPrice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_departure:
                if (sortType == Utils.SORT_TIME) {
                    if (sortManner == Utils.SORT_INCREMENTAL)
                        sortManner = Utils.SORT_DECREMENTAL;
                    else
                        sortManner = Utils.SORT_INCREMENTAL;
                } else {
                    sortType = Utils.SORT_TIME;
                    sortManner = Utils.SORT_INCREMENTAL;
                }
                setSortViews();
                adapter.setSelectedFlightPositionToDefault();
                adapter.createSortedFlightDataList();
                adapter.setTvCustomerFare();
                adapter.setTvBussinessFare();
                adapter.notifyDataSetChanged();
                rcViewFlights.smoothScrollToPosition(adapter.getSelectedFlightPosition());
                break;

            case R.id.rl_duration:
                if (sortType == Utils.SORT_DURATION) {
                    if (sortManner == Utils.SORT_INCREMENTAL)
                        sortManner = Utils.SORT_DECREMENTAL;
                    else
                        sortManner = Utils.SORT_INCREMENTAL;
                } else {
                    sortType = Utils.SORT_DURATION;
                    sortManner = Utils.SORT_INCREMENTAL;
                }
                setSortViews();
                adapter.setSelectedFlightPositionToDefault();
                adapter.createSortedFlightDataList();
                adapter.setTvCustomerFare();
                adapter.setTvBussinessFare();
                adapter.notifyDataSetChanged();
                rcViewFlights.smoothScrollToPosition(adapter.getSelectedFlightPosition());
                break;

            case R.id.rl_price:
                if (sortType == Utils.SORT_PRICE) {
                    if (sortManner == Utils.SORT_INCREMENTAL)
                        sortManner = Utils.SORT_DECREMENTAL;
                    else
                        sortManner = Utils.SORT_INCREMENTAL;
                } else {
                    sortType = Utils.SORT_PRICE;
                    sortManner = Utils.SORT_INCREMENTAL;
                }
                setSortViews();
                adapter.setSelectedFlightPositionToDefault();
                adapter.createSortedFlightDataList();
                adapter.setTvCustomerFare();
                adapter.setTvBussinessFare();
                adapter.notifyDataSetChanged();
                rcViewFlights.smoothScrollToPosition(adapter.getSelectedFlightPosition());
                break;

            case R.id.book:
                AppPreferences.getInstance(this).setBillingInfo("");
                final Bundle bundle = getIntent().getExtras();
                bundle.putSerializable("flightDataFirstWay", adapter.getFlightDataList().get(adapter.getSelectedFlightPosition()));
                bundle.putString("search_id", searchResponse.getData().getSearchId());
                bundle.putBoolean("ticket_enabled", searchResponse.isTicketEnabled());
                bundle.putString("fare_results", searchResponse.getData().getFareResults());
/*                if (AppPreferences.getInstance(this).getUserData().isEmpty())
                    Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.login_msg), null, getString(R.string.ok), getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            Intent intent = new Intent(FlightsSearchOnewayActivity.this, LoginActivity.class);
                                            bundle.putBoolean("isFromBook", true);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            });
                else {
                    Intent i = new Intent(this, FlightDetailsActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                }*/
                Intent i = new Intent(this, PassengerActivity.class);
                i.putExtras(bundle);
                startActivity(i);
                break;

            case R.id.back:
                finish();
                break;

        }

    }

    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    public int getSortType() {
        return sortType;
    }

    public String getDepartDate() {
        return departDate;
    }

    public int getSortManner() {
        return sortManner;
    }

    public TextView getTvCustomerFare() {
        return tvCustomerFare;
    }

    public TextView getTvBussinessFare() {
        return tvBusinessFare;
    }


    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public String getOriginCityCode() {
        return originCityCode;
    }

    public void setOriginCityCode(String originCityCode) {
        this.originCityCode = originCityCode;
    }

    public String getDestCityCode() {
        return destCityCode;
    }

    public void setDestCityCode(String destCityCode) {
        this.destCityCode = destCityCode;
    }

    public String getOriginCityName() {
        return originCityName;
    }

    public void setOriginCityName(String originCityName) {
        this.originCityName = originCityName;
    }

    public String getDestCityName() {
        return destCityName;
    }

    public void setDestCityName(String destCityName) {
        this.destCityName = destCityName;
    }

    public int getNoOfAdults() {
        return noOfAdults;
    }

    public void setNoOfAdults(int noOfAdults) {
        this.noOfAdults = noOfAdults;
    }

    public int getNoOfChildren() {
        return noOfChildren;
    }

    public void setNoOfChildren(int noOfChildren) {
        this.noOfChildren = noOfChildren;
    }

    public int getNoOfInfants() {
        return noOfInfants;
    }

    public void setNoOfInfants(int noOfInfants) {
        this.noOfInfants = noOfInfants;
    }
}