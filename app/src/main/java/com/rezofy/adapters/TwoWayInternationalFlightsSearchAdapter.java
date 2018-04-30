package com.rezofy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rezofy.R;

import java.util.List;

/**
 * Created by vkumar on 5/1/16.
 */
public class TwoWayInternationalFlightsSearchAdapter extends RecyclerView.Adapter<TwoWayInternationalFlightsSearchAdapter.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private  List     getList;
    private  Context  context;
    public TwoWayInternationalFlightsSearchAdapter(List getList, Context context)
    {
      this.getList=getList;
      this.context=context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        if(holder!=null)
        {
            /* holder.ivCarrierImage.setImageResource(R.drawable.images3);
             holder.tvDepTime.setText("04: 15 - 10: 30");
             holder.amount.setText("$ 8,53");
             holder.tvTotalTime.setText("1h 55m | non stop");
             holder.flightCode.setText("6E 154");*/
        }
    }

    @Override
    public int getItemCount()
    {
        return 50;
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rc_view_roundtrip_special, parent, false);
            vhItem = new ViewHolder(v);
            return vhItem;
        }

        return vhItem;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        View        itemView;
        ImageView   imageAirLogo;
        TextView    startTime,endTime,totalTime,stopCondition,amount,flightCode;
        public ViewHolder(View itemView)
        {
            super(itemView);
            this.itemView=itemView;
            init();
        }

        private void init()
        {
            imageAirLogo=(ImageView)itemView.findViewById(R.id.image_flight);
            startTime=(TextView)itemView.findViewById(R.id.customer_fare);
            endTime=(TextView)itemView.findViewById(R.id.customer_fare);
            totalTime=(TextView)itemView.findViewById(R.id.duration_and_type);
            stopCondition=(TextView)itemView.findViewById(R.id.duration_and_type);
            flightCode=(TextView)itemView.findViewById(R.id.category);
        }

    }
}
