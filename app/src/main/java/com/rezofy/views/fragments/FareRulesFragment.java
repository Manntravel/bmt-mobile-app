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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Linchpin66 on 27-01-2016.
 */
public class FareRulesFragment extends Fragment {

    private JSONObject fareRulesJSONObject;
    private final String KEY_FARE_RULES = "fareRules";
    private View fView;
    private RecyclerView rvFareRules;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (fView == null)
            fView = inflater.inflate(R.layout.fragment_fare_rules, container, false);
        createFareRulesJSONObject(getArguments().getString("fare_rule_response"));
        init();
        return fView;
    }

    private void init() {
        rvFareRules = (RecyclerView) fView.findViewById(R.id.fare_rules);
        rvFareRules.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvFareRules.setAdapter(new FareRulesAdapter(getContext(), fareRulesJSONObject));
    }

    private void createFareRulesJSONObject(String fareRulesResponse) {
        try {
            JSONObject responseObject = new JSONObject(fareRulesResponse);
            fareRulesJSONObject = responseObject.getJSONObject(KEY_FARE_RULES);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
