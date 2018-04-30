package com.rezofy.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.models.request_models.Traveller;
import com.rezofy.models.response_models.Leg;
import com.rezofy.models.response_models.ViewTicketResponse;
import com.rezofy.requests.Requests;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.activities.HomeActivity;
import com.rezofy.views.activities.MyTripsActivity;
import com.rezofy.views.activities.ViewTicketActivity;
import com.rezofy.views.custom_views.IconTextView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Linchpin66 on 25-01-2016.
 */
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> implements NetworkTask.Result {
    private Context ctx;
    private final int FIRST_ITEM = 0;
    private final int SECOND_ITEM = 1;
    private final int THIRD_ITEM = 2;
    private final int FOURTH_ITEM = 3;
    private ViewTicketResponse viewTicketResponse;
    private static final int ID_CANCEL_TRIP = 1;
    private int itemCount;
    private boolean isTripDetails;
    private int resultCode = 0;


    public TicketAdapter(Context ctx, ViewTicketResponse viewTicketResponse, boolean isTripDetails) {
        this.ctx = ctx;
        this.viewTicketResponse = viewTicketResponse;
        this.isTripDetails = isTripDetails;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FIRST_ITEM) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.ticket_first_item_view, null, false);
            return new FirstTicketViewHolder(view);
        } else if (viewType == SECOND_ITEM) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.ticket_second_item_view, null, false);
            return new SecondTicketViewHolder(view);
        } else if (viewType == THIRD_ITEM) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.ticket_third_item_view, null, false);
            return new ThirdTicketViewHolder(view);
        } else {
            View view = LayoutInflater.from(ctx).inflate(R.layout.ticket_fourth_item_view, null, false);
            return new FourthTicketViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (holder.getItemViewType() == FIRST_ITEM) {
            ((FirstTicketViewHolder) holder).tvTripStatus.setText(viewTicketResponse.getTrip().getStatus());
            ((FirstTicketViewHolder) holder).travelBookedOn.setText(ctx.getString(R.string.booked_on).concat(" ").concat(Utils.changeDateFormat(viewTicketResponse.getTrip().getBookingDate(), "dd/MM/yyyy", "dd MMM yyyy")));

        } else if (holder.getItemViewType() == SECOND_ITEM) {
            if (position == 1)
                ((SecondTicketViewHolder) holder).rlFlightDetails.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.bg_ticket_white));
            else
                ((SecondTicketViewHolder) holder).rlFlightDetails.setBackgroundColor(ctx.getResources().getColor(R.color.white));

            Leg leg;
            if (position <= viewTicketResponse.getTrip().getSelectedResult().getSegments().get(0).getLegs().size()) {
                leg = viewTicketResponse.getTrip().getSelectedResult().getSegments().get(0).getLegs().get(position - 1);
                ((SecondTicketViewHolder) holder).tvPnr.setText("PNR# " + viewTicketResponse.getTrip().getSelectedResult().getSegments().get(0).getAirlinePnr());
            } else {
                leg = viewTicketResponse.getTrip().getSelectedResult().getSegments().get(1).getLegs().get(position - viewTicketResponse.getTrip().getSelectedResult().getSegments().get(0).getLegs().size() - 1);
                ((SecondTicketViewHolder) holder).tvPnr.setText("PNR# " + viewTicketResponse.getTrip().getSelectedResult().getSegments().get(1).getAirlinePnr());
            }
            try {
                ((SecondTicketViewHolder) holder).gdvCarrierImage.setImageDrawable(new GifDrawable(ctx.getAssets(), new StringBuilder().append(ctx.getString(R.string.image_path_pre_string)).append(leg.getAirlineCode()).append(ctx.getString(R.string.image_path_post_string)).toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((SecondTicketViewHolder) holder).tvAirlineName.setText(leg.getAirlineName());
            ((SecondTicketViewHolder) holder).tvCarrierAndFlightNo.setText(UIUtils.getFlightToDisplay(leg.getAirlineCode(), leg.getFlightNumber()));
            ((SecondTicketViewHolder) holder).tvOrigin.setText(leg.getOrigin());
            if(null!=leg.getOriginDetail()) {
                ((SecondTicketViewHolder) holder).tvOriginAirport.setText(leg.getOriginDetail().getName());
                ((SecondTicketViewHolder) holder).tvOriginCity.setText(leg.getOriginDetail().getCountry());
            }
            if(null!=leg.getDestinationDetail()) {
                ((SecondTicketViewHolder) holder).tvDest.setText(leg.getDestination());
                ((SecondTicketViewHolder) holder).tvDestAirport.setText(leg.getDestinationDetail().getName());
                ((SecondTicketViewHolder) holder).tvDestCity.setText(leg.getDestinationDetail().getCountry());
            }
            ((SecondTicketViewHolder) holder).tvTravelDate.setText(Utils.changeDateFormat(leg.getDepartureDate(), "ddMMyy", "dd MMM yyyy"));
            ((SecondTicketViewHolder) holder).tvDepartTime.setText(UIUtils.getTimeToDisplay(leg.getDepartureTime()));
            ((SecondTicketViewHolder) holder).tvArrivalTime.setText(UIUtils.getTimeToDisplay(leg.getArrivalTime()));
            ((SecondTicketViewHolder) holder).tvDuration.setText(Utils.getDurationInHrsAndMins(leg.getDuration() + ""));

        } else if (holder.getItemViewType() == THIRD_ITEM) {
            ((ThirdTicketViewHolder) holder).tvPassengerNames.setText("");
            List<Traveller> travellers = viewTicketResponse.getTrip().getTravellers();
            for (Traveller traveller : travellers)
                ((ThirdTicketViewHolder) holder).tvPassengerNames.setText(((ThirdTicketViewHolder) holder).tvPassengerNames.getText() + traveller.getTitle() + ". " + traveller.getFirstName() + " " + traveller.getLastName() + "\n");
            ((ThirdTicketViewHolder) holder).tvFare.setText(UIUtils.getOriginalFareToDisplay(ctx, viewTicketResponse.getTrip().getTotalPrice()));
            ((ThirdTicketViewHolder) holder).tvEmail.setText("Email: "+ ctx.getString(R.string.contact_email));
            ((ThirdTicketViewHolder) holder).tvContact.setText("Phone: "+ ctx.getString(R.string.contact_no));

        } else {
            if (isTripDetails) {
                Date travelStartDate = Utils.getDateFromFormat(viewTicketResponse.getTrip().getStartDate(), Utils.DATE_FORMAT_dd_slash_mm_slash_yyyy);
                String departureTime = viewTicketResponse.getTrip().getSelectedResult().getSegments().get(0).getLegs().get(0).getDepartureTime();
                travelStartDate.setHours(Integer.parseInt(departureTime.substring(0, 2)));
                travelStartDate.setMinutes(Integer.parseInt(departureTime.substring(2)));
                travelStartDate.setSeconds(0);

                if (viewTicketResponse.getTrip().getCancellationStatus() != null && viewTicketResponse.getTrip().getCancellationStatus().equals(Utils.TICKET_STATUS_CANCEL_REQUESTED)) {
                    ((FourthTicketViewHolder) holder).rlTripCancellation.setVisibility(View.VISIBLE);
                    ((FourthTicketViewHolder) holder).tvMessage.setVisibility(View.VISIBLE);
                    ((FourthTicketViewHolder) holder).tvCancelBooking.setVisibility(View.GONE);

                } else if (viewTicketResponse.getTrip().isCancelAllowed() && travelStartDate.after(Calendar.getInstance().getTime())) {
                    ((FourthTicketViewHolder) holder).rlTripCancellation.setVisibility(View.VISIBLE);
                    ((FourthTicketViewHolder) holder).tvCancelBooking.setVisibility(View.VISIBLE);
                    ((FourthTicketViewHolder) holder).tvMessage.setVisibility(View.GONE);
                    ((FourthTicketViewHolder) holder).tvCancelBooking.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!Utils.isNetworkAvailable(ctx)) {
                                Utils.showAlertDialog(ctx, ctx.getString(R.string.app_name), ctx.getString(R.string.network_error), ctx.getString(R.string.ok), null, null, null);

                            } else {

                                Utils.showAlertDialog(ctx, ctx.getString(R.string.app_name), ctx.getString(R.string.txt_booking_cancel), null, ctx.getString(R.string.text_ok), ctx.getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                String url;
                                                NetworkTask networkTask = new NetworkTask(ctx, ID_CANCEL_TRIP);
                                                networkTask.setDialogMessage(ctx.getString(R.string.please_wait));
                                                networkTask.exposePostExecute(TicketAdapter.this);
                                                url = UIUtils.getBaseUrl(ctx) + WebServiceConstants.urlCancelTrip;
                                                Utils.appendLog("CancellationURL: " + url);
                                                String request = Requests.cancelTripRequest(viewTicketResponse.getTrip().getBookingRefNo());
                                                Utils.appendLog("CancellationRequest: " + request);
                                                String paramsArray[] = new String[]{url, request};
                                                networkTask.execute(paramsArray);

                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //((AppCompatActivity) activity).onBackPressed();
                                                break;
                                        }
                                    }
                                });


                            }
                        }
                    });
                }
                UIUtils.setNormalButtonProperties(((FourthTicketViewHolder) holder).tvCancelBooking);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return FIRST_ITEM;
        else if (position == itemCount - 2)
            return THIRD_ITEM;
        else if (position == itemCount - 1)
            return FOURTH_ITEM;
        else
            return SECOND_ITEM;
    }

    @Override
    public int getItemCount() {
        itemCount = 0;
        for (int i = 0; i < viewTicketResponse.getTrip().getSelectedResult().getSegments().size(); i++)
            for (int j = 0; j < viewTicketResponse.getTrip().getSelectedResult().getSegments().get(i).getLegs().size(); j++)
                ++itemCount;
        return itemCount += 3;
    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {
        Utils.appendLog("CancellationResponse: " + object);
        if (object == null || object.equals("")) {
            Utils.showAlertDialog(ctx, ctx.getString(R.string.app_name), ctx.getString(R.string.network_error), ctx.getString(R.string.ok), null, null, null);

        } else {
            try {
                viewTicketResponse = new Gson().fromJson(object, ViewTicketResponse.class);
                if (viewTicketResponse.getTrip().getCancellationStatus().equals(Utils.TICKET_STATUS_CANCEL_REQUESTED)) {
                    resultCode = Utils.TRIP_ACTION_CODE;
                    Utils.showAlertDialog(ctx, ctx.getString(R.string.app_name), ctx.getString(R.string.msg_cancellation_request_received), ctx.getString(R.string.ok), null, null, null);
                    notifyDataSetChanged();
                } else {
                    Utils.showAlertDialog(ctx, ctx.getString(R.string.app_name), ctx.getString(R.string.msg_cancellation_failure), ctx.getString(R.string.ok), null, "Share Logs", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            switch(i) {
                                case DialogInterface.BUTTON_NEUTRAL:
                                    boolean isPrimaryApp = Boolean.parseBoolean(ctx.getString(R.string.is_primary_app));
                                    if (isPrimaryApp) {
                                        Intent intent = new Intent(ctx, MyTripsActivity.class);
                                        ctx.startActivity(intent);

                                    } /*else {
                            if (!getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(Utils.TAG_MY_TRIPS_FRAGMENT))
                                loadDesiredFragment();
                        }*/
                                    dialog.dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    try {
                                        Utils.sendEmail((ViewTicketActivity)ctx);
                                    } catch (Exception e) {

                                    }
                                    break;
                            }
                        }
                    });
                }

            } catch (Exception e) {
                Utils.appendLog("Exception in Cancellation: " + e.getMessage());
                Utils.showAlertDialog(ctx, ctx.getString(R.string.app_name), ctx.getString(R.string.msg_cancellation_failure), ctx.getString(R.string.ok), null, "Share Logs", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        boolean isPrimaryApp = Boolean.parseBoolean(ctx.getString(R.string.is_primary_app));
                        if (isPrimaryApp) {
                            Intent intent = new Intent(ctx, MyTripsActivity.class);
                            ctx.startActivity(intent);

                        } /*else {
                            if (!getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(Utils.TAG_MY_TRIPS_FRAGMENT))
                                loadDesiredFragment();
                        }*/
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class FirstTicketViewHolder extends MyViewHolder {
        private TextView tvTripStatus;
        private TextView travelBookedOn;

        public FirstTicketViewHolder(View view) {
            super(view);
            tvTripStatus = (TextView) view.findViewById(R.id.trip_status);
            travelBookedOn = (TextView) view.findViewById(R.id.travel_book_on);
        }
    }

    private class SecondTicketViewHolder extends MyViewHolder {
        private RelativeLayout rlFlightDetails;
        private GifImageView gdvCarrierImage;
        private TextView tvAirlineName;
        private TextView tvCarrierAndFlightNo;
        private TextView tvPnr;
        private TextView tvOrigin;
        private TextView tvDest;
        private TextView tvOriginAirport;
        private TextView tvDestAirport;
        private TextView tvOriginCity;
        private TextView tvDestCity;
        private TextView tvTravelDate;
        private TextView tvDepartTime;
        private TextView tvArrivalTime;
        private TextView tvDuration;
        private IconTextView iTVFlight;

        public SecondTicketViewHolder(View view) {
            super(view);
            int themeColor = UIUtils.getThemeColor(ctx);
            iTVFlight = (IconTextView) view.findViewById(R.id.flight_icon);
            iTVFlight.setTextColor(themeColor);
            rlFlightDetails = (RelativeLayout) view.findViewById(R.id.flight_details);
            gdvCarrierImage = (GifImageView) view.findViewById(R.id.carrier_image);
            tvAirlineName = (TextView) view.findViewById(R.id.airline_name);
            tvCarrierAndFlightNo = (TextView) view.findViewById(R.id.carrier_and_flight_no);
            tvPnr = (TextView) view.findViewById(R.id.pnr);
            tvPnr.setTextColor(themeColor);
            tvOrigin = (TextView) view.findViewById(R.id.origin);
            tvOrigin.setTextColor(themeColor);
            tvDest = (TextView) view.findViewById(R.id.dest);
            tvDest.setTextColor(themeColor);
            tvOriginAirport = (TextView) view.findViewById(R.id.origin_airport);
            tvDestAirport = (TextView) view.findViewById(R.id.dest_airport);
            tvOriginCity = (TextView) view.findViewById(R.id.origin_city);
            tvDestCity = (TextView) view.findViewById(R.id.dest_city);
            tvTravelDate = (TextView) view.findViewById(R.id.travel_date);
            tvDepartTime = (TextView) view.findViewById(R.id.fare);
            tvArrivalTime = (TextView) view.findViewById(R.id.arrival_time);
            tvDuration = (TextView) view.findViewById(R.id.duration_first_way);
            tvDuration.setTextColor(themeColor);
        }
    }

    private class ThirdTicketViewHolder extends MyViewHolder {
        private TextView tvPassengerNames, tvFare, tvEmail, tvContact;

        public ThirdTicketViewHolder(View view) {
            super(view);
            tvPassengerNames = (TextView) view.findViewById(R.id.passenger_names);
            tvFare = (TextView) view.findViewById(R.id.fare);
            tvEmail=(TextView) view.findViewById(R.id.email);
            tvContact=(TextView) view.findViewById(R.id.contact);
        }
    }

    private class FourthTicketViewHolder extends MyViewHolder {
        private RelativeLayout rlTripCancellation;
        private TextView tvCancelBooking, tvMessage;

        public FourthTicketViewHolder(View itemView) {
            super(itemView);
            rlTripCancellation = (RelativeLayout) itemView.findViewById(R.id.rl_cancellation);
            tvCancelBooking = (TextView) itemView.findViewById(R.id.tv_cancel_booking);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }

    public int getResultCode() {
        return resultCode;
    }
}
