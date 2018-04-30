package com.rezofy.views.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.rezofy.R;
import com.rezofy.adapters.CustomSpinnerAdapter;
import com.rezofy.views.activities.CurrencyConverterActivity;

import java.util.ArrayList;

/**
 * Created by linchpin on 8/2/17.
 */

public class CurrencyConverterDialog  extends DialogFragment {

    LinearLayout llHeader;
    Spinner spSource;
    Spinner spTarget;
    RelativeLayout rlConvert;
    ArrayList<String> countries;
    CustomSpinnerAdapter customSpinnerAdapter;
    CustomSpinnerAdapter customSpinnerAdapter1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder;
        final AlertDialog alertDialog;


        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_currency,null);
        initViews(layout);
        builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        return alertDialog;

    }

    @Override
    public void onResume() {
        super.onResume();
        countries = new ArrayList<String>();
        countries.add("India");
        countries.add("Austrailia");
        countries.add("UK");
        countries.add("USA");
        countries.add("IRAK");
        customSpinnerAdapter=new CustomSpinnerAdapter(getActivity(),countries);

        customSpinnerAdapter1=new CustomSpinnerAdapter(getActivity(),countries);
        setDataToViews();
    }

    public void initViews(View view)
    {
        llHeader = (LinearLayout) view.findViewById(R.id.llHeader);
        spSource = (Spinner) view.findViewById(R.id.spSource);
        spTarget = (Spinner) view.findViewById(R.id.spTarget);
        rlConvert = (RelativeLayout)view.findViewById(R.id.rl_convert);


     }



    public void setDataToViews()
     {

         spSource.setAdapter(customSpinnerAdapter);
         spSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 String item = parent.getItemAtPosition(position).toString();

             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });



         spTarget.setAdapter(customSpinnerAdapter1);
         spTarget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 String item = parent.getItemAtPosition(position).toString();


             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });

         rlConvert.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getActivity(), CurrencyConverterActivity.class);
                 startActivity(intent);
                 dismiss();
             }
         });
     }



}
