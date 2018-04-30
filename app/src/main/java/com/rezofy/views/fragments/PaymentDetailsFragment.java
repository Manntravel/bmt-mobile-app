package com.rezofy.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rezofy.R;
import com.rezofy.adapters.PaymentDetailsAdapter;

import java.util.ArrayList;

public class PaymentDetailsFragment extends Fragment implements View.OnClickListener {
    private View view;
    private RecyclerView rcPaymentDetails;
    private LinearLayoutManager mLayoutManager;
    private PaymentDetailsAdapter paymentDetailsAdapter;
    private ArrayList list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.payment_details_layout, container, false);
        init();

        return view;
    }

    private void init() {
        list = new ArrayList();
        rcPaymentDetails = (RecyclerView) view.findViewById(R.id.rv_payment);
        rcPaymentDetails.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rcPaymentDetails.setLayoutManager(mLayoutManager);
        paymentDetailsAdapter = new PaymentDetailsAdapter(list, getActivity());
        rcPaymentDetails.setAdapter(paymentDetailsAdapter);

    }

    public void onClick(View v) {
        switch (v.getId()) {
             /* case R.id.layout_departure:
                break;
               case R.id.layout_return:
                break;
            */
        }
    }

}