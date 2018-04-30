package com.rezofy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rezofy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by linchpin11192 on 18-Feb-2016.
 */
public class FareRulesAdapter extends RecyclerView.Adapter<FareRulesAdapter.ViewHolder> {
    private Context context;
    private JSONObject fareRulesJSONObject;
    private HashMap<String, JSONObject> mapLegs;
    private List<String> listKeysLegs;

    public FareRulesAdapter(Context context, JSONObject fareRulesJSONObject) {
        this.context = context;
        this.fareRulesJSONObject = fareRulesJSONObject;
        createLegsHashMap();
    }

    private void createLegsHashMap() {
        mapLegs = new HashMap<>();
        List<String> listKeysSegments = new ArrayList<>();
        listKeysLegs = new ArrayList<>();
        Iterator<String> iteratorSegments = fareRulesJSONObject.keys();
        while (iteratorSegments.hasNext())
            listKeysSegments.add(iteratorSegments.next());
        for (String segmentKey : listKeysSegments) {
            try {
                JSONObject segmentJSONObject = fareRulesJSONObject.getJSONObject(segmentKey);
                Iterator<String> iteratorLegs = segmentJSONObject.keys();
                while (iteratorLegs.hasNext()) {
                    String keyLeg = iteratorLegs.next();
                    mapLegs.put(keyLeg, segmentJSONObject.getJSONObject(keyLeg));
                    listKeysLegs.add(keyLeg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_fare_rule_leg, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvLegTitle.setText(listKeysLegs.get(position));
        setRules(holder.llLeg, position);
    }

    private void setRules(LinearLayout llLeg, int position) {
        JSONObject objectLeg = mapLegs.get(listKeysLegs.get(position));
        List<String> listKeysRules = new ArrayList<>();
        List<String> listValuesRules = new ArrayList<>();
        Iterator<String> iteratorRules = objectLeg.keys();
        while (iteratorRules.hasNext()) {
            String keyRule = iteratorRules.next();
            listKeysRules.add(keyRule);
            try {
                listValuesRules.add(objectLeg.getString(keyRule));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < listKeysRules.size(); i++) {
            View viewFareRule = LayoutInflater.from(context).inflate(R.layout.layout_fare_rule, null, false);
            ((TextView) viewFareRule.findViewById(R.id.rule_title)).setText(listKeysRules.get(i));
            ((TextView) viewFareRule.findViewById(R.id.rule)).setText(listValuesRules.get(i));
            llLeg.addView(viewFareRule);
        }
    }

    @Override
    public int getItemCount() {
        return mapLegs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llLeg;
        private TextView tvLegTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            llLeg = (LinearLayout) itemView.findViewById(R.id.leg_layout);
            tvLegTitle = (TextView) itemView.findViewById(R.id.leg_title);
        }
    }
}
