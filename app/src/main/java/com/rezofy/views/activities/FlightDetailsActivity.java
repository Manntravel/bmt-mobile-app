package com.rezofy.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.adapters.FlightLegAdapter;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.models.response_models.FlightData;
import com.rezofy.requests.Requests;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.custom_views.IconTextView;

/**
 * Created by Linchpin66 on 20-01-2016.
 */
public class FlightDetailsActivity extends AppCompatActivity implements View.OnClickListener, NetworkTask.Result {
    private static final int ID_FARE_RULES = 1;
    private IconTextView iTVMenu;
    private String originCityName, destCityName, departDateValue, retDateValue, searchId, fareRules;
    private TextView tvFare, continueButton, tvFareRules, tvFareBreakUp, tvFareRefundable, tvTitle, tvPriceSource;
    private RelativeLayout fareInfo;
    private FlightData flightDataFirstWay, flightDataSecondWay;
    private RecyclerView rvFlightDetailsFirstWay, rvFlightDetailsSecondWay;
    private ImageView btnFloating;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.appendLog("inside onCreate of FlightDetails Activity");
        setContentView(R.layout.activity_flight);
        init();
        flightDetails();
        setProperties();
    }

    private void setProperties() {
        findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(this));
        iTVMenu.setTextColor(UIUtils.getThemeContrastColor(this));
        tvTitle.setTextColor(UIUtils.getThemeContrastColor(this));
        UIUtils.setNormalButtonProperties(continueButton);
        UIUtils.setFareProperties(tvFare);
    }

    private void flightDetails() {
        originCityName = getIntent().getStringExtra("originCityName");
        destCityName = getIntent().getStringExtra("destCityName");
        departDateValue = getIntent().getStringExtra("departDate");
        searchId = getIntent().getStringExtra("search_id");
        flightDataFirstWay = (FlightData) getIntent().getSerializableExtra("flightDataFirstWay");
//        if(flightDataFirstWay.getPricingSource() != null && flightDataFirstWay.getPricingSource().length() > 1) {
//            tvPriceSource.setVisibility(View.VISIBLE);
//            tvPriceSource.setText(flightDataFirstWay.getPricingSource());
//        } else {
//            tvPriceSource.setVisibility(View.GONE);
//
//        }
        rvFlightDetailsFirstWay.setAdapter(new FlightLegAdapter(this, flightDataFirstWay, originCityName, destCityName, departDateValue, Utils.FLIGHT_DIRECTION_OUTBOUND));
        if (flightDataFirstWay.isNoRefund())
            tvFareRefundable.setText(getString(R.string.non_refundable));
        else
            tvFareRefundable.setText(getString(R.string.refundable));
        if (getIntent().getSerializableExtra("flightDataSecondWay") != null) {
            retDateValue = getIntent().getStringExtra("retDate");
            flightDataSecondWay = (FlightData) getIntent().getSerializableExtra("flightDataSecondWay");
            if ((getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS))
                    || (getIntent().getStringExtra("fare_results").equals(Utils.FARE_RESULTS_TYPE_REGULAR) && getIntent().getStringExtra("selected_tab") == null)) {
                flightDataSecondWay.getSegments().remove(0);
                getIntent().putExtra("flightDataSecondWay", flightDataSecondWay);
                tvFare.setText(UIUtils.getFareToDisplay(this, flightDataFirstWay.getFare().getTotal().getPrice().getAmount()));
            } else
                tvFare.setText(UIUtils.getFareToDisplay(this, (float) (Math.ceil(Double.parseDouble(flightDataFirstWay.getFare().getTotal().getPrice().getAmount())) + Math.ceil(Double.parseDouble(flightDataSecondWay.getFare().getTotal().getPrice().getAmount())))));
            rvFlightDetailsSecondWay.setAdapter(new FlightLegAdapter(this, flightDataSecondWay, destCityName, originCityName, retDateValue, Utils.FLIGHT_DIRECTION_INBOUND));
            rvFlightDetailsSecondWay.setVisibility(View.VISIBLE);

//            if(flightDataSecondWay.getPricingSource() != null && flightDataSecondWay.getPricingSource().length() > 1) {
//                tvPriceSource.setVisibility(View.VISIBLE);
//                tvPriceSource.setText(flightDataSecondWay.getPricingSource());
//            } else {
//                tvPriceSource.setVisibility(View.GONE);
//
//            }

        } else {
            tvFare.setText(UIUtils.getFareToDisplay(this, flightDataFirstWay.getFare().getTotal().getPrice().getAmount()));
        }
    }

    private void init() {
        //recycler view ...
        rvFlightDetailsFirstWay = (RecyclerView) findViewById(R.id.flight_details_first_way);
        rvFlightDetailsFirstWay.setNestedScrollingEnabled(false);
        rvFlightDetailsFirstWay.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvFlightDetailsFirstWay.setLayoutManager(new LinearLayoutManager(this, 1, false));
        rvFlightDetailsSecondWay = (RecyclerView) findViewById(R.id.flight_details_second_way);
        rvFlightDetailsSecondWay.setNestedScrollingEnabled(false);
        rvFlightDetailsSecondWay.setHasFixedSize(true);
        rvFlightDetailsSecondWay.setLayoutManager(new LinearLayoutManager(this, 1, false));
        continueButton = (TextView) findViewById(R.id.continue_button);
        iTVMenu = (IconTextView) findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setText(getString(R.string.icon_text_d));
        iTVMenu.setTextSize(20);
        tvTitle = (TextView) findViewById(R.id.header).findViewById(R.id.title);
        tvTitle.setText(getString(R.string.flight_text));
        fareInfo = (RelativeLayout) findViewById(R.id.fare_info);
        fareInfo.setOnClickListener(this);

        tvFareBreakUp = (TextView) findViewById(R.id.fare_break_up);
        tvFareBreakUp.setTextColor(UIUtils.getThemeColor(this));
        tvFareBreakUp.setOnClickListener(this);
        tvFareRules = (TextView) findViewById(R.id.fare_rules);
        tvFareRules.setTextColor(UIUtils.getThemeColor(this));
        tvFareRules.setOnClickListener(this);
        tvFareRefundable = (TextView) findViewById(R.id.fare_refundable);

        continueButton.setOnClickListener(this);
        iTVMenu.setOnClickListener(this);
        tvFare = (TextView) findViewById(R.id.total_fare);
        //tvPriceSource = (TextView) findViewById(R.id.tv_price_source);
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


    @Override
    public void onClick(View view) {
        Bundle bundle = getIntent().getExtras();
        switch (view.getId()) {
            case R.id.continue_button:
                Intent i = new Intent(this, PassengerActivity.class);
                i.putExtras(bundle);
                startActivity(i);
                break;

            case R.id.left_icon:
                finish();
                break;

            case R.id.fare_break_up:
                try {
                    if(getIntent().getBooleanExtra("promocode_applied", false)) {
                        Intent intent = new Intent(this, UpdatedFareBreakUpActivity.class);
                        bundle = getIntent().getExtras();
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, FareBreakUpActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } catch (Exception e) {

                }

                break;

            case R.id.fare_rules:
                if (fareRules == null) {
                    if (!Utils.isNetworkAvailable(this)) {
                        Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

                    } else {
                        String url;
                        NetworkTask networkTask = new NetworkTask(this, ID_FARE_RULES);
                        networkTask.setDialogMessage(getString(R.string.please_wait));
                        networkTask.exposePostExecute(this);
                        url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlFareRules;
                        String paramsArray[] = new String[]{url, Requests.fareRuleRequest(searchId, flightDataFirstWay.getUniqueEntityId())};
                        networkTask.execute(paramsArray);
                    }
                } else {
                    startFareRulesActivity();
                }
                break;
        }
    }

    private void startFareRulesActivity() {
        Intent fareRuleIntent = new Intent(this, FareRuleActivity.class);
        fareRuleIntent.putExtra("fare_rule_response", fareRules);
        startActivity(fareRuleIntent);
    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {
        if (object == null || object.equals("")) {
            Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else {
            if (id == ID_FARE_RULES) {
                fareRules = object;
                Log.d("Trip","response is "+object);
                startFareRulesActivity();
            }
        }
    }
}
