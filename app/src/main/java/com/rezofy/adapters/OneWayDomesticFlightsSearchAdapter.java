package com.rezofy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.models.response_models.FlightData;
import com.rezofy.models.response_models.Leg;
import com.rezofy.models.response_models.Wallet;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.FlightsSearchOnewayActivity;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by vkumar on 5/1/16.
 */
public class OneWayDomesticFlightsSearchAdapter extends RecyclerView.Adapter<OneWayDomesticFlightsSearchAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private FlightsSearchOnewayActivity searchActivity;
    public List<FlightData> flightDataList;
    private int selectedFlightPosition = 0;

    public OneWayDomesticFlightsSearchAdapter(List<FlightData> flightDataList, Context context) {
        this.context = context;
        searchActivity = (FlightsSearchOnewayActivity) context;
        this.flightDataList = flightDataList;
        Utils.setSellingPriceInFlightDataList(flightDataList);
        createSortedFlightDataList();
        setTvCustomerFare();
        setTvBussinessFare();
    }

    public void createSortedFlightDataList() {
        if (searchActivity.getSortType() == Utils.SORT_PRICE && searchActivity.getSortManner() == Utils.SORT_INCREMENTAL)
            Collections.sort(flightDataList, UIUtils.priceIncComparator);

        else if (searchActivity.getSortType() == Utils.SORT_PRICE && searchActivity.getSortManner() == Utils.SORT_DECREMENTAL)
            Collections.sort(flightDataList, UIUtils.priceDecComparator);

        else if (searchActivity.getSortType() == Utils.SORT_TIME && searchActivity.getSortManner() == Utils.SORT_INCREMENTAL)
            Collections.sort(flightDataList, UIUtils.timeIncComparator);

        else if (searchActivity.getSortType() == Utils.SORT_TIME && searchActivity.getSortManner() == Utils.SORT_DECREMENTAL)
            Collections.sort(flightDataList, UIUtils.timeDecComparator);

        else if (searchActivity.getSortType() == Utils.SORT_DURATION && searchActivity.getSortManner() == Utils.SORT_INCREMENTAL)
            Collections.sort(flightDataList, UIUtils.durationIncComparator);

        else
            Collections.sort(flightDataList, UIUtils.durationDecComparator);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.llRoot.setOnClickListener(this);
            Leg firstLeg = flightDataList.get(position).getSegments().get(0).getLegs().get(0);
            Leg lastLeg = flightDataList.get(position).getSegments().get(0).getLegs().get(flightDataList.get(position).getSegments().get(0).getLegs().size() - 1);
            holder.llRoot.setTag(position);
            if (position == selectedFlightPosition)
                holder.llRoot.setBackgroundResource(R.color.selected_flight);
            else
                holder.llRoot.setBackgroundResource(android.R.color.white);
            try {
                holder.ivCarrierImage.setImageDrawable(new GifDrawable(context.getAssets(), new StringBuilder().append(context.getString(R.string.image_path_pre_string)).append(flightDataList.get(position).getCarrier()).append(context.getString(R.string.image_path_post_string)).toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            holder.startTime.setText(UIUtils.getTimeToDisplay(firstLeg.getDepartureTime()));
            holder.endTime.setText(UIUtils.getTimeToDisplay(lastLeg.getArrivalTime()));
            holder.totalTime.setText(new StringBuilder().append(Utils.getDurationInHrsAndMins(flightDataList.get(position).getSegments().get(0).getDuration()))
                    .append(" | ")
                    .append(UIUtils.getFlightType(flightDataList.get(position).getSegments().get(0).getNoOfStops())).toString());

            if (flightDataList.get(position).getPricingSource() != null) {
                String str[] = flightDataList.get(position).getPricingSource().split(" ");
                String price = "";
                if (str.length > 0) {
                    if (str.length > 2) {
                        price = str[0] + str[1];
                    } else {
                        price = flightDataList.get(position).getPricingSource();
                    }
                    holder.tvPricingSource.setText(price);
                    holder.tvPricingSource.setVisibility(View.VISIBLE);
                }
            } else {
                holder.tvPricingSource.setVisibility(View.INVISIBLE);
            }

            if (AppPreferences.getInstance(context).getB2B()) {
                holder.businessFare.setText(UIUtils.getFareToDisplay(context, flightDataList.get(position).getFare().getTotal().getPrice().getAmount()));
                holder.businessFare.setVisibility(View.VISIBLE);
            } else
                holder.businessFare.setVisibility(View.GONE);

            Log.d("Trip", "customer fare is " + flightDataList.get(position).getFare().getSellingPrice());
            holder.customerFare.setText(UIUtils.getFareToDisplay(context, flightDataList.get(position).getFare().getSellingPrice()));
            holder.flightCode.setText(UIUtils.getFlightToDisplay(flightDataList.get(position).getCarrier(), flightDataList.get(position).getSegments().get(0).getLegs().get(0).getFlightNumber()));
        } catch (Exception e) {
            Log.d("Trip", "eror in adapter " + e);
        }
    }


    @Override
    public int getItemCount() {
        return flightDataList.size();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rc_view_oneway, parent, false));

    }

    @Override
    public void onClick(View v) {
        int prevSelectedFlightPosition = selectedFlightPosition;
        selectedFlightPosition = (int) v.getTag();
        notifyItemChanged(selectedFlightPosition);
        notifyItemChanged(prevSelectedFlightPosition);
        setTvCustomerFare();
        setTvBussinessFare();
    }

    public void setTvCustomerFare() {
        searchActivity.getTvCustomerFare().setText(UIUtils.getFareToDisplay(context, flightDataList.get(selectedFlightPosition).getFare().getSellingPrice()));
    }

    public void setTvBussinessFare() {
        if (AppPreferences.getInstance(context).getB2B()) {
            searchActivity.getTvBussinessFare().setText(UIUtils.getFareToDisplay(context, flightDataList.get(selectedFlightPosition).getFare().getTotal().getPrice().getAmount()));
            searchActivity.getTvBussinessFare().setVisibility(View.VISIBLE);
            ((LinearLayout) searchActivity.getTvBussinessFare().getParent()).setVisibility(View.VISIBLE);
        } else {
            searchActivity.getTvBussinessFare().setVisibility(View.GONE);
            ((LinearLayout) searchActivity.getTvBussinessFare().getParent()).setVisibility(View.GONE);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        GifImageView ivCarrierImage;
        TextView startTime, endTime, totalTime, customerFare, businessFare, flightCode, tvPricingSource;
        LinearLayout llRoot;

        public ViewHolder(View itemView) {
            super(itemView);
            init();
        }

        private void init() {
            llRoot = (LinearLayout) itemView.findViewById(R.id.root);
            ivCarrierImage = (GifImageView) itemView.findViewById(R.id.image_flight);
            startTime = (TextView) itemView.findViewById(R.id.dep_time);
            endTime = (TextView) itemView.findViewById(R.id.arr_time);
            totalTime = (TextView) itemView.findViewById(R.id.duration_and_type);
            tvPricingSource = (TextView) itemView.findViewById(R.id.tv_pricing_source);
//            try {
            //  tvPricingSource.setMovementMethod(new ScrollingMovementMethod());
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            customerFare = (TextView) itemView.findViewById(R.id.customer_fare);
            businessFare = (TextView) itemView.findViewById(R.id.business_fare);
            flightCode = (TextView) itemView.findViewById(R.id.category);

            if (!AppPreferences.getInstance(context).getB2B()) {
                businessFare.setVisibility(View.INVISIBLE);
            }
        }
    }

    public List<FlightData> getFlightDataList() {
        return flightDataList;
    }

    public int getSelectedFlightPosition() {
        return selectedFlightPosition;
    }

    public void setSelectedFlightPositionToDefault() {
        this.selectedFlightPosition = 0;
    }
}
