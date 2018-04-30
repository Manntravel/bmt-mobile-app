package com.rezofy.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rezofy.R;
import com.rezofy.adapters.FareRulesAdapter;
import com.rezofy.adapters.ServiceFeeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Linchpin66 on 27-01-2016.
 */
public class ServiceFeeFragment extends Fragment {

    private View fView;
    private final String KEY_SERVICE_FEE = "serviceFee";
    private JSONArray serviceFeeJSONArray;
    private RecyclerView rvServiceFee;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (fView == null)
            fView = inflater.inflate(R.layout.fragment_fare_rules, container, false);
        createServiceFeeJSONArray(getArguments().getString("fare_rule_response"));
        init();
        return fView;
    }

    private void init() {
        rvServiceFee = (RecyclerView) fView.findViewById(R.id.fare_rules);
        rvServiceFee.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvServiceFee.setAdapter(new ServiceFeeAdapter(getContext(), serviceFeeJSONArray));
    }

    private void createServiceFeeJSONArray(String fareRulesResponse) {
        try {
            JSONObject responseObject = new JSONObject(fareRulesResponse);
            serviceFeeJSONArray = responseObject.getJSONArray(KEY_SERVICE_FEE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
