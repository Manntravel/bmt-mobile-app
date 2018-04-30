package com.rezofy.views.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rezofy.R;

/**
 * Created by linchpin on 2/2/17.
 */

public class CustomBottomSheetDialog extends BottomSheetDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder;
        final AlertDialog alertDialog;


        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_custom_bottom_sheet,null);






        builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        alertDialog = builder.create();



        return alertDialog;
    }
}
