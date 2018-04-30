package com.rezofy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.models.response_models.FlightData;
import com.rezofy.models.response_models.Leg;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Linchpin66 on 21-01-2016.
 */
public class FlightLegAdapter extends RecyclerView.Adapter<FlightLegAdapter.MyViewHolder> {
    private Context ctx;
    private FlightData flightData;
    private String originCityName, destCityName, departDateValue;
    private int flightDirection;

    public FlightLegAdapter(Context ctx, FlightData flightData, String originCityName, String destCityName, String departDateValue, int flightDirection) {
        this.flightData = flightData;
        this.ctx = ctx;
        this.originCityName = originCityName;
        this.destCityName = destCityName;
        this.departDateValue = departDateValue;
        this.flightDirection = flightDirection;
    }

    public FlightLegAdapter(Context ctx, FlightData flightData, int flightDirection) {
        this.flightData = flightData;
        this.ctx = ctx;
        this.originCityName = flightData.getSegments().get(0).getOrigin();
        this.destCityName = flightData.getSegments().get(0).getDestination();
        this.departDateValue = Utils.changeDateFormat(flightData.getSegments().get(0).getLegs().get(0).getDepartureDate(), Utils.DATE_FORMAT_ddMMyy, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy);
        this.flightDirection = flightDirection;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 1 : 2;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != 1) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.flight_item_view, null, false);
            return new SecondViewHolder(view);
        } else {
            View view = LayoutInflater.from(ctx).inflate(R.layout.flight_top_item_view, null, false);
            return new FirstViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (holder.getItemViewType() != 1) {
            Leg legData = flightData.getSegments().get(0).getLegs().get(position - 1);
            SecondViewHolder secholder = (SecondViewHolder) holder;

            secholder.originAirportNameText.setText(legData.getOriginDetail().getName());
            secholder.destAirPortNameText.setText(legData.getDestinationDetail().getName());
            secholder.originLocation.setText(new StringBuilder().append(legData.getOrigin()).append(", ").append(legData.getOriginDetail().getName()).append(", ").append(legData.getOriginDetail().getCountry()));
            secholder.destLocation.setText(new StringBuilder().append(legData.getDestination()).append(", ").append(legData.getDestinationDetail().getName()).append(", ").append(legData.getDestinationDetail().getCountry()));
            if (legData.getAirlineName() != null)
                secholder.airLine.setText(legData.getAirlineName());
            secholder.carrierFlightNumber.setText(UIUtils.getFlightToDisplay(legData.getAirline(), legData.getFlightNumber()));
            secholder.originflightTime.setText(UIUtils.getTimeToDisplay(legData.getDepartureTime()));
            secholder.destinationFlighttime.setText(UIUtils.getTimeToDisplay(legData.getArrivalTime()));
            secholder.originFlightDate.setText(Utils.changeDateFormat(legData.getDepartureDate(), "ddMMyy", "dd MMM"));
            secholder.destinationflightDate.setText(Utils.changeDateFormat(legData.getArrivalDate(), "ddMMyy", "dd MMM"));
            secholder.flightDetailsDuration.setText(Utils.getFormattedDurationInHrsAndMins(String.valueOf(legData.getDuration())));
            try {
                if (legData.getAirline() != null && legData.getAirline().length() == 2)
                    secholder.ivCarrierImage.setImageDrawable(new GifDrawable(ctx.getAssets(), new StringBuilder().append(ctx.getString(R.string.image_path_pre_string)).append(legData.getAirline()).append(ctx.getString(R.string.image_path_post_string)).toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (flightData.getSegments().get(0).getLegs().size() > 1) {
                if (position == 1) {
                    secholder.firstDashView.setVisibility(View.GONE);
                    secholder.secondDashView.setVisibility(View.VISIBLE);

                } else if (position == flightData.getSegments().get(0).getLegs().size()) {
                    secholder.firstDashView.setVisibility(View.VISIBLE);
                    secholder.secondDashView.setVisibility(View.GONE);

                } else {
                    secholder.firstDashView.setVisibility(View.VISIBLE);
                    secholder.secondDashView.setVisibility(View.VISIBLE);
                }

            } else {
                secholder.firstDashView.setVisibility(View.GONE);
                secholder.secondDashView.setVisibility(View.GONE);
            }

            if(flightData.getPricingSource() != null && flightData.getPricingSource().length() > 1) {
                secholder.tvPriceSource.setVisibility(View.VISIBLE);
                secholder.tvPriceSource.setText(flightData.getPricingSource());
            } else {
                secholder.tvPriceSource.setVisibility(View.INVISIBLE);

            }

        } else {

            FirstViewHolder firstViewHolder = (FirstViewHolder) holder;
            if (flightDirection == Utils.FLIGHT_DIRECTION_OUTBOUND)
                firstViewHolder.iTVFlightDirection.setText(ctx.getString(R.string.icon_text_x));
            else
                firstViewHolder.iTVFlightDirection.setText(ctx.getString(R.string.icon_text_J));
            firstViewHolder.originToDestination.setText(originCityName + " - " + destCityName);
            firstViewHolder.departDate.setText(Utils.changeDateFormat(departDateValue, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_EEE_space_comma_d_space_LLL));
            firstViewHolder.timeValue.setText("" + Utils.getDurationInHrsAndMins(String.valueOf(flightData.getSegments().get(0).getDuration())));
            firstViewHolder.nonStop.setText(UIUtils.getFlightType(flightData.getSegments().get(0).getNoOfStops()));
        }
    }

    @Override
    public int getItemCount() {
        return flightData.getSegments().get(0).getLegs().size() + 1;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);

        }
    }


    public class SecondViewHolder extends MyViewHolder {
        private TextView originAirportNameText, destAirPortNameText, originLocation, destLocation, carrierFlightNumber, originflightTime, originFlightDate, destinationFlighttime, destinationflightDate;
        private TextView airLine,  tvPriceSource;
        private GifImageView ivCarrierImage;
        private View firstDashView, secondDashView;
        private TextView flightDetailsDuration;

        SecondViewHolder(View itemView) {
            super(itemView);
            originAirportNameText = (TextView) itemView.findViewById(R.id.flight_details_airport_name);
            destAirPortNameText = (TextView) itemView.findViewById(R.id.flight_details_name_again);
            originLocation = (TextView) itemView.findViewById(R.id.flight_details_loc);
            destLocation = (TextView) itemView.findViewById(R.id.flight_details_loc_again);
            airLine = (TextView) itemView.findViewById(R.id.airline);
            carrierFlightNumber = (TextView) itemView.findViewById(R.id.carriar_flightNumber);
            ivCarrierImage = (GifImageView) itemView.findViewById(R.id.iv_carrier_image);
            firstDashView = itemView.findViewById(R.id.first_dash_line);
            secondDashView = itemView.findViewById(R.id.last_dash_line);
            flightDetailsDuration = (TextView) itemView.findViewById(R.id.flight_details_time_hm);
            originflightTime = (TextView) itemView.findViewById(R.id.flight_details_time);
            originFlightDate = (TextView) itemView.findViewById(R.id.flight_details_date);
            destinationFlighttime = (TextView) itemView.findViewById(R.id.flight_details_time_again);
            destinationflightDate = (TextView) itemView.findViewById(R.id.flight_details_date_again);
            tvPriceSource = (TextView) itemView.findViewById(R.id.tv_price_source);
        }
    }

    public class FirstViewHolder extends MyViewHolder {
        private TextView originToDestination, departDate, nonStop, timeValue;
        private IconTextView iTVFlightDirection;

        public FirstViewHolder(View itemView) {
            super(itemView);
            originToDestination = (TextView) itemView.findViewById(R.id.place_first_way);
            departDate = (TextView) itemView.findViewById(R.id.departDate);
            nonStop = (TextView) itemView.findViewById(R.id.non_stop);
            timeValue = (TextView) itemView.findViewById(R.id.time_value);
            iTVFlightDirection = (IconTextView) itemView.findViewById(R.id.flight_direction);
        }
    }
}
