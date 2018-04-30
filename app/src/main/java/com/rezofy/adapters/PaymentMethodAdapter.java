package com.rezofy.adapters;


import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.asynctasks.NetworkTask;

import com.rezofy.models.response_models.PaymentMethodsResponse;

import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.Utils;

import com.rezofy.views.activities.BusVoucherActivity;
import com.rezofy.views.activities.MyTripCustomWebViewActivity;
import com.rezofy.views.activities.MyTripHotelDetailActivity;
import com.rezofy.views.activities.MyTripsActivity;
import com.rezofy.views.fragments.MyTripsFragment;

import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by linchpin on 3/2/17.
 */

public class PaymentMethodAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private static final int ID_VIEW_TRIP =  1;
    private Context ctx;
    //TripList tripRecordsList;
    List<PaymentMethodsResponse.Gateways> gatewayses;

    private RecyclerView recyclerView;
    private final int request_code_webView = 1;
    public PaymentMethodAdapter(Context ctx, List<PaymentMethodsResponse.Gateways> gatewayses, RecyclerView mRecyclerView) {
        setConstructorData(ctx, gatewayses, mRecyclerView);
        this.ctx =  ctx;
    }

    public PaymentMethodAdapter(MyTripsFragment myTripsFragment, Context ctx, List<PaymentMethodsResponse.Gateways> gatewayses, RecyclerView mRecyclerView) {
        setConstructorData(ctx, gatewayses, mRecyclerView);

    }

    private void setConstructorData(Context ctx, List<PaymentMethodsResponse.Gateways> gatewayses, RecyclerView mRecyclerView) {
        this.ctx = ctx;

        this.gatewayses = gatewayses;
        recyclerView = mRecyclerView;


    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            // View view = LayoutInflater.from(ctx).inflate(R.layout.trip_item, parent, false);
            View view = LayoutInflater.from(ctx).inflate(R.layout.custom_item_payment_methods, parent, false);
            return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.llRoot.setTag(position);
            myViewHolder.llRoot.setOnClickListener(this);
            myViewHolder.ivPayMethod.setTag(position);
            try {
                myViewHolder.ivPayMethod.setImageDrawable(new GifDrawable(ctx.getAssets(), new StringBuilder().append(ctx.getString(R.string.image_path_pre_string)).append(gatewayses.get(position).getName()).append(ctx.getString(R.string.image_path_post_string)).toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            myViewHolder.tvPaymethodName.setText(gatewayses.get(position).getName());
        }


    }


    @Override
    public int getItemCount() {
        if ( gatewayses == null || gatewayses.isEmpty()) {

                recyclerView.setVisibility(View.GONE);

            return 0;
        } else {

                recyclerView.setVisibility(View.VISIBLE);

            return gatewayses.size();
        }
    }



    @Override
    public void onClick(View v) {
        if (!Utils.isNetworkAvailable(ctx)) {
            Utils.showAlertDialog(ctx, ctx.getString(R.string.app_name), ctx.getString(R.string.network_error), ctx.getString(R.string.ok), null, null, null);

        } else {
            int position = (int) v.getTag();
            Intent intent = new Intent(ctx, MyTripCustomWebViewActivity.class);
            if(ctx instanceof MyTripsActivity)
            {
                intent.putExtra("bookingRefNo", ((MyTripsActivity)ctx).bookingRefNo);
                ((MyTripsActivity)ctx).hidePaymentMethods();
            }
            else if(ctx instanceof MyTripHotelDetailActivity)
            {
                intent.putExtra("bookingRefNo", ((MyTripHotelDetailActivity)ctx).bookingRefNo);
                ((MyTripHotelDetailActivity)ctx).hidePaymentMethods();
            }
            else if(ctx instanceof BusVoucherActivity)
            {
                intent.putExtra("bookingRefNo", ((BusVoucherActivity)ctx).bookingRefNo);
                ((BusVoucherActivity)ctx).hidePaymentMethods();
            }


            intent.putExtra("paymentType", gatewayses.get(position).getName());
            intent.putExtra("token", AppPreferences.getInstance(ctx).getToken());

            ((Activity)ctx).startActivityForResult(intent, request_code_webView);

        }

    }



 class MyViewHolder extends RecyclerView.ViewHolder {
       ImageView ivPayMethod;
        TextView tvPaymethodName;
     LinearLayout llRoot;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivPayMethod = (ImageView) itemView.findViewById(R.id.ivPayMethods);
            tvPaymethodName = (TextView) itemView.findViewById(R.id.tvPaymethodName);
            llRoot = (LinearLayout)itemView.findViewById(R.id.llRoot);
        }


    }




}