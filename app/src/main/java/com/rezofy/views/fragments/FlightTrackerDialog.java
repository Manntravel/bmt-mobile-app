package com.rezofy.views.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rezofy.R;
import com.rezofy.views.activities.FlightTrackerActivity;

/**
 * Created by linchpin on 8/2/17.
 */

public class FlightTrackerDialog  extends DialogFragment {

    LinearLayout llHeader;
    EditText etAirlineName;
    EditText etFlightNo;
    RelativeLayout rlSearch;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder;
        final AlertDialog alertDialog;


        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_flight_tracker,null);
        initViews(layout);
        builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        return alertDialog;

    }

    public void initViews(View view)
    {
        llHeader = (LinearLayout) view.findViewById(R.id.llHeader);
        etAirlineName = (EditText) view.findViewById(R.id.etAirlineName);
        etFlightNo = (EditText) view.findViewById(R.id.etFlightNo);
        rlSearch = (RelativeLayout)view.findViewById(R.id.rl_search);
        setDataToViews();

    }

    public void setDataToViews()
    {

        rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FlightTrackerActivity.class);
                startActivity(intent);
                dismiss();
            }
        });

    }



}
