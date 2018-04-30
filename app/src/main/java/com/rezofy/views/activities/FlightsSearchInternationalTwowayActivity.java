package com.rezofy.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.rezofy.adapters.RoundTripSpecialAdapter;
import com.rezofy.models.response_models.SearchResponse;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;

public class FlightsSearchInternationalTwowayActivity extends FlightsSearchDomesticTwowayActivity implements View.OnClickListener {

    private String searchId;
    private static FlightsSearchInternationalTwowayActivity twoWayIntOb;
    SearchResponse searchResponse;

    public SearchResponse getSearchResponse() {
        return searchResponse;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twoWayIntOb = this;
        rcViewHeader.setVisibility(View.GONE);
        llNonGDS.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (roundTripSpecialAdapter != null && !roundTripSpecialAdapter.getFlightDataList().isEmpty()) {
            roundTripSpecialAdapter.notifyDataSetChanged();
            manageTvFares();
        }
    }

    public static FlightsSearchInternationalTwowayActivity getInstance() {
        return twoWayIntOb;
    }

    @Override
    protected void setAdapters(String data) {
        if (data != null) {
            searchResponse = new Gson().fromJson(data, SearchResponse.class);
            searchId = searchResponse.getData().getSearchId();
            roundTripSpecialAdapter = new RoundTripSpecialAdapter(this, searchResponse.getData().getResults());
            rcViewGDS.setAdapter(roundTripSpecialAdapter);
        }
        if (!roundTripSpecialAdapter.getFlightDataList().isEmpty())
            manageTvFares();
    }

    public RoundTripSpecialAdapter getRoundTripSpecialAdapter() {
        return roundTripSpecialAdapter;
    }

    @Override
    public void manageTvFares() {
        String customerFareToDisplay, businessFareToDisplay;
        manageTvBusinessFare();
        businessFareToDisplay = UIUtils.getFareToDisplay(this, roundTripSpecialAdapter.getFlightDataList().get(roundTripSpecialAdapter.getSelectedFlightPosition()).getFare().getTotal().getPrice().getAmount());
        customerFareToDisplay = UIUtils.getFareToDisplay(this, roundTripSpecialAdapter.getFlightDataList().get(roundTripSpecialAdapter.getSelectedFlightPosition()).getFare().getSellingPrice());
        tvBusinessFare.setText(businessFareToDisplay);
        tvCustomerFare.setText(customerFareToDisplay);
    }

    @Override
    protected void onBookClick() {
        AppPreferences.getInstance(this).setBillingInfo("");
        Bundle bundle = getIntent().getExtras();
        bundle.putSerializable("flightDataFirstWay", roundTripSpecialAdapter.getFlightDataList().get(roundTripSpecialAdapter.getSelectedFlightPosition()));
        bundle.putSerializable("flightDataSecondWay", roundTripSpecialAdapter.getFlightDataList().get(roundTripSpecialAdapter.getSelectedFlightPosition()));
        bundle.putString("search_id", searchId);
        bundle.putBoolean("ticket_enabled", searchResponse.isTicketEnabled());
        bundle.putString("fare_results", Utils.FARE_RESULTS_TYPE_REGULAR);
        Intent i = new Intent(this, PassengerActivity.class);
        i.putExtras(bundle);
        startActivity(i);

        /*if (!checkAndHandleGuestUser(bundle)) {
            Intent i = new Intent(this, FlightDetailsActivity.class);
            i.putExtras(bundle);
            startActivity(i);
        }*/
    }
}
