package com.rezofy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.models.response_models.FlightData;
import com.rezofy.models.response_models.Leg;
import com.rezofy.models.response_models.Wallet;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.FlightsSearchDomesticTwowayActivity;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by vkumar on 5/1/16.
 */
public class TwoWayDomesticFlightsSearchAdapter extends RecyclerView.Adapter<TwoWayDomesticFlightsSearchAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private FlightsSearchDomesticTwowayActivity searchActivity;
    private int sortManner;
    private int sortType;
    private int flightDirection;
    private List<FlightData> flightDataList;
    private int selectedFlightPosition = 0;

    public TwoWayDomesticFlightsSearchAdapter(Context context, int flightDirection) {
        this.context = context;
        this.flightDirection = flightDirection;
        searchActivity = (FlightsSearchDomesticTwowayActivity) context;
    }

    public void createSortedFlightDataList() {
        if (flightDirection == Utils.FLIGHT_DIRECTION_OUTBOUND) {
            sortType = searchActivity.getSortTypeOutbound();
            sortManner = searchActivity.getSortMannerOutbound();

        } else {
            sortType = searchActivity.getSortTypeInbound();
            sortManner = searchActivity.getSortMannerInbound();
        }

        if (sortType == Utils.SORT_PRICE && sortManner == Utils.SORT_INCREMENTAL)
            Collections.sort(flightDataList, UIUtils.priceIncComparator);

        else if (sortType == Utils.SORT_PRICE && sortManner == Utils.SORT_DECREMENTAL)
            Collections.sort(flightDataList, UIUtils.priceDecComparator);

        else if (sortType == Utils.SORT_TIME && sortManner == Utils.SORT_INCREMENTAL)
            Collections.sort(flightDataList, UIUtils.timeIncComparator);

        else
            Collections.sort(flightDataList, UIUtils.timeDecComparator);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.rlRoot.setTag(position);
        holder.rlRoot.setOnClickListener(this);

        if (position == selectedFlightPosition)
            holder.rlRoot.setBackgroundResource(R.color.selected_flight);
        else
            holder.rlRoot.setBackgroundResource(android.R.color.white);
        try {
            holder.ivCarrierImage.setImageDrawable(new GifDrawable(context.getAssets(), new StringBuilder().append(context.getString(R.string.image_path_pre_string)).append(flightDataList.get(position).getCarrier()).append(context.getString(R.string.image_path_post_string)).toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Leg lastLeg = flightDataList.get(position).getSegments().get(0).getLegs().get(flightDataList.get(position).getSegments().get(0).getLegs().size() - 1);
        holder.tvDepTime.setText(UIUtils.getTimeToDisplay(flightDataList.get(position).getSegments().get(0).getLegs().get(0).getDepartureTime()));
        holder.tvArrTime.setText(UIUtils.getTimeToDisplay(lastLeg.getArrivalTime()));
        holder.tvDurationAndType.setText(new StringBuilder().append(Utils.getDurationInHrsAndMins(flightDataList.get(position).getSegments().get(0).getDuration()))
                .append(" | ")
                .append(UIUtils.getFlightType(flightDataList.get(position).getSegments().get(0).getNoOfStops())).toString());
        if (AppPreferences.getInstance(context).getB2B()) {
            holder.tvBusinessFare.setText(new StringBuilder().append("|").append(UIUtils.getFareToDisplay(context, flightDataList.get(position).getFare().getTotal().getPrice().getAmount())).toString());
            holder.tvBusinessFare.setVisibility(View.VISIBLE);
        } else
            holder.tvBusinessFare.setVisibility(View.GONE);

        holder.tvCustomerFare.setText(UIUtils.getFareToDisplay(context, flightDataList.get(position).getFare().getSellingPrice()));
        holder.tvFlightCode.setText(UIUtils.getFlightToDisplay(flightDataList.get(position).getCarrier(), flightDataList.get(position).getSegments().get(0).getLegs().get(0).getFlightNumber()));

        if(flightDataList.get(position).getPricingSource() != null) {
            String str[] = flightDataList.get(position).getPricingSource().split(" ");
            String price = "";
            if(str.length > 0) {
                if(str.length > 2){
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
    }

    @Override
    public int getItemCount() {
        if (flightDataList == null)
            return 0;
        else
            return flightDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rc_view1, parent, false));
    }

    @Override
    public void onClick(View v) {
        selectedFlightPosition = (int) v.getTag();
        searchActivity.manageTvFares();
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlRoot;
        GifImageView ivCarrierImage;
        TextView tvDepTime, tvArrTime, tvDurationAndType, tvCustomerFare, tvBusinessFare, tvFlightCode, tvPricingSource, tvCashback;

        public ViewHolder(View itemView) {
            super(itemView);
            rlRoot = (RelativeLayout) itemView.findViewById(R.id.root);
            ivCarrierImage = (GifImageView) itemView.findViewById(R.id.image_flight);
            tvDepTime = (TextView) itemView.findViewById(R.id.dep_time);
            tvArrTime = (TextView) itemView.findViewById(R.id.arr_time);
            tvDurationAndType = (TextView) itemView.findViewById(R.id.duration_and_type);
            tvCustomerFare = (TextView) itemView.findViewById(R.id.customer_fare);
            tvBusinessFare = (TextView) itemView.findViewById(R.id.business_fare);
            tvFlightCode = (TextView) itemView.findViewById(R.id.category);
            tvPricingSource = (TextView) itemView.findViewById(R.id.tv_pricing_source);
        }
    }

    public List<FlightData> getFlightDataList() {
        return flightDataList;
    }

    public void setFlightDataList(List<FlightData> flightDataList) {
        this.flightDataList = flightDataList;
        Utils.setSellingPriceInFlightDataList(flightDataList);
        createSortedFlightDataList();
    }

    public int getSelectedFlightPosition() {
        return selectedFlightPosition;
    }

    public void setSelectedFlightPosition(int selectedFlightPosition) {
        this.selectedFlightPosition = selectedFlightPosition;
    }

    public void setSelectedFlightPositionToDefault() {
        this.selectedFlightPosition = 0;
    }


}
