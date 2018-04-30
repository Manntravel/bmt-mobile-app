package com.rezofy.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.models.response_models.FlightData;
import com.rezofy.models.response_models.Segment;
import com.rezofy.models.response_models.Wallet;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.FlightsSearchDomesticTwowayActivity;
import com.rezofy.views.activities.FlightsSearchInternationalTwowayActivity;
import com.rezofy.views.custom_views.RoundedImageView;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by linchpin11192 on 05-Feb-2016.
 */
public class RoundTripSpecialAdapter extends RecyclerView.Adapter<RoundTripSpecialAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private FlightsSearchDomesticTwowayActivity searchActivity;
    private List<FlightData> flightDataList;
    private int selectedFlightPosition = 0;


    public RoundTripSpecialAdapter(Context context) {
        this.context = context;
        searchActivity = (FlightsSearchDomesticTwowayActivity) context;
    }

    public void setFlightList(List<FlightData> flightDataList)
    {
        this.flightDataList = flightDataList;
    }

    public RoundTripSpecialAdapter(Context context, List<FlightData> flightDataList) {
        this.context = context;
        searchActivity = (FlightsSearchInternationalTwowayActivity) context;
        this.flightDataList = flightDataList;
        Utils.setSellingPriceInFlightDataList(flightDataList);
        if(!flightDataList.isEmpty()) {
            createSortedFlightDataList();
        }
    }

    public void createSortedFlightDataList() {
        if (searchActivity.getSortTypeGDS() == Utils.SORT_PRICE && searchActivity.getSortMannerGDS() == Utils.SORT_INCREMENTAL)
            Collections.sort(flightDataList, UIUtils.priceIncComparator);

        else if (searchActivity.getSortTypeGDS() == Utils.SORT_PRICE && searchActivity.getSortMannerGDS() == Utils.SORT_DECREMENTAL)
            Collections.sort(flightDataList, UIUtils.priceDecComparator);

        else if (searchActivity.getSortTypeGDS() == Utils.SORT_TIME && searchActivity.getSortMannerGDS() == Utils.SORT_INCREMENTAL)
            Collections.sort(flightDataList, UIUtils.timeIncComparator);

        else if (searchActivity.getSortTypeGDS() == Utils.SORT_TIME && searchActivity.getSortMannerGDS() == Utils.SORT_DECREMENTAL)
            Collections.sort(flightDataList, UIUtils.timeDecComparator);

        else if (searchActivity.getSortTypeGDS() == Utils.SORT_DURATION && searchActivity.getSortMannerGDS() == Utils.SORT_INCREMENTAL)
            Collections.sort(flightDataList, UIUtils.durationIncComparator);

        else
            Collections.sort(flightDataList, UIUtils.durationDecComparator);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rc_view_roundtrip_special, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(flightDataList.get(position).getSegments().size()>1) {
            holder.llRoot.setTag(position);
            holder.llRoot.setOnClickListener(this);

            if (position == selectedFlightPosition)
                holder.llRoot.setBackgroundResource(R.color.selected_flight);
            else
                holder.llRoot.setBackgroundResource(android.R.color.white);
            try {
                holder.gdvFlightImage.setImageDrawable(new GifDrawable(context.getAssets(), new StringBuilder().append(context.getString(R.string.image_path_pre_string)).append(flightDataList.get(position).getSegments().get(0).getLegs().get(0).getAirline()).append(context.getString(R.string.image_path_post_string)).toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            holder.tvAirlineName.setText(flightDataList.get(position).getSegments().get(0).getLegs().get(0).getAirlineName());
            holder.tvOutDepTime.setText(UIUtils.getTimeToDisplay(flightDataList.get(position).getSegments().get(0).getLegs().get(0).getDepartureTime()));
            holder.tvOutDuration.setText(Utils.getDurationInHrsAndMins(flightDataList.get(position).getSegments().get(0).getDuration()));
            holder.tvOutArrTime.setText(UIUtils.getTimeToDisplay(flightDataList.get(position).getSegments().get(0).getLegs().get(flightDataList.get(position).getSegments().get(0).getLegs().size() - 1).getArrivalTime()));
            if (AppPreferences.getInstance(context).getB2B()) {
                holder.tvBusinessFare.setTextColor(UIUtils.getThemeColor(context));
                holder.tvBusinessFare.setText(UIUtils.getFareToDisplay(context, flightDataList.get(position).getFare().getTotal().getPrice().getAmount()));
                holder.tvBusinessFare.setVisibility(View.VISIBLE);
            } else
                holder.tvBusinessFare.setVisibility(View.GONE);
            setDots(holder.llOutDots, flightDataList.get(position).getSegments().get(0));
            setPlaces(holder.llOutPlaces, flightDataList.get(position).getSegments().get(0));
            setDots(holder.llInDots, flightDataList.get(position).getSegments().get(1));
            setPlaces(holder.llInPlaces, flightDataList.get(position).getSegments().get(1));
            holder.tvCustomerFare.setTextColor(UIUtils.getThemeColor(context));
            holder.tvCustomerFare.setText(UIUtils.getFareToDisplay(context, flightDataList.get(position).getFare().getSellingPrice()));
            holder.tvInDepTime.setText(UIUtils.getTimeToDisplay(flightDataList.get(position).getSegments().get(1).getLegs().get(0).getDepartureTime()));
            holder.tvInDuration.setText(Utils.getDurationInHrsAndMins(flightDataList.get(position).getSegments().get(1).getDuration()));
            holder.tvInArrTime.setText(UIUtils.getTimeToDisplay(flightDataList.get(position).getSegments().get(1).getLegs().get(flightDataList.get(position).getSegments().get(1).getLegs().size() - 1).getArrivalTime()));
            if (flightDataList.get(position).isNoRefund())
                holder.tvFareRefundable.setText(context.getString(R.string.text_non_refundable));
            else
                holder.tvFareRefundable.setText(context.getString(R.string.text_refundable));

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

        }
    }

    private void setPlaces(LinearLayout llPlaces, Segment segment) {
        llPlaces.removeAllViews();
        for (int i = 0; i <= segment.getLegCount(); i++) {
            TextView tvPlace = new TextView(context);
            tvPlace.setTypeface(null, Typeface.BOLD);
            LinearLayout.LayoutParams tvPlaceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tvPlace.setLayoutParams(tvPlaceParams);
            llPlaces.addView(tvPlace);
            if (i == 0 || i == segment.getLegCount()) {
                tvPlace.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tvPlace.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                tvPlace.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                tvPlace.setTextColor(context.getResources().getColor(R.color.grey_second));
            }
            if (i != segment.getLegCount()) {
                tvPlace.setText(segment.getLegs().get(i).getOrigin());
                View gapView = new View(context);
                LinearLayout.LayoutParams gapViewParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                gapView.setLayoutParams(gapViewParams);
                llPlaces.addView(gapView);
            } else {
                tvPlace.setText(segment.getLegs().get(i - 1).getDestination());
            }
        }

    }

    private void setDots(LinearLayout llDots, Segment segment) {
        llDots.removeAllViews();
        for (int i = 0; i <= segment.getLegCount(); i++) {
            RoundedImageView rImageView = new RoundedImageView(context);
            LinearLayout.LayoutParams iTvParams = new LinearLayout.LayoutParams(20, 20);
            rImageView.setLayoutParams(iTvParams);
            if (i == 0 || i == segment.getLegCount()) {
                rImageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.riv_bg_grey));
            } else {
                rImageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.riv_bg_white));
            }
            llDots.addView(rImageView);
            if (i != segment.getLegCount()) {
                View gapView = new View(context);
                LinearLayout.LayoutParams gapViewParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                gapView.setLayoutParams(gapViewParams);
                llDots.addView(gapView);
            }
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
    public void onClick(View v) {
        selectedFlightPosition = (int) v.getTag();
        searchActivity.manageTvFares();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llRoot;
        private GifImageView gdvFlightImage;
        private TextView tvAirlineName, tvOutDepTime, tvOutDuration, tvOutArrTime, tvInDepTime, tvInDuration,
                tvInArrTime, tvBusinessFare, tvCustomerFare, tvFareRefundable, tvPricingSource, tvCashback, tvEcashback;
        private LinearLayout llOutDots, llOutPlaces, llInDots, llInPlaces;
        RelativeLayout rlCashback, rlEcashback;

        public ViewHolder(View itemView) {
            super(itemView);
            llRoot = (LinearLayout) itemView.findViewById(R.id.root);
            gdvFlightImage = (GifImageView) itemView.findViewById(R.id.image_flight);
            tvAirlineName = (TextView) itemView.findViewById(R.id.category);
            tvOutDepTime = (TextView) itemView.findViewById(R.id.out_dep_time);
            tvOutDuration = (TextView) itemView.findViewById(R.id.out_duration);
            tvOutArrTime = (TextView) itemView.findViewById(R.id.out_arr_time);
            tvInDepTime = (TextView) itemView.findViewById(R.id.in_dep_time);
            tvInDuration = (TextView) itemView.findViewById(R.id.in_duration);
            tvInArrTime = (TextView) itemView.findViewById(R.id.in_arr_time);
            llOutDots = (LinearLayout) itemView.findViewById(R.id.out_dots);
            llOutPlaces = (LinearLayout) itemView.findViewById(R.id.out_places);
            llInDots = (LinearLayout) itemView.findViewById(R.id.in_dots);
            llInPlaces = (LinearLayout) itemView.findViewById(R.id.in_places);
            tvBusinessFare = (TextView) itemView.findViewById(R.id.business_fare);
            tvCustomerFare = (TextView) itemView.findViewById(R.id.customer_fare);
            tvFareRefundable = (TextView) itemView.findViewById(R.id.fare_refundable);
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
}
