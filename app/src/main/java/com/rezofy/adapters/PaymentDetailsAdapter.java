package com.rezofy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rezofy.R;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by vkumar on 5/1/16.
 */
public class PaymentDetailsAdapter extends RecyclerView.Adapter<PaymentDetailsAdapter.ViewHolder> {

    private  static final int TYPE_ITEM = 1;
    private  ArrayList      getList;
    private  Context        context;

    public PaymentDetailsAdapter(ArrayList getList , Context context)
    {
        this.getList=getList;
        this.context=context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        if(holder!=null)
        {
            /* holder.ivCarrierImage.playGif(flightDataList.get(position).getSegments().get(0).getLegs().get(0).getAirline());
             holder.startTime.setText(flightDataList.get(position).getSegments().get(0).getLegs().get(0).getDepartureTime().substring(0, 2) + ":"
                     + flightDataList.get(position).getSegments().get(0).getLegs().get(0).getDepartureTime().substring(2));
            holder.endTime.setText(flightDataList.get(position).getSegments().get(0).getLegs().get(0).getArrivalTime().substring(0, 2) + ":"
                    + flightDataList.get(position).getSegments().get(0).getLegs().get(0).getArrivalTime().substring(2));
            holder.totalTime.setText(getDurationInHrsAndMins(flightDataList.get(position).getSegments().get(0).getDuration()) + " | "
                    + getFlightType(flightDataList.get(position).getSegments().get(0).getNoOfStops()));
            holder.customerFare.setText(context.getString(R.string.rupee_symbol)
                    + Utils.formatIntMoney(Math.round(Float.parseFloat(flightDataList.get(position).getFare().getTotal().getPrice().getAmount()))));
            holder.flightCode.setText(flightDataList.get(position).getCarrier() + " "
                    + flightDataList.get(position).getSegments().get(0).getLegs().get(0).getFlightNumber());*/
        }
    }



    @Override
    public int getItemCount()
    {
        return 5;
    }

    @Override
    public int getItemViewType(int position)
    {
        return TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ViewHolder     vhItem=null;
        if(viewType == TYPE_ITEM  && vhItem==null)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rc_view_oneway, parent, false);
            vhItem = new ViewHolder(v);
            return vhItem;
        }
        return vhItem;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        View            itemView;
        GifImageView ivCarrierImage;
        TextView        startTime,endTime,totalTime,customerFare,businessFare,flightCode;
        public ViewHolder(View itemView)
        {
            super(itemView);
            this.itemView=itemView;
            init();
        }
        private void init()
        {
           /* ivCarrierImage= (GifDecoderView) itemView.findViewById(R.id.image_flight);
            startTime=(TextView)itemView.findViewById(R.id.dep_time);
            endTime=(TextView)itemView.findViewById(R.id.arr_time);
            totalTime=(TextView)itemView.findViewById(R.id.duration_and_type);
            customerFare=(TextView)itemView.findViewById(R.id.customer_fare);
            businessFare=(TextView)itemView.findViewById(R.id.business_fare);
            flightCode=(TextView)itemView.findViewById(R.id.category);
            businessFare.setVisibility(View.INVISIBLE);*/
        }
    }
}
